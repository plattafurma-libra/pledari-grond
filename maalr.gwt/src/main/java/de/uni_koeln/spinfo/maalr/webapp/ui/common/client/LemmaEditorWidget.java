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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.ControlLabel;
import com.github.gwtbootstrap.client.ui.Controls;
import com.github.gwtbootstrap.client.ui.Fieldset;
import com.github.gwtbootstrap.client.ui.HelpInline;
import com.github.gwtbootstrap.client.ui.Legend;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.HasDirection.Direction;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription;
import de.uni_koeln.spinfo.maalr.common.shared.description.UseCase;
import de.uni_koeln.spinfo.maalr.common.shared.description.ValueSpecification;
import de.uni_koeln.spinfo.maalr.common.shared.description.ValueType;
import de.uni_koeln.spinfo.maalr.common.shared.description.ValueValidator;
import de.uni_koeln.spinfo.maalr.common.shared.searchconfig.TranslationMap;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.i18n.LocalizedStrings;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.shared.util.Logger;

/**
 * A dynamic form to edit or create entries. It is used within the editor area as well
 * as within the dialogs provided to 'default'-users.
 * @author sschwieb
 *
 */
public class LemmaEditorWidget extends SimplePanel {
	
	private LemmaDescription description;
	
	/**
	 * Contains a mapping of field-ids to ui-elements (such as {@link TextBox}, {@link ListBox} etc.)
	 * Required to set and update the values of an entry. 
	 */
	private Map<String, HasText> fields = new HashMap<String, HasText>();
	
	/**
	 * Contains a mapping of field-ids to {@link ControlGroup}-elements.
	 * Required to change the ui-style of the elements when feedback is presented in case of
	 * an invalid entry.
	 */
	private Map<String, ControlGroup> groups = new HashMap<String, ControlGroup>();
	
	/**
	 * Contains a mapping of field-ids to {@link HelpInline}-elements.
	 * Required to provide feedback in case of an invalid entry.
	 */
	private Map<String, HelpInline> feedback = new HashMap<String, HelpInline>();

	/**
	 * Contains a mapping of field-ids to {@link ValueSpecification}-elements.
	 * Required to validate the input provided by a user.
	 * See {@link ValueSpecification#getValidator()} and {@link ValueValidator#validate(String)}
	 * for details.
	 */
	private HashMap<String, ValueSpecification> valueSpecifications;

	private LemmaVersion initial;
	
	private HorizontalPanel langA;
	private HorizontalPanel langB;

	/**
	 * Copies the data from the given {@link LemmaVersion} into
	 * the editor and updates the ui.
	 * @param
	 */
	public void setData(LemmaVersion lemma) {
		List<String> toSet = new ArrayList<String>();
		toSet.addAll(description.getEditorFields(true));
		toSet.addAll(description.getEditorFields(false));
		if(lemma == null) {
			for (String key : toSet) {
				HasText field = fields.get(key);
				if(field != null) {
					field.setText(null);
				}
			}
		} else {
			for (String key : toSet) {
				HasText field = fields.get(key);
				if(field != null) {
					field.setText(lemma.getEntryValue(key));
				}
			}	
		}
		initial = new LemmaVersion();
		initial.setEntryValues(new HashMap<String, String>(lemma.getEntryValues()));
		initial.setMaalrValues(new HashMap<String, String>(lemma.getMaalrValues()));
	}
	
	/**
	 * Copies the values from the editor into the given
	 * {@link LemmaVersion}.
	 * @param lemma must not be <code>null</code>.
	 */
	public void updateFromEditor(LemmaVersion lemma) {
		List<String> toSet = new ArrayList<String>();
		toSet.addAll(description.getEditorFields(true));
		toSet.addAll(description.getEditorFields(false));
		for (String key : toSet) {
			HasText field = fields.get(key);
			if(field != null) {
				String text = field.getText();
				if(text != null && text.trim().length() > 0) {
					lemma.putEntryValue(key, text.trim());
				} else {
					lemma.removeEntryValue(key);
				}
			}
		}
	}

	/**
	 * Create a new {@link LemmaEditorWidget} for the given {@link LemmaDescription}.
	 * @param description the {@link LemmaDescription}
	 * @param useCase the {@link UseCase} which defines if the widget is used within
	 * the 'default' user environment ({@link UseCase#FIELDS_FOR_SIMPLE_EDITOR}) or within
	 * the editor environment ({@link UseCase#FIELDS_FOR_ADVANCED_EDITOR}). Other usecases
	 * are not supported.
	 * @param vertical Whether or not the fields should be oriented vertically.
	 * @param columns The number of columns
	 * @param showLanguageHeader Whether or not a language header should be shown
	 * @param toggleAntlr The button responsible for displaying the antlr input field. Can be
	 * <code>null</code>. If a button is provided, a handler must be set to show/hide
	 * the input field. 
	 * 
	 */
	public LemmaEditorWidget(final LemmaDescription description, final UseCase useCase, boolean vertical, final int columns, final boolean showLanguageHeader, Button toggleAntlr) {
		this.description = description;
		CellPanel base = null;
		if(vertical) {
			base = new VerticalPanel();
		} else {
			base = new HorizontalPanel();
		}
		base.setWidth("98%");
		base.getElement().getStyle().setMargin(1, Unit.PCT);
		if(toggleAntlr != null) {
			HorizontalPanel panel = new HorizontalPanel();
			panel.getElement().getStyle().setWidth(100, Unit.PCT);
			panel.add(toggleAntlr);
			panel.setCellHorizontalAlignment(toggleAntlr, HorizontalAlignmentConstant.endOf(Direction.LTR));
		}
		setWidget(base);
		final CellPanel finalBase = base;
		LocalizedStrings.afterLoad(new AsyncCallback<TranslationMap>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(TranslationMap translations) {
				langA = createFields(description, true, useCase, columns, showLanguageHeader, translations);
				langB = createFields(description, false, useCase, columns, showLanguageHeader, translations);
				finalBase.add(langA);
				finalBase.add(langB);
			}
		});
		
		setStyleName("lemma-editor", true);
	}

	private HorizontalPanel createFields(LemmaDescription description, boolean firstLanguage, UseCase useCase, int columns,  boolean displayHeader, TranslationMap translation) {
		String languageLabel = description.getLanguageName(firstLanguage);
		ArrayList<String> fieldIds = description.getFields(useCase, firstLanguage); 
		HorizontalPanel panel = new HorizontalPanel();
		panel.setWidth("100%");
		List<Fieldset> cols = new ArrayList<Fieldset>();
		for(int i = 0; i < columns; i++) {
			Fieldset fieldSet = new Fieldset();
			fieldSet.setStyleName("form-horizontal");
			if(displayHeader) {
				if(i == 0) {
					fieldSet.add(new Legend(translation.get(languageLabel)));
				} else {
					Legend legend = new Legend();
					legend.getElement().getStyle().setProperty("minHeight", "40px");
					fieldSet.add(legend);
				}
			}
			panel.add(fieldSet);
			cols.add(fieldSet);
		}
		ArrayList<ValueSpecification> all = description.getValues(useCase);
		valueSpecifications = new HashMap<String, ValueSpecification>();
		for (ValueSpecification valueSpecification : all) {
			valueSpecifications.put(valueSpecification.getInternalName(), valueSpecification);
		}
		int counter = 0;
		for (String item : fieldIds) {
			Fieldset set = cols.get(counter%cols.size()); 
			ControlGroup group = new ControlGroup();
			groups.put(item, group);
			HelpInline help = new HelpInline();
			feedback.put(item, help);
			ValueSpecification vs = valueSpecifications.get(item);
			if(vs == null) {
				vs = new ValueSpecification();
				vs.setInternalName(item);
				vs.setType(ValueType.TEXT);
				Logger.getLogger(getClass()).warn("No Specification for " + item + ", generating default...");
				valueSpecifications.put(item, vs);
//				continue;
			}
			String labelText = translation.get(vs.getInternalName());
			if(labelText == null || labelText.trim().length() == 0) {
				labelText = vs.getInternalName();
			}
			ControlLabel label = new ControlLabel(labelText);
			group.add(label);
			ValueType type = vs.getType();
			Controls control = new Controls();
			Widget widget = null;
			switch(type) {
				case ENUM: widget = buildListBoxFor(vs); break;
				case TEXT: widget = buildTextBoxFor(vs); break;
				case ORACLE: widget = buildOracleFor(vs); break;
			}
			widget.getElement().setId("maalr_editor_" + vs.getInternalName());
			control.add(widget);
			control.add(help);
			group.add(control);
			set.add(group);
			counter++;
		}
		return panel;
	}

	private Widget buildOracleFor(final ValueSpecification vs) {
		SuggestOracle oracle = new SuggestOracle() {

			CommonServiceAsync service = GWT.create(CommonService.class);
			
			@Override
			public void requestSuggestions(final Request request, final Callback callback) {
				service.getSuggestionsForField(vs.getInternalName(), request.getQuery(), request.getLimit(), new AsyncCallback<ArrayList<String>>() {

					@Override
					public void onFailure(Throwable caught) {
						// Ignore failure
					}

					@Override
					public void onSuccess(ArrayList<String> result) {
						Collection<Suggestion> suggestions = new ArrayList<Suggestion>();
						if(result != null) {
							for (final String string : result) {
								suggestions.add(new Suggestion() {
	
									@Override
									public String getDisplayString() {
										return string;
									}
	
									@Override
									public String getReplacementString() {
										return string;
									}
									
								});
							}
						}
						Response response = new Response(suggestions);
						callback.onSuggestionsReady(request, response);
					}
				});
			}
			
		};
		SuggestBox box = new SuggestBox(oracle);
		fields.put(vs.getInternalName(), box);
		return box;
	}

	private Widget buildTextBoxFor(final ValueSpecification vs) {
		TextBox box = new TextBox();
		fields.put(vs.getInternalName(), box);
//		box.addChangeHandler(new ChangeHandler() {
//			
//			@Override
//			public void onChange(ChangeEvent event) {
//				validate(vs.getInternalName());
//			}
//		});
		return box;
	}

	private Widget buildListBoxFor(final ValueSpecification vs) {
		HasTextListBox box = new HasTextListBox();
		List<String> values = vs.getValidator().getAllowedValues();
		for (String string : values) {
			if(string == null) string = "";
			box.addItem(string);
		}
		fields.put(vs.getInternalName(), box);
//		box.addChangeHandler(new ChangeHandler() {
//			
//			@Override
//			public void onChange(ChangeEvent event) {
//				validate(vs.getInternalName());
//			}
//		});
		return box;
	}
	
	class HasTextListBox extends ListBox implements HasText {

		@Override
		public String getText() {
			if(super.getSelectedIndex() == -1) return null;
			String value = super.getItemText(super.getSelectedIndex());
			if(value == null || value.trim().length() == 0) {
				return null;
			}
			return value;
		}

		@Override
		public void setText(String text) {
			if(text == null) {
				super.setSelectedIndex(-1);
			}
			for(int i = 0; i < super.getItemCount(); i++) {
				if(text.equals(super.getItemText(i))) {
					super.setSelectedIndex(i);
					break;
				}
			}
		}
		
	}


	/**
	 * Resets the data shown by the editor, by clearing
	 * all fields and displaying the values which were
	 * defined in the lemma set by {@link LemmaEditorWidget#setData(LemmaVersion)).
	 */
	public void clearData() {
		if(initial == null) {
			setData(new LemmaVersion());
		} else {
			setData(initial);
		}
	}

	public boolean isValid(boolean all, boolean showError) {
		boolean valid = true;
		Set<String> keys = fields.keySet();
		boolean oneValid = false;
		for (String key : keys) {
			if(!validate(key, showError)) {
				valid = false;
			} else {
				oneValid = true;
			}
		}
		return all ? valid : oneValid;
	}

	private boolean validate(String field, boolean showError) {
		ValueSpecification valueSpecification = valueSpecifications.get(field);
		if(valueSpecification != null && valueSpecification.getValidator() != null) {
			String remark = valueSpecification.getValidator().validate(fields.get(field).getText());
			if(remark != null) {
				if(showError) {
					groups.get(field).setType(ControlGroupType.ERROR);
					feedback.get(field).setText(remark);
				} else {
					groups.get(field).setType(ControlGroupType.SUCCESS);
					feedback.get(field).setText("");
				}
				return false;
			} else {
				groups.get(field).setType(ControlGroupType.SUCCESS);
				feedback.get(field).setText("");
				return true;
			}
		}
		return true;
	}

	public void markFields(Map<String, Difference> compared, Map<String, String> oldValues) {
		for (String field : groups.keySet()) {
			feedback.get(field).setVisible(false);
			groups.get(field).setType(ControlGroupType.NONE);
		}
		for (String field : compared.keySet()) {
			if(compared.get(field) != Difference.NEW) {
				feedback.get(field).setText(compared.get(field).getDisplayName() + ", was: " + oldValues.get(field));
			} else {
				feedback.get(field).setText(compared.get(field).getDisplayName());
			}
			feedback.get(field).setVisible(true);
			groups.get(field).setType(ControlGroupType.WARNING);
		}
	}

}
