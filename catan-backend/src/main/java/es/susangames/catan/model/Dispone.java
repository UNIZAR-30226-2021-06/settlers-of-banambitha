package es.susangames.catan.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import es.susangames.catan.composite_keys.DisponePK;

@Entity
@IdClass(DisponePK.class)
public class Dispone implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(nullable = false, updatable = false)
	private String usuario_id;
	
	@Id
	@Column(nullable = false, updatable = false)
	private String producto_id;
	
	
	@MapsId
	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;
	
	@MapsId
	@ManyToOne
	@JoinColumn(name = "producto_id")
	private Producto producto;

	public Dispone() {}

	public Dispone(String usuario_id, String producto_id) {
		this.usuario_id = usuario_id;
		this.producto_id = producto_id;
	}

	public String getProducto_id() {
		return producto_id;
	}

	public void setProducto_id(String producto_id) {
		this.producto_id = producto_id;
	}

	public String getUsuario_id() {
		return usuario_id;
	}

	public void setUsuario_id(String usuario_id) {
		this.usuario_id = usuario_id;
	}

	
	
}
