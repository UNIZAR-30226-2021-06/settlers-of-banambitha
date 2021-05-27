package es.susangames.catan.logica;

import org.json.JSONArray;

import org.json.JSONException;

/**
 * Esta clase define a los jugadores de la partida, sus cartas y acciones que puede realizar
 * @author: Fabian Conde Lafuente y Víctor García García
 */
public class Jugadores {
//Campos de la clase
	private ColorJugador color;
	
	private Integer puntosVictoria;
	
	// RECURSOS
	private Integer madera;
	private Integer lana;
	private Integer cereales;
	private Integer arcilla;
	private Integer mineral;

	// CARTAS DESARROLLO
	private Integer numCartasCaballeros;
	private Integer numCartasPuntoVictoria;
	private Integer numCartasContruccionCarreteras;
	private Integer numCartasDescubrimiento;
	private Integer numCartasMonopolio;
	
	// CARTAS ESPECIALES
	private Boolean granRutaComercial;
	private Boolean granEjecitoCaballeria;
	
	// ASENTAMIENTO TODO Utilizar esto
	//	private int MAX_NUM_POBLADOS = 5;
	//	private int MAX_NUM_CIUDADES = 4;
	//	private int MAX_NUM_CARRETERAS = 15;
	
	private int numPobladosConstruidos;
	private int numCiudadesConstruidos;
	private int numCaminosConstruidos;
	
	private Boolean primerosAsentamientosConstruidos;
	private Boolean primerosCaminosConstruidos;
	
	/**
     * Constructor de jugador
     */
	public Jugadores (ColorJugador color) {
		this.color = color;
		this.puntosVictoria = 0;
		this.madera = 0; this.lana = 0; this.cereales = 0; this.arcilla = 0; this.mineral = 0;
		
		this.numCartasCaballeros = 0;
		this.numCartasPuntoVictoria = 0;
		this.numCartasContruccionCarreteras = 0;
		this.numCartasDescubrimiento = 0;
		this.numCartasMonopolio = 0;
		
		this.granRutaComercial = false;
		this.granEjecitoCaballeria = false;
		
		this.numPobladosConstruidos = 0;
		this.numCiudadesConstruidos = 0;
		this.numCaminosConstruidos = 0;
		
		this.primerosAsentamientosConstruidos = false;
		this.primerosCaminosConstruidos = false;
	}
	
	/**
     * Método que añade a lo recursos del jugador los producidos por una ciudad o pueblo de este.
     */
	public void anyadirRecurso (TipoTerreno tipo, Boolean produceCiudad) {
		int numProducidos = 1;
		if (produceCiudad) numProducidos = 2;
		switch (tipo) {
		case Bosque:
			this.madera+=numProducidos;
			break;
		case Pasto:
			this.lana+=numProducidos;
			break;
		case Sembrado:
			this.cereales+=numProducidos;
			break;
		case Cerro:
			this.arcilla+=numProducidos;
			break;
		case Montanya:
			this.mineral+=numProducidos;
			break;
		case Desierto:
			break;
		}
	}
	
	/**
     * Método que devuelve si el jugador puede construir un pueblo
     * @return devuelve si el jugador puede construir un pueblo
     */
	public Boolean puedeConstruirPueblo () {
		return this.madera >= 1 && this.lana >= 1 && this.cereales >= 1 && this.arcilla >= 1;
	}
	
	/**
     * Método que devuelve si el jugador puede construir una ciudad
     * @return cierto si el jugador puede construir una ciudad
     */
	public Boolean puedeConstruirCiudad () {
		// Materiales necesarios: mineral x3, cereales x2.
		return this.mineral >= 3 && this.cereales >= 2;
	}
	
	/**
     * Método que devuelve si el jugador puede construir una carretera
     * @return cierto si el jugador puede construir una carretera
     */
	public Boolean puedeConstruirCamino () {
		System.out.println("Madera: " + madera);
		System.out.println("Arcilla: " + arcilla);
		return this.madera >= 1 && this.arcilla >= 1;
	}
	
	/**
     * Método que recalcula los puntos de victoria del jugador
     */
	public void actualizarPuntosVictoria () {
		this.puntosVictoria = this.numCartasPuntoVictoria + this.numPobladosConstruidos +
				this.numCiudadesConstruidos * 2;
	}
	
	/**
     * Método que devulve los puntos de victoria del jugador
     * @return los puntos de victoria del jugador
     */
	public Integer getPuntosVictoria () {
		return this.puntosVictoria;
	}
	
	/**
     * Método que devulve los puntos de victoria del jugador
     * @return los puntos de victoria del jugador
     */
	public Boolean mismoJugador (Jugadores j) {
		return this == j;
	}
	
	/**
     * Método que elimina la mitad de los recursos del jugador en caso de que tenga más de 7.
     */
	public void eliminarRecursos() {
		int num_recursos = this.madera + this.lana + this.cereales + this.arcilla + this.mineral;
		// Quitamos la mitad de los recursos solamente si tiene mas de 7 en total.
		if (num_recursos > 7) {
			int recursos_eliminar = num_recursos / 2;
			System.out.println("Recursos a eliminar del jugador " + this.color.numeroColor() + ": " + recursos_eliminar);
			for (int i = 0; i < recursos_eliminar; i++) {
				switch(i%5) {
				case 0:
					if(this.madera>0) {this.madera--; break;}
				case 1:
					if(this.lana>0) {this.lana--; break;}
				case 2:
					if(this.cereales>0) {this.cereales--; break;}
				case 3:
					if(this.arcilla>0) {this.arcilla--; break;}
				case 4:
					if(this.mineral>0) {this.mineral--; break;}
				}
			}
		}
	}
	/**
     * Método que devulve el color del jugador
     * @return color del jugador
     */
	public ColorJugador getColor () {
		return this.color;
	}
	
	/**
     * Método que devuelve si el jugador puede construir una carta de desarrollo
     * @return cierto si el jugador puede construir una carta de desarrollo
     */
	public Boolean puedeConstruirCartasDesarrollo () {
		// Material: lana x1, mineral x1, cereales x1
		return this.lana>=1 && this.mineral >=1 && this.cereales>=1;
	}
	
	/**
     * Método que elimina los recusos empleados en construir un asentamiento y lo añade al numero de asentamientos del jugador
     */
	public void construirAsentamiento () {
		// Eliminar recursos. --> madera >= 1 && lana >= 1 && cereales >= 1 && arcilla >= 1;
		this.madera--;
		this.lana--;
		this.cereales--;
		this.arcilla--;
		this.numPobladosConstruidos++;
		if (numPobladosConstruidos >= 2) {
			primerosAsentamientosConstruidos = true;
		}
		this.puntosVictoria++;
	}
	
	public void construirPrimerAsentamiento () {
		this.numPobladosConstruidos++;
		if (numPobladosConstruidos >= 2) {
			primerosAsentamientosConstruidos = true;
		}
		this.puntosVictoria++;
	}
	
	public void construirPrimerCamino () {
		this.numCaminosConstruidos++;
		if (numCaminosConstruidos >= 2) {
			primerosCaminosConstruidos = true;
		}
	}
	
	/**
     * Método que elimina los recusos empleados en mejorar un asentamiento y actualiza al numero de ciudades.
     */
	public void mejorarAsentamiento () {
		// Eliminar recursos. -->  mineral x3, cereales x2.
		this.mineral -= 3;
		this.cereales -= 2;
		this.numPobladosConstruidos--;
		this.numCiudadesConstruidos++;
		this.puntosVictoria++;
	}
	/**
     * Método que elimina los recusos empleados en construir una carretera y actualiza al numero de carreteras.
     */
	public void construirCamino () {
		// Eliminar recursos. --> madera >= 1 && arcilla >= 1;
		this.madera--;
		this.arcilla--;
		this.numCaminosConstruidos++;
		if (numCaminosConstruidos >= 2) {
			this.primerosCaminosConstruidos = true;
		}
	}
	
	/**
     * Método que elimina los recusos empleados en construir una carretera y actualiza al numero de cartas.
     */
	public void construirCartasDesarrollo () {
		// Caballero, Puntos de victoria, Construcci�n de carreteras, Descubrimiento o Monopolio.
		int carta = (int) Math.floor(Math.random()*4);
		switch(carta) {
		case 0:
			this.numCartasCaballeros++;
			break;
		case 1:
			this.numCartasContruccionCarreteras++;
			break;
		case 2:
			this.numCartasDescubrimiento++;
			break;
		case 3:
			this.numCartasMonopolio++;
			break;
		case 4:
			this.numCartasPuntoVictoria++;
			break;
		}
	}
	
	public void usarCartaCaballero() {
		this.numCartasCaballeros--;
	}
	
	/**
     * Método que intercambiua carta de victoria por punto de victoria.
     */
	public void usarCartasPuntosVictoria() {
		this.puntosVictoria++;
		this.numCartasPuntoVictoria--;
	}
	
	public void usarCartaMonopolio () {
		this.numCartasMonopolio--;
	}
	
	public void usarCartaDescubrimiento() {
		if (this.numCartasDescubrimiento > 0){
			this.numCartasDescubrimiento--;
			// switch (tipo1) {
			// 		case 0:
			// 			this.madera += 2;
			// 		case 1:
			// 			this.lana += 2;
			// 		case 2:
			// 			this.cereales += 2;
			// 		case 3:								
			// 			this.arcilla += 2;
			// 		case 4:
			// 			this.mineral += 2;
			// }
			// switch (tipo2) {
			// 		case 0:
			// 			this.madera += 2;
			// 		case 1:
			// 			this.lana += 2;
			// 		case 2:
			// 			this.cereales += 2;
			// 		case 3:								
			// 			this.arcilla += 2;
			// 		case 4:
			// 			this.mineral += 2;
			// }
		}
	}
	
	public void usarCartasConstruccionCarretera() {
		if (this.numCartasContruccionCarreteras > 0){
			this.numCartasContruccionCarreteras--;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Jugadores other = (Jugadores) obj;
		if (color != other.color)
			return false;
		return true;
	}
	
	/**
     * Método que envia informacion de los recursos del jugador.
     */
	public JSONArray recursosJugadorToJSON () throws JSONException {
		JSONArray jsonArray = new JSONArray();
		jsonArray.put(this.getMadera());
		jsonArray.put(this.getLana());
		jsonArray.put(this.getCereales());
		jsonArray.put(this.getArcilla());
		jsonArray.put(this.getMineral());
		return jsonArray;
	}
	
	/**
     * Método que envia informacion del jugador.
     */
	public JSONArray cartasJugadorToJSON () throws JSONException {
		Integer numGranEjecitoCaballeria = 1;
		if (!this.granEjecitoCaballeria) numGranEjecitoCaballeria = 0;
		Integer numGranRutaComercial = 1;
		if (!this.granRutaComercial) numGranRutaComercial = 0;
		return new JSONArray ("[" + this.numCartasCaballeros + "," +
				this.numCartasPuntoVictoria + "," +this.numCartasContruccionCarreteras + "," + this.numCartasDescubrimiento +
				"," + this.numCartasMonopolio + "," + numGranEjecitoCaballeria + "," + numGranRutaComercial + "]");
	}

	public Integer getMadera() {
		return this.madera;
	}

	public void setMadera(Integer madera) {
		this.madera = madera;
	}

	public Integer getLana() {
		return this.lana;
	}

	public void setLana(Integer lana) {
		this.lana = lana;
	}

	public Integer getCereales() {
		return this.cereales;
	}

	public void setCereales(Integer cereales) {
		this.cereales = cereales;
	}

	public Integer getArcilla() {
		return this.arcilla;
	}

	public void setArcilla(Integer arcilla) {
		this.arcilla = arcilla;
	}

	public Integer getMineral() {
		return this.mineral;
	}

	public void setMineral(Integer mineral) {
		this.mineral = mineral;
	}
	
	public Boolean hayCartasContruccionCarreteras () {
		return this.numCartasContruccionCarreteras > 0;
	}

	public Boolean hayCartasCartasMonopolio () {
		return this.numCartasMonopolio > 0;
	}

	public Boolean hayCartasCartasCaballeros () {
		return this.numCartasCaballeros > 0;
	}

	public Boolean getPrimerosAsentamientosConstruidos() {
		return this.primerosAsentamientosConstruidos;
	}

	public Boolean getPrimerosCaminosConstruidos() {
		return this.primerosCaminosConstruidos;
	}

} //Cierre de la clase
