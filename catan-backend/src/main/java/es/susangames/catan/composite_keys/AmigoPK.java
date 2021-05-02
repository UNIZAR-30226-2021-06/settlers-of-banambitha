package es.susangames.catan.composite_keys;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AmigoPK implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String usuario1_id;
	private String usuario2_id;
	
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
