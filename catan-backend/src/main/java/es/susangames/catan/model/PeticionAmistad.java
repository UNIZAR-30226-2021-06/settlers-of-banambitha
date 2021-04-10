package es.susangames.catan.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import org.javatuples.Pair;

import es.susangames.catan.composite_keys.PeticionPK;

@Entity
@IdClass(PeticionPK.class)
public class PeticionAmistad implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String usuario1_id;
	@Id
	private String usuario2_id;

	@MapsId
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "usuario1_id")
	private Usuario usuario1;

	@MapsId
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "usuario2_id")
	private Usuario usuario2;

	public PeticionAmistad() {
	}

	public PeticionAmistad(String from, String to) {
		this.usuario1_id = from;
		this.usuario2_id = to;
	}

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

	public Pair<Amigo,Amigo> generarAmistad(){
		
		String usuario1 = this.usuario1_id;
		String usuario2 = this.usuario2_id;
		
		return new Pair<Amigo,Amigo>(new Amigo(usuario1, usuario2), new Amigo(usuario2, usuario1));
	}

}
