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
package de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.general;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.uni_koeln.spinfo.maalr.common.shared.DatabaseException;
import de.uni_koeln.spinfo.maalr.common.shared.NoDatabaseAvailableException;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.IndexException;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.NoIndexAvailableException;
import de.uni_koeln.spinfo.maalr.lucene.stats.IndexStatistics;
import de.uni_koeln.spinfo.maalr.mongo.stats.BackupInfos;
import de.uni_koeln.spinfo.maalr.mongo.stats.DatabaseStatistics;
import de.uni_koeln.spinfo.maalr.sigar.info.SigarSummary;

@RemoteServiceRelativePath("rpc/backend")
public interface BackendService extends RemoteService {
	
	public void importDatabase(int maxItems) throws Exception;

	String dropDatabase() throws DatabaseException;

	String reloadDatabase() throws DatabaseException, IndexException;

	String rebuildIndex() throws NoDatabaseAvailableException, IndexException;

	DatabaseStatistics getDatabaseStats() throws NoDatabaseAvailableException;

	IndexStatistics getIndexStats() throws NoIndexAvailableException;
	
	SigarSummary getSystemSummary();

	BackupInfos getBackupInfos();

}
