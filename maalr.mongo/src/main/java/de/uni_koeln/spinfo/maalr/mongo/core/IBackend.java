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
package de.uni_koeln.spinfo.maalr.mongo.core;

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.mongo.exceptions.InvalidEntryException;

public interface IBackend {
	
	/*
	 *  * - Eintrag hinzufügen (3,4,5), Bei 1 und 2 nur Vorschlag
	 * - Eintrag aktualisieren  (3,4,5), Bei 1 und 2 nur Vorschlag
	 * - Eintrag löschen (3,4,5), Bei 2 nur Vorschlag
	 * - Auf existierende Version zurücksetzen (3,4,5)
	 * - Eintrag bewerten (2,3,4,5)
	 * - Eintrag übernehmen (3,4,5)
	 */
	
	
	public void insert(LemmaVersion entry) throws InvalidEntryException;
	
	public void update(LemmaVersion oldEntry, LemmaVersion newEntry) throws InvalidEntryException;
	
	public void delete(LemmaVersion entry) throws InvalidEntryException;
	
	public void restore(LemmaVersion invalid, LemmaVersion valid) throws InvalidEntryException;
	
	public void rate(LemmaVersion entry, boolean rateUp) throws InvalidEntryException;
	
	public void accept(LemmaVersion entry) throws InvalidEntryException;

	/*
	 * Separate Usecases:
	 * 
	 * - Datenbank erzeugen mit Antlr (Nur Admin)
	 * - Datenbank indexieren mit Lucene (Nur Admin)
	 * - Lucene-Index aktualisieren (Automatisiert, sonst nur Admin)
	 * 
	 * 
	 * Benutzer-bezogene DB-Aktionen:
	 * 
	 * 5 User: 
	 * 1. Gast ohne Anmeldung,
	 * 2. Gast mit Anmeldung über OpenID,
	 * 3. vertrauenswürdige, externe Autoren, 
	 * 4. interne Autoren,
	 * 5. Admin mit Vollzugriffsrechten (nicht über's Web).
	 * 
	 * TODO: User-DB!
	 *
	 * - Eintrag hinzufügen (3,4,5), Bei 1 und 2 nur Vorschlag
	 * - Eintrag aktualisieren  (3,4,5), Bei 1 und 2 nur Vorschlag
	 * - Eintrag löschen (3,4,5), Bei 2 nur Vorschlag
	 * - Auf existierende Version zurücksetzen (3,4,5)
	 * - Eintrag bewerten (2,3,4,5)
	 * - Eintrag übernehmen (3,4,5)
	 * 
	 * -> Anforderung an Daten-Datenbank:
	 * 
	 * Jeder Eintrag enthält zusätzlich zu den Nutzdaten:
	 *  - Timestamp der Änderung/Version
	 *  - ID des Benutzers
	 *  - ggfs. Liste offener Änderungsvorschläge
	 *  
	 *  
	 *  LEMMA | USW. | TimeStamp | ID | List<Änderung>
	 *  haus  | ...  | 1234      | 1  | 
	 *  
	 *  Änderung ist äquivalent zu Lemma, aber zusätzlich:
	 *  - markiert als unüberprüft/Vorschlag (d.h. ignoriert in Lucene-Index)
	 *  - Verweis (ID) auf geändertes Lemma (bei Änderung) bzw. null (bei Neueintrag)
	 *  - 
	 * 
	 */
	
	
}
