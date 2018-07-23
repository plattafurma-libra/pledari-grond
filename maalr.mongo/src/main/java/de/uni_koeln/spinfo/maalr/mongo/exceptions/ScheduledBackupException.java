package de.uni_koeln.spinfo.maalr.mongo.exceptions;

public class ScheduledBackupException extends Exception {

	private static final long serialVersionUID = 1554960643927518116L;

	public ScheduledBackupException() {
		super();
	}

	public ScheduledBackupException(String message) {
		super(message);
	}

}