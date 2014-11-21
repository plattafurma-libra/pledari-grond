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
package de.uni_koeln.spinfo.maalr.sigar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.SystemUtils;
import org.hyperic.sigar.Sigar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SigarWrapper {

	private static ScheduledExecutorService executors;
	
	private static Logger logger = LoggerFactory.getLogger(SigarWrapper.class);

	static {
		initialize();
	}
	
	private static void initialize() {
		logger.info("Initializing...");
		String libBase = "/hyperic-sigar-1.6.5/sigar-bin/lib/";
		try {
			String prefix = "libsigar";
			if (SystemUtils.IS_OS_WINDOWS) {
				prefix = "sigar";
			}
			String arch = getSystemArch();
			String suffix = getSystemSuffix();
			String resourceName = prefix + "-" + arch + "-" + suffix;
			
			logger.info("Native library: " + resourceName);

			String resource = libBase + resourceName;
			URL toLoad = SigarWrapper.class.getResource(resource);
			File home = new File(System.getProperty("user.home"));
			File libs = new File(home, ".maalr/libs/sigar");
			libs.mkdirs();
			File tmp = new File(libs, resourceName);
			tmp.deleteOnExit();
			FileOutputStream fos = new FileOutputStream(tmp);
			InputStream in = toLoad.openStream();
			int i;
			while ((i = in.read()) != -1) {
				fos.write(i);
			}
			in.close();
			fos.close();
			logger.info("Library copied to " + tmp.getAbsolutePath());
			String oldPath = System.getProperty("java.library.path");
			System.setProperty("java.library.path", oldPath + SystemUtils.PATH_SEPARATOR + libs.getAbsolutePath());
			logger.info("java.library.path updated: " + System.getProperty("java.library.path"));
			executors =	Executors.newScheduledThreadPool(1, new ThreadFactory() {
				
				public Thread newThread(Runnable r) {
					Thread t = new Thread(r);
					t.setDaemon(true);
					return t;
				}
			});
			logger.info("Initialization completed.");
		} catch (Exception e) {
			logger.error("Failed to initialize!", e);
		}
	}
	
	public static Sigar getSigarInstance() {
		return new Sigar();
	}
	
	public static SigarReporter newReporter(int milliSeconds) {
		SigarReporter reporter = new SigarReporter(getSigarInstance());
		executors.scheduleWithFixedDelay(reporter, 0, milliSeconds, TimeUnit.MILLISECONDS);
		return reporter;
	}
	
	private static String getSystemArch() {
		String arch = System.getProperty("os.arch");
		if(SystemUtils.IS_OS_MAC_OSX) {
			if(arch.contains("64")) {
				return "universal64";
			}
			return "universal";
		}
		if(SystemUtils.IS_OS_WINDOWS) {
			if(arch.contains("64")) {
				return "amd64";
			}
			return "x86";
		}
		if(SystemUtils.IS_OS_AIX) {
			if(arch.contains("64")) {
				return "ppc64";
			}
			return "ppc";
		}
		if(SystemUtils.IS_OS_SOLARIS) {
			if(arch.contains("64")) {
				return "sparc64";
			}
			return "sparc";
		}
		if(SystemUtils.IS_OS_FREE_BSD) {
			if(arch.contains("64")) {
				return "amd64";
			}
			return "x86";
		}
		if(SystemUtils.IS_OS_HP_UX) {
			if(arch.contains("64")) {
				return "ia64";
			}
			return "pa";
		}
		if(SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_UNIX) {
			if(arch.contains("64")) {
				return "amd64";
			}
			return "x86";
		}
		
				return null;
	}

	private static String getSystemSuffix() {
		if (SystemUtils.IS_OS_AIX) {
			return "aix-5.so";
		}
		if (SystemUtils.IS_OS_FREE_BSD) {
			return "freebsd-6.so";
		}
		if (SystemUtils.IS_OS_HP_UX) {
			return "hpux-11.sl";
		}
		if (SystemUtils.IS_OS_WINDOWS) {
			return "winnt.dll";
		}
		if (SystemUtils.IS_OS_MAC_OSX) {
			return "macosx.dylib";
		}
		if (SystemUtils.IS_OS_SOLARIS) {
			return "solaris.so";
		}
		if(SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_UNIX) {
			return "linux.so";
		}
		throw new RuntimeException("Unknown OS: " + System.getProperty("os.name"));
	}

}
