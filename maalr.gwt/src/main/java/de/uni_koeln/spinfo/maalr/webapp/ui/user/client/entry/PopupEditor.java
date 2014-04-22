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
package de.uni_koeln.spinfo.maalr.webapp.ui.user.client.entry;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ButtonGroup;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.ControlLabel;
import com.github.gwtbootstrap.client.ui.Fieldset;
import com.github.gwtbootstrap.client.ui.HelpInline;
import com.github.gwtbootstrap.client.ui.PageHeader;
import com.github.gwtbootstrap.client.ui.TextArea;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LightUserInfo;
import de.uni_koeln.spinfo.maalr.common.shared.searchconfig.TranslationMap;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.AntlrEditorWidget;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.CommonService;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.CommonServiceAsync;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.LemmaEditorWidget;

public class PopupEditor extends VerticalPanel {
	
	private TextArea comment;
	private AntlrEditorWidget antlr;
	private LemmaEditorWidget content;
	private HTML introText;
	private TextBox email;
	private HelpInline commentFeedback;
	private ControlGroup commentGroup;

	public PopupEditor(String header, String sub, String description, final LemmaEditorWidget content, TranslationMap translation, boolean withComment, boolean withEmail, Button...buttons) {
		this.content = content;
		PageHeader ph = new PageHeader();
		ph.setText(header);
		ph.setSubtext(sub);
		add(ph);
		introText = new HTML(description);
		add(introText);
		
//		Well editorWell = new Well();
//		editorWell.add(content);
//		add(editorWell);

		//without surrounding well:
		add(new HTML("<br>")); //<br> for vertical spacing
		add(content);
	
		Fieldset set = null;
		if(withComment) {
			set = new Fieldset();
			commentGroup = new ControlGroup();
			commentGroup.add(new ControlLabel(translation.get("dialog.comment.header")));
			comment = new TextArea();
			//uncomment this to restrict max input size (current solution is a MaalrException, see SpringBackend.java)
			//comment.getElement().setAttribute("maxlength", "500");
			comment.getElement().getStyle().setWidth(100, Unit.PCT);
			comment.getElement().getStyle().setProperty("resize", "none");
			comment.setVisibleLines(3);
			comment.setPlaceholder(translation.get("dialog.comment.placeholder"));
			commentGroup.add(comment);
			commentFeedback = new HelpInline();
			commentGroup.add(commentFeedback);
			set.add(commentGroup);
		}
		if(withEmail) {
			if(set == null) {
				set = new Fieldset();
			}
			ControlGroup group = new ControlGroup();
			group.add(new ControlLabel(translation.get("dialog.email.header")));
			email = new TextBox();
			email.setPlaceholder(translation.get("dialog.email.placeholder"));
			email.getElement().getStyle().setWidth(100, Unit.PCT);
			group.add(email);
			set.add(group);
			
		}
		if(set != null) {
			add(set);
		}
		checkUserLoggedIn();
		ButtonGroup group = new ButtonGroup();
		add(group);
		for (Button button : buttons) {
			group.add(button);
		}
	}

	private void checkUserLoggedIn() {
		CommonServiceAsync service = GWT.create(CommonService.class);
		service.getCurrentUser(new AsyncCallback<LightUserInfo>() {
			
			@Override
			public void onSuccess(LightUserInfo result) {
				if(result == null) {
					email.setText(null);
				} else {
					email.setText(result.getEmail());
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
	}

	public void reset() {
		if(antlr != null) {
			antlr.reset();
		}
		if(comment != null) {
			comment.setText("");
		}
	}

	public void updateFromEditor(LemmaVersion lemma) {
		content.updateFromEditor(lemma);
		if(comment != null && comment.getText().trim().length() != 0) {
			lemma.putEntryValue(LemmaVersion.COMMENT, comment.getText().trim());
		} else {
			lemma.removeEntryValue(LemmaVersion.COMMENT);
		}
		if(email != null && email.getText().trim().length() != 0) {
			lemma.putMaalrValue("", "");
			lemma.putEntryValue(LemmaVersion.EMAIL, email.getText().trim());
		}
	}

	

	public boolean isValid() {
		boolean contentValid = content.isValid(false, false);
		boolean commentValid = true;
		if(comment != null) {
			if(comment.getText().trim().length() == 0) {
				commentValid = false;
			}
		}
		if(!commentValid && !contentValid) {
			content.isValid(false, true);
			commentFeedback.setText("Please leave a comment/question or fill out the fields above.");
			commentGroup.setType(ControlGroupType.ERROR);
		} else {
			commentFeedback.setText("");
			commentGroup.setType(ControlGroupType.SUCCESS);
		}
		return contentValid || commentValid;
	}


}
