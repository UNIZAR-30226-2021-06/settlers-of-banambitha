package es.susangames.catan.logica;

import org.json.JSONObject;

// Acciones del juego
public class ControladorJuego {
	private Tablero tablero;
	
	private Jugadores j[];
	private int num_jugadores;
	
	ControladorJuego(int num_jugadores) {
		this.num_jugadores = num_jugadores;
		j = new Jugadores[num_jugadores];
		tablero = new Tablero();
	}
	
	public JSONObject generarTablero () {
		
	}
	
	/*
	 * Genera un n�mero aleatorio n del 1 al 6. Simula la acci�n de lanzar un dado.
	 * 
	 * @return Devuelve n, siendo n un entero 1 <= n <= 6.
	 * */
	public int generarNumero () {
		return (int) Math.floor( Math.random()*5 + 1 );
	}
	
	/*
	 * Carga un tablero ubicado en la base de datos.
	 * */
	public void cargarTablero () {
		
	}
	
	public void construirAsentamiento (Integer idHexagono, Coordenadas c, Jugadores j) {
		// Comprobar si el jugador puede construir el asentamiento.
		if (j.puedeConstruirPueblo()) {
			tablero.construirAsentamiento(idHexagono,c, j);
		}
	}
	
	public void comerciar(Jugadores j) {
		
	}
	
	/*
	 * Guarda un tablero en la base de datos.
	 * */
	public void guardarTablero () {
		
	}
	
	public JSONObject JSONmessage ( JSONObject j ) {
		
		Integer id_jugador = j.getInt("player");
		Jugadores jug = j[id_jugador-1];
		
		JSONObject move = j.getJSONObject("move");
		
		switch ( move.getString("name") ) {
		case "construir poblado":
			
			if ( jug.puedeConstruirPueblo() ) {
				
			}
			break;
		case "mejorar poblado":
			break;
		case "crear carretera":
			break;
		case "mover ladron":
			break;
		case "finalizar turno":
			break;
		case "comerciar":
			break;
		case "comerciar con puerto":
			break;
		default:
			// No existe la accion solicitada.
		}
	}
	
}
