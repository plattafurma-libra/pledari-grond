package de.uni_koeln.spinfo.maalr.webapp.ui.user.client.entry;

import java.util.List;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.ModalFooter;
import com.github.gwtbootstrap.client.ui.constants.BackdropType;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;

import de.uni_koeln.spinfo.maalr.common.shared.CorpusResponse;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.QuotationService;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.QuotationServiceAsync;
import de.uni_koeln.spinfo.maalr.webapp.ui.user.client.DictionaryConstants;

public class QuotationPopup {

	private static QuotationServiceAsync service = GWT.create(QuotationService.class);
	private Dictionary dictionary = DictionaryConstants.getLocaleDictionary();

	public static void show(LemmaVersion selected) {

		final Modal popup = new Modal(true);
		popup.setBackdrop(BackdropType.NORMAL);
		popup.setCloseVisible(true);
		popup.setWidth(1000);

		final Button close = new Button("close");
		close.getElement().getStyle().setMarginTop(5, Unit.PX);
		close.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				popup.hide();
			}
		});
		ModalFooter footer = new ModalFooter(close);
		footer.getElement().getStyle().setProperty("border-top", "1px solid #EEEEEE");
		popup.add(footer);

		final String entryValue = selected.getEntryValue("RStichwort");

		service.findQuotations(entryValue,
				new AsyncCallback<List<CorpusResponse>>() {

					@Override
					public void onSuccess(List<CorpusResponse> result) {
						buildWrapper(result);
					}

					private void buildWrapper(List<CorpusResponse> result) {
						popup.setTitle("Examples for '" + entryValue + "'");
						SafeHtmlBuilder builder = new SafeHtmlBuilder();
						builder.appendHtmlConstant("<div class=\"quotationWrapper\">");
//						builder.appendHtmlConstant("<h3>Examples for <i>'" + entryValue + "'</i></h3>");
						if (result.isEmpty()) {
							builder.appendHtmlConstant("<div class=\"noQuotation\">No quotations found for '"
									+ entryValue + "'<div></div>");
						} else {
							builder.appendHtmlConstant("<ul style=\"width: 94%;\">");
							for (CorpusResponse corpusResponse : result) {
								builder.appendHtmlConstant("<li><div class=\"quotations\">"
										+ "[...]"
										+ corpusResponse.getQuotation()
										+ " [...]" + "</div></li>");
							}
							builder.appendHtmlConstant("</ul>");
						}
						builder.appendHtmlConstant("</div>");
						popup.add(new HTML(builder.toSafeHtml()));
						popup.show();
					}

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}
				});

	}

}
