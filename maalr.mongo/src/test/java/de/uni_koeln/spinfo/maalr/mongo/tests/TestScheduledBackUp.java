/*******************************************************************************
 * Copyright 2013 Sprachliche Informationsverarbeitung, University of Cologne
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uni_koeln.spinfo.maalr.mongo.tests;


public class TestScheduledBackUp {

//	private static MongodExecutable mongodExe;
//	private static MongodProcess mongod;
//	private static Database db;
//	private static Logger logger = LoggerFactory.getLogger(System.class);
//	
//	@BeforeClass
//	public static void startTestMongoDB() throws Exception {
//		MongoDBRuntime runtime = MongoDBRuntime.getDefaultInstance();
//		mongodExe = runtime.prepare(new MongodConfig(Version.V2_2_0, 27017, Network.localhostIsIPv6()));
//		mongod = mongodExe.start();
//		URI resource = System.class.getResource("/data_rm.tab.zip").toURI();
//		File file = new File(resource);
//		createFromSQLDump(file, 2000);
//	}
//	
//	@AfterClass
//	public static void stopTestMongoDB() {
//		db.deleteAllEntries();
//		mongodExe.cleanup();
//		mongod.stop();
//	}
//
//	public static void createFromSQLDump(File file, int maxEntries) throws IOException, NoDatabaseAvailableException, InvalidEntryException {
//		logger.info("createFromSQLDump...");
//		if (!file.exists()) {
//			logger.info("No data to import - file " + file + " does not exist.");
//			return;
//		}
//		BufferedReader br;
//		ZipFile zipFile = null;
//		if (file.getName().endsWith(".zip")) {
//			logger.info("Trying to read data from zip file=" + file.getName());
//			zipFile = new ZipFile(file);
//			String entryName = file.getName().replaceAll(".zip", "");
//			ZipEntry entry = zipFile.getEntry(entryName);
//			if (entry == null) {
//				logger.info("No file named " + entryName + " found in zip file - skipping import");
//				zipFile.close();
//				return;
//			}
//			br = new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry), "UTF-8"));
//		} else {
//			logger.info("Trying to read data from file " + file);
//			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
//		}
//
//		String line = br.readLine();
//		String[] keys = line.split("\t", -1);
//		db = Database.getInstance();
//		List<DBObject> entries = new ArrayList<DBObject>();
//		int counter = 0;
//		String userId = "admin";
//		while ((line = br.readLine()) != null) {
//			String[] values = line.split("\t", -1);
//			if (values.length != keys.length) {
//				logger.warn("Ignoring entry: Attribute mismatch (" + values.length + " entries found, " + keys.length + " entries expected) in line " + line);
//				continue;
//			}
//			LemmaVersion version = new LemmaVersion();
//			for (int i = 0; i < keys.length; i++) {
//				String value = values[i].trim();
//				String key = keys[i].trim();
//				if (value.length() == 0)
//					continue;
//				if (key.length() == 0)
//					continue;
//				version.setValue(key, value);
//			}
//			LexEntry entry = new LexEntry(version);
//			entry.setCurrent(version);
//			entry.getCurrent().setStatus(Status.NEW_ENTRY);
//			entry.getCurrent().setVerification(Verification.ACCEPTED);
//			long timestamp = System.currentTimeMillis();
//			String embeddedTimeStamp = version.getEntryValue(LemmaVersion.TIMESTAMP);
//			if (embeddedTimeStamp != null) {
//				timestamp = Long.parseLong(embeddedTimeStamp);
//				version.removeEntryValue(LemmaVersion.TIMESTAMP);
//			}
//			entry.getCurrent().setUserId(userId);
//			entry.getCurrent().setTimestamp(timestamp);
//			entry.getCurrent().setCreatorRole(Role.ADMIN_5);
//			entries.add(Converter.convertLexEntry(entry));
//			logger.info("entry: " + entry);
//			if (entries.size() == 10000) {
//				db.insertBatch(entries);
//				entries.clear();
//			}
//			counter++;
//			if (counter == maxEntries) {
//				logger.warn("Skipping db creation, as max entries is " + maxEntries);
//				break;
//			}
//		}
//		db.insertBatch(entries);
//		entries.clear();
//		br.close();
//		if (zipFile != null) {
//			zipFile.close();
//		}
//		logger.info("Dataloader initialized!");
//	}
//	
//	@Test
//	public void findOne() {
//		String value = "Winterfrost";
//		Iterator<LexEntry> entries = db.getEntries();
//		int count = 0;
//		while(entries.hasNext()) {
//			LexEntry next = entries.next();
//			LemmaVersion current = next.getCurrent();
//			String entryValue = current.getEntryValue("DStichwort");
//			if(entryValue.equals(value)) {
//				count++;
//				break;
//			}
//		}
//		Assert.assertEquals(1, count);
//		
//	}
//	
//	@Test
//	public void testResource() throws URISyntaxException {
//		File file = new File(getClass().getResource("/data.tab.zip").toURI());
//		Assert.assertNotNull(file);
//		Assert.assertTrue(file.exists());
//		Assert.assertTrue(file.canRead());
//	}
//
//	@Test
//	public void backupEveryMinute() throws UnknownHostException, InterruptedException {
//		Assert.assertTrue(true);
//
//		// Read config
//		String backupLocation = Configuration.getInstance().getBackupLocation();
//		String triggerTime = Configuration.getInstance().getTriggerTime();
//		int backupNums = Integer.parseInt(Configuration.getInstance().getBackupNums());
//
//		Assert.assertNotNull(backupLocation);
//		Assert.assertNotNull(triggerTime);
//		Assert.assertNotNull(backupNums);
//
//		System.out.println("backupLocation=" + backupLocation);
//		System.out.println("triggerTime=" + triggerTime);
//		System.out.println("backupNums=" + backupNums);
//
//		BackUpHelper helper = BackUpHelper.getInstance();
//		helper.setBackup(BackUpHelper.Period.PER_MINUTE, triggerTime, backupLocation, backupNums, true);
//
//		// 60000 ms = 1 min
//		Thread.sleep(60000);
//
//		List<File> files = helper.getScheduledBackUpFiles();
//		
//		Assert.assertTrue(files.size() > 0 || files.size() <= 7);
//		
//		for (File file : files) 
//			logger.info("backup file: " + file.getAbsolutePath());
//		
//		logger.info("latest backup: " + helper.latestBackup());
//	}

}
