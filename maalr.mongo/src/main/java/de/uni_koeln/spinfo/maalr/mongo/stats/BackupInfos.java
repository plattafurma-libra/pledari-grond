package de.uni_koeln.spinfo.maalr.mongo.stats;

import java.io.Serializable;
import java.util.List;

/**
 * Wrapper class for GWT module. Wraps general information about the scheduled
 * back up.
 * 
 * @author Mihail Atanassov (atanassov.mihail@gmail.com)
 * 
 */
public class BackupInfos implements Serializable {

	private static final long serialVersionUID = 753709468739198232L;

	private List<FileInfo> infos;

	private String backupLocation;

	private String backupNums;

	private String triggerTime;

	public BackupInfos() {
	}

	public BackupInfos(List<FileInfo> infos) {
		this.infos = infos;
	}

	public BackupInfos backupDir(String backupLocation) {
		this.setBackupDir(backupLocation);
		return this;
	}

	public void setBackupDir(String backupLocation) {
		this.backupLocation = backupLocation;
	}

	public void setBackupNum(String backupNums) {
		this.backupNums = backupNums;
	}

	public BackupInfos backupNum(String backupNums) {
		this.backupNums = backupNums;
		return this;
	}

	public void setBackupTime(String triggerTime) {
		this.triggerTime = triggerTime;
	}

	public BackupInfos backupTime(String triggerTime) {
		this.setBackupTime(triggerTime);
		return this;
	}

	public List<FileInfo> getInfos() {
		return infos;
	}

	public String getBackupLocation() {
		return backupLocation;
	}

	public String getBackupNums() {
		return backupNums;
	}

	public String getTriggerTime() {
		return triggerTime;
	}

}