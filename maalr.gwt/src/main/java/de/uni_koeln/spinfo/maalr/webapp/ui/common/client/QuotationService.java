package de.uni_koeln.spinfo.maalr.webapp.ui.common.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.uni_koeln.spinfo.maalr.common.shared.CorpusResponse;

@RemoteServiceRelativePath("rpc/quotation")
public interface QuotationService extends RemoteService {

	List<CorpusResponse> findQuotations(String entryValue);

}
