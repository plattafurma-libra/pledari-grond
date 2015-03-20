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
package de.uni_koeln.spinfo.maalr.conjugator.generator;

import java.util.HashMap;

import org.slf4j.LoggerFactory;

import de.uni_koeln.spinfo.maalr.common.server.IOverlayGenerator;
import de.uni_koeln.spinfo.maalr.common.shared.GenerationFailedException;
import de.uni_koeln.spinfo.maalr.conjugator.generator.ConjugationGenerator;

/**
 * Generates verb forms based on the given verb class and the infinitive. The
 * generation is done by delegating the method call to an instance of
 * {@link ConjugationGenerator}.
 * 
 * The instances of this class are created with ClassLoader API within the editor
 * service implementation {@link EditorServiceImpl#getOverlayEditorPreset(String overlayType,
			String presetId, String base)}
 */
public class VerbClassGenerator implements IOverlayGenerator {

	public HashMap<String, String> buildPreset(String presetId, String infinitive) throws GenerationFailedException {
		ConjugationGenerator generator = new ConjugationGenerator();
		Integer preset = Integer.parseInt(presetId);
		try {
			return generator.generateConjugation(infinitive, preset);
		} catch (Exception e) {
			LoggerFactory.getLogger(getClass()).warn("Failed to generate word forms", e);
			throw new GenerationFailedException("Failed to generate word forms for infinitive '" 
					+ infinitive + "': " + e.getMessage());
		}
	}

}
