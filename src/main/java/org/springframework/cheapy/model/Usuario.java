package org.springframework.cheapy.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "usuarios")
public class Usuario extends BaseEntity{
	
	/* nombre, apellidos, dni, direccion, telefono, email, username
	  (id,nombre, apellidos, dni, direccion, telefono, email, usuar)*/
	
	private static final long serialVersionUID = 1L;

	@NotBlank
	private String	nombre;

	@NotBlank
	private String	apellidos;

	@NotBlank
	private String	dni;

	@NotBlank
	private String	direccion;

	@NotBlank
	//@Pattern(regexp = "([+][^0][\\d]{0,2})?[ ]?([(][\\d]{0,4}[)])?[ ]?([\\d]{6,10})$")
	private String	telefono;

	@Email
	@NotBlank
	private String	email;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "username", referencedColumnName = "username")
	private User usuar;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public User getUsuar() {
		return usuar;
	}

	public void setUsuar(User username) {
		this.usuar = username;
	}
	
	
}
