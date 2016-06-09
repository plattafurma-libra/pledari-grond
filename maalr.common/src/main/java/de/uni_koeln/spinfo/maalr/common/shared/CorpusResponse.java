package de.uni_koeln.spinfo.maalr.common.shared;

import java.io.Serializable;


public class CorpusResponse implements Serializable {
	
	private static final long serialVersionUID = 3689197067568360181L;
	private String quotation;
	
	public CorpusResponse() {
	}

	public CorpusResponse(String quotation) {
		this.setQuotation(quotation);
	}

	public String getQuotation() {
		return quotation;
	}

	public void setQuotation(String quotation) {
		this.quotation = quotation;
	}

}
