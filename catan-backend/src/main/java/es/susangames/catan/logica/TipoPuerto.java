package es.susangames.catan.logica;

public enum TipoPuerto {
	Nada(-1), PuertoBasico(0), PuertoMadera(1), PuertoLana(2),
	PuertoCereales(3), PuertoArcilla(4), PuertoMinerales(5);
	
	private int identificador;
	
	private TipoPuerto (int identificador) {
		this.identificador = identificador;
	}
	
	public Boolean esBasico () {
		return this.identificador == 0;
	}
	
	public Boolean esEspecial () {
		return this.identificador == 1 || this.identificador == 2 || this.identificador == 3
				|| this.identificador == 4 || this.identificador == 5;
	}
	
	public Boolean esPuertoMadera () {
		return this.identificador == 1;
	}
	
	public Boolean esPuertoLana () {
		return this.identificador == 2;
	}
	
	public Boolean esPuertoCereales () {
		return this.identificador == 3;
	}
	
	public Boolean esPuertoArcilla () {
		return this.identificador == 4;
	}
	
	public Boolean esPuertoMineral () {
		return this.identificador == 5;
	}
}
