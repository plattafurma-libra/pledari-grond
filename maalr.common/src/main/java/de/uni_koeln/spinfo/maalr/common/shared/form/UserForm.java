package de.uni_koeln.spinfo.maalr.common.shared.form;

public class UserForm {

	private String email;
	private String password;
	private String confirm;

	public UserForm() {
	}

	public UserForm(final String email, final String password, final String confirm) {
		setEmail(email);
		setPassword(password);
		setConfirm(confirm);
	}
	
	public void setConfirm(final String confirm) {
		this.confirm = confirm;
	}
	
	public String getConfirm() {
		return confirm;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public void setPassword(final 	String password) {
		this.password = password;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserForm other = (UserForm) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserForm [email=" + email + ", password=" + password + "]";
	}

}
