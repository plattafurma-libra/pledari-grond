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
package de.uni_koeln.spinfo.maalr.common.server.util;

import java.io.File;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription;

public class DescriptionLoader {
	
	private static LemmaDescription lemmaDescription;

	public static LemmaDescription getLemmaDescription(Properties properties) {
		if(lemmaDescription != null) {
			return lemmaDescription;
		}
		try {
			JAXBContext ctx = JAXBContext.newInstance(LemmaDescription.class);
			Unmarshaller unmarshaller = ctx.createUnmarshaller();
			lemmaDescription = (LemmaDescription) unmarshaller.unmarshal(new File(properties.getProperty("lemma.file")));
			return lemmaDescription;
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}
	
}
