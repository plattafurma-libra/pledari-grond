package de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.user;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import de.uni_koeln.spinfo.maalr.common.shared.Constants;
import de.uni_koeln.spinfo.maalr.common.shared.LightUserInfo;
import de.uni_koeln.spinfo.maalr.common.shared.Role;

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
	
	@UiField
	Label errorLabel;

	private LightUserInfo user;

	private static UserFormUiBinder uiBinder = GWT
			.create(UserFormUiBinder.class);

	interface UserFormUiBinder extends UiBinder<Widget, UserForm> {
	}

	public UserForm(LightUserInfo user) {
		
		this.user = user;
		
		initWidget(uiBinder.createAndBindUi(this));
		
		errorLabel.setVisible(false);
		for (Role r : Role.values()) {
			if(r.getRoleId().equals(Constants.Roles.PERSONA))
				continue;
			roles.addItem(r.getRoleName(), r.toString());
		}
		
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	    Set<ConstraintViolation<LightUserInfo>> violations = validator.validate(user, Default.class);
	    if (!violations.isEmpty()) {
	      StringBuffer errorMessage = new StringBuffer();
	      int i = 1;
	      for (ConstraintViolation<LightUserInfo> constraintViolation : violations) {
	    	  errorMessage.append(i + ") ");
	    	  errorMessage.append(constraintViolation.getPropertyPath() + ": ");
	    	  errorMessage.append(constraintViolation.getMessage());
	    	  i++;
	      }
	      errorLabel.setText(errorMessage.toString());
	      errorLabel.setVisible(true);
	      return;
	    }
	}

}
