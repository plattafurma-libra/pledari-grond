package de.uni_koeln.spinfo.maalr.common.shared.form;


public class UserFormValidationResponse {

	private String status;
	private String errorMessage;
	private String redirect;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}

	public String getRedirect() {
		return redirect;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

}
