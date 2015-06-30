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
package de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.wrapper;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtml;

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.common.shared.Role;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorConstants;

public class LexEntryCellWrapper implements ICellWrapper {

	private static final DateTimeFormat format = DateTimeFormat.getFormat("dd/MM/y, HH:mm:ss");

	private LemmaVersionCellWrapper version;
	private EditorConstants constants = GWT.create(EditorConstants.class);

	public LexEntryCellWrapper(LexEntry object, LemmaVersion version) {
		this.entry = object;
		this.version = new LemmaVersionCellWrapper(version);
	}

	private LexEntry entry;
	private int row;
	
	@Override
	public SafeHtml getUserDetails() {
		return version.getUserDetails();
	}
	
	@Override
	public String getIP() {
		return version.getIP();
	}
	
	@Override
	public SafeHtml getRoleLabel() {
		return version.getRoleLabel();
	}
	
	
	public String getFilterButtonTitle() {
		if(entry.getMostRecent().getUserInfo() == null || entry.getMostRecent().getUserInfo().getRole() == Role.GUEST_1) {
			return constants.filterIp();
		} else {
			return constants.filterUser();
		}
	}
	
	@Override
	public String getComment() {
		return version.getComment();
	}
	
	@Override
	public SafeHtml getLemma() {
		return version.getLemma();
	}

//	public SafeHtml getTrustLabel() {
//		SafeHtmlBuilder builder = new SafeHtmlBuilder();
//		builder.appendHtmlConstant("<span class=\"label " + getTrustLevelColor() + "\">");
//		builder.appendEscaped("Trust: 5.3");
//		builder.appendHtmlConstant("</span>");
//		return builder.toSafeHtml();
//	}
	
	
//	private String getTrustLevelColor() {
//		return Math.random() > 0.5 ? LabelType.INFO.get() : LabelType.WARNING.get();
//	}


	public LexEntry getLexEntry() {
		return entry;
	}

	@Override
	public LemmaVersion getLemmaVersion() {
		return version.getLemmaVersion();
	}

	@Override
	public SafeHtml getVerificationLabel() {
		return version.getVerificationLabel();
	}
	
	@Override
	public SafeHtml getVerifierId() {
		return version.getVerifierId();
	}
	
	@Override
	public SafeHtml getStatusLabel() {
		return version.getStatusLabel();
	}
	
	

	@Override
	public SafeHtml getShortLemma() {
		return version.getShortLemma();
	}

	@Override
	public String getShortComment() {
		return version.getShortComment();
	}

	@Override
	public String getDate() {
		return format.format(new Date(version.getLemmaVersion().getTimestamp()));
	}

	public void setRow(int index) {
		this.row = index;
	}

	public int getRow() {
		return row;
	}

	@Override
	public String getRejectLabel() {
		if(entry.getCurrent() == null || !entry.getCurrent().isApproved()) {
			return constants.delete();
		}
		return constants.reject();
	}

	
	
}
