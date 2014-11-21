package de.uni_koeln.spinfo.maalr.webapp.ui.editor.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

public interface EditorUiBinderMessages extends Messages {
	
	public static final EditorUiBinderMessages LANG = GWT.create(EditorUiBinderMessages.class);
	
	public String search();
	
	public String lemmaHistory();
	
	public String showAll();
	
	public String deleteHistory();
	
	public String lemmata();
	
	public String newEntry();
	
	public String export();
	
	public String columns();
	
	public String configureColumns();
	
	public String multiSelectBtn();
	
	public String selectAllBtn();
	
	public String deselectAllBtn();
	
	public String rejectSelectedBtn();
	
	public String editBtn();
	
	public String deleteBtn();
	
	public String orderLangOne();
	
	public String orderLangTwo();
	
	public String filterUserBtn();
	
	public String filterVerifierBtn();
	
	public String from();
	
	public String to();
	
	public String ipName();
	
	public String lemmaState();
	
	public String editor();
	
	public String userRole();
	
	public String verification();
	
	public String resetBtn();
	
	public String searchBtn();
	
	public String accountDetails();
	
	public String login();
	
	public String created();
	
	public String lastActive();
	
	public String firstName();
	
	public String lastName();
	
	public String email();
	
	public String role();
	
	public String showEditHistory();
	
	public String saveBtn();
	
}
