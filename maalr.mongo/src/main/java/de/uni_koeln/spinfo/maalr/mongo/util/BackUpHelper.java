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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_koeln.spinfo.maalr.common.server.util.Configuration;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.common.shared.NoDatabaseAvailableException;
import de.uni_koeln.spinfo.maalr.mongo.core.Database;
import de.uni_koeln.spinfo.maalr.mongo.stats.BackupInfos;
import de.uni_koeln.spinfo.maalr.mongo.stats.FileInfo;

/**
 * Utility class for handling scheduled backups.
 * 
 * @author Mihail Atanassov (atanassov.mihail@gmail.com)
 * 
 */
public class BackUpHelper {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private static BackUpHelper instance;

	private Timer timer;

	private ScheduledBackUp scheduledBackUp;
	
	private String dir;

	public enum Period { 
		
		WEEKLY(604800000L), DAILY(86400000L), HOURLY(3600000L), PER_MINUTE(600000L);
		
		private long time;

		private Period(long time) {
			this.time = time;
		}
		
		public long getTime() {
			return time;
		}
		
	}

	public static synchronized BackUpHelper getInstance() {
		if (instance == null) {
			instance = new BackUpHelper();
		}
		return instance;
	}

	public void setBackup(Period p, final String time, final String dir, int nums, boolean now) {
		this.dir = dir;
		
		if(timer != null) timer.cancel();
		
		Date executionTime = now ? new Date() : getTriggerTime(time);
		
		timer = new Timer();
		scheduledBackUp = new ScheduledBackUp(dir, nums);
		timer.schedule(scheduledBackUp, executionTime, getPeriod(p));
		logger.info("Next backup starts on [" + executionTime + "] period [" + p + "]");
	}
	
	private List<File> getScheduledBackUpFiles() {
		File directory = new File(dir);
		directory.mkdirs();
		File[] listFiles = directory.listFiles();
		List<File> files = new ArrayList<File>();
		for (File file : listFiles)
			if(file.getName().endsWith(".zip"))
				files.add(file);
		return files;
	}
	
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
				fileInfo.setCreationDate(String.format("date/%s time/%s", split[5], split[6]));
				list.add(fileInfo);
			} catch (Exception e) {
				logger.warn("Invalid backup file name: " + file.getAbsolutePath());
			}
		}
		BackupInfos backupInfos = new BackupInfos(list);
		backupInfos.setBackupDir(Configuration.getInstance().getBackupLocation());
		backupInfos.setBackupNum(Configuration.getInstance().getBackupNums());
		backupInfos.setBackupTime(Configuration.getInstance().getTriggerTime());
		return backupInfos;
	}
	
	public void cancel() {
		if (timer != null) timer.cancel();
	}
	
	public File latestBackup() {
		return scheduledBackUp.getLatestBackup();
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

	class ScheduledBackUp extends TimerTask {

		private File dir;
		private StringBuilder fileName;
		private int num;
		private List<File> files;
		private File latestBackup;

		ScheduledBackUp(final String dir, int nums) {
			this.num = nums;
			this.dir = new File(dir);
			this.dir.mkdirs();
		}

		@Override
		public void run() {
			checkBackupNum();
			File backupFile = triggerBackUp(createName());
			boolean valid = validate();
			if(!valid && backupFile != null) {
				boolean deleted = false; //backupFile.delete();
				if(deleted) {
					logger.info("Deleted invalid backup " + backupFile);
				} else {
					logger.warn("Failed to delete invalid backup " + backupFile);
				}
			}
		}
		
		List<File> getBackUpFiles() {
			return this.files;
		}
		
		File getLatestBackup() {
			return this.latestBackup;
		}

		private void checkBackupNum() {
			files = getFiles();
			if (files.size() >= num) {
				removeOldest(files);	
			}
		}
		
		private List<File> getFiles() {
			File[] listFiles = dir.listFiles();
			List<File> files = new ArrayList<File>();
			FileFilter filter = getFileFilter();
			for (File file : listFiles) {
				if (filter.accept(file))
					files.add(file);
			}
			return files;
		}

		private void removeOldest(List<File> files) {
			sort(files);
			for (File file : files) {
				logger.info("file: " + file.getName());
			}
			File toRemove = files.remove(0);
			boolean deleted = toRemove.delete();
			if(!deleted) {
				logger.warn("Failed to delete backup file " + toRemove + "!");
			} else {
				logger.info("Deleted backup file: " + toRemove.getName());				
			}
		}

		private void sort(List<File> files) {
			Collections.sort(files, new Comparator<File>() {
				@Override
				public int compare(File o1, File o2) {
					Date date = new Date(o1.lastModified());
					Date anotherDate = new Date(o2.lastModified());
					return date.compareTo(anotherDate);
				}
			});
		}

		private FileFilter getFileFilter() {
			return new FileFilter() {
				@Override
				public boolean accept(File f) {
					return f.exists() && f.getName().endsWith(".zip") && f.canRead() && !f.isHidden();
				}
			};
		}

		private File triggerBackUp(final String fileName) {
			File backupFile = new File(dir, fileName + ".zip");
			try {
				Database.getInstance().exportData(true, false, new FileOutputStream(backupFile), fileName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return backupFile;
		}

		private boolean validate() {
			File file = new File(dir, fileName.toString() + ".zip");
			if (getFileFilter().accept(file)) {
				try {
					logger.info("Validating backup " + file);
					FileInputStream input = new FileInputStream(file);
					Iterator<LexEntry> data = Database.getInstance().getExportedData(input);
					int counter = 0;
					while(data.hasNext()) {
						Validator.validate(data.next());
						counter++;
					}
					logger.info("Validated " + counter + " entries.");
					this.latestBackup = file;
					files.add(file);
					logger.info(logMessage(true));
					input.close();
					return true;
				} catch (Exception e) {
					e.printStackTrace();
				}
				logger.error(logMessage(false));
				return false;
			}
			logger.error(logMessage(false));
			return false;
		}

		private String logMessage(boolean b) {
			if (b)
				return "Scheduled backup successfully completed on "
						+ new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss")
								.format(new Date());
			else
				return "BackUp failed on "
						+ new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss")
								.format(new Date())
						+ " For more information see log-files...";
		}

		private String createName() {
			fileName = new StringBuilder();
			fileName.append("maalr_db_dump_");
			fileName.append("all_versions_");
			fileName.append(new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()));
			return fileName.toString();
		}
	}

}
