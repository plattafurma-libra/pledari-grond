package de.uni_koeln.spinfo.maalr.common.shared;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "LemmaVersion")
public class LightLemmaVersion implements Serializable {
	
	private static final long serialVersionUID = 3996120378520043526L;

	private HashMap<String, String> fields;
	
	private static HashSet<String> PUBLIC_KEYS;
	
	static {
		PUBLIC_KEYS = new HashSet<String>();
		PUBLIC_KEYS.add("RStichwort");
		PUBLIC_KEYS.add("DStichwort");
		PUBLIC_KEYS.add("RGenus");
		PUBLIC_KEYS.add("DGenus");
		PUBLIC_KEYS.add("conjunctivsing1");
		PUBLIC_KEYS.add("futurplural2");
		PUBLIC_KEYS.add("futurplural3");
		PUBLIC_KEYS.add("gerundium");
		PUBLIC_KEYS.add("conjunctivsing3");
		PUBLIC_KEYS.add("conjunctivsing2");
		PUBLIC_KEYS.add("futurplural1");
		PUBLIC_KEYS.add("imperativ2");
		PUBLIC_KEYS.add("imperativ1");
		PUBLIC_KEYS.add("participperfectfs");
		PUBLIC_KEYS.add("infinitiv");
		PUBLIC_KEYS.add("preschentplural1");
		PUBLIC_KEYS.add("participperfectfp");
		PUBLIC_KEYS.add("cundizionalsing1");
		PUBLIC_KEYS.add("cundizionalsing2");
		PUBLIC_KEYS.add("conjunctivplural1");
		PUBLIC_KEYS.add("preschentplural3");
		PUBLIC_KEYS.add("cundizionalsing3");
		PUBLIC_KEYS.add("conjunctivplural2");
		PUBLIC_KEYS.add("preschentplural2");
		PUBLIC_KEYS.add("preschentsing3");
		PUBLIC_KEYS.add("preschentsing2");
		PUBLIC_KEYS.add("preschentsing1");
		PUBLIC_KEYS.add("cundizionalplural3");
		PUBLIC_KEYS.add("cundizionalplural2");
		PUBLIC_KEYS.add("conjunctivplural3");
		PUBLIC_KEYS.add("cundizionalplural1");
		PUBLIC_KEYS.add("futursing2");
		PUBLIC_KEYS.add("participperfectmp");
		PUBLIC_KEYS.add("futursing1");
		PUBLIC_KEYS.add("futursing3");
		PUBLIC_KEYS.add("participperfectms");
		PUBLIC_KEYS.add("imperfectplural2");
		PUBLIC_KEYS.add("imperfectsing1");
		PUBLIC_KEYS.add("imperfectplural1");
		PUBLIC_KEYS.add("imperfectsing2");
		PUBLIC_KEYS.add("imperfectsing3");
		PUBLIC_KEYS.add("imperfectplural3");
	}

	public LightLemmaVersion() {
		// Default constructor used by javax.xml.bind.*
	}
	
	public LightLemmaVersion(LemmaVersion copy) {
		HashMap<String, String> tmp = copy.getEntryValues();
		fields = new HashMap<>();
		for (String key : PUBLIC_KEYS) {
			String value = tmp.get(key);
			if(value != null)
				fields.put(key, value);
		}
	}
	
	@XmlElementWrapper(name="fields")
	public HashMap<String, String> getEntryValues() {
		return fields;
	}

}
