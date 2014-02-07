/*
 * Copyright 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package de.uni_koeln.spinfo.maalr.webapp.util;

import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.logging.server.StackTraceDeobfuscator;
import com.google.gwt.logging.shared.RemoteLoggingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Helper class to collect log messages from the client.
 * 
 * TODO: When in production, remote logging must be disabled by
 * a) removing the servlet declaration in web.xml and
 * b) remove the logging setup in the GWT modules.
 */
public class Slf4JRemoteLoggingService extends RemoteServiceServlet implements
		RemoteLoggingService {

	private static final long serialVersionUID = 7331320386155698140L;

	private static StackTraceDeobfuscator deobfuscator = new StackTraceDeobfuscator(
			"WEB-INF/deploy");

	public final String logOnServer(LogRecord lr) {
		String strongName = getPermutationStrongName();
		Logger logger = LoggerFactory.getLogger(lr.getLoggerName());
		if(lr.getLevel().intValue() <= Level.FINE.intValue()) {
			logger.debug(lr.getMessage());
			return null;
		}
		if (lr.getLevel() == Level.INFO) {
			if (lr.getThrown() != null) {
				lr = deobfuscator.deobfuscateLogRecord(lr, strongName);
				logger.info(lr.getMessage(), lr.getThrown());
			} else {
				logger.info(lr.getMessage());
			}
			return null;
		}
		if (lr.getLevel() == Level.WARNING) {
			if (lr.getThrown() != null) {
				lr = deobfuscator.deobfuscateLogRecord(lr, strongName);
				logger.warn(lr.getMessage(), lr.getThrown());
			} else {
				logger.warn(lr.getMessage());
			}
			return null;
		}
		if (lr.getLevel() == Level.SEVERE) {
			if (lr.getThrown() != null) {
				lr = deobfuscator.deobfuscateLogRecord(lr, strongName);
				logger.error(lr.getMessage(), lr.getThrown());
			} else {
				logger.error(lr.getMessage());
			}
			return null;
		}
		return null;
	}

}
