package es.susangames.catan.logica;

public enum TipoTerreno {
	Bosque(0), Pasto(1), Sembrado(2), Cerro(3), Montanya(4), Desierto(5);
	
	private int terreno;
	
	private TipoTerreno (int terreno) {
		this.terreno = terreno;
	}
	
	public Boolean esDesierto () {
		return terreno == 5;
	}
	
	public TipoTerreno generarTerreno ( int terreno ) {
		if (terreno == 0) return Bosque;
		else if (terreno == 1) return Pasto;
		else if (terreno == 2) return Sembrado;
		else if (terreno == 3) return Cerro;
		else if (terreno == 4) return Montanya;
		else return Desierto;
	}
	
	public String toString () {
		switch (terreno) {
		case 0:
			return "Bosque";
		case 1:
			return "Pasto";
		case 2:
			return "Sembrado";
		case 3:
			return "Cerro";
		case 4:
			return "Montanya";
		case 5:
			return "Desierto";
		default:
			return "vacio";
		}
	}
}
