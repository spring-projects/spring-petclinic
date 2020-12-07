package org.springframework.samples.petclinic.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CredentialDTO extends BaseDTO {

	@NotNull
	private String provider;

	@NotNull
	@Size(min = CommonParameter.EMAIL_MIN, max = CommonParameter.EMAIL_MAX, message = CommonError.FORMAT_BETWEEN
			+ CommonParameter.EMAIL_MIN + " AND " + CommonParameter.EMAIL_MAX + " !")
	@Pattern(regexp = CommonParameter.EMAIL_REGEXP, message = CommonError.EMAIL_FORMAT)
	private String email;

	@NotNull
	@Size(min = CommonParameter.PASSWORD_MIN, max = CommonParameter.PASSWORD_MAX, message = CommonError.FORMAT_BETWEEN
		+ CommonParameter.PASSWORD_MIN + " AND " + CommonParameter.PASSWORD_MAX + " !")
	private String password;

	@NotNull
	private Boolean verified;

	private String token;

	private Date expiration;

	public CredentialDTO(UserDTO user) {
		this.setProvider(CommonParameter.DEFAULT_PROVIDER);
		this.email = user.getEmail();
		this.password = user.getId().toString();
		this.verified = false;
		this.setToken();
		this.setExpiration();
	}

	public CredentialDTO(@NotNull String provider, @NotNull @Size(min = CommonParameter.EMAIL_MIN, max = CommonParameter.EMAIL_MAX, message = CommonError.FORMAT_BETWEEN
		+ CommonParameter.EMAIL_MIN + " AND " + CommonParameter.EMAIL_MAX + " !") @Pattern(regexp = CommonParameter.EMAIL_REGEXP, message = CommonError.EMAIL_FORMAT) String email, @NotNull @Size(min = CommonParameter.PASSWORD_MIN, max = CommonParameter.PASSWORD_MAX, message = CommonError.FORMAT_BETWEEN
		+ CommonParameter.PASSWORD_MIN + " AND " + CommonParameter.PASSWORD_MAX + " !") String password, @NotNull Boolean verified) {
		this.provider = provider;
		this.email = email;
		this.password = password;
		this.verified = verified;
	}

	public void setDefaultProvider() {
		this.provider = CommonParameter.DEFAULT_PROVIDER;
	}

	public Boolean isVerified() {
		return verified;
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
