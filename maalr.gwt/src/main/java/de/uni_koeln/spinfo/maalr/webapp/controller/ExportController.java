package de.uni_koeln.spinfo.maalr.webapp.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import de.uni_koeln.spinfo.maalr.common.shared.Constants;
import de.uni_koeln.spinfo.maalr.webapp.service.ExportService;

@Controller
public class ExportController {
	
	@Autowired
	private ExportService exportService;
	
	public enum Format {
		
		XML("application/xml"), JSON("application/json"), CSV("application/csv");
		
		private String contentType;
		
		Format(String contentType) {
			this.contentType = contentType;
		}
		
		public String getContentType() {
			return contentType;
		}
	}
	
	
	@Secured( { Constants.Roles.PERSONA, Constants.Roles.OPENID_2, Constants.Roles.TRUSTED_IN_4 })
	@RequestMapping(value = "/export/data/xml")
	public void getXml(HttpServletResponse response) throws InterruptedException, ExecutionException, IOException {
		Future<File> export = exportService.export(Format.XML);
		File tmpFile = export.get();
		response.setContentType(Format.XML.getContentType());
		response.setHeader("Content-Disposition", "attachment; filename=" + tmpFile.getName()); 
		InputStream is = new FileInputStream(export.get());
		IOUtils.copy(is, response.getOutputStream());
		response.flushBuffer();
		tmpFile.delete();
	}
	
	@Secured( { Constants.Roles.PERSONA, Constants.Roles.OPENID_2, Constants.Roles.TRUSTED_IN_4 })
	@RequestMapping(value = "/export/data/json")
	public void getJSON(HttpServletResponse response) {
		exportService.export(Format.JSON);
	}
	
	@Secured( { Constants.Roles.PERSONA, Constants.Roles.OPENID_2, Constants.Roles.TRUSTED_IN_4 })
	@RequestMapping(value = "/export/data/csv")
	public void getCSV(HttpServletResponse response) {
		exportService.export(Format.CSV);
	}
	

}
