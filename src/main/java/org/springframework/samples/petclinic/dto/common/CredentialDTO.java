package org.springframework.samples.petclinic.dto.common;

import org.springframework.samples.petclinic.common.CommonError;
import org.springframework.samples.petclinic.common.CommonParameter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Simple Data Transfert Object representing a Credential.
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
public class CredentialDTO extends BaseDTO {

	@NotNull
	private String provider;

	@NotNull
	@Size(min = CommonParameter.EMAIL_MIN, max = CommonParameter.EMAIL_MAX, message = CommonError.FORMAT_BETWEEN
			+ CommonParameter.EMAIL_MIN + " AND " + CommonParameter.EMAIL_MAX + " !")
	@Pattern(regexp = CommonParameter.EMAIL_REGEXP, message = CommonError.EMAIL_FORMAT)
	private String email;

	@NotNull
	private Boolean verified;

	private String token;

	private Date expiration;

	@NotNull
	@Size(min = CommonParameter.PASSWORD_MIN, max = CommonParameter.PASSWORD_MAX, message = CommonError.FORMAT_BETWEEN
			+ CommonParameter.PASSWORD_MIN + " AND " + CommonParameter.PASSWORD_MAX + " !")
	private String password;

	public CredentialDTO() {
	}

	public CredentialDTO(UserDTO user) {
		this.verified = false;
		this.setToken();
		this.setExpiration();
		this.setProvider(CommonParameter.DEFAULT_PROVIDER);
		this.email = user.getEmail();
		this.password = user.getId().toString();
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public void setDefaultProvider() {
		this.provider = CommonParameter.DEFAULT_PROVIDER;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean isVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setExpiration() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Timestamp(cal.getTime().getTime()));
		cal.add(Calendar.MINUTE, CommonParameter.TOKEN_EXPIRATION);
		this.expiration = cal.getTime();
	}

	public void setToken() {
		this.token = UUID.randomUUID().toString();
	}

	public boolean isNotExpired() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Timestamp(cal.getTime().getTime()));

		return this.expiration.after(Date.from(Instant.now()));
	}

}
