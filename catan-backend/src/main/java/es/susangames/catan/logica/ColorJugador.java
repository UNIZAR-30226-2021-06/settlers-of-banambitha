package es.susangames.catan.logica;

/**
 * Esta clase define un enumerador de los colores que identifican a los jugadores de la partida
 * @author: Fabian Conde Lafuente
 */
public enum ColorJugador {
	 //Campos de la clase
	Azul(0), Rojo(1), Amarillo(2), Verde(3);
	
	private int color;
	
	/**
     * Constructor para el enumerador de colores
     */
	private ColorJugador( int color ) {
		this.color = color;
	}
	 //Cierre del constructor
	 
	 /**
     * Método que devuelve el color del jugador que la invoca
     * @return el color del jugador
     */
	public Integer numeroColor () {
		return this.color;
	}
	
	/**
     * Método que devuelve cierto si el color es igual al color del jugador
     * @return cierto si el color es igual al color del jugador
     */
	public Boolean mismoColor(ColorJugador j) {
		return this.color == j.numeroColor();
	}
	
	/**
     * Método que devuelve el color dado el id de este
     * @return devuelve devuelve el color dado el id de este
     */
	public String getStringColor () {
		switch (color) {
		case 0:
			return "Azul";
		case 1:
			return "Rojo";
		case 2:
			return "Amarillo";
		case 3:
			return "Verde";
		default:
			return "Null";
		}
	}
} //Cierre de la clase
