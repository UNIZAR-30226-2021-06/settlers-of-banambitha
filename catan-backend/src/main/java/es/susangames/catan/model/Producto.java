package es.susangames.catan.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Producto implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "producto_id", nullable = false, updatable = false)
	private String nombre;
	
	@Column(nullable = false, updatable = true)
	private Integer precio;
	
	@Column(columnDefinition = "VARCHAR(10) CHECK (tipo IN ('AVATAR', 'APARIENCIA'))", nullable = false, updatable = true)
	private String tipo;
	
	
	public Producto() {}

	public String getNombre() {
		return nombre;
	}
	
	public Producto(String nombre, Integer precio, String tipo) {
		this.nombre = nombre;
		this.precio = precio;
		this.tipo = tipo;
	}
	

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getPrecio() {
		return precio;
	}

	public void setPrecio(Integer precio) {
		this.precio = precio;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	
	
	
}
