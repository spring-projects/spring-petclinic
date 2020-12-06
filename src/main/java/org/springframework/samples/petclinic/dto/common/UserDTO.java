package org.springframework.samples.petclinic.dto.common;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.samples.petclinic.common.CommonError;
import org.springframework.samples.petclinic.common.CommonParameter;
import org.springframework.samples.petclinic.model.common.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

import java.util.*;

@Getter
@Setter
public class UserDTO extends PersonDTO implements Serializable, UserDetails {

	@Size(min = CommonParameter.EMAIL_MIN, max = CommonParameter.EMAIL_MAX, message = CommonError.FORMAT_BETWEEN
			+ CommonParameter.EMAIL_MIN + " AND " + CommonParameter.EMAIL_MAX + " !")
	@Pattern(regexp = CommonParameter.EMAIL_REGEXP, message = CommonError.EMAIL_FORMAT)
	private String email;

	@Size(min = CommonParameter.PASSWORD_MIN, max = CommonParameter.PASSWORD_MAX, message = CommonError.FORMAT_BETWEEN
			+ CommonParameter.PASSWORD_MIN + " AND " + CommonParameter.PASSWORD_MAX + " !")
	private String password;

	@Size(min = CommonParameter.PASSWORD_MIN, max = CommonParameter.PASSWORD_MAX, message = CommonError.FORMAT_BETWEEN
			+ CommonParameter.PASSWORD_MIN + " AND " + CommonParameter.PASSWORD_MAX + " !")
	private String matchingPassword;

	private boolean enabled;

	private boolean accountNonExpired;

	private boolean accountNonLocked;

	private boolean credentialsNonExpired;

	private Collection<RoleDTO> roles;

	@Size(max = CommonParameter.PHONE_MAX, message = CommonError.FORMAT_LESS + CommonParameter.PHONE_MAX)
	// @Pattern(regexp = CommonParameter.PHONE_REGEXP, message = CommonError.PHONE_FORMAT)
	private String telephone;

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

	public UserDTO() {
		super();
		this.enabled = false;
		this.accountNonLocked = true;
		this.accountNonExpired = true;
		this.credentialsNonExpired = true;
	}

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

	public boolean hasRole(String roleName) {
		for (RoleDTO roleDTO : this.roles) {
			if (roleDTO.getName().equals(roleName))
				return true;
		}
		return false;
	}

	public boolean hasPrivilege(String privilegeName) {
		for (RoleDTO roleDTO : this.roles) {
			for (PrivilegeDTO privilegeDTO : roleDTO.getPrivileges()) {
				if (privilegeDTO.getName().equals(privilegeName))
					return true;
			}
		}
		return false;
	}

	protected Collection<RoleDTO> getRolesInternal() {
		if (this.roles == null) {
			this.roles = new HashSet<>();
		}

		return this.roles;
	}

	public Collection<RoleDTO> getRoles() {
		return getRolesInternal();
	}

	public void addRole(RoleDTO role) {
		if (this.getRoles() == null || !this.getRoles().contains(role)) {
			getRolesInternal().add(role);
		}
		if (!role.getUsers().contains(this)) {
			role.addUser(this);
		}
	}

	public void removeRole(RoleDTO role) {
		this.roles.remove(role);
	}

	private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (String privilege : privileges) {
			authorities.add(new SimpleGrantedAuthority(privilege));
		}
		return authorities;
	}

	private List<String> getPrivileges(Collection<RoleDTO> roles) {

		List<String> privileges = new ArrayList<>();
		List<PrivilegeDTO> collection = new ArrayList<>();
		for (RoleDTO role : roles) {
			collection.addAll(role.getPrivileges());
		}
		for (PrivilegeDTO item : collection) {
			privileges.add(item.getName());
		}
		return privileges;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return getGrantedAuthorities(getPrivileges(this.roles));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof UserDTO))
			return false;

		UserDTO userDTO = (UserDTO) o;

		return new EqualsBuilder().appendSuper(super.equals(o)).append(isEnabled(), userDTO.isEnabled())
				.append(isAccountNonExpired(), userDTO.isAccountNonExpired())
				.append(isAccountNonLocked(), userDTO.isAccountNonLocked())
				.append(isCredentialsNonExpired(), userDTO.isCredentialsNonExpired())
				.append(getEmail(), userDTO.getEmail()).append(getPassword(), userDTO.getPassword())
				.append(getMatchingPassword(), userDTO.getMatchingPassword()).append(getRoles(), userDTO.getRoles())
				.append(getTelephone(), userDTO.getTelephone()).append(getStreet1(), userDTO.getStreet1())
				.append(getStreet2(), userDTO.getStreet2()).append(getStreet3(), userDTO.getStreet3())
				.append(getZipCode(), userDTO.getZipCode()).append(getCity(), userDTO.getCity())
				.append(getCountry(), userDTO.getCountry()).isEquals();
	}

	@Override
	public String toString() {
		return "UserDTO{" + "email='" + email + '\'' + ", password='" + password + '\'' + ", matchingPassword='"
				+ matchingPassword + '\'' + ", enabled=" + enabled + ", accountNonExpired=" + accountNonExpired
				+ ", accountNonLocked=" + accountNonLocked + ", credentialsNonExpired=" + credentialsNonExpired
				+ ", roles=" + roles + ", telephone='" + telephone + '\'' + ", street1='" + street1 + '\''
				+ ", street2='" + street2 + '\'' + ", street3='" + street3 + '\'' + ", zipCode='" + zipCode + '\''
				+ ", city='" + city + '\'' + ", country='" + country + '\'' + '}';
	}

	public void encode(String rawPassword) {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

		this.password = bCryptPasswordEncoder.encode(rawPassword);
		this.matchingPassword = this.password;
	}

	public boolean matches(String rawPassword) {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

		return bCryptPasswordEncoder.matches(rawPassword, this.password);
	}

}
