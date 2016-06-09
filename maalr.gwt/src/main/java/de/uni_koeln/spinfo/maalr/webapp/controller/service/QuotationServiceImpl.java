package de.uni_koeln.spinfo.maalr.webapp.controller.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import de.uni_koeln.spinfo.maalr.common.shared.CorpusResponse;
import de.uni_koeln.spinfo.maalr.webapp.controller.json.PGResult;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.QuotationService;

@Service("quotationService")
public class QuotationServiceImpl implements QuotationService {

	// private String url =
	// "http://edison.spinfo.uni-koeln.de:4563/quotations/json";
	private String url = "http://localhost:4563/quotations/json";

	public List<CorpusResponse> findQuotations(String entryValue) {

		// String request = url + "/rumantsch grischun" + "/" + entryValue;
//		String request = url;

		RestTemplate restTemplate = new RestTemplate();

		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		JSONObject request = new JSONObject();
		request.put("language", "rumantsch grischun");
		request.put("query", entryValue);
		
		HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);
		PGResult[] response1 = restTemplate.getForObject(url, PGResult[].class, entity);
		for (PGResult pgResult : response1) {
			System.out.println(pgResult.getQuotation());
		}
//		 ResponseEntity<PGResult[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, PGResult[].class);

		Map<String, String> vars = new HashMap<>();
		vars.put("language", "rumantsch grischun");
		vars.put("query", entryValue);
		ResponseEntity<PGResult[]> response = restTemplate.getForEntity(
				"http://localhost:4563/quotations/json/{language}/{query}",
				PGResult[].class, vars);

		if (response.getStatusCode() == HttpStatus.OK) {
			PGResult[] rgResponses = response.getBody();
			List<CorpusResponse> cr = new ArrayList<CorpusResponse>();
			for (PGResult r : rgResponses) {
				cr.add(new CorpusResponse(r.getQuotation()));
			}
			return cr;
		} else {
			return null;
		}
	}

}
