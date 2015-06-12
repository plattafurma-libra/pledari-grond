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
package de.uni_koeln.spinfo.maalr.webapp.ui.common.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.CheckBox;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.ControlLabel;
import com.github.gwtbootstrap.client.ui.Controls;
import com.github.gwtbootstrap.client.ui.Fieldset;
import com.github.gwtbootstrap.client.ui.Form;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.RadioButton;
import com.github.gwtbootstrap.client.ui.base.TextBox;
import com.github.gwtbootstrap.client.ui.constants.FormType;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestBox.DefaultSuggestionDisplay;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import de.uni_koeln.spinfo.maalr.common.server.searchconfig.ColumnSelector;
import de.uni_koeln.spinfo.maalr.common.server.searchconfig.QueryBuilder;
import de.uni_koeln.spinfo.maalr.common.server.searchconfig.QueryKey;
import de.uni_koeln.spinfo.maalr.common.shared.SimpleWebLogger;
import de.uni_koeln.spinfo.maalr.common.shared.searchconfig.TranslationMap;
import de.uni_koeln.spinfo.maalr.common.shared.searchconfig.UiConfiguration;
import de.uni_koeln.spinfo.maalr.common.shared.searchconfig.UiField;
import de.uni_koeln.spinfo.maalr.common.shared.searchconfig.UiFieldType;
import de.uni_koeln.spinfo.maalr.lucene.query.MaalrQuery;
import de.uni_koeln.spinfo.maalr.lucene.query.MaalrQueryFormatter;
import de.uni_koeln.spinfo.maalr.lucene.query.QueryResult;
import de.uni_koeln.spinfo.maalr.services.user.shared.SearchService;
import de.uni_koeln.spinfo.maalr.services.user.shared.SearchServiceAsync;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.i18n.LocalizedStrings;

/**
 * This class renders a maalr search field and is responsible for sending and receiving
 * queries. It is created dynamically based on the {@link UiConfiguration}s defined
 * in the search configuration.
 * 
 * @author sschwieb
 *
 */
public class ConfigurableSearchArea extends Form {
	
	private Button submit;
	private TreeMap<String, String> currentValues = new TreeMap<String, String>();
	private Map<String, Widget> elementsById = new HashMap<String, Widget>();
	private Map<String, String> defaultsById = new HashMap<String, String>();
	private UiConfiguration configuration;
	private UiConfiguration defaultConfiguration;
	private UiConfiguration extendedConfiguration;
	private SimplePanel optionsPanel;
	private static final int QUERY_SCHEDULER_DELAY = 150;
	private final HistoryTimer historyTimer = new HistoryTimer();
	private SearchServiceAsync service;
	private IResultDisplay resultDisplay;
	private Button extended;
	private TextBox focusWidget;
	private boolean withHistory;
	private String historyPrefix;
	private Map<String, String> oracleHistory = new HashMap<String, String>();
	
	/**
	 * This timer is responsible of updating the browser's location
	 * bar after a delayed/'live' query has been executed.
	 *
	 */
	private class HistoryTimer extends Timer {

		private MaalrQuery maalrQuery;
		
		@Override
		public void run() {
			if(!withHistory) return;
			if(!currentValues.equals(maalrQuery.getValues())) {
				return;
			}
			String url = maalrQuery.toURL();
			if(historyPrefix != null) {
				url = historyPrefix + url;
			}
			History.newItem(url);
		}

		public void scheduleUrlUpdate() {
			maalrQuery = new MaalrQuery();
			maalrQuery.setValues(removeNullValues(currentValues));
			schedule(2000);
		}
		
	}
	
	
	private final Timer feedbackTimer = new Timer() {
		@Override
		public void run() {
			submit.setStyleName("active", false);
		}
	};
	
	public MaalrQuery getQuery() {
		final MaalrQuery maalrQuery = new MaalrQuery();
		maalrQuery.setValues(removeNullValues(currentValues));
		return maalrQuery;
	}
	
	/**
	 * This timer is responsible of running delayed queries
	 * when a user modifies a text field.
	 * 
	 */
	private final Timer queryTimer = new Timer() {

		@Override
		public void run() {
			final MaalrQuery maalrQuery = new MaalrQuery();
			maalrQuery.setValues(removeNullValues(currentValues));
			SearchHelper.setLastQuery(maalrQuery);
			showSearchFeedback();
			service.search(maalrQuery, new AsyncCallback<QueryResult>() {

				@Override
				public void onSuccess(QueryResult result) {
					resultDisplay.updateResult(maalrQuery, result);
					historyTimer.scheduleUrlUpdate();
				}

				@Override
				public void onFailure(Throwable caught) {
					
				}
			});
			
		}
	};
	
	/**
	 * Helper method to remove all key-value pairs from a map where the 
	 * value is <code>null</code>.
	 * @param input
	 * @return
	 */
	private TreeMap<String, String> removeNullValues(Map<String,String> input) {
		TreeMap<String, String> copy = new TreeMap<String, String>();
		Set<Entry<String, String>> entries = currentValues.entrySet();
		for (Entry<String, String> entry : entries) {
			if(entry.getValue() != null) {
				copy.put(entry.getKey(), entry.getValue());
			}
		}
		return copy;
	}
	
	public ConfigurableSearchArea(IResultDisplay resultDisplay, boolean useEditorUi, boolean withHistory, String historyPrefix) {
		this.resultDisplay = resultDisplay;
		this.withHistory = withHistory;
		this.historyPrefix = historyPrefix;
		setType(FormType.INLINE);
		optionsPanel = new SimplePanel();
		
		// Wrap the search area
		optionsPanel.getElement().setId("searchoptions");
		
		add(optionsPanel);
		extended = new Button();
		//extended.setText(configuration.getMoreLabel());
		extended.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(configuration == defaultConfiguration) {
					setExtended(true);
				} else {
					setExtended(false);
				}
			}
		});
		add(extended);
		if(configuration == null) {
			service = GWT.create(SearchService.class);
			String locale = Document.get().getElementsByTagName("html").getItem(0).getAttribute("lang");
			service.getUserConfigurations(locale, useEditorUi, new AsyncCallback<ArrayList<UiConfiguration>>() {

				@Override
				public void onFailure(Throwable caught) {
				}

				@Override
				public void onSuccess(ArrayList<UiConfiguration> results) {
					defaultConfiguration = results.get(0);
					extendedConfiguration = results.get(1);
					extended.setText(defaultConfiguration.getMoreLabel());
					configuration = defaultConfiguration;
					buildSearchPanel(configuration);
				}
			});
		} else {
			extended.setText(configuration.getMoreLabel());
			buildSearchPanel(configuration);
		}
	}
	
	/**
	 * Set the search area to either extended/advanced or default/simple mode. 
	 * @param extended
	 */
	public void setExtended(boolean extended) {
		configuration = extended ? extendedConfiguration : defaultConfiguration;
		this.extended.setText(extended ? configuration.getLessLabel() : configuration.getMoreLabel());
		buildSearchPanel(configuration);
		if(SearchHelper.getLastQuery() != null) {
			setQuery(SearchHelper.getLastQuery());
		}
		setFocus(true);
	}

	public void buildSearchPanel(UiConfiguration config) {
		MaalrQueryFormatter.setUiConfiguration(configuration);
		Fieldset set = new Fieldset();
		List<UiField> fields = config.getFields();
		focusWidget = null;
		for (UiField field : fields) {
			set.add(buildField(field));
		}
		optionsPanel.clear();
		optionsPanel.add(set);
	}

	private ControlGroup buildField(final UiField field) {
		ControlGroup group = new ControlGroup();
		if(field.getType() != UiFieldType.CHECKBOX) {
			ControlLabel label = new ControlLabel(field.getLabel());
			group.add(label);
		}
		Controls controls = new Controls();
		group.add(controls);
		Widget widget = null;
		switch(field.getType()) {
			case CHECKBOX: widget = buildCheckBox(field); break;
			case COMBO: widget = buildComboBox(field); break;
			case RADIO: widget = buildRadioArea(field); break; 
			case TEXT: widget = buildTextBox(field); break;
			case ORACLE: widget = buildOracle(field); break;
		}
		
		widget.getElement().setId(field.getId());
		// Wrap the default configuration elements for CSS purposes
		if(configuration.equals(defaultConfiguration)) {
			if(field.getType().equals(UiFieldType.CHECKBOX)) {
				widget.getElement().setClassName("default-config-maalr checkbox");
			}
		}
		
		currentValues.put(field.getId(), field.getInitialValue());
		elementsById.put(field.getId(), widget);
		defaultsById.put(field.getId(), field.getInitialValue());
		group.add(widget);
		
		if(field.hasSubmitButton()) {
			submit = new Button(field.getSubmitLabel());
			submit.getElement().getStyle().setMarginLeft(10, Unit.PX);
			final Widget tmp = widget;
			submit.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent arg0) {
					if(tmp instanceof HasValue) {
						currentValues.put(field.getId(), (String) ((HasValue)tmp).getValue());
					}
					fireUpdate();
				}
			});
			group.add(submit);
		}
		return group;
	}
	
	private Widget buildRadioArea(final UiField field) {
		VerticalPanel options = new VerticalPanel();
		ArrayList<String> values = field.getValues();
		if(values != null) {
			for(int i = 0; i < values.size(); i++) {
				final String value = values.get(i);
				final RadioButton rb = new RadioButton(field.getId());
				rb.setText(field.getValueLabels().get(i));
				if(value.equals(field.getInitialValue())) {
					rb.setValue(true);
				}
				rb.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
	
					@Override
					public void onValueChange(ValueChangeEvent<Boolean> arg0) {
						currentValues.put(field.getId(), value);
						fireUpdate();
					}
				});
				rb.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent arg0) {
						currentValues.put(field.getId(), value);
						fireUpdate();
					}
				});
				options.add(rb);
			}
		}
		return options;
	}
	
	private Widget buildOracle(final UiField field) {
		MultiWordSuggestOracle oracle = new MultiWordSuggestOracle() {

			@Override
			public void requestSuggestions(final Request request, final Callback callback) {
				service.getSuggestions(field.getId(), request.getQuery(), request.getLimit(), new AsyncCallback<List<String>>() {

					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(List<String> result) {
						Response response = new Response();
						List<Suggestion> suggestions = new ArrayList<Suggestion>();
						for (String string : result) {
							suggestions.add(new MultiWordSuggestion(string, string));
						}
						response.setSuggestions(suggestions);
						callback.onSuggestionsReady(request, response);
					}
				});
				
			}
			
			
			
		};
		final SuggestBox box = new SuggestBox(oracle);
		box.setValue(field.getInitialValue());
		
		box.getValueBox().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				String old = oracleHistory.get(field.getId());
				if(old != null) {
					box.getValueBox().setText(old);
				}
				box.getValueBox().selectAll();
				box.showSuggestionList();
			}
		});
		
		box.addKeyDownHandler(new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					box.getValueBox().selectAll();
					event.stopPropagation();
					event.preventDefault();
					box.setFocus(true);
					fireDelayedUpdate();
				}
			}
		});
		
		box.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if(box.getValue() == null || box.getValue().trim().length() == 0) {
					currentValues.remove(field.getId());
				} else {
					currentValues.put(field.getId(), box.getValue());
				}
				oracleHistory.put(field.getId(), box.getValue());
				//fireDelayedUpdate();
			}

		});
		box.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				String string = event.getSelectedItem().getReplacementString();
				if(box.getValue() == null || box.getValue().trim().length() == 0) {
					currentValues.remove(field.getId());
				} else {
					currentValues.put(field.getId(), string);
				}
				fireDelayedUpdate();
			}
		});
		return box;
	}

	private Widget buildTextBox(final UiField field) {
		final TextBox box = new TextBox();
		
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
	        public void execute () {
	        	box.setFocus(true);
	        }
		});
		
		box.setValue(field.getInitialValue());
		
		box.addKeyDownHandler(new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					box.selectAll();
					event.stopPropagation();
					event.preventDefault();
					box.setFocus(true);
					fireUpdate();
				}
			}
		});
		
		box.addKeyUpHandler(new KeyUpHandler() {
			
			String last = null;
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if(box.getValue() == null || box.getValue().trim().length() == 0) {
					currentValues.remove(field.getId());
				} else {
					currentValues.put(field.getId(), box.getValue());
				}
				if(last.equals(box.getValue())) {
					return;
				}
				last = box.getValue();
				fireDelayedUpdate();
			}

		});
		box.addMouseUpHandler(new MouseUpHandler() {

			@Override
			public void onMouseUp(MouseUpEvent event) {
				if (box.getText().length() > 0)
					box.selectAll();
			}
		});
		if(focusWidget == null) {
			focusWidget = box;
		}
		return box;
	}

	/**
	 * Creates a combo box for the given field. Depending on the field,
	 * the list of selectable items is generated differently: if it refers to a
	 * {@link ColumnSelector} or a {@link QueryBuilder}, the combo box
	 * will display the (translated) options. If it refers to a {@link QueryKey},
	 * the list will be populated by performing a query on the database,
	 * returning distinct values for the referenced column(s).
	 * @param field
	 * @return
	 */
	private Widget buildComboBox(final UiField field) {
		final ListBox box = new ListBox();
		ArrayList<String> values = field.getValues();

		if(values != null) {
			for(int i = 0; i < values.size(); i++) {
				String value = values.get(i);
				String label = value;
				if(label == null) {
					label = "-";
				} else {
					label = field.getValueLabels().get(i);
				}
				box.addItem(label, value);
			}
			box.setSelectedValue(field.getInitialValue());
			box.addChangeHandler(new ChangeHandler() {
				
				@Override
				public void onChange(ChangeEvent arg0) {
					String value = box.getValue();
					if(value == null) {
						currentValues.remove(field.getId());
					} else {
						currentValues.put(field.getId(), value);
					}
					fireUpdate();
				}
			});
		} else {
			box.addItem("-", (String) null);
			service.getSuggestions(field.getId(), "", 120, new AsyncCallback<List<String>>() {

				@Override
				public void onFailure(Throwable arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(List<String> values) {
					for (String value : values) {
						box.addItem(value, value);
					}
				}
				
			});
			box.addChangeHandler(new ChangeHandler() {
				
				@Override
				public void onChange(ChangeEvent arg0) {
					if(box.getSelectedIndex() == 0) {
						currentValues.remove(field.getId());
					} else {
						currentValues.put(field.getId(), box.getValue());						
					}
					fireUpdate();
				}
			});
		}
		return box;
	}

	private Widget buildCheckBox(final UiField field) {
		final CheckBox box = new CheckBox(field.getLabel());
		String initialValue = field.getInitialValue();
		if("true".equals(initialValue)) {
			box.setValue(true);
			currentValues.put(field.getId(), "true");
		} else {
			currentValues.put(field.getId(), "false");
		}
		box.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> value) {
				currentValues.put(field.getId(), box.getValue().toString());
				fireUpdate();
			}
		});
		box.getElement().setClassName("gwt-checkbox-maalr checkbox");
		return box;
	}
	
	private void fireUpdate() {
		showSearchFeedback();
		addToHistory(currentValues);
	}
	
	private void fireDelayedUpdate() {
		queryTimer.cancel();
		queryTimer.schedule(QUERY_SCHEDULER_DELAY);
	}
	
	private void showSearchFeedback() {
		if(submit == null || !submit.isVisible()) return;
		if(submit.getStyleName().indexOf("active") == -1) {
			submit.setStyleName("active", true);
		}
		feedbackTimer.schedule(150);
	}
	
	private void addToHistory(TreeMap<String, String> map) {
		queryTimer.cancel();
		String url = MaalrQuery.toUrl(map);
		if(historyPrefix != null) {
			url = historyPrefix + url;
		}
		if (!historyContains(url)) {
			History.newItem(url);
			showSearchFeedback();
		}
	}
	
	private boolean historyContains(String url) {
		return History.getToken().equals(url);
	}

	public void setQuery(MaalrQuery maalrQuery) {
		final String label = MaalrQueryFormatter.getQueryLabel(maalrQuery);
		if(label != null) {
			LocalizedStrings.afterLoad(new AsyncCallback<TranslationMap>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(TranslationMap result) {
					String title = result.get("maalr.query.result_title");
					String pageName = result.get("maalr.page.title");
					String searchPhrase = "'" + label + "'";
					
					if(title != null) {
						 Window.setTitle(pageName + " - " + title.replaceAll("\\{0\\}", searchPhrase));
					}
				}
			});
			
		}
		currentValues.clear();
		Set<Entry<String, Widget>> widgets = elementsById.entrySet();
		for (Entry<String, Widget> entry : widgets) {
			String value = maalrQuery.getValue(entry.getKey());
			if(value == null) {
				value = defaultsById.get(entry.getKey());
			}
			currentValues.put(entry.getKey(), value);
			setValue(entry.getValue(), value);
		}
	}

	private void setValue(Widget widget, String value) {
		if(widget instanceof CheckBox) {
			((CheckBox) widget).setValue(Boolean.parseBoolean(value));
		}
		if(widget instanceof ListBox) {
			((ListBox) widget).setSelectedValue(value);
		}
		if(widget instanceof VerticalPanel) {
			VerticalPanel vp = (VerticalPanel) widget;
			for(int i = 0; i < vp.getWidgetCount(); i++) {
				RadioButton rb = (RadioButton) vp.getWidget(i);
				if(rb.getText().equals(value)) {
					rb.setValue(true);
					break;
				}
			}
		}
		if(widget instanceof TextBox) {
			TextBox box = (TextBox) widget;
			if(box.getValue() != null && box.getValue().equals(value)) {
				return;
			}
			box.setValue(value);
			return;
		}
		if(widget instanceof SuggestBox) {
			SuggestBox box = (SuggestBox) widget;
			DefaultSuggestionDisplay display = (DefaultSuggestionDisplay) box.getSuggestionDisplay();
			if(display.isSuggestionListShowing()) return;
			if(box.getValue() != null && box.getValue().equals(value)) {
				return;
			}
			box.setValue(value);
			return;
		}
	}
	
	public void setFocus(final boolean selectAll) {
		if(focusWidget != null) {
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
		        public void execute () {
		        	focusWidget.setFocus(true);
		        	if(selectAll) {
		        		focusWidget.selectAll();
		        	}
		        }
		   });
		}
	}
	
}
