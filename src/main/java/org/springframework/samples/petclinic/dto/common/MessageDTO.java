package org.springframework.samples.petclinic.dto.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.samples.petclinic.common.CommonError;
import org.springframework.samples.petclinic.common.CommonParameter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class MessageDTO implements Serializable {

	@NotNull
	@Size(min = CommonParameter.FIRSTNAME_MIN, max = CommonParameter.FIRSTNAME_MAX, message = CommonError.FORMAT_BETWEEN
			+ CommonParameter.FIRSTNAME_MIN + " AND " + CommonParameter.FIRSTNAME_MAX + " !")
	private String firstName;

	@NotNull
	@Size(min = CommonParameter.LASTNAME_MIN, max = CommonParameter.LASTNAME_MAX, message = CommonError.FORMAT_BETWEEN
			+ CommonParameter.LASTNAME_MIN + " AND " + CommonParameter.LASTNAME_MAX + " !")
	private String lastName;

	@NotNull
	@Size(min = CommonParameter.EMAIL_MIN, max = CommonParameter.EMAIL_MAX, message = CommonError.FORMAT_BETWEEN
			+ CommonParameter.EMAIL_MIN + " AND " + CommonParameter.EMAIL_MAX + " !")
	@Pattern(regexp = CommonParameter.EMAIL_REGEXP, message = CommonError.EMAIL_FORMAT)
	private String from;

	@NotNull
	@Size(min = CommonParameter.EMAIL_MIN, max = CommonParameter.EMAIL_MAX, message = CommonError.FORMAT_BETWEEN
			+ CommonParameter.EMAIL_MIN + " AND " + CommonParameter.EMAIL_MAX + " !")
	@Pattern(regexp = CommonParameter.EMAIL_REGEXP, message = CommonError.EMAIL_FORMAT)
	private String to;

	@NotNull
	private String subject;

	@NotNull
	private String content;

	private String link;

	@JsonCreator
	public MessageDTO(@JsonProperty("firstName") String firstName, @JsonProperty("lastName") String lastName,
			@JsonProperty("from") String from, @JsonProperty("to") String to, @JsonProperty("subject") String subject,
			@JsonProperty("content") String content, @JsonProperty("link") String link) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.from = from;
		this.to = to;
		this.subject = subject;
		this.content = content;
		this.link = link;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public String toString() {
		return "MessageDTO{" + "first name='" + firstName + '\'' + ", last name='" + lastName + '\'' + ", from='" + from
				+ '\'' + ", to='" + to + '\'' + ", subject='" + subject + '\'' + ", content='" + content + '\''
				+ ", link='" + link + '\'' + '}';
	}

}
