package es.susangames.catan.logica;

/**
 * Esta clase define un enumerador de los distintos terrenos del juego
 * @author: Fabian Conde Lafuente
 */
public enum TipoTerreno {
//Campos de la clase
	Bosque(0), Pasto(1), Sembrado(2), Cerro(3), Montanya(4), Desierto(5);
	
	private int terreno;
	
	/**
     * Constructor para el enumerador de terrenos
     */
	private TipoTerreno (int terreno) {
		this.terreno = terreno;
	}
	/**
     * Método que devuelve cierto si el terreno es desierto
     * @return cierto si el terreno es desierto
     */
	public Boolean esDesierto () {
		return terreno == 5;
	}
	
	/**
     * Método que devuelve el tipo de terreno
     * @return tipo de terreno
     */
	public TipoTerreno generarTerreno ( int terreno ) {
		if (terreno == 0) return Bosque;
		else if (terreno == 1) return Pasto;
		else if (terreno == 2) return Sembrado;
		else if (terreno == 3) return Cerro;
		else if (terreno == 4) return Montanya;
		else return Desierto;
	}
	
	/**
     * Método que devuelve el tipo de terreno
     * @return tipo de terreno
     */
	public String getStringTipoTerreno () {
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
} //Cierre de la clase
