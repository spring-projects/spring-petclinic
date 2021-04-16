package org.springframework.cheapy.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

	@NotBlank(message="No debe estar vacío")
	private String	nombre;

	@NotBlank(message="No debe estar vacío")
	private String	apellidos;

	@NotBlank(message="No debe estar vacío")
	private String	direccion;

	@Enumerated(value = EnumType.STRING)
	private Municipio municipio;
	
	@Email
	@NotBlank(message="No debe estar vacío")
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


	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public User getUsuar() {
		return usuar;
	}

	public void setUsuar(User usuar) {
		this.usuar = usuar;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	
	
}
