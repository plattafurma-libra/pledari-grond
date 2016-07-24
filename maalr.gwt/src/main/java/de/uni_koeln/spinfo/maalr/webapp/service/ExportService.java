package de.uni_koeln.spinfo.maalr.webapp.service;

import java.io.File;
import java.util.concurrent.Future;

import org.springframework.stereotype.Service;

import de.uni_koeln.spinfo.maalr.webapp.controller.ExportController.Format;

@Service
public interface ExportService {

	Future<File> export(Format format);

}
