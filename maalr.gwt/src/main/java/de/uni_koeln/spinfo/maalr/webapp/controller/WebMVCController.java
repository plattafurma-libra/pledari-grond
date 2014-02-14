/*******************************************************************************
 * Copyright 2013 Sprachliche Informationsverarbeitung, University of Cologne
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uni_koeln.spinfo.maalr.webapp.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Principal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import de.uni_koeln.spinfo.maalr.common.server.util.Configuration;
import de.uni_koeln.spinfo.maalr.common.shared.Role;
import de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription;
import de.uni_koeln.spinfo.maalr.common.shared.description.ValueFormat;
import de.uni_koeln.spinfo.maalr.common.shared.searchconfig.Localizer;
import de.uni_koeln.spinfo.maalr.login.LoginManager;
import de.uni_koeln.spinfo.maalr.login.MaalrUserInfo;
import de.uni_koeln.spinfo.maalr.login.UserInfoBackend;
import de.uni_koeln.spinfo.maalr.lucene.Index;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.BrokenIndexException;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.InvalidQueryException;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.NoIndexAvailableException;
import de.uni_koeln.spinfo.maalr.lucene.query.MaalrQuery;
import de.uni_koeln.spinfo.maalr.lucene.query.QueryResult;
import de.uni_koeln.spinfo.maalr.mongo.exceptions.InvalidUserException;

@Controller
public class WebMVCController {

	@Autowired
	private LoginManager loginManager;

	@Autowired
	private UserInfoBackend users;
	
	@Autowired
	private Index index;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private Configuration configuration = Configuration.getInstance();
	
	private String getLocale(HttpSession session, HttpServletRequest request) {
		String locale = (String) request.getParameter("pl");
		if(locale == null) {
			locale = (String) session.getAttribute("pl");
			return locale;
		} else {
			return locale;
		}
	}
	
	private String getLocalizedString(String key, HttpSession session, HttpServletRequest request) {
		String locale = getLocale(session, request);
		if(locale == null) {
			return ResourceBundle.getBundle("de.uni_koeln.spinfo.maalr.webapp.i18n.text").getString(key);
		} else {
			return ResourceBundle.getBundle("de.uni_koeln.spinfo.maalr.webapp.i18n.text", new Locale(locale)).getString(key);
		}
	}

	@ModelAttribute("query")
	public MaalrQuery getQuery() {
		return new MaalrQuery();
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView showResults(HttpSession session, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("index");
		setPageTitle(mv, getLocalizedString("maalr.index_page.title", session, request));
		session.setAttribute("language", Configuration.getInstance().getLemmaDescription().getLanguageName(true));
		return mv;
	}

	private void setPageTitle(ModelAndView mv, String title) {
		mv.addObject("pageTitle", title);
	}

	@RequestMapping("/")
	public ModelAndView showIndex(HttpSession session, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("index");
		setPageTitle(mv, getLocalizedString("maalr.index_page.title", session, request));
		return mv;
	}

	@ModelAttribute("pageTitle")
	private String getHtmlPageTitle() {
		return configuration.getLongName();
	}

	@ModelAttribute("user")
	private MaalrUserInfo currentUser(final HttpServletRequest request, Principal principal) {
		if (principal != null) {
			final String currentUser = principal.getName();
			if (loginManager.loggedIn())
				if (currentUser != null) {
					MaalrUserInfo user = (MaalrUserInfo) request.getSession().getAttribute("user");
					if (user == null) {
						user = users.getOrCreateCurrentUser();
						request.getSession().setAttribute("user", user);
					}
					return user;
				}
		}
		return null;
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search() {
		return new ModelAndView("search");
	}

	@RequestMapping(value = "/translate", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView translate(@ModelAttribute("query") MaalrQuery query, BindingResult br, final HttpServletRequest request) {
		
		ModelAndView mv = new ModelAndView("index");
		mv.addObject("search", query);
		
		try {
			// if (query.getSearchPhrase() != null && query.getSearchPhrase().trim().length() > 0) {
			// 		setPageTitle(mv, "Translations of " + query.getSearchPhrase());
			// }
			QueryResult result = index.query(query, true);
			mv.addObject("result", result);
			return mv;
		} catch (InvalidQueryException e) {
			return getIndexExceptionView(e);
		} catch (NoIndexAvailableException e) {
			return getIndexExceptionView(e);
		} catch (BrokenIndexException e) {
			return getIndexExceptionView(e);
		} catch (IOException e) {
			return getIndexExceptionView(e);
		} catch (InvalidTokenOffsetsException e) {
			return getIndexExceptionView(e);
		}
	}

	@RequestMapping("/login")
	public ModelAndView login(HttpSession session, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("login");
		setPageTitle(mv, getLocalizedString("maalr.login_page.title", session, request));
		return mv;
	}
	
	/**
	 * This method maps dictionary-urls to queries. URLs must match the pattern
	 * described in the request mapping annotations, as
	 * http://localhost:8080/dictionary/tudestg-e-rumantsch/nase.html The
	 * {@link MaalrQuery} parameter will be initialized by spring, such that the
	 * parameters defined in the request mapping will be copied into the model.
	 * 
	 * See http://static.springsource.org/spring/docs/3.1.x/spring-framework-
	 * reference/html/mvc.html#mvc-ann-modelattrib-methods
	 * @param session 
	 * @param request 
	 * 
	 * @throws Exception
	 */
	@RequestMapping("/dictionary/{values[language]}/{values[searchPhrase]}")
	public ModelAndView search(@ModelAttribute("query") MaalrQuery query, BindingResult br, HttpServletResponse response, HttpSession session, HttpServletRequest request) {
		try {
			query.setPageSize(100);
			ModelAndView mv = new ModelAndView("dictionary");
			String firstLanguage = Configuration.getInstance().getLemmaDescription().getFirstLanguage().getId();
			String language = query.getValue("language");
			boolean isFirst = firstLanguage.equals(language);
			QueryResult result = index.queryExact(query.getValue("searchPhrase"), isFirst, true);
			mv.addObject("result", result);
			//mv.addObject("query", query);
			String key = null;
			if(language.equals(configuration.getLemmaDescription().getLanguageName(true))) {
				key = "dict.title_lang1";
			} else {
				key = "dict.title_lang2";
			}
			String title = Localizer.getTranslation(getLocale(session, request), key);
			title = title.replaceAll("\\{0\\}", query.getValue("searchPhrase"));
			setPageTitle(mv, title);
			// TODO: Required to display umlauts etc in XML output.
			// However, this is done automatically in JSON & HTML...
			// should be configured somewhere else?
			response.setCharacterEncoding("UTF-8");
			return mv;
		} catch (InvalidQueryException e) {
			return getIndexExceptionView(e);
		} catch (NoIndexAvailableException e) {
			return getIndexExceptionView(e);
		} catch (BrokenIndexException e) {
			return getIndexExceptionView(e);
		} catch (IOException e) {
			return getIndexExceptionView(e);
		} catch (InvalidTokenOffsetsException e) {
			return getIndexExceptionView(e);
		}
	}

	private ModelAndView getIndexExceptionView(Exception e) {
		ModelAndView mv = new ModelAndView("jsp/errors/indexError");
		mv.addObject("exception", e);
		return mv;
	}

	@RequestMapping("/admin/admin")
	public ModelAndView admin() {
		ModelAndView mv = new ModelAndView("admin/admin");
		return mv;
	}

	@RequestMapping("/agid")
	public ModelAndView agid() {
		ModelAndView mv = new ModelAndView("static/agid");
		setPageTitle(mv, "Help");
		return mv;
	}

	@RequestMapping("/agidplug-in")
	public ModelAndView agidPlugin() {
		ModelAndView mv = new ModelAndView("static/agidplug-in");
		setPageTitle(mv, "Search Plug-In");
		return mv;
	}

	@RequestMapping("/infos")
	public ModelAndView infos() {
		ModelAndView mv = new ModelAndView("static/infos");
		setPageTitle(mv, "Infos");
		return mv;
	}

	@RequestMapping("/maalr")
	public ModelAndView maalr() {
		ModelAndView mv = new ModelAndView("static/maalr");
		setPageTitle(mv, "About Maalr");
		return mv;
	}

	@RequestMapping("/abreviaziuns")
	public ModelAndView abbreviations() {
		ModelAndView mv = new ModelAndView("static/abreviaziuns");
		setPageTitle(mv, "Abreviaziuns");
		return mv;
	}

	@RequestMapping("/browse")
	public ModelAndView newAlphaList(@ModelAttribute("query") MaalrQuery query,
			BindingResult br, HttpSession session, HttpServletRequest request) {
		try {
			return newAlphaList(query.getValue("language"), "A", 0,
					query, br, session, request);
		} catch (NoIndexAvailableException e) {
			return getIndexExceptionView(e);
		} catch (BrokenIndexException e) {
			return getIndexExceptionView(e);
		} catch (InvalidQueryException e) {
			return getIndexExceptionView(e);
		}
	}

	@RequestMapping("/editor/editor")
	public ModelAndView editor() {
		ModelAndView mv = new ModelAndView("editor/editor");
		return mv;
	}
	
	@RequestMapping(value = "/dowanload/backup/{fileName}", method = { RequestMethod.GET }, produces = { "application/zip" })
	public void downloadBackup (@PathVariable("fileName") String fileName, HttpServletRequest request, HttpServletResponse response) {
		try {
			String dir = Configuration.getInstance().getBackupLocation();
			File zip = new File(dir, fileName + ".zip");
			response.setContentType("application/zip");
			response.setContentLength((int)zip.length());
			OutputStream os = response.getOutputStream();
			os.write(IOUtils.toByteArray(new FileInputStream(zip)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	@Secured({Constants.Roles.ADMIN_5})
//	@RequestMapping(value = "/admin/importDB", method = { RequestMethod.POST })
//	public void importDB(HttpServletRequest request, HttpServletResponse response) throws InvalidEntryException, NoDatabaseAvailableException, IOException, JAXBException, XMLStreamException  {
//		adminController.importDatabase(request);
//	}
	
	@RequestMapping("/browse/{language}")
	public ModelAndView newAlphaList(@PathVariable("language") String language,
			@ModelAttribute("query") MaalrQuery query, BindingResult br, HttpSession session, HttpServletRequest request)
			throws NoIndexAvailableException, BrokenIndexException,
			InvalidQueryException {
		return newAlphaList(language, "A", 0, query, br, session, request);
	}

	@RequestMapping("/browse/{language}/{letter}")
	public ModelAndView newAlphaList(@PathVariable("language") String language,
			@PathVariable("letter") String letter,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@ModelAttribute("query") MaalrQuery query, BindingResult br, HttpSession session, HttpServletRequest request)
			throws NoIndexAvailableException, BrokenIndexException,
			InvalidQueryException {
		if(language == null) language = Configuration.getInstance().getLemmaDescription().getLanguageName(true);
		QueryResult result = index.getAllStartingWith(language, letter, page);
		ModelAndView mv = new ModelAndView("jsp/browse_dictionary");
		mv.addObject("result", result);
		mv.addObject("letter", letter);
		mv.addObject("page", page);
		mv.addObject("language", language);
		boolean first = true;
		if(language.equals(configuration.getLemmaDescription().getLanguageName(true))) {
			mv.addObject("otherLanguage", configuration.getLemmaDescription().getLanguageName(false));
		} else {
			mv.addObject("otherLanguage", configuration.getLemmaDescription().getLanguageName(true));
			first = false;
		}
		mv.addObject("query", query);
		if(result.getEntries().size() < 2 ) {
			
			String template = getLocalizedString("maalr.dict_title", session, request);
			String title = template.replaceAll("\\{0\\}", Localizer.getTranslation(getLocale(session, request), (first ? "dict.lang1_lang2" : "dict.lang2_lang1")));
			setPageTitle(mv, title);
			//setPageTitle(mv, language + " dictionary index");
		} else {
			LemmaDescription desc = configuration.getLemmaDescription();
			ValueFormat format = desc.getResultList(first).get(0);
			String template = getLocalizedString("maalr.dict_title_ext", session, request);
			String langPair = Localizer.getTranslation(getLocale(session, request), (first ? "dict.lang1_lang2" : "dict.lang2_lang1"));
			String title = template.replaceAll("\\{0\\}", langPair);
			String a = format.apply(result.getEntries().get(0), null);
			String b = format.apply(result.getEntries().get(result.getEntries().size()-1), null);
			title = title.replaceAll("\\{1\\}", a).replaceAll("\\{2\\}", b);
			setPageTitle(mv, title);
		}
		return mv;
	}
	
//	@RequestMapping("/test")
//	public ModelAndView speedTest() {
//		return new ModelAndView("test");
//	}

	
	
	
	@RequestMapping(value = "/persona/signedin",  method = RequestMethod.GET)
	@ResponseBody
	public String isSignedIn(HttpServletRequest request, Model model) throws IOException {
		if (loginManager.getCurrentUser() != null) {
			if (loginManager.getCurrentUser().getRole().equals(Role.PERSONA))
				return loginManager.getCurrentUser().getEmail();
		}
		return null;
	}
	
	@RequestMapping(value = "/persona/logout",  method = RequestMethod.POST)
	@ResponseBody
	public String logoutPersona(HttpServletRequest request, Model model) throws IOException {
		loginManager.logout();
		logger.info("Persona logout!");
		return "/";
	}
	
	@RequestMapping(value = "/persona/login",  method = RequestMethod.POST)
	@ResponseBody
	public String authenticateWithPersona(@RequestParam String assertion, HttpServletRequest request, Model model) throws IOException {
		// Already signed in ???
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			if (!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
				Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
				for (GrantedAuthority grantedAuthority : authorities) {
					logger.info("GrantedAuthority: " + grantedAuthority.getAuthority());
					if (grantedAuthority.getAuthority().equals("ROLE_ADMIN"))
						return "/admin/admin";
					if (grantedAuthority.getAuthority().equals("ROLE_TRUSTED_IN"))
						return "/editor/editor";
					return "/";
				}
			}
		}
	    
	    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
	    params.add("assertion", assertion);
	    params.add("audience", request.getScheme() + "://" + request.getServerName() + ":" + (request.getServerPort() == 80 ? "" : request.getServerPort()));
	    
	    // Initialize RestTamplate
	    RestTemplate restTemplate = new RestTemplate();
	    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
	    restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
	    
	    PersonaVerificationResponse response = restTemplate.postForObject("https://verifier.login.persona.org/verify", params, PersonaVerificationResponse.class);
	   
	    logger.info("PersonaVerificationResponse: " + response.toString());
	    
		if (response.getStatus().equals("okay")) {
			// Set session timeout to 30 minutes
			request.getSession().setMaxInactiveInterval(30 * 60);
			MaalrUserInfo user = users.getByEmail(response.getEmail());
			logger.info("FOUND BY EMAIL: " + user);
			if (user == null) {
				user = register(response);
				authUser(user);
				return "/";
			} else {
				authUser(user);
				return "/";
			}
		} else {
			logger.warn("Persona authentication failed due to reason: " + response.getReason());
			throw new IllegalStateException("Authentication failed");
		}
	}

	private MaalrUserInfo register(PersonaVerificationResponse response) {
		MaalrUserInfo user = new MaalrUserInfo();
		user.setEmail(response.getEmail());
		user.setLogin(response.getEmail().split("@")[0]);
		user.setProviderId("Persona");
		user.setFirstname(response.getEmail().split("@")[0]);
		user.setLastname("unknown");
		user.setRole(Role.PERSONA);
		user.setProviderUserId("unknown");
		return store(user);
	}
	
	
	private MaalrUserInfo store(MaalrUserInfo user) {
		try {
			return users.insert(user);
		} catch (InvalidUserException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void authUser(MaalrUserInfo user) {
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getRoleId());
		authorities.add(authority);
		UserDetails details = new User(user.getLogin(), "ignored", authorities);
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
				details, details.getPassword(), details.getAuthorities()));
	}

}
