package org.springframework.samples.petclinic.model.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.samples.petclinic.common.CommonError;
import org.springframework.samples.petclinic.common.CommonParameter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Class used to manage Credentials for users
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Entity(name = "Credential")
@Table(name = "credentials")
@Getter
@Setter
public class Credential extends BaseEntity {

	private static final int TOKEN_EXPIRATION = 60 * 24;

	@NotNull
	@Column(name = "provider_id")
	private Integer providerId;

	@NotNull
	@Size(min = CommonParameter.EMAIL_MIN, max = CommonParameter.EMAIL_MAX, message = CommonError.FORMAT_BETWEEN
			+ CommonParameter.EMAIL_MIN + " AND " + CommonParameter.EMAIL_MAX + " !")
	@Pattern(regexp = CommonParameter.EMAIL_REGEXP, message = CommonError.EMAIL_FORMAT)
	@Column(name = "email", length = CommonParameter.EMAIL_MAX)
	private String email;

	@NotNull
	@Size(min = CommonParameter.PASSWORD_MIN, max = CommonParameter.PASSWORD_MAX, message = CommonError.FORMAT_BETWEEN
			+ CommonParameter.PASSWORD_MIN + " AND " + CommonParameter.PASSWORD_MAX + " !")
	@Column(name = "password", length = CommonParameter.PASSWORD_MAX)
	private String password;

	@NotNull
	@Column(name = "verified")
	private Boolean verified;

	@Column(name = "token")
	private String token;

	@Column(name = "expiration")
	private Date expiration;

	public Boolean isVerified() {
		return verified;
	}

	public void setExpiration() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Timestamp(cal.getTime().getTime()));
		cal.add(Calendar.MINUTE, TOKEN_EXPIRATION);
		this.expiration = cal.getTime();
	}

	public void setToken() {
		this.token = UUID.randomUUID().toString();
	}

}
