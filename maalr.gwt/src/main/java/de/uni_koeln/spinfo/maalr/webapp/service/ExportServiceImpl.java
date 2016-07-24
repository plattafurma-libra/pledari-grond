package de.uni_koeln.spinfo.maalr.webapp.service;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Future;

import javax.xml.bind.JAXBException;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import de.uni_koeln.spinfo.maalr.common.shared.NoDatabaseAvailableException;
import de.uni_koeln.spinfo.maalr.mongo.core.Database;
import de.uni_koeln.spinfo.maalr.webapp.controller.ExportController.Format;

@Component
public class ExportServiceImpl implements ExportService {
	
	@Async
	@Override
	public Future<File> export(Format format) {
		try {
			String export = Database.getInstance().export(false);
			return new AsyncResult<File>(new File(export));
		} catch (NoDatabaseAvailableException | NoSuchAlgorithmException
				| JAXBException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
