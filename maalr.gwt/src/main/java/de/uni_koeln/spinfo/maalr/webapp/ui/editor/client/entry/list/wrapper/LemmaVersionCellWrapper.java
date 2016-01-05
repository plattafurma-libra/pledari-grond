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

import com.google.gwt.core.shared.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LightUserInfo;
import de.uni_koeln.spinfo.maalr.common.shared.Role;
import de.uni_koeln.spinfo.maalr.common.shared.description.Escaper;
import de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription;
import de.uni_koeln.spinfo.maalr.common.shared.description.UseCase;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.AsyncLemmaDescriptionLoader;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorConstants;

public class LemmaVersionCellWrapper implements ICellWrapper {
	
	private LemmaVersion lemma;
	private static final LemmaDescription lemmaDescription = AsyncLemmaDescriptionLoader.getDescription();
	private static final DateTimeFormat format = DateTimeFormat.getFormat("dd/MM/y, kk:mm:ss");
	private EditorConstants constants = GWT.create(EditorConstants.class);

	public LemmaVersionCellWrapper(LemmaVersion lemma) {
		this.lemma = lemma;
	}
	
	public interface LemmaTemplates extends SafeHtmlTemplates {
		
	     @Template("<div class=\"label {0}\">{1}</div>")
	     SafeHtml verificationLabel(String labelStyle, String name);
	     
	     @Template("<div class=\"label {0}\">{1}</div>")
	     SafeHtml statusLabel(String labelStyle, String name);
	     
	     @Template("<div class=\"label {0}\">{1}</div>")
	     SafeHtml roleLabel(String labelStyle, String name);
	     
	     @Template("<div>{0}&nbsp;{1}</div>")
	     SafeHtml userName(String firstName, String lastName);
	     
	     @Template("<div>{0}</div>")
	     SafeHtml login(String login);
	     
	     @Template("<div>{0}</div>")
	     SafeHtml verifier(String verifierId);
	     
	     @Template("<div style=\"font-size:90%;\">{0}</div>")
	     SafeHtml ipSmall(String ip);
	     
	     @Template("<div>{0}</div>")
	     SafeHtml ipDefault(String ip);
	     
	   }
	
	private static final LemmaTemplates templates = GWT.create(LemmaTemplates.class);
	
	private Escaper escaper = new Escaper() {
		
		@Override
		public String escape(String text) {
			return SafeHtmlUtils.htmlEscape(text);
		}
	};
	
	@Override
	public SafeHtml getVerificationLabel() {
		String verificationName = (lemma.getVerification().getVerificationName().equals("Accepted"))?constants.accepted():constants.unverified();
		return templates.verificationLabel(getVerificationLabelStyle(), verificationName);
	}

	@Override
	public SafeHtml getStatusLabel() {
		String stateName = (lemma.getStatus().getStateName().equals("New")) ? constants.newEntry() : constants.modified();
		return templates.statusLabel(getStateStyle(), stateName);
	}

	@Override
	public SafeHtml getVerifierId() {
		if(lemma.getVerifierId() == null) return templates.verifier("-");
		return templates.verifier(lemma.getVerifierId());
	}

	@Override
	public SafeHtml getUserDetails() {
		LightUserInfo userInfo = lemma.getUserInfo();
		SafeHtmlBuilder builder = new SafeHtmlBuilder();
		if(userInfo != null && userInfo.getRole() != Role.GUEST_1) {
			String firstName = userInfo.getFirstName();
			String lastName = userInfo.getLastName();
			if(firstName != null && lastName != null) {
				builder.append(templates.userName(firstName, lastName));
			} else {
				builder.append(templates.login(userInfo.getLogin()));
			}
			if(lemma.getIP() != null) {
				builder.append(templates.ipSmall(lemma.getIP()));
			} else {
				builder.append(templates.ipSmall(constants.unknownIp()));
			}
		} else {
			if(lemma.getIP() != null) {
				builder.append(templates.ipDefault(lemma.getIP()));
			} else {
				builder.append(templates.ipDefault(constants.unknownIp()));
			}
		}
		return builder.toSafeHtml();
	}
	
	@Override
	public String getIP() {
		return getDisplayString(lemma.getIP());
	}
	
	@Override
	public SafeHtml getRoleLabel() {
		String role = getRole();
		if(role.equals("Admin")) role = constants.admin();
		if(role.equals("Internal")) role = constants.internal();
		if(role.equals("External")) role = constants.external();
		if(role.equals("Open ID")) role = constants.openId();
		if(role.equals("Guest")) role = constants.guest();
		return templates.roleLabel(getRoleStyle(), role);
	}

//	@Override
//	public SafeHtml getRoleLabel() {
//		if(getRole().equals("Admin"))return templates.roleLabel(getRoleStyle(), constants.admin());
//		String role = getRole().equals("Guest")?constants.guest():constants.internal();
//		return templates.roleLabel(getRoleStyle(), role);
//	}

	private String getRoleStyle() {
		if(lemma.getUserInfo() == null) {
			return "guest-label";
		}
		switch(lemma.getUserInfo().getRole()) {
			case ADMIN_5: return "admin-label";
			case TRUSTED_IN_4: return "internal-label";
			case TRUSTED_EX_3: return "trusted-label";
			case OPENID_2: return "openid-label";
			case GUEST_1: return "guest-label";
			default: return "guest-label";
		}
	}
	

	@Override
	public String getComment() {
		String comment = lemma.getEntryValue(LemmaVersion.COMMENT);
		if(comment == null) return "";
		return comment;
	}

	@Override
	public SafeHtml getLemma() {
		SafeHtmlBuilder builder = new SafeHtmlBuilder();
		builder.appendHtmlConstant(lemmaDescription.toString(lemma, UseCase.RESULT_LIST, true));
		builder.appendHtmlConstant(" ⇔ ");
		builder.appendHtmlConstant(lemmaDescription.toString(lemma, UseCase.RESULT_LIST, false));
		return builder.toSafeHtml();
	}

	@Override
	public SafeHtml getShortLemma() {
		SafeHtmlBuilder builder = new SafeHtmlBuilder();
		String first = lemmaDescription.toString(lemma, UseCase.RESULT_LIST, true).trim();
		if(first.length() > 85) {
			first = escaper.escape(first.substring(0,40) + "..." + first.substring(first.length()-40));
		}
		String second = lemmaDescription.toString(lemma, UseCase.RESULT_LIST, false).trim();
		if(second.length() > 85) {
			second = escaper.escape(second.substring(0,40) + "..." + first.substring(first.length()-40));
		}
		builder.appendHtmlConstant(first);
		builder.appendHtmlConstant(" ⇔ ");
		builder.appendHtmlConstant(second);
		return builder.toSafeHtml();
	}

	@Override
	public String getShortComment() {
		String comment = lemma.getEntryValue(LemmaVersion.COMMENT);
		if(comment == null) return "";
		comment = comment.trim();
		if(comment.length() > 80) {
			comment = comment.substring(0, 40) + "..." + comment.substring(comment.length()-40);
		}
		return comment;
	}

	@Override
	public LemmaVersion getLemmaVersion() {
		return lemma;
	}

	@Override
	public String getDate() {
		return format.format(new Date(lemma.getTimestamp()));
	}
	

	private String getRole() {
		if(lemma.getUserInfo() == null) {
			return Role.GUEST_1.getRoleName();
		}
		return lemma.getUserInfo().getRole().getRoleName();
	}
	

	private String getVerificationLabelStyle() {
		switch (lemma.getVerification()) {
			case ACCEPTED: return "verification-accepted-label";
			case OUTDATED: return "verification-outdated-label";
			case REJECTED: return "verification-rejected-label";
			case UNVERIFIED: return "verification-unverified-label";
			default: return "";
		}
	}
	
	private String getStateStyle() {
		switch (lemma.getStatus()) {
			case DELETED: return "status-deleted-label";
			case NEW_ENTRY: return "status-created-label";
			case NEW_MODIFICATION: return "status-modified-label";
			case UNDEFINED: return "status-undefined-label";
			default: return "";
		}
	}
	
	
	private String getDisplayString(String toDisplay) {
		if (toDisplay == null || toDisplay.trim().length() == 0) {
			return "--";
		}
		return toDisplay.trim();
	}

	@Override
	public String getRejectLabel() {
		return constants.reject();
	}
	

}
