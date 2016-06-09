package de.uni_koeln.spinfo.maalr.webapp.ui.common.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.uni_koeln.spinfo.maalr.common.shared.CorpusResponse;

public interface QuotationServiceAsync {

	void findQuotations(String entryValue,
			AsyncCallback<List<CorpusResponse>> callback);

}
