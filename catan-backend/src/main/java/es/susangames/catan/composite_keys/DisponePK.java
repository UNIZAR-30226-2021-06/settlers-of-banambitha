package es.susangames.catan.composite_keys;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class DisponePK implements Serializable {

	private static final long serialVersionUID = 1L;

	private String usuario_id;
	private String producto_id;
	
	
	public String getUsuario_Id() {
		return usuario_id;
	}
	public void setUsuario_Id(String usuario_Id) {
		this.usuario_id = usuario_Id;
	}
	public String getProducto_Id() {
		return producto_id;
	}
	public void setProductoId(String producto_Id) {
		this.producto_id = producto_Id;
	}

}
