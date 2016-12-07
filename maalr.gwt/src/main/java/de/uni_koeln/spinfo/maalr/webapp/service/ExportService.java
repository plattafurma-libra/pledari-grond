package de.uni_koeln.spinfo.maalr.webapp.service;

import java.io.File;

import org.springframework.stereotype.Service;

import de.uni_koeln.spinfo.maalr.webapp.controller.ExportController.Format;

@Service
public interface ExportService {

	File export(Format format);

}