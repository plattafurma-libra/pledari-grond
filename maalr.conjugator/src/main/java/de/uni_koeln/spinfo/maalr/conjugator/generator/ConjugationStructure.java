package de.uni_koeln.spinfo.maalr.conjugator.generator;

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

import java.util.HashMap;

public class ConjugationStructure {

	public static final String verb = "verb";
	public static final String infinitiv = "infinitiv";
	public static final String root = "root";
	public static final String ending = "ending";
	public static final String reflexive = "reflexive";
	public static final String conjugationclass = "conjugationclass";

	public static final String preschentsing1 = "preschentsing1";
	public static final String preschentsing2 = "preschentsing2";
	public static final String preschentsing3 = "preschentsing3";
	public static final String preschentplural1 = "preschentplural1";
	public static final String preschentplural2 = "preschentplural2";
	public static final String preschentplural3 = "preschentplural3";

	public static final String imperfectsing1 = "imperfectsing1";
	public static final String imperfectsing2 = "imperfectsing2";
	public static final String imperfectsing3 = "imperfectsing3";
	public static final String imperfectplural1 = "imperfectplural1";
	public static final String imperfectplural2 = "imperfectplural2";
	public static final String imperfectplural3 = "imperfectplural3";

	public static final String conjunctivsing1 = "conjunctivsing1";
	public static final String conjunctivsing2 = "conjunctivsing2";
	public static final String conjunctivsing3 = "conjunctivsing3";
	public static final String conjunctivplural1 = "conjunctivplural1";
	public static final String conjunctivplural2 = "conjunctivplural2";
	public static final String conjunctivplural3 = "conjunctivplural3";

	public static final String cundizionalsing1 = "cundizionalsing1";
	public static final String cundizionalsing2 = "cundizionalsing2";
	public static final String cundizionalsing3 = "cundizionalsing3";
	public static final String cundizionalplural1 = "cundizionalplural1";
	public static final String cundizionalplural2 = "cundizionalplural2";
	public static final String cundizionalplural3 = "cundizionalplural3";

	public static final String participperfectms = "participperfectms";
	public static final String participperfectfs = "participperfectfs";
	public static final String participperfectmp = "participperfectmp";
	public static final String participperfectfp = "participperfectfp";

	public static final String imperativ1 = "imperativ1";
	public static final String imperativ2 = "imperativ2";

	public static final String gerundium = "gerundium";

	public static final String futursing1 = "futursing1";
	public static final String futursing2 = "futursing2";
	public static final String futursing3 = "futursing3";
	public static final String futurplural1 = "futurplural1";
	public static final String futurplural2 = "futurplural2";
	public static final String futurplural3 = "futurplural3";

	public String[] msi = new String[] { preschentsing1, preschentsing2,
			preschentsing3, preschentplural1, preschentplural2,
			preschentplural3, imperfectsing1, imperfectsing2, imperfectsing3,
			imperfectplural1, imperfectplural2, imperfectplural3,
			conjunctivsing1, conjunctivsing2, conjunctivsing3,
			conjunctivplural1, conjunctivplural2, conjunctivplural3,
			cundizionalsing1, cundizionalsing2, cundizionalsing3,
			cundizionalplural1, cundizionalplural2, cundizionalplural3,
			participperfectms, participperfectfs, participperfectmp,
			participperfectfp, imperativ1, imperativ2, gerundium, futursing1,
			futursing2, futursing3, futurplural1, futurplural2, futurplural3

	};

	private HashMap<String, String> values;

	private String isReflexive;

	public HashMap<String, String> getValues() {
		return values;
	}

	public void setValues(HashMap<String, String> values) {
		this.values = values;

	}

	public ConjugationStructure() {
		this.values = new HashMap<String, String>();

	}

	public String getVerb() {
		return values.get(verb);
	}

	public String getInfinitiv() {
		return values.get(infinitiv);
	}

	public String getRoot() {
		return values.get(root);
	}

	public String getEnding() {
		return values.get(ending);
	}

	public String getReflexive() {
		return values.get(reflexive);

	}

	public String getConjugationclass() {
		return values.get(conjugationclass);
	}

	public String getPreschentsing1() {
		return values.get(preschentsing1);
	}

	public String getPreschentsing2() {
		return values.get(preschentsing2);
	}

	public String getPreschentsing3() {
		return values.get(preschentsing3);
	}

	public String getPreschentplural1() {
		return values.get(preschentplural1);
	}

	public String getPreschentplural2() {
		return values.get(preschentplural2);
	}

	public String getPreschentplural3() {
		return values.get(preschentplural3);
	}

	public String getImperfectsing1() {
		return values.get(imperfectsing1);
	}

	public String getImperfectsing2() {
		return values.get(imperfectsing2);
	}

	public String getImperfectsing3() {
		return values.get(imperfectsing3);
	}

	public String getImperfectplural1() {
		return values.get(imperfectplural1);
	}

	public String getImperfectplural2() {
		return values.get(imperfectplural2);
	}

	public String getImperfectplural3() {
		return values.get(imperfectplural3);
	}

	public String getConjunctivsing1() {
		return values.get(conjunctivsing1);
	}

	public String getConjunctivsing2() {
		return values.get(conjunctivsing2);
	}

	public String getConjunctivsing3() {
		return values.get(conjunctivsing3);
	}

	public String getConjunctivplural1() {
		return values.get(conjunctivplural1);
	}

	public String getConjunctivplural2() {
		return values.get(conjunctivplural2);
	}

	public String getConjunctivplural3() {
		return values.get(conjunctivplural3);
	}

	public String getCundizionalsing1() {
		return values.get(cundizionalsing1);
	}

	public String getCundizionalsing2() {
		return values.get(cundizionalsing2);
	}

	public String getCundizionalsing3() {
		return values.get(cundizionalsing3);
	}

	public String getCundizionalplural1() {
		return values.get(cundizionalplural1);
	}

	public String getCundizionalplural2() {
		return values.get(cundizionalplural2);
	}

	public String getCundizionalplural3() {
		return values.get(cundizionalplural3);
	}

	public String getParticipperfectms() {
		return values.get(participperfectms);
	}

	public String getParticipperfectfs() {
		return values.get(participperfectfs);
	}

	public String getParticipperfectmp() {
		return values.get(participperfectmp);
	}

	public String getParticipperfectfp() {
		return values.get(participperfectfp);
	}

	public String getImperativ1() {
		return values.get(imperativ1);
	}

	public String getImperativ2() {
		return values.get(imperativ2);
	}

	public String getGerundium() {
		return values.get(gerundium);
	}

	public String getFutursing1() {
		return values.get(futursing1);
	}

	public String getFutursing2() {
		return values.get(futursing2);
	}

	public String getFutursing3() {
		return values.get(futursing3);
	}

	public String getFuturplural1() {
		return values.get(futurplural1);
	}

	public String getFuturplural2() {
		return values.get(futurplural2);
	}

	public String getFuturplural3() {
		return values.get(futurplural3);
	}

	public void setVerb(String v) {
		values.put(verb, v);
	}

	public void setInfinitiv(String inf) {
		values.put(infinitiv, inf);
	}

	public void setRoot(String r) {
		values.put(root, r);
	}

	public void setEnding(String end) {
		values.put(ending, end);
	}

	public void setReflexive(String ref) {
		values.put(reflexive, ref);
	}

	public void setConjugationClass(String cc) {
		values.put(conjugationclass, cc);
	}

	public void setPreschentsing1(String ps1) {
		values.put(preschentsing1, ps1);
	}

	public void setPreschentsing2(String ps2) {
		values.put(preschentsing2, ps2);
	}

	public void setPreschentsing3(String ps3) {
		values.put(preschentsing3, ps3);
	}

	public void setPreschentplural1(String pp1) {
		values.put(preschentplural1, pp1);
	}

	public void setPreschentplural2(String pp2) {
		values.put(preschentplural2, pp2);
	}

	public void setPreschentplural3(String pp3) {
		values.put(preschentplural3, pp3);
	}

	public void setImperfectsing1(String is1) {
		values.put(imperfectsing1, is1);
	}

	public void setImperfectsing2(String is2) {
		values.put(imperfectsing2, is2);
	}

	public void setImperfectsing3(String is3) {
		values.put(imperfectsing3, is3);
	}

	public void setImperfectplural1(String ip1) {
		values.put(imperfectplural1, ip1);
	}

	public void setImperfectplural2(String ip2) {
		values.put(imperfectplural2, ip2);
	}

	public void setImperfectplural3(String ip3) {
		values.put(imperfectplural3, ip3);
	}

	public void setConjunctivsing1(String cs1) {
		values.put(conjunctivsing1, cs1);
	}

	public void setConjunctivsing2(String cs2) {
		values.put(conjunctivsing2, cs2);
	}

	public void setConjunctivsing3(String cs3) {
		values.put(conjunctivsing3, cs3);
	}

	public void setConjunctivplural1(String cp1) {
		values.put(conjunctivplural1, cp1);
	}

	public void setConjunctivplural2(String cp2) {
		values.put(conjunctivplural2, cp2);
	}

	public void setConjunctivplural3(String cp3) {
		values.put(conjunctivplural3, cp3);
	}

	public void setCundizionalsing1(String cus1) {
		values.put(cundizionalsing1, cus1);
	}

	public void setCundizionalsing2(String cus2) {
		values.put(cundizionalsing2, cus2);
	}

	public void setCundizionalsing3(String cus3) {
		values.put(cundizionalsing3, cus3);
	}

	public void setCundizionalplural1(String cup1) {
		values.put(cundizionalplural1, cup1);
	}

	public void setCundizionalplural2(String cup2) {
		values.put(cundizionalplural2, cup2);
	}

	public void setCundizionalplural3(String cup3) {
		values.put(cundizionalplural3, cup3);
	}

	public void setParticipperfectms(String ppms) {
		values.put(participperfectms, ppms);
	}

	public void setParticipperfectfs(String ppfs) {
		values.put(participperfectfs, ppfs);
	}

	public void setParticipperfectmp(String ppmp) {
		values.put(participperfectmp, ppmp);
	}

	public void setParticipperfectfp(String ppfp) {
		values.put(participperfectfp, ppfp);
	}

	public void setImperativ1(String imp1) {
		values.put(imperativ1, imp1);
	}

	public void setImperativ2(String imp2) {
		values.put(imperativ2, imp2);
	}

	public void setGerundium(String ger) {
		values.put(gerundium, ger);
	}

	public void setFutursing1(String futs1) {
		values.put(futursing1, futs1);
	}

	public void setFutursing2(String futs2) {
		values.put(futursing2, futs2);
	}

	public void setFutursing3(String futs3) {
		values.put(futursing3, futs3);
	}

	public void setFuturplural1(String futp1) {
		values.put(futurplural1, futp1);
	}

	public void setFuturplural2(String futp2) {
		values.put(futurplural2, futp2);
	}

	public void setFuturplural3(String futp3) {
		values.put(futurplural3, futp3);
	}

	public String getIsReflexive() {
		return isReflexive;
	}

	public void setIsReflexive(String isReflexive) {
		this.isReflexive = isReflexive;
	}

	public String getValue(String key) {
		switch (key) {

		case verb:
			return getVerb();
		case infinitiv:
			return getInfinitiv();
		case root:
			return getRoot();
		case ending:
			return getEnding();
		case reflexive:
			return getReflexive();
		case conjugationclass:
			return getConjugationclass();
		case preschentsing1:
			return getPreschentsing1();
		case preschentsing2:
			return getPreschentsing2();
		case preschentsing3:
			return getPreschentsing3();
		case preschentplural1:
			return getPreschentplural1();
		case preschentplural2:
			return getPreschentplural2();
		case preschentplural3:
			return getPreschentplural3();
		case imperfectsing1:
			return getImperfectsing1();
		case imperfectsing2:
			return getImperfectsing2();
		case imperfectsing3:
			return getImperfectsing3();
		case imperfectplural1:
			return getImperfectplural1();
		case imperfectplural2:
			return getImperfectplural2();
		case imperfectplural3:
			return getImperfectplural3();
		case conjunctivsing1:
			return getConjunctivsing1();
		case conjunctivsing2:
			return getConjunctivsing2();
		case conjunctivsing3:
			return getConjunctivsing3();
		case conjunctivplural1:
			return getConjunctivplural1();
		case conjunctivplural2:
			return getConjunctivplural2();
		case conjunctivplural3:
			return getConjunctivplural3();
		case cundizionalsing1:
			return getCundizionalsing1();
		case cundizionalsing2:
			return getCundizionalsing2();
		case cundizionalsing3:
			return getCundizionalsing3();
		case cundizionalplural1:
			return getCundizionalplural1();
		case cundizionalplural2:
			return getCundizionalplural2();
		case cundizionalplural3:
			return getCundizionalplural3();
		case participperfectms:
			return getParticipperfectms();
		case participperfectfs:
			return getParticipperfectfs();
		case participperfectmp:
			return getParticipperfectmp();
		case participperfectfp:
			return getParticipperfectfp();
		case imperativ1:
			return getImperativ1();
		case imperativ2:
			return getImperativ2();
		case gerundium:
			return getGerundium();
		case futursing1:
			return getFutursing1();
		case futursing2:
			return getFutursing2();
		case futursing3:
			return getFutursing3();
		case futurplural1:
			return getFuturplural1();
		case futurplural2:
			return getFuturplural2();
		case futurplural3:
			return getFuturplural3();

		default:
			break;
		}
		return values.get(key);
	}
}
