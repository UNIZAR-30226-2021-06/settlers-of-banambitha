package es.susangames.catan.logica;

import java.util.List;

public class Aristas {
	private CoordenadasAristas coord;
	
	private int id;
	
	private Boolean tieneCamino;
	private Jugadores propietario;
	
	private Boolean puedeConstruirJugador[];
	
	private TipoPuerto puerto;
	
	Aristas(CoordenadasAristas coord, int id) {
		this.coord = coord;
		this.tieneCamino = false;
		this.propietario = null;
		this.puerto = TipoPuerto.Nada;
		this.id = id;
		this.puedeConstruirJugador = new Boolean[] {false, false, false, false};
	}
	
	Aristas(CoordenadasAristas coord, TipoPuerto puerto, Jugadores propietario, int id) {
		this.coord = coord;
		this.puerto = puerto;
		this.tieneCamino = true;
		this.propietario = propietario;
		this.id = id;
		this.puedeConstruirJugador = new Boolean[] {false, false, false, false};
	}
	
	public CoordenadasAristas getCoordenadasAristas () {
		return this.coord;
	}
	
	public Boolean tieneCamino () {
		return this.tieneCamino;
	}
	
	public Jugadores getPropietario () {
		return this.propietario;
	}
	
	public void setCarretera (Jugadores j) {
		if ( !tieneCamino() && puedeConstruirJugador[j.getColor().numeroColor()]) {
			this.propietario = j;
			this.tieneCamino = true;
			// Ninguno podrá ya construir un camino --> Todo a false.
			puedeConstruirJugador[0] = false;
			puedeConstruirJugador[1] = false;
			puedeConstruirJugador[2] = false;
			puedeConstruirJugador[3] = false;
		}
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
	
	/*
	 * Actualiza la aristas como posible lugar donde el jugador j puede construir un camino.
	 * */
	public void posibleCarreteraDeJugador (Jugadores j) {
		puedeConstruirJugador[j.getColor().numeroColor()] = true;
	}
	
	public String getAsentamientoJugador () {
		if ( tieneCamino() ) {
			return "Camino" + propietario.getColor().getStringColor();
		}
		return "Nada";
	}
	
	public Coordenadas getCoordenadasVertice1 () {
		return new Coordenadas (this.coord.getX(),this.coord.getY());
	}
	
	public Coordenadas getCoordenadasVertice2 () {
		return new Coordenadas (this.coord.getFin_x(),this.coord.getFin_y());
	}
}
