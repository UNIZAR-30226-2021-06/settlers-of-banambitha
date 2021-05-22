package es.susangames.catan.model;

import java.io.Serializable;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


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
	
	@Column(columnDefinition = "int4 default 0 CHECK (saldo >= 0)", nullable = false, updatable = true)
	private Integer saldo;
	
	@Column(columnDefinition = "varchar(10) default 'Español' CHECK (idioma IN ('Español','English'))", nullable = false, updatable = true)
	private String idioma;
	
	@Column(nullable = true, updatable = true)
	private String partida;
	
	@Column(nullable = true, updatable = true)
	private Date bloqueado;
	
	@Column(columnDefinition = "int4 default 0 CHECK (informes >= 0)", nullable = false, updatable = true)
	private Integer informes;
	
	@Column(columnDefinition = "varchar(100) default 'user_profile_image_original.png'", nullable = false, updatable = false, insertable = false)
	private String avatar;
	
	@Column(columnDefinition = "varchar(100) default 'apariencia_clasica.png'", nullable = false, updatable = false, insertable = false)
	private String apariencia;
	
	@ManyToOne
	@JoinColumn(name = "avatar")
	private Producto Avatar;
	
	@ManyToOne
	@JoinColumn(name = "apariencia")
	private Producto Apariencia;
	
	
	public Usuario() {}

	public Usuario(String nombre, String email, String contrasenya, Integer saldo, String idioma, String partida, Date bloqueado, Integer informes, String avatar, String apariencia) {
		this.nombre = nombre;
		this.email = email;
		this.contrasenya = contrasenya;
		this.saldo = saldo;
		this.idioma = idioma;
		this.partida = partida;
		this.bloqueado = bloqueado;
		this.informes = informes;
		this.avatar = avatar;
		this.apariencia = apariencia;
	}

	public Usuario(String nombre, String contrasenya) {
		this.nombre = nombre;
		this.email = null;
		this.contrasenya = contrasenya;
		this.saldo = null;
		this.idioma = null;
		this.partida = null;
		this.bloqueado = null;
		this.informes = null;
		this.avatar = null;
		this.apariencia = null;
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
	
	public String getIdioma() {
		return idioma;
	}
	
	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}
	
	public String getPartida() {
		return partida;
	}

	public void setPartida(String partida) {
		this.partida = partida;
	}
	
	public Date getBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(Date bloqueado) {
		this.bloqueado = bloqueado;
	}

	public Integer getInformes() {
		return informes;
	}

	public void setInformes(Integer informes) {
		this.informes = informes;
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
