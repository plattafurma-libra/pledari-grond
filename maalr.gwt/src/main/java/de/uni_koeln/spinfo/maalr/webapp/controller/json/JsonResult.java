package de.uni_koeln.spinfo.maalr.webapp.controller.json;

import java.util.List;
import java.util.Map;

public class JsonResult {
	
	private List<Map<String, String>> data;
	private String nothingFoundMessage;

	public List<Map<String, String>> getData() {
		return data;
	}

	public void setData(List<Map<String, String>> data) {
		this.data = data;
	}

	public JsonResult(List<Map<String, String>> data) {
		super();
		this.data = data;
	}

	public void setNothingFoundMessage(String message) {
		this.nothingFoundMessage = message;
	}

	public String getNothingFoundMessage() {
		return nothingFoundMessage;
	}
	
	

}