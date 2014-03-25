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

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.ModalFooter;
import com.github.gwtbootstrap.client.ui.ProgressBar;
import com.github.gwtbootstrap.client.ui.base.ProgressBarBase.Style;
import com.github.gwtbootstrap.client.ui.constants.BackdropType;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import de.uni_koeln.spinfo.maalr.webapp.ui.common.shared.util.Logger;

public class Dialog extends Modal {
	
	private VerticalPanel contentWrapper;
	private ProgressBar progessBar;
	
	public Dialog(String title, Widget content, boolean closeVisible, Widget... widgets) {
		super.setAnimation(true); 
		super.setBackdrop(BackdropType.STATIC);
		super.setTitle(title);
		super.setHideOthers(false);
		super.setDynamicSafe(true);
		ModalFooter footer = new ModalFooter(widgets);
		contentWrapper = new VerticalPanel();
		contentWrapper.setSize("100%", "100%");
		contentWrapper.add(content);
		progessBar = new ProgressBar(Style.ANIMATED);
		contentWrapper.add(progessBar);
		progessBar.setVisible(false);
		super.add(contentWrapper);
		super.add(footer);
		super.setCloseVisible(closeVisible);
	}
	
	private static String getMessageFrom(Throwable error) {
		if(error == null) {
			return "An unexpected error occurred.";
		}
		if(error.getMessage() != null) {
			return error.getMessage().trim();
		}
		return getMessageFrom(error.getCause()).trim();
	}
	
	
	public static void showError(String title, Throwable error) {
		Button okButton = new Button("OK");
		String errorMessage = getMessageFrom(error);
		Label messageLabel = null;
		Logger.getLogger(Dialog.class).info("Message: " + errorMessage + ", length: " + errorMessage.length());
		if(errorMessage.equals("0")) {
			messageLabel = new Label("We're sorry, but it was impossible to access the server. Please check your network connection and make sure the server is running.");
		} else {
			messageLabel = new Label("We're sorry, but something unexpected happened.\n\nError Message: " + errorMessage + (errorMessage.endsWith(".") ? "" : "."));
		}
		final Dialog dialog = new Dialog(title, messageLabel, true, okButton);
		// FIXME: There seems to be a bug in gwt-bootstrap: Under some circumstances
		// (multiple modal dialogs), the clickhandler will not be called.
		okButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				dialog.hide();
			}
		});
		dialog.show();
	}

	public static void showError(String title, String message) {
		Button okButton = new Button("OK");
		Label messageLabel = null;
		Logger.getLogger(Dialog.class).info("Message: " + message);
		messageLabel = new Label(message);
		final Dialog dialog = new Dialog(title, messageLabel, true, okButton);
		dialog.getElement().setId("showErrorDialogMaalr");
		okButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				dialog.hide();
			}
		});
		dialog.show();
	}

	public static void showError(String title) {
		Button okButton = new Button("OK");
		Label messageLabel = new Label(title);
		final Dialog dialog = new Dialog(title, messageLabel, true, okButton);
		okButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				dialog.hide();
			}
		});
		dialog.show();
	}

	public static void confirm(String dialogTitle, String message, String confirmDeleteButton, String cancelDeleteButton, final Command ok, final Command cancel, boolean okIsPrimary) {
//		public static void confirm(String dialogTitle, String message, final Command ok, final Command cancel, boolean okIsPrimary) {
		Button okButton = new Button(confirmDeleteButton);
		okButton.setType(okIsPrimary ? ButtonType.PRIMARY : ButtonType.DEFAULT);
		Button cancelButton = new Button(cancelDeleteButton);
		cancelButton.setType(!okIsPrimary ? ButtonType.PRIMARY : ButtonType.DEFAULT);
		Label label = new Label(message);
		final Dialog dialog = new Dialog(dialogTitle, label, false, okButton, cancelButton);
		okButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				dialog.hide();
				if(ok != null) {
					ok.execute();
				}
			}
		});
		cancelButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				dialog.hide();
				if(cancel != null) {
					cancel.execute();
				}
			}
		});
		dialog.show();
	}

	public static void showInfo(String dialogTitle, String message) {
		Button okButton = new Button("OK");
		okButton.setType(ButtonType.PRIMARY);
		Label label = new Label(message);
		final Dialog dialog = new Dialog(dialogTitle, label, false, okButton);
		okButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				dialog.hide();
			}
		});
		dialog.show();
	}
	
	

}
