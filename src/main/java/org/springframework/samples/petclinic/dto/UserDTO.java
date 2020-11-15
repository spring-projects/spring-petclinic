package org.springframework.samples.petclinic.dto;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.samples.petclinic.common.CommonError;
import org.springframework.samples.petclinic.common.CommonParameter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.*;

public class UserDTO extends PersonDTO implements Serializable {

	@Size(min = CommonParameter.PASSWORD_MIN, max = CommonParameter.PASSWORD_MAX, message = CommonError.FORMAT_BETWEEN
			+ CommonParameter.PASSWORD_MIN + " AND " + CommonParameter.PASSWORD_MAX + " !")
	private String password;

	@Size(min = CommonParameter.PASSWORD_MIN, max = CommonParameter.PASSWORD_MAX, message = CommonError.FORMAT_BETWEEN
			+ CommonParameter.PASSWORD_MIN + " AND " + CommonParameter.PASSWORD_MAX + " !")
	private String matchingPassword;

	@Size(min = CommonParameter.EMAIL_MIN, max = CommonParameter.EMAIL_MAX, message = CommonError.FORMAT_BETWEEN
			+ CommonParameter.EMAIL_MIN + " AND " + CommonParameter.EMAIL_MAX + " !")
	@Pattern(regexp = CommonParameter.EMAIL_REGEXP, message = CommonError.EMAIL_FORMAT)
	private String email;

	@Size(max = CommonParameter.PHONE_MAX, message = CommonError.FORMAT_LESS + CommonParameter.PHONE_MAX)
	@Pattern(regexp = CommonParameter.PHONE_REGEXP, message = CommonError.PHONE_FORMAT)
	private String telephone;

	private Set<RoleDTO> roles;

	@Size(max = CommonParameter.STREET_MAX, message = CommonError.FORMAT_LESS + CommonParameter.STREET_MAX + " !")
	private String street1;

	@Size(max = CommonParameter.STREET_MAX, message = CommonError.FORMAT_LESS + CommonParameter.STREET_MAX + " !")
	private String street2;

	@Size(max = CommonParameter.STREET_MAX, message = CommonError.FORMAT_LESS + CommonParameter.STREET_MAX + " !")
	private String street3;

	@Size(min = CommonParameter.ZIP_MIN, max = CommonParameter.ZIP_MAX,
			message = CommonError.FORMAT_BETWEEN + CommonParameter.ZIP_MIN + " AND " + CommonParameter.ZIP_MAX + " !")
	private String zipCode;

	@Size(max = CommonParameter.CITY_MAX, message = CommonError.FORMAT_LESS + CommonParameter.CITY_MAX + " !")
	private String city;

	@Size(max = CommonParameter.COUNTRY_MAX, message = CommonError.FORMAT_LESS + CommonParameter.COUNTRY_MAX + " !")
	private String country;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	protected Set<RoleDTO> getRolesInternal() {
		if (this.roles == null) {
			this.roles = new HashSet<>();
		}
		return this.roles;
	}

	protected void setRolesInternal(Set<RoleDTO> roles) {
		this.roles = roles;
	}

	@XmlElement
	public List<RoleDTO> getRoles() {
		List<RoleDTO> sortedRoles = new ArrayList<>(getRolesInternal());
		PropertyComparator.sort(sortedRoles, new MutableSortDefinition("name", true, true));
		return Collections.unmodifiableList(sortedRoles);
	}

	public void addRole(RoleDTO role) {
		getRolesInternal().add(role);
	}

	public String getStreet1() {
		return street1;
	}

	public void setStreet1(String street1) {
		this.street1 = street1;
	}

	public String getStreet2() {
		return street2;
	}

	public void setStreet2(String street2) {
		this.street2 = street2;
	}

	public String getStreet3() {
		return street3;
	}

	public void setStreet3(String street3) {
		this.street3 = street3;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}
