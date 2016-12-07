package de.uni_koeln.spinfo.maalr.webapp.service;

import java.io.File;
import java.io.FileFilter;

import org.springframework.stereotype.Component;

import de.uni_koeln.spinfo.maalr.webapp.controller.ExportController.Format;

@Component
public class ExportServiceImpl implements ExportService {

	@Override
	public File export(Format format) {
		File dir = null;
		switch (format) {
			case XML: dir = new File("formats/xml/"); break;
			case JSON: dir = new File("formats/json/"); break;
			case CSV: dir = new File("formats/csv/"); break;
			default: break;
			}
		if (dir == null)
			return null;

		if (dir.listFiles().length > 0) {
			File file = dir.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return pathname.getName().endsWith(".zip");
				}
			})[0];
			
			return file;
		}
		return null;
	}

}