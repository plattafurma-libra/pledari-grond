package de.uni_koeln.spinfo.maalr.webapp.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * For Details see <a
 * href="https://developer.mozilla.org/en-US/Persona/Remote_Verification_API"
 * >Remote_Verification_API</a>.
 * 
 * @author Mihail Atanassov (atanassov.mihail@gmail.com)
 * 
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonaVerificationResponse {

	private String email;
	private String status;
	private String reason;
	private String audience;
	private String issuer;
	private long expires;

	public PersonaVerificationResponse() {
	}

	public PersonaVerificationResponse(String email, String status,
			String reason, String audience, String issuer, long expires) {
		this.email = email;
		this.status = status;
		this.audience = audience;
		this.issuer = issuer;
		this.expires = expires;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAudience() {
		return audience;
	}

	public void setAudience(String audience) {
		this.audience = audience;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public long getExpires() {
		return expires;
	}

	public void setExpires(long expires) {
		this.expires = expires;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public String toString() {
		return "PersonaVerificationResponse [email=" + email + ", status="
				+ status + ", reason=" + reason + ", audience=" + audience
				+ ", issuer=" + issuer + ", expires=" + expires + "]";
	}

}
