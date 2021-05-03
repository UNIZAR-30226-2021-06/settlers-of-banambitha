package es.susangames.catan.composite_keys;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PeticionPK implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String usuario1_id;
	private String usuario2_id;

	public String getFrom() {
		return usuario1_id;
	}
	public void setFrom(String from) {
		this.usuario1_id = from;
	}
	public String getTo() {
		return usuario2_id;
	}
	public void setTo(String to) {
		this.usuario2_id = to;
	}

}
