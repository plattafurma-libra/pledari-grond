package de.uni_koeln.spinfo.maalr.webapp.service;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Service
public class AccountService {

	private static final Logger LOG = LoggerFactory.getLogger(AccountService.class);
	
	@Autowired
	private UserInfoBackend usersRepository;
	
	@Autowired
	private PGAutenticationProvider authProvider;
	
	@Autowired
	private ApplicationContext appContext;

	private static final String BASE_MESSAGES = "de.uni_koeln.spinfo.maalr.webapp.i18n.text";
	private static final String USER_EXISTS = "maalr.signup.error.user.already.exists";
	private static final String PW_OR_EMAIL_EMPTY = "maalr.signup.error.password.or.email.empty";
	private static final String PW_TOO_SHORT = "maalr.signup.error.password.to.short";
	private static final String PW_CONFIRM_NOT_SAME = "maalr.signup.error.password.confirm.not.same";
	private static final String EMAIL_NOT_VALID = "maalr.signup.error.email.not.valid";

	public @ResponseBody
	UserFormValidationResponse createAccount(UserForm userForm, final String locale) {

		if (userForm.getPassword().isEmpty() || userForm.getPassword() == null
				|| userForm.getEmail().isEmpty() || userForm.getEmail() == null) {
			return error(PW_OR_EMAIL_EMPTY, locale);
		}

		if (userForm.getPassword().length() < 6) {
			return error(PW_TOO_SHORT, locale);
		}

		if (!userForm.getPassword().equals(userForm.getConfirm())) {
			return error(PW_CONFIRM_NOT_SAME, locale);
		}

		try {
			InternetAddress email = new InternetAddress(userForm.getEmail());
			email.validate();
		} catch (AddressException ex) {
			return error(EMAIL_NOT_VALID, locale);
		}

		MaalrUserInfo user = new MaalrUserInfo(userForm.getEmail(),
				userForm.getPassword(), Role.TRUSTED_EX_3);
		try {
			MaalrUserInfo insert = usersRepository.insert(user);
		} catch (InvalidUserException e) {
			return error(USER_EXISTS, locale);
		}

		UserFormValidationResponse response = new UserFormValidationResponse();

		response.setStatus("SUCCESS");
		response.setRedirect("login");

		return response;
	}

	private UserFormValidationResponse error(final String errorCode,
			final String locale) {
		
		LOG.error("Error occurred during account creation status={}", errorCode);
		
		UserFormValidationResponse response = new UserFormValidationResponse();
		response.setStatus("ERROR");
		response.setErrorMessage(ResourceBundle.getBundle(BASE_MESSAGES,
				new Locale(locale)).getString(errorCode));
		return response;
	}

}
