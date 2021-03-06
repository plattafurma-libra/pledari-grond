package de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.user;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.HelpInline;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import de.uni_koeln.spinfo.maalr.common.shared.Constants;
import de.uni_koeln.spinfo.maalr.common.shared.LightUserInfo;
import de.uni_koeln.spinfo.maalr.common.shared.Role;
import de.uni_koeln.spinfo.maalr.services.admin.shared.UserService;
import de.uni_koeln.spinfo.maalr.services.admin.shared.UserServiceAsync;


/**
 * @author Mihail Atanassov <atanassov.mihail@gmail.com>
 */
public class UserForm extends Composite {
	
	@UiField TextBox login;
	@UiField TextBox password;
	@UiField ListBox roles;
	@UiField HelpInline errorLogin;
	@UiField HelpInline errorPassword;
	
	private Modal parent;

	private LightUserInfo toBeInserted;
	private UserServiceAsync service;

	private static UserFormUiBinder uiBinder = GWT.create(UserFormUiBinder.class);


	interface UserFormUiBinder extends UiBinder<Widget, UserForm> {
	}

	public UserForm(LightUserInfo user, Modal modal, Button execute, final AsyncCallback<List<LightUserInfo>> callback) {
		initWidget(uiBinder.createAndBindUi(this));
		toBeInserted = user;
		parent = modal;
		service = GWT.create(UserService.class);
		execute.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				clearErrorMessages();
				
				toBeInserted.setLogin(login.getValue());
				toBeInserted.setPassword(password.getValue());
				toBeInserted.setRole(Role.valueOf(roles.getSelectedValue()));
				
				if(isValid(toBeInserted)) {
					service.insertNewUser(toBeInserted, new AsyncCallback<LightUserInfo>() {
						
						@Override
						public void onSuccess(LightUserInfo result) {
							service.getAllUsers(0, Integer.MAX_VALUE, null, false, callback);
						}
						
						@Override
						public void onFailure(Throwable caught) {
						}
					});
					parent.hide();
				}
			}

			private void clearErrorMessages() {
				Style loginStyle = login.getElement().getStyle();
				loginStyle.setBorderColor("#CCCCCC");
				errorLogin.setVisible(false);
				Style passwordStyle = password.getElement().getStyle();
				passwordStyle.setBorderColor("#CCCCCC");
				errorPassword.setVisible(false);
			}

			private boolean isValid(LightUserInfo toBeInserted) {
				Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
			    Set<ConstraintViolation<LightUserInfo>> violations = validator.validate(toBeInserted, Default.class);
			    if (!violations.isEmpty()) {
			      for (ConstraintViolation<LightUserInfo> constraintViolation : violations) {
			    	  Path propertyPath = constraintViolation.getPropertyPath();
			    	  
			    	  if(propertyPath.toString().equalsIgnoreCase(Constants.Users.LOGIN)) {
			    		  String loginErrorMessage = constraintViolation.getMessage();
			    		  showErrorMessage(login, errorLogin, loginErrorMessage);
			    	  }
			    	  if(propertyPath.toString().equalsIgnoreCase(Constants.Users.PASSWORD)) {
			    		  String passwordErrorMessage = constraintViolation.getMessage();
			    		  showErrorMessage(password, errorPassword, passwordErrorMessage);
			    	  }
			      }
			      return false;
			    }
				return true;
			}
		});

		errorLogin.setVisible(false);
		errorPassword.setVisible(false);
		
		for (Role r : Role.values()) {
			roles.addItem(r.getRoleName(), r.toString());
		}
	}
	
	private void showErrorMessage(TextBox input, HelpInline helpLine, String errorMessage) {
		Style inputStyle = input.getElement().getStyle();
		inputStyle.setBorderColor("#FF0000");
		helpLine.setText(errorMessage);
		Style helpLineStyle = helpLine.getElement().getStyle();
		helpLineStyle.setColor("#FF0000");
		helpLineStyle.setFontSize(10, Unit.PX);
		helpLine.setVisible(true);
	}

}

