package es.susangames.catan.logica;

public enum TipoAsentamiento {
	Nada(0), Pueblo(1), Ciudad(2);
	
	private int tipo;
	
	private TipoAsentamiento(int tipo) {
		this.tipo = tipo;
	}
	
	public Boolean tieneAsentamiento () {
		return tipo == 0;
	}
	
	public Boolean esCiudad () {
		return this.tipo == 2;
	}
}
