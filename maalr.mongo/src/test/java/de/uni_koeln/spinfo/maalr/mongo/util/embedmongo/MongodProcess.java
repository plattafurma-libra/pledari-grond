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
package de.uni_koeln.spinfo.maalr.mongo.util.embedmongo;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.flapdoodle.embedmongo.Files;
import de.flapdoodle.embedmongo.io.BlockLogWatchProcessor;
import de.flapdoodle.embedmongo.io.IBlockProcessor;
import de.flapdoodle.embedmongo.io.Processors;
import de.flapdoodle.embedmongo.runtime.Network;
import de.flapdoodle.embedmongo.runtime.ProcessControl;

public class MongodProcess {

	static final Logger _logger = LoggerFactory.getLogger(MongodProcess.class);

	private final MongodConfig _config;
	private final MongodExecutable _mongodExecutable;
	private ProcessControl _process;
	private int _mongodProcessId;

	private File _dbDir;

	boolean _stopped = false;

	private Distribution _distribution;

	public MongodProcess(Distribution distribution, MongodConfig config, MongodExecutable mongodExecutable)
			throws IOException {
		_config = config;
		_mongodExecutable = mongodExecutable;
		_distribution = distribution;

		try {
			File dbDir;
			if (config.getDatabaseDir() != null) {
				dbDir = Files.createOrCheckDir(config.getDatabaseDir());
			} else {
				dbDir = Files.createTempDir("embedmongo-db");
				_dbDir = dbDir;
			}
			//			ProcessBuilder processBuilder = new ProcessBuilder(enhanceCommandLinePlattformSpecific(distribution,
			//					getCommandLine(_config, _mongodExecutable.getFile(), dbDir)));
			//			processBuilder.redirectErrorStream();
			//			_process = new ProcessControl(processBuilder.start());
			_process = ProcessControl.fromCommandLine(Mongod.enhanceCommandLinePlattformSpecific(distribution,
					Mongod.getCommandLine(_config, _mongodExecutable.getFile(), dbDir)));

			Runtime.getRuntime().addShutdownHook(new JobKiller());

			IBlockProcessor monoOut = new IBlockProcessor() {
				
				StringBuilder sb = new StringBuilder();
				
				@Override
				public synchronized void process(String block) {
					sb.append(block);
					while(sb.length() > 0) {
						int br = sb.indexOf("\n");
					if(br > -1) {
						String string = sb.substring(0, br).substring(20);		
						if(string.startsWith("[conn") || string.startsWith("[interruptThread]") || string.startsWith("[FileAllocator]")) {
							_logger.debug(string);
						} else {
							_logger.info(string);	
						}
						
						sb.replace(0, br+1, "");
					} else {
						break;
					}
					}
					
				}
				
				@Override
				public void onProcessed() {
					// TODO Auto-generated method stub
					
				}
			};
			BlockLogWatchProcessor logWatch = new BlockLogWatchProcessor("waiting for connections on port", "failed", monoOut );
			Processors.connect(_process.getReader(), logWatch);
			Processors.connect(_process.getError(), Processors.namedConsole("[mongod error]"));
			logWatch.waitForResult(20000);
			
//			LogWatch logWatch = LogWatch.watch(_process.getReader(), "waiting for connections on port", "failed", 20000);
			if (logWatch.isInitWithSuccess()) {
				_mongodProcessId = Mongod.getMongodProcessId(logWatch.getOutput(), -1);
//				ConsoleOutput consoleOutput = new ConsoleOutput(_process.getReader());
			} else {
				throw new IOException("Could not start mongod process");
			}

		} catch (IOException iox) {
			stop();
			throw iox;
		}
	}


	public synchronized void stop() {
		if (!_stopped) {

			_logger.info("try to stop mongod");
			if (!sendKillToMongodProcess()) {
				_logger.warn("could not stop mongod, try next");
				if (!sendStopToMongoInstance()) {
					_logger.warn("could not stop mongod with db command, try next");
					if (!tryKillToMongodProcess()) {
						_logger.error("could not stop mongod the second time!!");
					}
				}
			}

			_process.stop();

			if ((_dbDir != null) && (!Files.forceDelete(_dbDir)))
				_logger.warn("Could not delete temp db dir: " + _dbDir);

//			if (_mongodExecutable.getFile() != null) {
//				if (!Files.forceDelete(_mongodExecutable.getFile())) {
//					_stopped = true;
//					_logger.warning("Could not delete mongod executable NOW: " + _mongodExecutable.getFile());
//				}
//			}
		}
	}

	private boolean sendStopToMongoInstance() {
		try {
			return Mongod.sendShutdown(Network.getLocalHost(), _config.getPort());
		} catch (UnknownHostException e) {
			_logger.error("Failed to send stop message", e);
		}
		return false;
	}

	private boolean sendKillToMongodProcess() {
		if (_mongodProcessId != -1) {
			return ProcessControl.killProcess(_distribution.getPlatform(), _mongodProcessId);
		}
		return false;
	}

	private boolean tryKillToMongodProcess() {
		if (_mongodProcessId != -1) {
			return ProcessControl.tryKillProcess(_distribution.getPlatform(), _mongodProcessId);
		}
		return false;
	}

	class JobKiller extends Thread {

		@Override
		public void run() {
			MongodProcess.this.stop();
		}
	}
}
