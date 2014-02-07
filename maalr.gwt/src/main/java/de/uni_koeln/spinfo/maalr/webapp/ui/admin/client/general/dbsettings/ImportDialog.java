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
package de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.general.dbsettings;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.FileUpload;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.ModalFooter;
import com.github.gwtbootstrap.client.ui.ProgressBar;
import com.github.gwtbootstrap.client.ui.base.ProgressBarBase.Style;
import com.github.gwtbootstrap.client.ui.constants.BackdropType;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ImportDialog extends Modal {

    public ImportDialog() {
    	setBackdrop(BackdropType.STATIC);
		setTitle("Import Data...");
		setAnimation(true);
    	VerticalPanel panel = new VerticalPanel();
    	panel.setSpacing(20);
		add(panel);
		buildWidget(panel);
    }

    public void buildWidget(VerticalPanel parent) {
    	parent.add(new Label("Select a database dump which will replace all currently stored entries. " +
				"Note that this operation cannot be undone."));
    	final FormPanel form = new FormPanel();
    	form.setEncoding(FormPanel.ENCODING_MULTIPART);
    	form.setMethod(FormPanel.METHOD_POST);
    	form.setAction("../admin/importDB");
    	final FileUpload fu = new FileUpload();
    	fu.setName("file");
    	fu.getElement().getStyle().setMarginTop(20, Unit.PX);
    	fu.getElement().getStyle().setMarginBottom(20, Unit.PX);
    	VerticalPanel panel = new VerticalPanel();
    	form.add(panel);
    	panel.setWidth("100%");
    	panel.add(fu);
    	final ProgressBar progress = new ProgressBar(Style.ANIMATED);
    	progress.setVisible(false);
    	progress.setPercent(100);
    	panel.add(progress);
    	final Button submit = new Button("Submit");
    	final Button cancel = new Button("Cancel");
    	submit.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				progress.setVisible(true);
				submit.setEnabled(false);
				cancel.setEnabled(false);
				form.submit();	
			}
		});
    	cancel.setType(ButtonType.PRIMARY);
    	cancel.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
    	form.addSubmitHandler(new SubmitHandler() {
    		public void onSubmit(SubmitEvent event) {
    			if (!"".equalsIgnoreCase(fu.getFilename())) {
    				progress.setVisible(true);
    			}
    			else{
    				event.cancel(); // cancel the event
    				hide();
    			}
    		}
    	});

    	form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
    		public void onSubmitComplete(SubmitCompleteEvent event) {
    			hide();
    		}
    	});
    	parent.add(form);
    	ModalFooter footer = new ModalFooter(submit, cancel);
    	add(footer);
    }
}
