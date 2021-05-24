package es.susangames.catan.logica;

public class Vertices {
	
	/*
	 * Representa las coordenadas en un plano.
	 * */
	private Coordenadas coord;
	
	/*
	 * Identificador del objeto Vertice
	 * */
	private int id;
	
	/*
	 * Representa el tipo de asentamiento que tiene este Vertice. 
	 * */
	private TipoAsentamiento asentamiento;
	
	/*
	 * Esta variable debe de tener valor solamente en el caso de que haya un asentamiento en 
	 * el vertice. En el caso de que haya asentamiento, esta variable apuntará al jugador que la
	 * haya construido.
	 * */
	private Jugadores propietario;
	
	/*
	 * Representa cual de los 4 jugadores que juegan una partida puede construir en este Vertice. 
	 * */
	private Boolean puedeConstruirJugador[]; 

	private Boolean asentamientoAdyacente;
	
	/*
	 * Crear un tipo de dato Vertice que representa una intersección donde se pueden colocar
	 * asentamientos (poblados o ciudades).
	 * @param c				Representa las coordenadas en un plano del Vertice.
	 * @param asentamiento	asigna un tipo de asentamiento al vertice a través del enum TipoAsentamiento
	 * 						(posibles valores Nada, Poblado, Ciudad).
	 * @param id			Identificador del tipo de dato Vertice.
	 * */
	Vertices (Coordenadas c, TipoAsentamiento asentamiento) {
		this.id = 0;
		this.propietario = null;
		this.coord = c;
		this.asentamiento = asentamiento;
		this.puedeConstruirJugador = new Boolean[] {false, false, false, false};
		this.asentamientoAdyacente = false;
	}
	
	/*
	 * Crear un tipo de dato Vertice que representa una intersección donde se pueden colocar
	 * asentamientos (poblados o ciudades).
	 * @param c				Representa las coordenadas en un plano del Vertice.
	 * @param asentamiento	asigna un tipo de asentamiento al vertice a través del enum TipoAsentamiento
	 * 						(posibles valores Nada, Poblado, Ciudad).
	 * @param id			Identificador del tipo de dato Vertice.
	 * */
	Vertices (Coordenadas c) {
		this.coord = c;
		this.asentamiento = TipoAsentamiento.Nada;
		this.puedeConstruirJugador = new Boolean[] {false, false, false, false};
		this.propietario = null;
		this.id = 0;
	}
	
	/*
	 * Devuelve el identificador del Vertice.
	 * @return el identificador del Vertice.
	 * */
	public Integer getIdentificador () {
		return this.id;
	}

	public void setIdentificador (int id) {
		this.id = id;
	}
	
	/*
	 * @return el valor de las coordenadas en el plano del Vertice.
	 * */
	public Coordenadas getCoordenadas () {
		return this.coord;
	}
	
	/*
	 * @return true si y solo si el Vertice tiene un asentamiento construido.
	 * */
	public Boolean tieneAsentamiento () {
		return this.asentamiento.tieneAsentamiento();
	}
	
	/*
	 * @return true si y solo si el Vertice tiene una ciudad construida.
	 * */
	public Boolean tieneCiudad () {
		return this.asentamiento.esCiudad();
	}
	
	/*
	 * Si el Vertice tiene un asentamiento construido, ejecuta la acción del juego dando al jugador
	 * materias primas.
	 * @param material tipo de materia prima que el Vertice da al jugador.
	 * */
	public void producir (TipoTerreno material) {
		try  {
			if (material == null) {
				throw new Exception ("function producir (TipoTerreno material): el parametro "
						+ "material no puede ser nulo.");
			}
			if (propietario != null && this.tieneAsentamiento()) {
				propietario.anyadirRecurso(material, tieneCiudad());
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}	
	}
	
	public void construirPrimerAsentamiento (Jugadores j) {
		this.asentamiento = TipoAsentamiento.Pueblo;
		this.propietario = j;
		puedeConstruirJugador[0] = false;
		puedeConstruirJugador[1] = false;
		puedeConstruirJugador[2] = false;
		puedeConstruirJugador[3] = false;
	}
	
	/*
	 * La función comprueba que el vertice se encuentre vacio y que al jugador j se le este 
	 * permitido construir ahí. 
	 * @param j jugador que desea construir
	 * */
	public void construirAsentamiento (Jugadores j) {
		if (!asentamiento.tieneAsentamiento() && puedeConstruirJugador[j.getColor().numeroColor()]
			&& !this.asentamientoAdyacente) {
			this.asentamiento = TipoAsentamiento.Pueblo;
			this.propietario = j;
			// Ninguno podrá ya construir un asentamiento --> Todo a false.
			puedeConstruirJugador[0] = false;
			puedeConstruirJugador[1] = false;
			puedeConstruirJugador[2] = false;
			puedeConstruirJugador[3] = false;
		} else if (this.asentamientoAdyacente) {
			asentamientoAdyacente();
		}
	}
	
	/*
	 * El asentamiento del Vertice se actualiza como una ciudad.
	 * */
	public void mejorarAsentamiento () {
		this.asentamiento = TipoAsentamiento.Ciudad;
	}

	/*
	 * @return el objeto Jugador que representa al propietario del asentamiento. En el caso de no
	 * 			existir ningún asentamiento construido devuelve null.
	 * */
	public Jugadores getPropietario() {
		return propietario;
	}

	/*
	 * @return un cadena de caracteres con el asentamiento y el identificador del jugador. En el 
	 * 		caso de no haber asentamiento devuelve "Nada".
	 * */
	public String getAsentamientoJugador () {
		if (this.propietario != null)
			return asentamiento.getStringAsentamiento() + propietario.getColor().getStringColor();
		else
			return asentamiento.getStringAsentamiento();
	}
	
	/*
	 * Actualiza la vertice como posible lugar donde el jugador j puede construir un asentamiento.
	 * */
	public void posibleAsentamientoDeJugador (Jugadores j) {
		if (!tieneAsentamiento())
			puedeConstruirJugador[j.getColor().numeroColor()] = true;
	}
	
	public void asentamientoAdyacente () {
		this.asentamientoAdyacente = true;
		puedeConstruirJugador[0] = false;
		puedeConstruirJugador[1] = false;
		puedeConstruirJugador[2] = false;
		puedeConstruirJugador[3] = false;
	}

	public void setPosibleAsentamientoDeJugador(int i) {
		if (i>3 || i<0) {
			puedeConstruirJugador[i] = false;
		}
	}
	
	/*
	 * @return un vector de booleanos con la información de la variable puedeConstruirJugador.
	 * */
	public Boolean[] getPosibleAsentamientoDeJugador () {
		return puedeConstruirJugador;
	}
	
	/*
	 * @return true si y solo si el jugador i puede construir en el Vertice
	 * */
	public Boolean getPosibleAsentamientoDeJugador (int i) {
		if (i > 3) {
			return false;
		}
		return puedeConstruirJugador[i];
	}

	public Boolean tieneAsentamientoAdyacente () {
		return this.asentamientoAdyacente;
	}
	
}
