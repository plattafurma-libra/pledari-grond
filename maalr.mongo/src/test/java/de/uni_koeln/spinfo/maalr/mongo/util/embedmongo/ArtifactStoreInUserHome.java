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

import de.flapdoodle.embedmongo.config.IArtifactStoragePathNaming;


public class ArtifactStoreInUserHome implements IArtifactStoragePathNaming {
	static final String STORE_POSTFIX=".embedmongo";
	
	@Override
	public String getPath() {
		String homePath = System.getProperty("user.home");
		File f = new File(homePath);
		File store = new File(f,STORE_POSTFIX);
		return store.getAbsolutePath();
		//return System.getProperty("user.home") + "/"+STORE_POSTFIX+"/";//"/.embedmongo/";
	}
}
