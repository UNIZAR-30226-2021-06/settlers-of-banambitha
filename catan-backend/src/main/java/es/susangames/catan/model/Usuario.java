package es.susangames.catan.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "usuarioId", nullable = false, updatable = false)
	private String nombre;
	
	@Column(nullable = false, updatable = false, unique = true)
	private String email;
	
	@Column(nullable = false, updatable = true)
	private String contrasenya;
	
	@Column(columnDefinition = "int4 default 0", nullable = false, updatable = true)
	private Integer saldo;
	
	@Column(columnDefinition = "varchar(100) default 'Original'", nullable = false, updatable = true)
	private String avatar;
	
	@Column(columnDefinition = "varchar(100) default 'Clasica'", nullable = false, updatable = true)
	private String apariencia;
	
	
	public Usuario() {}

	public Usuario(String nombre, String email, String contrasenya, Integer saldo, String avatar, String apariencia) {
		this.nombre = nombre;
		this.email = email;
		this.contrasenya = contrasenya;
		this.saldo = saldo;
		this.avatar = avatar;
		this.apariencia = apariencia;
	}

	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getContrasenya() {
		return contrasenya;
	}
	
	public void setContrasenya(String contrasenya) {
		this.contrasenya = contrasenya;
	}
	
	public Integer getSaldo() {
		return saldo;
	}
	
	public void setSaldo(Integer saldo) {
		this.saldo = saldo;
	}
	
	public String getAvatar() {
		return avatar;
	}
	
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	public String getApariencia() {
		return apariencia;
	}
	
	public void setApariencia(String apariencia) {
		this.apariencia = apariencia;
	}
	
}
