package de.uni_koeln.spinfo.maalr.mongo.util.backup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.mongo.core.Database;
import de.uni_koeln.spinfo.maalr.mongo.exceptions.ScheduledBackupException;
import de.uni_koeln.spinfo.maalr.mongo.util.Validator;

@Service("scheduledBackupHelper")
public class ScheduledBackupHelper extends AbstractBackupHelper {

	private static final Logger LOG = LoggerFactory.getLogger(ScheduledBackupHelper.class);

	@Value("${backup.location:backup}")
	private String backupDir;

	@Value("${db.name:rumantsch}")
	private String dbName;

	@Scheduled(initialDelayString = "${backup.initial.delay}", fixedRateString = "${backup.fixed.rate}")
	public void backup() throws ScheduledBackupException {

		LOG.info("started scheduled backup");

		String backupFileName = buildName();

		LOG.info("backup file name created: {}", backupFileName);

		File backupFile = buidlBackup(backupDir, backupFileName);

		LOG.info("backup file created: {}", backupFile.getAbsolutePath());

		if (valid(backupFile)) {

			LOG.info("backup file valid");

			cleanup();

		} else {

			throw new ScheduledBackupException("back up file is not valid.");
		}

		LOG.info("finished scheduled backup");
	}

	private File buidlBackup(String dir, String backupFileName) {

		File backupFile = new File(dir, backupFileName);

		try {

			Database.getInstance().exportData(true, false, new FileOutputStream(backupFile), backupFileName);

		} catch (Exception e) {
			LOG.error("error occured... {}", e);
		}

		return backupFile;
	}

	private void cleanup() throws ScheduledBackupException {

		List<File> backupFiles = listBackupFilesAsc(backupDir);

		try {
			if (backupFiles.size() >= 7) {
				Files.delete(backupFiles.get(0).toPath());
			}

		} catch (Exception e) {
			throw new ScheduledBackupException(
					String.format("could not delete obsolete backup file: %s", backupFiles.get(0).getAbsolutePath()));
		}

		LOG.info("list of backupFiles...");

		for (File file : backupFiles) {

			LOG.info("{}", file);

		}

	}

	private boolean valid(File backupFile) {

		if (null == backupFile) {
			return false;
		}

		if (!filter().accept(backupFile)) {
			return false;
		}

		try (FileInputStream input = new FileInputStream(backupFile)) {

			Iterator<LexEntry> data = Database.getInstance().getExportedData(input);

			while (data.hasNext()) {
				Validator.validate(data.next());
			}

			return true;

		} catch (Exception e) {

			LOG.error("error occured: {}", e);

			return false;
		}

	}

	private String buildName() {
		return String.format("%s_db_dump_%s.zip", dbName,
				new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()));
	}

}
