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
package de.uni_koeln.spinfo.maalr.login;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 * This filter will put the current login into the "Mapped Diagnostic Context", which is used by
 * slf4j to add additional information to the logging output.
 * 
 * @see http://logback.qos.ch/manual/mdc.html
 * @author schwieb
 *
 */
@Service("springLogUserLogin")
public class UserToMdcFilter implements javax.servlet.Filter
{
	
	@Autowired
	LoginManager loginManager;
	
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	HttpServletRequest h = (HttpServletRequest) request;
    	/*
    	 * Set the "src" field to either contain the name of the current thread
    	 * (if logging server-side messages) or the string "REMOTE GWT", if a
    	 * log event has been passed from a client.
    	 */
    	if(h.getRequestURI().endsWith("remote_logging")) {
    		MDC.put("src", "REMOTE GWT");
    	} else {
    		MDC.put("src", Thread.currentThread().getName());
    	}
    	/*
    	 * Set the "login" field to either contain the id of the currently
    	 * logged in user, or the ip address of the request.
    	 */
    	String login = (String) h.getSession().getAttribute("uname");
    	if(login == null) {
    	//	logger.info("Querying for login");
    		login = loginManager.getCurrentUserId();
        	if(login == null || "anonymousUser".equals(login)) {
        		login = h.getRemoteAddr();
        	}
        //	logger.info("Setting login to " + login);
           h.getSession().setAttribute("uname", login);
    	}
    	 MDC.put("login", login);
         try {
             chain.doFilter(request, response);
         } finally {
             MDC.remove("login");
             MDC.remove("src");
         }
    }

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Nothing to do
	}

	@Override
	public void destroy() {
		// Nothing to do
	}

}
