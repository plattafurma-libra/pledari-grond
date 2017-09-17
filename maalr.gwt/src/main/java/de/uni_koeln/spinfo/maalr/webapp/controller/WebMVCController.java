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
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import de.uni_koeln.spinfo.maalr.common.server.util.Configuration;
import de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription;
import de.uni_koeln.spinfo.maalr.common.shared.description.ValueFormat;
import de.uni_koeln.spinfo.maalr.common.shared.form.UserForm;
import de.uni_koeln.spinfo.maalr.common.shared.form.UserFormValidationResponse;
import de.uni_koeln.spinfo.maalr.common.shared.searchconfig.Localizer;
import de.uni_koeln.spinfo.maalr.login.MaalrUserInfo;
import de.uni_koeln.spinfo.maalr.login.UserInfoBackend;
import de.uni_koeln.spinfo.maalr.login.custom.PGAuthenticationProvider;
import de.uni_koeln.spinfo.maalr.lucene.Index;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.BrokenIndexException;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.InvalidQueryException;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.NoIndexAvailableException;
import de.uni_koeln.spinfo.maalr.lucene.query.MaalrQuery;
import de.uni_koeln.spinfo.maalr.lucene.query.QueryResult;
import de.uni_koeln.spinfo.maalr.webapp.service.AccountService;

@Controller public class WebMVCController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired private Index index;
	@Autowired private UserInfoBackend users;
	@Autowired private AccountService accountService;
	@Autowired private PGAuthenticationProvider authProvider;

	private Configuration configuration = Configuration.getInstance();
	
	private String getLocale(HttpSession session, HttpServletRequest request) {
		String localeCode = configuration.getLocaleCode();
		String locale = (String) request.getParameter("locale");
		if(locale == null) {
			locale = (String) session.getAttribute("locale");
			if(locale == null) {
				session.setAttribute("locale", localeCode);
				locale = localeCode;
			}
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

	private void setPageTitle(ModelAndView mv, String title) {
		mv.addObject("pageTitle", title);
	}
	
	@RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
	public ModelAndView showIndex(HttpSession session, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("index");
		setPageTitle(mv, getLocalizedString("maalr.index_page.title", session, request));
		mv.addObject("dictContext", configuration.getDictContext());
		session.setAttribute("language", configuration.getLemmaDescription().getLanguageName(true));
		return mv;
	}

	//@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView showResults(HttpSession session, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("index");
		setPageTitle(mv, getLocalizedString("maalr.index_page.title", session, request));
		mv.addObject("dictContext", configuration.getDictContext());
		session.setAttribute("language", configuration.getLemmaDescription().getLanguageName(true));
		return mv;
	}
	
	@ModelAttribute("pageTitle")
	private String getHtmlPageTitle() {
		return configuration.getLongName();
	}

	@ModelAttribute("user")
	private MaalrUserInfo currentUser(final HttpServletRequest request, Principal principal) {
		if (principal != null) {
			final String userName = principal.getName();
//			logger.info("CURRENT USER : " + principal);
			if (authProvider.loggedIn())
				if (userName != null) {
					MaalrUserInfo user = (MaalrUserInfo) request.getSession().getAttribute("user");
					if (user == null) {
						user = users.getByLogin(userName);
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
		mv.addObject("dictContext", configuration.getDictContext());
		mv.addObject("search", query);
		
		try {
			// if (query.getSearchPhrase() != null && query.getSearchPhrase().trim().length() > 0) {
			// 		setPageTitle(mv, "Translations of " + query.getSearchPhrase());
			// }
			QueryResult result = index.query(query, true);
			mv.addObject("result", result);
			return mv;
		} catch (Exception e) {
			return getErrorView(e);
		}
	}

	@RequestMapping("/login")
	public ModelAndView login(HttpSession session, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("login");
		setPageTitle(mv, getLocalizedString("maalr.login_page.title", session, request));
		mv.addObject("dictContext", configuration.getDictContext());
		mv.addObject("userForm", new UserForm());
		return mv;
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST, produces="application/json", consumes="application/json")
	public @ResponseBody UserFormValidationResponse processRegistration(@RequestBody UserForm userForm, HttpSession session, HttpServletRequest request) {
		return accountService.createAccount(userForm, getLocale(session, request));
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
			mv.addObject("dictContext", configuration.getDictContext());
			String firstLanguage = Configuration.getInstance().getLemmaDescription().getFirstLanguage().getId();
			String language = query.getValue("language");
			boolean isFirst = firstLanguage.equals(language);
			QueryResult result = index.queryExact(query.getValue("searchPhrase"), isFirst, true);
			mv.addObject("result", result);
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
		} catch (Exception e) {
			return getErrorView(e);
		}
	}

	@RequestMapping("/admin/admin")
	public ModelAndView admin() {
		ModelAndView mv = new ModelAndView("admin/admin");
		mv.addObject("dictContext", configuration.getDictContext());
		return mv;
	}

	@RequestMapping("/maalr")
	public ModelAndView maalr() {
		ModelAndView mv = new ModelAndView("static/maalr");
		mv.addObject("dictContext", configuration.getDictContext());
		setPageTitle(mv, "About Maalr");
		return mv;
	}

	@RequestMapping("/browse")
	public ModelAndView newAlphaList(@ModelAttribute("query") MaalrQuery query,
			BindingResult br, HttpSession session, HttpServletRequest request) {
		try {
			return newAlphaList(query.getValue("language"), "A", 0,
					query, br, session, request);
		} catch (Exception e) {
			return getErrorView(e);
		}
	}

	private ModelAndView getErrorView(Exception e) {
		logger.error("An error occurred", e);
		ModelAndView mv = new ModelAndView("error");
		mv.addObject("dictContext", configuration.getDictContext());
		return mv;
	}

	@RequestMapping("/editor/editor")
	public ModelAndView editor() {
		ModelAndView mv = new ModelAndView("editor/editor");
		mv.addObject("dictContext", configuration.getDictContext());
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
		ModelAndView mv = new ModelAndView("browse_dictionary");
		mv.addObject("dictContext", configuration.getDictContext());
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
}
