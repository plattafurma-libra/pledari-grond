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
package de.uni_koeln.spinfo.maalr.mongo.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import de.uni_koeln.spinfo.maalr.common.server.util.Configuration;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.mongo.core.Database;
import de.uni_koeln.spinfo.maalr.mongo.exceptions.BackUpHelperException;
import de.uni_koeln.spinfo.maalr.mongo.stats.BackupInfos;
import de.uni_koeln.spinfo.maalr.mongo.stats.FileInfo;

/**
 * Utility class for handling scheduled backups.
 * 
 * @author Mihail Atanassov (atanassov.mihail@gmail.com)
 * 
 */
@Service
public class BackUpHelper {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	Map<String, ScheduledBackUp> cache = new HashMap<>();

	public enum Period { 
		
		WEEKLY(604800000L), DAILY(86400000L), HOURLY(3600000L), PER_MINUTE(600000L);
		
		private Long time;

		private Period(long time) {
			this.time = time;
		}
		
		public Long getTime() {
			return time;
		}

	}

	public void setBackup(Period p, final String time, final String parent, boolean now) throws BackUpHelperException {
		
		if(parent == null) {
			throw new BackUpHelperException("Backup directory can not be null.");
		}
		
		ScheduledBackUp scheduledBackUp = cache.get(parent);
		
		if(null == scheduledBackUp) {
			
			Date executionTime = now ? new Date() : getTriggerTime(time);
			
			logger.info("Backup starts on {} period {}", executionTime, p);
			
			scheduledBackUp = new ScheduledBackUp(parent);
			
			Timer timer = new Timer();
			timer.schedule(scheduledBackUp, executionTime, getPeriod(p));
			
			cache.put(parent, scheduledBackUp);
		}
	}
	
	private List<File> getScheduledBackUpFiles() {
		List<File> files = new ArrayList<File>();
		for (String parent : cache.keySet()) {
			
			File directory = new File(parent);
			directory.mkdirs();
			File[] listFiles = directory.listFiles();
			
			for (File file : listFiles) {
				if(file.getName().endsWith(".zip")) {
					files.add(file);
				}
			}
		}
		
		return files;
	}
	
	/**
	 * Returns a wrapper class (pojo) for displaying backup information within the admin-gwt-view.
	 */
	public BackupInfos getBackupInfos() {
		List<FileInfo> list = new ArrayList<FileInfo>();
		List<File> scheduledBackUpFiles = getScheduledBackUpFiles();
		for (File file : scheduledBackUpFiles) {
			
			try {
				FileInfo fileInfo = new FileInfo();
				fileInfo.setAbsolutePath(file.getAbsolutePath());
				
				fileInfo.setLastModified(new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss").format(new Date(file.lastModified())));
				fileInfo.setName(file.getName());
				fileInfo.setParent(file.getParentFile().getAbsolutePath());
				
				double bytes = file.length();
				double kilobytes = (bytes / 1024);
				double megabytes = (kilobytes / 1024);
				
				fileInfo.setSize(new DecimalFormat("##.##").format(megabytes));
				
				String[] split = fileInfo.getName().replace(".zip", "").split("_");
				fileInfo.setCreationDate(String.format("date/%s time/%s", split[3], split[4]));
				list.add(fileInfo);
			} catch (Exception e) {
				logger.warn("Invalid backup file name: {}", file.getAbsolutePath());
				logger.error("Error occured: {}", e);
			}
		}
		BackupInfos backupInfos = new BackupInfos(list);
		backupInfos.setBackupDir(Configuration.getInstance().getBackupLocation());
		backupInfos.setBackupNum(Configuration.getInstance().getBackupNums());
		backupInfos.setBackupTime(Configuration.getInstance().getTriggerTime());
		return backupInfos;
	}
	
	private long getPeriod(Period p) {
		switch (p) {
			case WEEKLY: return p.getTime();
			case DAILY: return p.getTime();
			case HOURLY: return p.getTime();
			case PER_MINUTE: return p.getTime();
			default: return 0L;
		}
	}

	private Date getTriggerTime(final String triggerTime) {
		String[] split = triggerTime.split(":");
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(split[0]));
		c.set(Calendar.MINUTE, Integer.parseInt(split[1]));
		c.set(Calendar.SECOND, Integer.parseInt(split[2]));
		return c.getTime();
	}

	private class ScheduledBackUp extends TimerTask {

		private File parent;
		
		private static final int MAX_COUNT = 10; 

		ScheduledBackUp(final String parent) {
			this.parent = new File(parent);
			this.parent.mkdirs();
		}

		@Override
		public void run() {
			
			final String backupFileName = createName();
			
			File backupFile = triggerBackUp(parent, backupFileName);
			
			boolean valid = validate(parent, backupFileName);
			
			if(valid && backupFile != null) {
				List<File> files = listFiles();
				if (files.size() > MAX_COUNT) {
					cleanup(files);	
				}
			}
		}
		
		private File triggerBackUp(final File parent, final String fileName) {
			File backupFile = new File(parent, fileName);
			try {
				Database.getInstance().exportData(true, false, new FileOutputStream(backupFile), fileName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return backupFile;
		}
		
		private void cleanup(List<File> files) {
			sort(files);
			File toRemove = files.get(0);
			toRemove.delete();
		}
		
		private List<File> listFiles() {
			return Arrays.asList(parent.listFiles(getFileFilter()));
		}
		
		private FileFilter getFileFilter() {
			return new FileFilter() {
				@Override
				public boolean accept(File f) {
					return f.exists() && f.getName().endsWith(".zip") && f.canRead() && !f.isHidden();
				}
			};
		}
		
		private void sort(List<File> files) {
			Collections.sort(files, new Comparator<File>() {
				@Override
				public int compare(File o1, File o2) {
					Date d1 = new Date(o1.lastModified());
					Date d2 = new Date(o2.lastModified());
					return d1.compareTo(d2);
				}
			});
		}

		private boolean validate(File parent, final String fileName) {
			File file = new File(parent, fileName);
			if (getFileFilter().accept(file)) {
				try (FileInputStream input = new FileInputStream(file)){
					Iterator<LexEntry> data = Database.getInstance().getExportedData(input);
					while(data.hasNext()) {
						Validator.validate(data.next());
					}
					return true;
				} catch (Exception e) {
					logger.info("Error occured: {}", e);
				}
				return false;
			}
			return false;
		}

		private String createName() {
			return String.format("maalr_db_dump_%s.zip", new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()));
		}
	}

}
