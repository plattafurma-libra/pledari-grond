package de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.user;


import javax.validation.Validator;
import javax.validation.groups.Default;

import com.google.gwt.core.client.GWT;
import com.google.gwt.validation.client.AbstractGwtValidatorFactory;
import com.google.gwt.validation.client.GwtValidation;
import com.google.gwt.validation.client.impl.AbstractGwtValidator;

import de.uni_koeln.spinfo.maalr.common.shared.LightUserInfo;

public class UserValidatorFactory extends AbstractGwtValidatorFactory {

	@GwtValidation(value = LightUserInfo.class, groups = {Default.class})
	public interface GwtValidator extends Validator {
	}

	@Override
	public AbstractGwtValidator createValidator() {
		return GWT.create(GwtValidator.class);
	  }

}