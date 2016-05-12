package de.uni_koeln.spinfo.maalr.webapp.controller.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PGResult {

	private String filename;
	private String language;
	private String chapter;
	private String volume;
	private String quotation;
	private String url;
	
	public PGResult() {
	}

	public PGResult(String filename, String language,
			String chapter, String volume, String quotation, String url) {
		super();
		this.filename = filename;
		this.language = language;
		this.chapter = chapter;
		this.volume = volume;
		this.quotation = quotation;
		this.url = url;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getChapter() {
		return chapter;
	}

	public void setChapter(String chapter) {
		this.chapter = chapter;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getQuotation() {
		return quotation;
	}

	public void setQuotation(String quotation) {
		this.quotation = quotation;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@Override
	public String toString() {
		return "RumantschGrischunCorpusResponse [filename=" + filename
				+ ", language=" + language + ", chapter=" + chapter
				+ ", volume=" + volume + ", quotation=" + quotation + ", url="
				+ url + "]";
	}

}
