package es.susangames.catan.logica;

import java.util.List;

public class Aristas {
	private CoordenadasAristas coord;
	
	private Boolean tieneCarretera;
	private Jugadores propietario;
	
	private List<Integer> jugadoresQuePuedenConstruir;
	
	private TipoPuerto puerto;
	
	Aristas(CoordenadasAristas coord) {
		this.coord = coord;
		this.tieneCarretera = false;
		this.propietario = null;
		this.puerto = TipoPuerto.Nada;
	}
	
	Aristas(CoordenadasAristas coord, TipoPuerto puerto, Jugadores propietario) {
		this.coord = coord;
		this.puerto = puerto;
		this.tieneCarretera = true;
		this.propietario = propietario;
	}
	
	public CoordenadasAristas getCoordenadasAristas () {
		return this.coord;
	}
	
	public Boolean tieneCarretera () {
		return this.tieneCarretera;
	}
	
	public Jugadores getPropietario () {
		return this.propietario;
	}
	
	public void setCarretera (Jugadores j) {
		this.propietario = j;
		this.tieneCarretera = true;
	}
	
	public void setPuerto (int tipoPuerto) {
		switch(tipoPuerto) {
		case -1:
			this.puerto = TipoPuerto.Nada; break;
		case 0:
			this.puerto = TipoPuerto.PuertoBasico; break;
		case 1:
			this.puerto = TipoPuerto.PuertoMadera; break;
		case 2:
			this.puerto = TipoPuerto.PuertoLana; break;
		case 3:
			this.puerto = TipoPuerto.PuertoCereales; break;
		case 4:
			this.puerto = TipoPuerto.PuertoArcilla; break;
		case 5:
			this.puerto = TipoPuerto.PuertoMinerales; break;
		}
	}
	
	public TipoPuerto getPuerto () {
		return this.puerto;
	}
}
