package es.susangames.catan.logica;

public class Vertices {
	private Coordenadas coord;
	
	private TipoAsentamiento asentamiento;
	private Jugadores propietario;
	
	Vertices (Coordenadas c, TipoAsentamiento asentamiento) {
		this.coord = c;
		this.asentamiento = asentamiento;
	}
	
	Vertices (Coordenadas c) {
		this.coord = c;
		this.asentamiento = TipoAsentamiento.Nada;
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
		this.asentamiento = TipoAsentamiento.Pueblo;
		this.propietario = j;
	}
	
	public void mejorarAsentamiento () {
		this.asentamiento = TipoAsentamiento.Ciudad;
	}

	public Jugadores getPropietario() {
		return propietario;
	}

	public void setPropietario(Jugadores propietario) {
		this.propietario = propietario;
	}

}
