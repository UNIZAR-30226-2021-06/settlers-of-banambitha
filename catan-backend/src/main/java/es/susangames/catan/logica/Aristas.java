package es.susangames.catan.logica;
/*
 * 
 * */
public class Aristas {
	private CoordenadasAristas coord;
	
	private int id;
	
	private Boolean tieneCamino;
	private Jugadores propietario;
	
	private Boolean puedeConstruirJugador[];
	
	private TipoPuerto puerto;
	
	private Integer numJugadores;
	
	Aristas(CoordenadasAristas coord) {
		this.coord = coord;
		this.tieneCamino = false;
		this.propietario = null;
		this.puerto = TipoPuerto.Nada;
		this.numJugadores = 4;
		this.puedeConstruirJugador = new Boolean[numJugadores];
		for ( int i = 0; i < numJugadores; ++i) {
			this.puedeConstruirJugador[i] = false;
		}
	}
	
	Aristas(CoordenadasAristas coord, TipoPuerto puerto, Jugadores propietario) {
		this.coord = coord;
		this.puerto = puerto;
		this.tieneCamino = true;
		this.propietario = propietario;
		this.numJugadores = 4;
		this.puedeConstruirJugador = new Boolean[numJugadores];
		for ( int i = 0; i < numJugadores; ++i) {
			this.puedeConstruirJugador[i] = false;
		}
	}
	
	public Integer getIdentificador () {
		return this.id;
	}
	
	public void setIdentificador (int id) {
		this.id = id;
	}

	public CoordenadasAristas getCoordenadasAristas () {
		return this.coord;
	}
	
	public TipoPuerto getTipoPuerto () {
		return this.puerto;
	}
	
	public Boolean tienePuerto () {
		return this.getTipoPuerto().esBasico() || this.getTipoPuerto().esEspecial();
	}
	
	public Boolean tieneCamino () {
		return this.tieneCamino;
	}
	
	public Jugadores getPropietario () {
		return this.propietario;
	}
	
	/*
	 * Devuelve true si y solo si ha podido construir un camino en la Arista.
	 * */
	public Boolean setCamino (Jugadores j) {
		try {
			if (j == null) {
				throw new Exception ("function setCarretera (Jugadores j): El parámetro j corresponde a un jugador.");
			} 
			if ( !tieneCamino() && puedeConstruirJugador[j.getColor().numeroColor()]) {
				this.propietario = j;
				this.tieneCamino = true;
				// Ninguno podrá ya construir un camino --> Todo a false.
				puedeConstruirJugador[0] = false;
				puedeConstruirJugador[1] = false;
				puedeConstruirJugador[2] = false;
				puedeConstruirJugador[3] = false;
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
	}
	
	public void setPuerto (int tipoPuerto) {
		switch(tipoPuerto) {
		case 0:
			this.puerto = TipoPuerto.PuertoBasico; break;
		case 1:
			this.puerto = TipoPuerto.PuertoMadera; break;
		case 2:
			this.puerto = TipoPuerto.PuertoLana; break;
		case 3:
			this.puerto = TipoPuerto.PuertoCereales; break;
		case 4:
			this.puerto = TipoPuerto.PuertoArcilla; break;
		case 5:
			this.puerto = TipoPuerto.PuertoMinerales; break;
		default:
			this.puerto = TipoPuerto.Nada; break;
		}
	}
	
	public TipoPuerto getPuerto () {
		return this.puerto;
	}
	
	/*
	 * Actualiza la arista como posible lugar donde el jugador con identificador identificador si 
	 * el valor del parámetro identificador es mayor o igual a 0 y menor al numero de jugadores.
	 * @return true si y solo si el valor de identificador es valido.
	 * */
	public Boolean posibleCaminoDeJugador (int identificador) {
		try {
			if (identificador >= 0 && identificador < this.numJugadores) {
				if (!this.tieneCamino()) {
					puedeConstruirJugador[identificador] = true;
				} else {
					puedeConstruirJugador[identificador] = false;
				}
				return true;
			} else {
				throw new Exception ("function posibleCarreteraDeJugador (int identificador): El "
						+ "identificador debe de pertenecer a un jugador, se esperaban valores "
						+ "0 <= i < " + this.numJugadores);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
	}
	
	public String getCaminoJugador () {
		if ( tieneCamino() ) {
			return "Camino" + propietario.getColor().getStringColor();
		}
		return "Nada";
	}
	
	public Coordenadas getCoordenadasVertice1 () {
		return new Coordenadas (this.coord.getX(),this.coord.getY());
	}
	
	public Coordenadas getCoordenadasVertice2 () {
		return new Coordenadas (this.coord.getFin_x(),this.coord.getFin_y());
	}
	
	public Boolean[] getPosibleCaminoDeJugador () {
		return puedeConstruirJugador;
	}
	
	
	public Boolean getPosibleCaminoDeJugador (int identificador) {
		try {
			if (identificador >= this.numJugadores || identificador < 0) {
				throw new Exception("function getPosibleCaminoDeJugador (int identificador): El "
						+ "valor de la variable i introducido no corresponde a ningún "
						+ "jugador, los identificadores de jugadores disponibles son 0 <= i < " 
						+ this.numJugadores);
			}
			return puedeConstruirJugador[identificador];
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
		
	}
}
