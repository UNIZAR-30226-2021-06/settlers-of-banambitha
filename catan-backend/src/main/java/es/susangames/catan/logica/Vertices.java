package es.susangames.catan.logica;

public class Vertices {
	private Coordenadas coord;
	
	private int id;
	
	private TipoAsentamiento asentamiento;
	private Jugadores propietario;
	
	private Boolean puedeConstruirJugador[]; 
	
	Vertices (Coordenadas c, TipoAsentamiento asentamiento, int id) {
		this.coord = c;
		this.asentamiento = asentamiento;
		this.id = id;
		this.puedeConstruirJugador = new Boolean[] {false, false, false, false};
	}
	
	Vertices (Coordenadas c, int id) {
		this.coord = c;
		this.asentamiento = TipoAsentamiento.Nada;
		this.id = id;
		this.puedeConstruirJugador = new Boolean[] {false, false, false, false};
	}
	
	public Coordenadas getCoordenadas () {
		return this.coord;
	}
	
	public Boolean tieneAsentamiento () {
		return this.asentamiento.tieneAsentamiento();
	}
	
	public Boolean tieneCiudad () {
		return this.asentamiento.esCiudad();
	}
	
	public void producir (TipoTerreno material) {
		if (propietario != null) {
			propietario.anyadirRecurso(material, tieneCiudad());
		}
	}
	
	public void construirAsentamiento (Jugadores j) {
		if (!asentamiento.tieneAsentamiento() && puedeConstruirJugador[j.getColor().numeroColor()]) {
			this.asentamiento = TipoAsentamiento.Pueblo;
			this.propietario = j;
			// Ninguno podrá ya construir un asentamiento --> Todo a false.
			puedeConstruirJugador[0] = false;
			puedeConstruirJugador[1] = false;
			puedeConstruirJugador[2] = false;
			puedeConstruirJugador[3] = false;
		}
	}
	
	public void mejorarAsentamiento () {
		this.asentamiento = TipoAsentamiento.Ciudad;
	}

	public Jugadores getPropietario() {
		return propietario;
	}

	public String getAsentamientoJugador () {
		return asentamiento.getStringAsentamiento() + propietario.getColor().getStringColor();
	}
	
	/*
	 * Actualiza la vertice como posible lugar donde el jugador j puede construir un asentamiento.
	 * */
	public void posibleAsentamientoDeJugador (Jugadores j) {
		puedeConstruirJugador[j.getColor().numeroColor()] = true;
	}
	
	public Boolean[] getPosibleAsentamientoDeJugador () {
		return puedeConstruirJugador;
	}
	
	public Boolean getPosibleAsentamientoDeJugador (int i) {
		if (i > 3) {
			return false;
		}
		return puedeConstruirJugador[i];
	}
}
