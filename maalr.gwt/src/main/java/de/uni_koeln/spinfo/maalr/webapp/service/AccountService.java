package de.uni_koeln.spinfo.maalr.webapp.service;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import de.uni_koeln.spinfo.maalr.common.shared.Role;
import de.uni_koeln.spinfo.maalr.common.shared.form.UserForm;
import de.uni_koeln.spinfo.maalr.common.shared.form.UserFormValidationResponse;
import de.uni_koeln.spinfo.maalr.login.MaalrUserInfo;
import de.uni_koeln.spinfo.maalr.login.UserInfoBackend;
import de.uni_koeln.spinfo.maalr.login.custom.PGAutenticationProvider;
import de.uni_koeln.spinfo.maalr.mongo.exceptions.InvalidUserException;

@Service public class AccountService {

	@Autowired private UserInfoBackend usersRepository;
	@Autowired private PGAutenticationProvider authProvider;
	@Autowired private ApplicationContext appContext;

	private static final String BASE_MESSAGES = "de.uni_koeln.spinfo.maalr.webapp.i18n.text";
	private static final String USER_EXISTS = "maalr.signup.error.user.already.exists";
	private static final String PW_OR_EMAIL_EMPTY = "maalr.signup.error.password.or.email.empty";
	private static final String PW_TOO_SHORT = "maalr.signup.error.password.to.short";
	private static final String EMAIL_NOT_VALID = "maalr.signup.error.email.not.valid";

	public @ResponseBody UserFormValidationResponse createAccount(UserForm userForm, String locale) {

		UserFormValidationResponse response = new UserFormValidationResponse();
		boolean valid = true;
		String errorMessage;

		try {
			InternetAddress email = new InternetAddress(userForm.getEmail());
			email.validate();
		} catch (AddressException ex) {
			valid = false;
		}

		if (userForm.getPassword().isEmpty() || userForm.getPassword() == null
				|| userForm.getEmail().isEmpty() || userForm.getEmail() == null) {
			response.setStatus("ERROR");
			errorMessage = ResourceBundle.getBundle(BASE_MESSAGES,
					new Locale(locale)).getString(PW_OR_EMAIL_EMPTY);
			response.setErrorMessage(errorMessage);
			return response;
		}

		if (userForm.getPassword().length() < 4) {
			response.setStatus("ERROR");
			errorMessage = ResourceBundle.getBundle(BASE_MESSAGES,
					new Locale(locale)).getString(PW_TOO_SHORT);
			response.setErrorMessage(errorMessage);
		}
		
		if (valid) {
			MaalrUserInfo user = new MaalrUserInfo(userForm.getEmail(),
					userForm.getPassword(), Role.TRUSTED_EX_3);
			try {
				usersRepository.insert(user);
			} catch (InvalidUserException e) {
				e.printStackTrace();
				response.setStatus("ERROR");
				errorMessage = ResourceBundle.getBundle(BASE_MESSAGES,
						new Locale(locale)).getString(USER_EXISTS);
				response.setErrorMessage(errorMessage);
				return response;
			}
			response.setStatus("SUCCESS");
			response.setRedirect("login");
			return response;
		} else {
			response.setStatus("ERROR");
			errorMessage = ResourceBundle.getBundle(BASE_MESSAGES,
					new Locale(locale)).getString(EMAIL_NOT_VALID);
			response.setErrorMessage(errorMessage);
			return response;
		}
	}

}
