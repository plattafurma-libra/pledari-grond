package de.uni_koeln.spinfo.maalr.webapp.service;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import de.uni_koeln.spinfo.maalr.common.server.util.Configuration;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.common.shared.LightLemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.NoDatabaseAvailableException;
import de.uni_koeln.spinfo.maalr.common.shared.description.UseCase;
import de.uni_koeln.spinfo.maalr.mongo.core.Converter;
import de.uni_koeln.spinfo.maalr.mongo.core.Database;
import de.uni_koeln.spinfo.maalr.mongo.core.MD5OutputStream;

@Service("scheduledExportCreater")
public class ScheduledExportCreater {

	private static final Logger LOG = LoggerFactory.getLogger(ScheduledExportCreater.class);

	private static final String PATH_XML = "formats/xml/";
	private static final String PATH_JSON = "formats/json/";
	private static final String XML_INFIX = "_data_xml";
	private static final String JSON_INFIX = "_data_json";
	private static final String CSV_INFIX = "_data_csv";
	private static final String PATH_CSV = "formats/csv/";
	private static final String ZIP_SUFFIX = ".zip";
	private static final String UTF_8 = "UTF-8";
	private static final String NEW_LINE_SEPARATOR = "\n";
	
	@Scheduled(initialDelayString = "${export.initial.delay}", fixedRateString = "${export.fixed.rate}")
	public void schedule() throws InterruptedException, ExecutionException {
		try {
			
			LOG.info("creating csv export...");
			exportCSV(Database.getInstance().getAll());
			
			LOG.info("creating xml export...");
			exportXML(Database.getInstance().getAll());
			
			LOG.info("creating json export...");
			exportJSON(Database.getInstance().getAll());
			
		} catch (NoDatabaseAvailableException | NoSuchAlgorithmException
				| IOException e) {
			LOG.error("public export failed with error: {}", e);
		}
		
	}
	
	private void exportCSV(DBCursor cursor) throws IOException, NoSuchAlgorithmException {
		File dir = createDirs(PATH_CSV);
		String fileName = createFileName(CSV_INFIX);
		File file = new File(dir, fileName + ZIP_SUFFIX);
		FileOutputStream fos = new FileOutputStream(file, false);
		exportDataCSV(fos, fileName, cursor);
	}
	
	private void exportXML(DBCursor cursor) throws IOException, NoSuchAlgorithmException {
		File dir = createDirs(PATH_XML);
		String fileName = createFileName(XML_INFIX);
		File file = new File(dir, fileName + ZIP_SUFFIX);
		FileOutputStream fos = new FileOutputStream(file, false);
		exportDataXml(fos, fileName, cursor);
	}
	
	private void exportJSON(DBCursor cursor) throws IOException, NoSuchAlgorithmException {
		File dir = createDirs(PATH_JSON);
		String fileName = createFileName(JSON_INFIX);
		File file = new File(dir, fileName + ZIP_SUFFIX);
		FileOutputStream fos = new FileOutputStream(file, false);
		exportDataJson(fos, fileName, cursor);
	}

	private File createDirs(String dirPath) {
		File dir = new File(dirPath);
		dir.mkdirs();
		return dir;
	}

	private String createFileName(String formInfix) {
		return Configuration.getInstance().getMaalrImpl() + formInfix;
	}
	
	private void exportDataCSV(OutputStream outputStream, String fileName, DBCursor cursor) throws IOException {
		
		File tmp = null;
		BufferedReader bufferedReader = null;
		ZipOutputStream zipOutputStream = null;
		CSVPrinter csvFilePrinter = null;

		try {
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
			zipOutputStream = new ZipOutputStream(bufferedOutputStream, Charset.forName(UTF_8));
			ZipEntry zipEntry = new ZipEntry(fileName + ".csv");
			zipOutputStream.putNextEntry(zipEntry);

			List<String> header = new ArrayList<>();
			header.addAll(Configuration.getInstance().getLemmaDescription().getFields(UseCase.FIELDS_FOR_ADVANCED_EDITOR, true));
			header.addAll(Configuration.getInstance().getLemmaDescription().getFields(UseCase.FIELDS_FOR_ADVANCED_EDITOR, false));
			
			CSVFormat csvFileFormat = CSVFormat.MYSQL.withHeader(header.toArray(new String[header.size()])).withRecordSeparator(NEW_LINE_SEPARATOR);
			tmp = new File(fileName + ".csv");
			csvFilePrinter = new CSVPrinter(new FileWriter(tmp), csvFileFormat);
			
			while (cursor.hasNext()) {
				DBObject object = cursor.next();
				LexEntry entry = Converter.convertToLexEntry(object);
				LemmaVersion lemmaVersion = entry.getCurrent();
				if (lemmaVersion != null) {
					List<String> dataRecord = new ArrayList<>();
					for (String key : header) {
						dataRecord.add(lemmaVersion.getEntryValue(key));
					}
					csvFilePrinter.printRecord(dataRecord);
				}
			}
			bufferedReader = new BufferedReader (new FileReader(tmp));
			int len;
			while((len = bufferedReader.read()) != -1) {
				zipOutputStream.write(len);
			}
		} finally {
			
			csvFilePrinter.close();
			tmp.delete();
			bufferedReader.close();
			zipOutputStream.closeEntry();
			zipOutputStream.close();
			
		}
	}
	
	private void exportDataJson(OutputStream outputStream, String fileName, DBCursor cursor) throws JsonGenerationException,
			JsonMappingException, IOException, NoSuchAlgorithmException {
		
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
		ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream);
		ZipEntry zipEntry = new ZipEntry(fileName + ".json");
		zipOutputStream.putNextEntry(zipEntry);
		
		JSONArray entries = new JSONArray();
		while (cursor.hasNext()) {
			DBObject object = cursor.next();
			LexEntry entry = Converter.convertToLexEntry(object);
			LemmaVersion copy = entry.getCurrent();
			if (copy != null) {
				//JSONObject version = new JSONObject();
				LightLemmaVersion lemmaVersion = new LightLemmaVersion(copy);
				entries.put(lemmaVersion.getEntryValues());
			}
		}
		
		String jsonData = entries.toString(1);
		byte[] bytes = jsonData.getBytes();
		for (byte b : bytes) {
			zipOutputStream.write(b);
		}
		
		zipOutputStream.closeEntry();
		zipOutputStream.close();
		
	}
	
	private void exportDataXml(OutputStream os, String fileName, DBCursor cursor) throws IOException,NoSuchAlgorithmException {
		
		ZipOutputStream zout = new ZipOutputStream(new BufferedOutputStream(os));
		zout.putNextEntry(new ZipEntry(fileName + ".xml"));
		MD5OutputStream md5 = new MD5OutputStream(zout);

		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(md5, UTF_8));
		out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		out.write("\n<entries>\n");

		JAXBContext context;
		Marshaller marshaller;
		try {
			context = JAXBContext.newInstance(LightLemmaVersion.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, UTF_8);
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT,true);

			while (cursor.hasNext()) {
				DBObject object = cursor.next();
				LexEntry entry = Converter.convertToLexEntry(object);
				LemmaVersion copy = entry.getCurrent();
				if (copy != null) {
					marshaller.marshal(new LightLemmaVersion(copy), out);
				}
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		out.write("\n</entries>\n");
		out.close();
		zout.closeEntry();
		zout.close();
	}

}