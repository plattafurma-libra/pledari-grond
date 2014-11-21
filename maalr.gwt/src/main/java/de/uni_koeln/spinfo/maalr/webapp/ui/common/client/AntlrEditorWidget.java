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

import java.util.Map;

import org.antlr.runtime.RecognitionException;

import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.TextArea;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.uni_koeln.spinfo.maalr.antlr.client.Antlr4Maalr;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;

public class AntlrEditorWidget extends VerticalPanel {
	
	private TextArea antlr;
	private Antlr4Maalr parser;
	private Alert alert;
	private LemmaEditorWidget editor;

	public AntlrEditorWidget(LemmaEditorWidget editor) {
		this.editor = editor;
		getElement().getStyle().setWidth(100, Unit.PCT);
		VerticalPanel vp = new VerticalPanel();
		add(vp);
		vp.getElement().getStyle().setWidth(100, Unit.PCT);
		antlr = new TextArea();
		antlr.getElement().getStyle().setWidth(100, Unit.PCT);
		antlr.getElement().getStyle().setProperty("resize", "none");
		antlr.getElement().getStyle().setProperty("spellCheck", "false");
		antlr.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				parseData(antlr.getText());
			}
		});
		antlr.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				parseData(antlr.getText());
			}
		});
		vp.add(antlr);
		alert = new Alert();
		alert.setClose(false);
		alert.setVisible(false);
		vp.add(alert);
	}
	
	public void parseData(String text) {
		if(parser == null) {
			parser = new Antlr4Maalr();
		}
		try {
			Map<String, String> data = parser.validate(text);
			if(data != null) {
				LemmaVersion lv = new LemmaVersion();
				lv.getEntryValues().putAll(data);
				editor.setData(lv);
				alert.setType(AlertType.SUCCESS);
				alert.setText("Successfully parsed input");
				alert.setVisible(false);
			}
		} catch (RecognitionException e) {
			alert.setType(AlertType.ERROR);
			String failed = null;
			if(e.charPositionInLine >= 0) {
				if(text.substring(e.charPositionInLine).length() > 0) {
					failed = ":\"" + text.substring(e.charPositionInLine) + "\" does not match the grammar. ";
				}
			}
			String tokenSubMessage = "";
			if(e.token != null) {
				tokenSubMessage = " Unexpected token: " + e.token.getText();
			}
			alert.setText("Failed to parse from position " + e.charPositionInLine + (failed == null ? "" : failed) + tokenSubMessage);
			alert.setVisible(true);
		}
	}

	public void reset() {
		antlr.setText("");
	}


}
