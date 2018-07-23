package de.uni_koeln.spinfo.maalr.mongo.util.backup;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public abstract class AbstractBackupHelper {

	protected List<File> listBackupFilesAsc(String backupDir) {
		List<File> backupFiles = Arrays.asList(new File(backupDir).listFiles(filter()));

		Collections.sort(backupFiles, new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				Date d1 = new Date(o1.lastModified());
				Date d2 = new Date(o2.lastModified());
				return d1.compareTo(d2);
			}
		});
		return backupFiles;
	}

	protected FileFilter filter() {
		return new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.exists() && f.getName().endsWith(".zip") && f.canRead() && !f.isHidden();
			}
		};
	}

}