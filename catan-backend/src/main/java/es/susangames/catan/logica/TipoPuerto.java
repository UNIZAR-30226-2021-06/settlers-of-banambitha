package es.susangames.catan.logica;

/**
 * Esta clase define un enumerador de los distintos tipos de puertos del juego
 * @author: Fabian Conde Lafuente
 */
public enum TipoPuerto {
//Campos de la clase
	Nada(-1), PuertoBasico(0), PuertoMadera(1), PuertoLana(2),
	PuertoCereales(3), PuertoArcilla(4), PuertoMinerales(5);
	
	private int identificador;
	
	/**
     * Constructor para el enumerador de puertos
     */
	private TipoPuerto (int identificador) {
		this.identificador = identificador;
	}
	/**
     * Método que devuelve cierto si es un puerto basico
     * @return cierto si es un puerto basico
     */
	public Boolean esBasico () {
		return this.identificador == 0;
	}
	
	/**
     * Método que devuelve cierto si es un puerto especial
     * @return cierto si es un puerto especial
     */
	public Boolean esEspecial () {
		return this.identificador == 1 || this.identificador == 2 || this.identificador == 3
				|| this.identificador == 4 || this.identificador == 5;
	}
	/**
     * Método que devuelve cierto si es un puerto de madera
     * @return cierto si es un puerto de madera
     */
	public Boolean esPuertoMadera () {
		return this.identificador == 1;
	}
	/**
     * Método que devuelve cierto si es un puerto de lana
     * @return cierto si es un puerto de lana
     */
	public Boolean esPuertoLana () {
		return this.identificador == 2;
	}
	/**
     * Método que devuelve cierto si es un puerto de cereales
     * @return cierto si es un puerto de cereales
     */
	public Boolean esPuertoCereales () {
		return this.identificador == 3;
	}
	/**
     * Método que devuelve cierto si es un puerto de arcilla
     * @return cierto si es un puerto de arcilla
     */
	public Boolean esPuertoArcilla () {
		return this.identificador == 4;
	}
	/**
     * Método que devuelve cierto si es un puerto de mineral
     * @return cierto si es un puerto de mineral
     */
	public Boolean esPuertoMineral () {
		return this.identificador == 5;
	}
} //Cierre de la clase
