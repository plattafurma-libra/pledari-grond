package de.uni_koeln.spinfo.maalr.webapp.service;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import de.uni_koeln.spinfo.maalr.common.server.util.Configuration;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.common.shared.NoDatabaseAvailableException;
import de.uni_koeln.spinfo.maalr.common.shared.description.UseCase;
import de.uni_koeln.spinfo.maalr.mongo.core.Converter;
import de.uni_koeln.spinfo.maalr.mongo.core.Database;
import de.uni_koeln.spinfo.maalr.mongo.core.MD5OutputStream;

public class ExportFormatScheduler {


	private static ExportFormatScheduler instance;

	private Logger logger = LoggerFactory.getLogger(getClass());

	private ScheduledExecutorService scheduledExecutorService;
	
	private static final String PATH_XML = "formats/xml/";

	private static final String PATH_JSON = "formats/json/";

	private static final String XML_INFIX = "_data_xml_";

	private static final String JSON_INFIX = "_data_json_";
	
	private static final String CSV_INFIX = "_data_csv_";

	private static final String PATH_CSV = "formats/csv/";
	
	private static final String ZIP_SUFFIX = ".zip";
	
	private static final String UTF_8 = "UTF-8";
	
	private ExportFormatScheduler() {
		// Singleton
	}

	public void run() throws InterruptedException, ExecutionException {

		if (scheduledExecutorService != null)
			return;

		logger.info("Scheduled format export started!");
		scheduledExecutorService = Executors.newScheduledThreadPool(2);

		ScheduledFuture<?> scheduledFuture = scheduledExecutorService
				.scheduleAtFixedRate(new Runnable() {

					@Override
					public void run() {
						// TODO: 
						logger.info("Running the export format task... periodically (every three minutes)");
						try {
							logger.info("Creating open data (.xml) export...");
							exportXML(Database.getInstance().getAll());
							logger.info("Creating open data (.json) export...");
							exportJSON(Database.getInstance().getAll());
							logger.info("Creating open data (.csv) export...");
							exportCSV(Database.getInstance().getAll());
						} catch (NoDatabaseAvailableException
								| NoSuchAlgorithmException | IOException e) {
							e.printStackTrace();
						}
					}

				}, 0, 3, TimeUnit.MINUTES);
	}

	public static synchronized ExportFormatScheduler getInstance() {
		if (instance == null) {
			instance = new ExportFormatScheduler();
		}
		return instance;
	}
	
	private void exportCSV(DBCursor cursor) throws IOException, NoSuchAlgorithmException {
		File dir = createDirs(PATH_CSV);
		String fileName = createFileName(CSV_INFIX);
		File file = new File(dir, fileName + ZIP_SUFFIX);
		FileOutputStream fos = new FileOutputStream(file, false);
		exportDataCSV(fos, fileName, cursor);
	}
	
	public void exportXML(DBCursor cursor) throws IOException, NoSuchAlgorithmException {
		File dir = createDirs(PATH_XML);
		String fileName = createFileName(XML_INFIX);
		File file = new File(dir, fileName + ZIP_SUFFIX);
		FileOutputStream fos = new FileOutputStream(file, false);
		exportDataXml(fos, fileName, cursor);
	}
	
	public void exportJSON(DBCursor cursor) throws IOException, NoSuchAlgorithmException {
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
		return Configuration.getInstance().getMaalrImpl() + formInfix 
				+ DateFormat.getDateInstance().format(new Date());
	}
	
	private void exportDataCSV(OutputStream os, String fileName, DBCursor cursor) throws IOException {
		
		ZipOutputStream zout = new ZipOutputStream(new BufferedOutputStream(os));
		zout.putNextEntry(new ZipEntry(fileName + ".csv"));
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(zout, UTF_8));
		
		List<String> header = new ArrayList<>();
		header.addAll(Configuration.getInstance().getLemmaDescription().getFields(UseCase.FIELDS_FOR_ADVANCED_EDITOR, true));
		header.addAll(Configuration.getInstance().getLemmaDescription().getFields(UseCase.FIELDS_FOR_ADVANCED_EDITOR, false));
		
		CSVFormat csvFileFormat = CSVFormat.MYSQL.withHeader(header.toArray(new String[header.size()]));
		CSVPrinter csvFilePrinter = new CSVPrinter(out, csvFileFormat);
		
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
		csvFilePrinter.flush();
		zout.closeEntry();
		zout.close();
	}

	
	private void exportDataJson(OutputStream os, String fileName, DBCursor cursor) throws JsonGenerationException,
			JsonMappingException, IOException, NoSuchAlgorithmException {

		ZipOutputStream zout = new ZipOutputStream(new BufferedOutputStream(os));
		zout.putNextEntry(new ZipEntry(fileName + ".json"));
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(zout, UTF_8));
		
		JSONArray entries = new JSONArray();
		while (cursor.hasNext()) {
			DBObject object = cursor.next();
			LexEntry entry = Converter.convertToLexEntry(object);
			LemmaVersion lemmaVersion = entry.getCurrent();
			if (lemmaVersion != null) {
				JSONObject version = new JSONObject();
				version.put("timeStamp", lemmaVersion.getTimestamp());
				version.put("values", lemmaVersion.getEntryValues());
				entries.put(version);
			}
		}
		out.write(entries.toString(1));
		out.flush();
		zout.closeEntry();
		zout.close();
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
			context = JAXBContext.newInstance(LemmaVersion.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, UTF_8);
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT,true);

			while (cursor.hasNext()) {
				DBObject object = cursor.next();
				LexEntry entry = Converter.convertToLexEntry(object);
				LemmaVersion version = entry.getCurrent();
				if (version != null) {
					marshaller.marshal(version, out);
				}
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		out.write("\n</entries>\n");
		out.flush();
		zout.closeEntry();
		zout.close();
	}

}
