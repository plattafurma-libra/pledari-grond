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
package de.uni_koeln.spinfo.maalr.webapp.ui.common.shared.util;

import java.util.logging.Level;

/**
 * Helper to simplify java logging
 * @author stephan
 *
 */
public class Logger {
	
	private java.util.logging.Logger delegate;
	
	public Logger(Class<?> cls) {
		delegate = java.util.logging.Logger.getLogger(cls.getName());
	}

	public static Logger getLogger(Class<?> cls) {
		return new Logger(cls);
	}
	
	public void debug(String msg) {
		delegate.fine(msg);
	}
	
	public void warn(String msg) {
		delegate.log(Level.WARNING, msg);
	}
	
	public void warn(String msg, Throwable ex) {
		delegate.log(Level.WARNING, msg, ex);
	}
	
	public void error(String msg) {
		delegate.log(Level.SEVERE, msg);
	}
	
	public void error(String msg, Throwable ex) {
		delegate.log(Level.SEVERE, msg, ex);
	}

	public void info(String msg) {
		delegate.info(msg);
	}
	

}
