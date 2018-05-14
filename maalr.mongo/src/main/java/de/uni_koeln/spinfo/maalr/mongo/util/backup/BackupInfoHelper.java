package de.uni_koeln.spinfo.maalr.mongo.util.backup;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.uni_koeln.spinfo.maalr.mongo.stats.BackupInfos;
import de.uni_koeln.spinfo.maalr.mongo.stats.FileInfo;

@Service("backupInfoHelper")
public class BackupInfoHelper extends AbstractBackupHelper {

	private static final Logger LOG = LoggerFactory
			.getLogger(BackupInfoHelper.class);

	@Value("${backup.location:backup}")
	private String backupDir;

	@Value("${backup.num:7}")
	private String backupNum;

	/**
	 * Returns a wrapper class for displaying backup information within the gwt
	 * admin-module.
	 */
	public BackupInfos getBackupInfos() {

		LOG.debug("admin-gwt-module called backup infos...");

		List<FileInfo> list = new ArrayList<FileInfo>();

		List<File> backupFiles = listBackupFilesAsc(backupDir);

		try {
			for (File file : backupFiles) {

				list.add(new FileInfo().absolutePath(file.getAbsolutePath())
						.lastModified(getLastModified(file))
						.name(file.getName())
						.parent(file.getParentFile().getAbsolutePath())
						.creationDate(getCreationDate(file))
						.size(getSize(file)));
			}
		} catch (Exception e) {
			LOG.error("error occured: {}", e);
		}

		return new BackupInfos(list)
				.backupDir(backupDir)
				.backupNum(backupNum);
	}

	private String getSize(File file) {
		return new DecimalFormat("##.##").format((file.length() / 1024) / 1024);
	}

	private String getCreationDate(File file) {
		return new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss")
				.format(new Date(file.lastModified()));
	}

	private String getLastModified(File file) {
		return new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss")
				.format(new Date(file.lastModified()));
	}

}
