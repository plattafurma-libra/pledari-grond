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
package de.uni_koeln.spinfo.pg.mvc;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import de.uni_koeln.spinfo.maalr.common.server.util.Configuration;

@Controller
public class PledariController {

	private Configuration configuration = Configuration.getInstance();

	private void setPageTitle(ModelAndView mv, String title) {
		mv.addObject("dictContext", configuration.getDictContext());
		mv.addObject("pageTitle", title);
	}

	@ModelAttribute("pageTitle")
	private String getHtmlPageTitle() {
		return configuration.getLongName();
	}

	@RequestMapping("/help")
	public ModelAndView agid() {
		ModelAndView mv = new ModelAndView("static/agid");
		mv.addObject("dictContext", configuration.getDictContext());
		setPageTitle(mv, "Help");
		return mv;
	}

	@RequestMapping("/agidplug-in")
	public ModelAndView agidPlugin() {
		ModelAndView mv = new ModelAndView("static/agidplug-in");
		mv.addObject("dictContext", configuration.getDictContext());
		setPageTitle(mv, "Search Plug-In");
		return mv;
	}

	@RequestMapping("/infos")
	public ModelAndView infos() {
		ModelAndView mv = new ModelAndView("static/infos");
		mv.addObject("dictContext", configuration.getDictContext());
		setPageTitle(mv, "Infos");
		return mv;
	}

	@RequestMapping("/embed")
	public ModelAndView embed() {
		ModelAndView mv = new ModelAndView("static/json");
		mv.addObject("dictContext", configuration.getDictContext());
		setPageTitle(mv, "Embed");
		return mv;
	}

	@RequestMapping("/iframe")
	public ModelAndView iframe() {
		ModelAndView mv = new ModelAndView("static/iframe");
		mv.addObject("dictContext", configuration.getDictContext());
		return mv;
	}

	@RequestMapping("/abreviaziuns")
	public ModelAndView abbreviations() {
		ModelAndView mv = new ModelAndView("static/abreviaziuns");
		mv.addObject("dictContext", configuration.getDictContext());
		setPageTitle(mv, "Abreviaziuns");
		return mv;
	}

	@RequestMapping("/feeds/latest")
	@ResponseBody
	public List<String> latestFeeds() throws IOException {
		String[] urls = { "http://liarumantscha.ch/?ctrl=feed&type=1",
				"http://liarumantscha.ch/?ctrl=feed&type=2",
				"http://liarumantscha.ch/?ctrl=feed&type=3" };

		List<String> toReturn = new ArrayList<>();
		for (String url : urls) {
			URL connect = new URL(url);
			BufferedInputStream bis = new BufferedInputStream(
					connect.openStream());
			int i;
			StringBuffer sb = new StringBuffer();
			while ((i = bis.read()) != -1) {
				sb.append((char) i);
			}
			bis.close();
			toReturn.add(sb.toString());
		}
		return toReturn;
	}
}
