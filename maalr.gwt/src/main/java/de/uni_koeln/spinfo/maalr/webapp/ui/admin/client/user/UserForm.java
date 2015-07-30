package de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.user;

import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class UserForm extends Composite {
	
	@UiField
	TextBox login;
	
	@UiField
	TextBox firstName;
	
	@UiField
	TextBox lastName;
	
	@UiField
	TextBox email;
	
	@UiField
	ListBox roles;

	private static UserFormUiBinder uiBinder = GWT
			.create(UserFormUiBinder.class);

	interface UserFormUiBinder extends UiBinder<Widget, UserForm> {
	}

	public UserForm() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
