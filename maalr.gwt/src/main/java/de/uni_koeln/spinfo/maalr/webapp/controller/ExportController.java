package de.uni_koeln.spinfo.maalr.webapp.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

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
	
	@Secured({ Constants.Roles.GUEST_1, Constants.Roles.TRUSTED_EX_3, Constants.Roles.TRUSTED_IN_4, Constants.Roles.ADMIN_5 })
	@RequestMapping(value = "/export/data/xml")
	public void getXml(HttpServletResponse response) throws InterruptedException, ExecutionException, IOException {
		File export = exportService.export(Format.XML);
		response.setContentType(Format.XML.getContentType());
		response.setHeader("Content-Disposition", "attachment; filename=" + export.getName()); 
		stream(response, export);
	}

	@Secured({ Constants.Roles.GUEST_1, Constants.Roles.TRUSTED_EX_3, Constants.Roles.TRUSTED_IN_4, Constants.Roles.ADMIN_5 })
	@RequestMapping(value = "/export/data/json")
	public void getJSON(HttpServletResponse response) throws FileNotFoundException, IOException {
		File export = exportService.export(Format.JSON);
		response.setContentType(Format.JSON.getContentType());
		response.setHeader("Content-Disposition", "attachment; filename=" + export.getName()); 
		stream(response, export);
	}
	
	@Secured({ Constants.Roles.GUEST_1, Constants.Roles.TRUSTED_EX_3, Constants.Roles.TRUSTED_IN_4, Constants.Roles.ADMIN_5 })
	@RequestMapping(value = "/export/data/csv")
	public void getCSV(HttpServletResponse response) throws FileNotFoundException, IOException {
		File export = exportService.export(Format.CSV);
		response.setContentType(Format.CSV.getContentType());
		response.setHeader("Content-Disposition", "attachment; filename=" + export.getName()); 
		stream(response, export);
	}
	
	private void stream(HttpServletResponse response, File export) throws FileNotFoundException, IOException {
		InputStream is = new FileInputStream(export);
		IOUtils.copy(is, response.getOutputStream());
		response.flushBuffer();
	}
	
}