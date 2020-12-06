package org.springframework.samples.petclinic.model.common;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.samples.petclinic.common.CommonError;
import org.springframework.samples.petclinic.common.CommonParameter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;

/**
 * Class used to manage application users
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Entity
@Table(name = "users")
@Getter
@Setter
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

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private Collection<Role> roles;

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

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	protected Collection<Role> getRolesInternal() {
		if (this.roles == null) {
			this.roles = new HashSet<>();
		}

		return this.roles;
	}

	public Collection<Role> getRoles() {
		return getRolesInternal();
	}

	public void addRole(Role role) {
		if (this.getRoles() == null || !this.getRoles().contains(role)) {
			getRolesInternal().add(role);
		}
		if (!role.getUsers().contains(this)) {
			role.addUser(this);
		}
	}

	public void removeRole(Role role) {
		if (this.roles != null) {
			this.roles.remove(role);
			role.getUsers().remove(this);
		}
	}

	private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (String privilege : privileges) {
			authorities.add(new SimpleGrantedAuthority(privilege));
		}
		return authorities;
	}

	private List<String> getPrivileges(Collection<Role> roles) {

		List<String> privileges = new ArrayList<>();
		List<Privilege> collection = new ArrayList<>();
		for (Role role : roles) {
			collection.addAll(role.getPrivileges());
		}
		for (Privilege item : collection) {
			privileges.add(item.getName());
		}
		return privileges;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return getGrantedAuthorities(getPrivileges(this.roles));
	}

}
