package org.springframework.samples.petclinic.model.common;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.samples.petclinic.common.CommonError;
import org.springframework.samples.petclinic.common.CommonParameter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.*;

/**
 * Class used to manage application users
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Entity(name = "User")
@Table(name = "users")
public class User extends Person implements Serializable, UserDetails {

	@NotNull
	@Size(min = CommonParameter.EMAIL_MIN, max = CommonParameter.EMAIL_MAX, message = CommonError.FORMAT_BETWEEN
			+ CommonParameter.EMAIL_MIN + " AND " + CommonParameter.EMAIL_MAX + " !")
	@Pattern(regexp = CommonParameter.EMAIL_REGEXP, message = CommonError.EMAIL_FORMAT)
	@Column(name = "email", unique = true, length = CommonParameter.EMAIL_MAX)
	private String email;

	@Size(min = CommonParameter.PASSWORD_MIN, max = CommonParameter.PASSWORD_MAX, message = CommonError.FORMAT_BETWEEN
			+ CommonParameter.PASSWORD_MIN + " AND " + CommonParameter.PASSWORD_MAX + " !")
	@Column(name = "password", length = CommonParameter.PASSWORD_MAX)
	private String password;

	@NotNull
	@Column(name = "enabled")
	private boolean enabled;

	@NotNull
	@Column(name = "account_unexpired")
	private boolean accountNonExpired;

	@NotNull
	@Column(name = "account_unlocked")
	private boolean accountNonLocked;

	@NotNull
	@Column(name = "credential_unexpired")
	private boolean credentialsNonExpired;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;

	@Size(max = CommonParameter.PHONE_MAX, message = CommonError.FORMAT_LESS + CommonParameter.PHONE_MAX)
	// @Pattern(regexp = CommonParameter.PHONE_REGEXP, message = CommonError.PHONE_FORMAT)
	@Column(name = "telephone", length = CommonParameter.EMAIL_MAX)
	private String telephone;

	@Size(max = CommonParameter.STREET_MAX, message = CommonError.FORMAT_LESS + CommonParameter.STREET_MAX + " !")
	@Column(name = "street1", length = CommonParameter.STREET_MAX)
	private String street1;

	@Size(max = CommonParameter.STREET_MAX, message = CommonError.FORMAT_LESS + CommonParameter.STREET_MAX + " !")
	@Column(name = "street2", length = CommonParameter.STREET_MAX)
	private String street2;

	@Size(max = CommonParameter.STREET_MAX, message = CommonError.FORMAT_LESS + CommonParameter.STREET_MAX + " !")
	@Column(name = "street3", length = CommonParameter.STREET_MAX)
	private String street3;

	@Size(max = CommonParameter.ZIP_MAX, message = CommonError.FORMAT_LESS + CommonParameter.ZIP_MAX + " !")
	@Column(name = "zip_code", length = CommonParameter.ZIP_MAX)
	private String zipCode;

	@Size(max = CommonParameter.CITY_MAX, message = CommonError.FORMAT_LESS + CommonParameter.CITY_MAX + " !")
	@Column(name = "city", length = CommonParameter.CITY_MAX)
	private String city;

	@Size(max = CommonParameter.COUNTRY_MAX, message = CommonError.FORMAT_LESS + CommonParameter.COUNTRY_MAX + " !")
	@Column(name = "country", length = CommonParameter.COUNTRY_MAX)
	private String country;

	@Override
	public String getUsername() {
		return email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

		this.roles.forEach(role -> grantedAuthorities.add(new SimpleGrantedAuthority(role.getName())));

		return grantedAuthorities;
	}

	protected Set<Role> getRolesInternal() {
		if (this.roles == null) {
			this.roles = new HashSet<>();
		}
		return this.roles;
	}

	protected void setRolesInternal(Set<Role> roles) {
		this.roles = roles;
	}

	@XmlElement
	public List<Role> getRoles() {
		List<Role> sortedRoles = new ArrayList<>(getRolesInternal());
		PropertyComparator.sort(sortedRoles, new MutableSortDefinition("name", true, true));
		return Collections.unmodifiableList(sortedRoles);
	}

	public int getNrOfRoles() {
		return getRolesInternal().size();
	}

	public void addRole(Role role) {
		getRolesInternal().add(role);
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
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
