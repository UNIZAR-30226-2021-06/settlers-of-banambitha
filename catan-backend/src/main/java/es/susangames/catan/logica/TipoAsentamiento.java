package es.susangames.catan.logica;

/**
 * Esta clase define un enumerador de los distintos tipos de asentamientos del juego
 * @author: Fabian Conde Lafuente
 */
public enum TipoAsentamiento {
	//Campos de la clase
	Nada(0), Pueblo(1), Ciudad(2);
	
	private int tipo;
	
	/**
     * Constructor para el enumerador de asentamientos
     */
	private TipoAsentamiento(int tipo) {
		this.tipo = tipo;
	}
	
	/**
     * Método que devuelve cierto si es un asentamiento vacio
     * @return cierto si es un asentamiento vacio
     */
	public Boolean tieneAsentamiento () {
		return tipo != 0;
	}
	/**
     * Método que devuelve cierto si es un asentamiento ciudad
     * @return cierto si es un asentamiento ciudad
     */
	public Boolean esCiudad () {
		return this.tipo == 2;
	}
	/**
     * Método que devuelve el tipo de asentamiento
     * @return tipo de asentamiento
     */
	public String getStringAsentamiento () {
		switch (tipo) {
		case 0:
			return "Nada";
		case 1:
			return "Pueblo";
		case 2:
			return "Ciudad";
		default:
			return "Null";
		}
	}
} //Cierre de la clase
