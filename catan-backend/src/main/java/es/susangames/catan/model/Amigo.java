package es.susangames.catan.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import es.susangames.catan.composite_keys.AmigoPK;

@Entity
@IdClass(AmigoPK.class)
public class Amigo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(nullable = false, updatable = false)
	private String usuario1_id;
	
	@Id
	@Column(nullable = false, updatable = false)
	private String usuario2_id;
	
	@MapsId
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "usuario1_id")
	private Usuario usuario1;
	
	@MapsId
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "usuario2_id")
	private Usuario usuario2;

	
	public Amigo() {}

	public Amigo(String usuario1_id, String usuario2_id) {
		this.usuario1_id = usuario1_id;
		this.usuario2_id = usuario2_id;
	}
	

	public String getUsuario1_id() {
		return usuario1_id;
	}

	public void setUsuario1_id(String usuario1_id) {
		this.usuario1_id = usuario1_id;
	}

	public String getUsuario2_id() {
		return usuario2_id;
	}

	public void setUsuario2_id(String usuario2_id) {
		this.usuario2_id = usuario2_id;
	}

}
