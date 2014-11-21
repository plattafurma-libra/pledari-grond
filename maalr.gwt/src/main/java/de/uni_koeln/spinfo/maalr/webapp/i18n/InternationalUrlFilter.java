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
package de.uni_koeln.spinfo.maalr.webapp.i18n;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;


@Service("internationalUrlFilter")
public class InternationalUrlFilter implements Filter {
	
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		//long start = System.nanoTime();
		HttpServletRequest h = (HttpServletRequest) request;
		String uri = h.getRequestURI();
		String path = h.getContextPath();
		Locale userLocale = request.getLocale();
		h.getSession().setAttribute("lang", userLocale.getLanguage());
		String url = uri.substring(path.length());
		/*
		 * Idee: die URL in ihre Bestandteile zerlegen und die einzelnen Teile
		 * in einem Keyword-Tree matchen. Auf jeder Stufe wird dabei die eigentliche
		 * URL um ein neues Element ergänzt.
		 * 
		 * Funktioniert auf diese Weise gut für statische URLs, aufbau eines Baums
		 * für URLs mit fest definierten Klassen (z.B. Übersetzungsrichtung) ist ebenfalls möglich.
		 * 
		 * Funktioniert aber nicht für dynamische Bestandteile - da muss eine Wildcard
		 * rein.
		 * 
		 * :wörterbuch:deutsch-rumantsch:nase.html oder .json oder .xml
		 * 
		 * Baum:
		 * wörterbuch -> dictionary
		 *		deutsch-rumantsch -> tudesg->rumantsch
		 *			* -> *
		 *				ending
		 * 
		 * Filter-Funktionalität:
		 * 
		 * a) Prüfen, ob die URL schon in die Zielsprache übersetzt wurde. Falls ja:
		 * doFilter() aufrufen, sonst übersetzen (andernfalls Endlos-Schleife).
		 * 
		 * b) die gewählte Sprache irgendwo in der Session hinterlegen, damit der
		 * Rest des Programms (GUI) entsprechend der URL auch die richtigen Elemente
		 * darstellt.
		 * 
		 * c) Hilfsklasse notwendig, die URLs entsprechend der Sprache generiert,
		 * z.B. für dictionary (s.o.), aber auch für "translate", einschließlich der
		 * Durchblättern-Funktion. HTTP-GET sollte auch übersetzt werden (also Parameter-Namen),
		 * POST nicht.
		 * 
		 * d) Die Generierung des Baums darf lange dauern, die Abfrage muss aber
		 * schnell sein - also z.B. Pattern.compile() beim Aufbau des Baums, nicht
		 * beim Abfragen. 
		 *  
		 */
		if ("/hilfe.html".equals(url) || "/help.html".equals(url)) {
			// change url and forward
			RequestDispatcher dispatcher = request
					.getRequestDispatcher("/agid.html");
		//	long end = System.nanoTime();
			dispatcher.forward(request, response);
		} else {
		//	long end = System.nanoTime();
			chain.doFilter(request, response);
		}
    }

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

}
