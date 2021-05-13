package es.susangames.catan.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;


@Entity
public class Estadisticas implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private String usuarioId;
	
	@MapsId
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "usuarioId")
	private Usuario usuario;
	
	@Column(columnDefinition = "int4 default 0", nullable = false, updatable = false)
	private Integer	partidasJugadas = 0;
	
	@Column(columnDefinition = "int4 default 0", nullable = false, updatable = false)
	private Integer totalDeVictorias = 0;
	
	@Column(columnDefinition = "float4 default 0", nullable = false, updatable = false)
	private Float porcentajeDeVictorias = 0.0f;
	
	@Column(columnDefinition = "int4 default 0", nullable = false, updatable = false)
	private Integer rachaDeVictoriasActual = 0;
	
	@Column(columnDefinition = "int4 default 0", nullable = false, updatable = false)
	private Integer mayorRachaDeVictorias = 0;
	
	
	public Estadisticas() {}
	
	public Estadisticas(String usuario) {
		this.usuarioId = usuario;
	}
	
	public Estadisticas(String usuario, Integer partidasJugadas, Integer totalDeVictorias, Float porcentajeDeVictorias,
			Integer rachaDeVictoriasActual, Integer mayorRachaDeVictorias) {
		
		this.usuarioId = usuario;
		this.partidasJugadas = partidasJugadas;
		this.totalDeVictorias = totalDeVictorias;
		this.porcentajeDeVictorias = porcentajeDeVictorias;
		this.rachaDeVictoriasActual = rachaDeVictoriasActual;
		this.mayorRachaDeVictorias = mayorRachaDeVictorias;
	}

	public String getUsuario() {
		return usuarioId;
	}
	
	public void setUsuario(String usuario) {
		this.usuarioId = usuario;
	}
	
	public Integer getPartidasJugadas() {
		return partidasJugadas;
	}
	
	public void setPartidasJugadas(Integer partidasJugadas) {
		this.partidasJugadas = partidasJugadas;
	}
	
	public Integer getTotalDeVictorias() {
		return totalDeVictorias;
	}
	
	public void setTotalDeVictorias(Integer totalDeVictorias) {
		this.totalDeVictorias = totalDeVictorias;
	}
	
	public Float getPorcentajeDeVictorias() {
		return porcentajeDeVictorias;
	}
	
	public void setPorcentajeDeVictorias(Float porcentajeDeVictorias) {
		this.porcentajeDeVictorias = porcentajeDeVictorias;
	}
	
	public Integer getRachaDeVictoriasActual() {
		return rachaDeVictoriasActual;
	}
	
	public void setRachaDeVictoriasActual(Integer rachaDeVictoriasActual) {
		this.rachaDeVictoriasActual = rachaDeVictoriasActual;
	}
	
	public Integer getMayorRachaDeVictorias() {
		return mayorRachaDeVictorias;
	}
	
	public void setMayorRachaDeVictorias(Integer mayorRachaDeVictorias) {
		this.mayorRachaDeVictorias = mayorRachaDeVictorias;
	}
	
	public void onVictory() {
		this.partidasJugadas++;
		this.totalDeVictorias++;
		this.porcentajeDeVictorias = ((float) this.totalDeVictorias / this.partidasJugadas) * 100;
		this.rachaDeVictoriasActual++;
		
		if(this.rachaDeVictoriasActual > this.mayorRachaDeVictorias) {
			this.mayorRachaDeVictorias++;
		}
	}
	
	public void onDefeat() {
		this.partidasJugadas++;
		this.porcentajeDeVictorias = ((float)this.totalDeVictorias / this.partidasJugadas) * 100;
	}
}
