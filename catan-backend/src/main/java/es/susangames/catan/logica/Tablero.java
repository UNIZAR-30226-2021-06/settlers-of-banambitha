package es.susangames.catan.logica;


import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Tablero {
	// Mapa con las coordenadas de los centros de los 19 hexagonos del tablero. 
	private Map<Integer, Hexagonos> hexagonos;
	public Integer NUM_HEXAGONOS = 19;
	
	private int NUM_FILAS = 5;
	private int num_hexagonos_fila[] = {3,4,5,4,3};
	
	private double apotema = Math.sin(Math.PI/3);
	private double tam = 1;
	
	private Integer vector_valores[] = {5, 2, 6, 3, 8, 10, 9, 12, 11, 4, 8, 10, 9, 4, 5, 6, 3, 11};
	private TipoTerreno vector_terrenos[] = {TipoTerreno.Bosque, TipoTerreno.Bosque, TipoTerreno.Bosque, TipoTerreno.Bosque,
			TipoTerreno.Pasto, TipoTerreno.Pasto, TipoTerreno.Pasto, TipoTerreno.Pasto,
			TipoTerreno.Sembrado, TipoTerreno.Sembrado, TipoTerreno.Sembrado,TipoTerreno.Sembrado,
			TipoTerreno.Cerro, TipoTerreno.Cerro, TipoTerreno.Cerro,
			TipoTerreno.Montanya, TipoTerreno.Montanya, TipoTerreno.Montanya, TipoTerreno.Desierto};
	private Integer vector_puertos[] = {1,2,3,4,5,0,0,0,0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
			-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	
	private Jugadores j[];
	private int num_jugadores;
	
	int turno;
	int dados;
	
	Tablero (Integer num_jugadores) {
		this.num_jugadores = num_jugadores;
		j = new Jugadores[num_jugadores];
		
		j[0] = new Jugadores(ColorJugador.Azul);
		j[1] = new Jugadores(ColorJugador.Rojo);
		j[2] = new Jugadores(ColorJugador.Amarillo);
		j[3] = new Jugadores(ColorJugador.Verde);
		
		turno = 0; dados = 0;
		
		hexagonos = new HashMap<Integer,Hexagonos>();
		
		List<TipoTerreno> list_terrenos = Arrays.asList(this.vector_terrenos);
		Collections.shuffle(list_terrenos);
		list_terrenos.toArray(this.vector_terrenos);
		
		List<Integer> list_valores = Arrays.asList(this.vector_valores);
		Collections.shuffle(list_valores);
		list_valores.toArray(this.vector_valores);
		
		List<Integer> list_puertos = Arrays.asList(this.vector_puertos);
		Collections.shuffle(list_puertos);
		list_puertos.toArray(this.vector_puertos);
		
		generarHexagonos ();
		
		generarPuertos ();

		mostrarHexagonos();
	}
	
	private void generarHexagonos () {
		Coordenadas aux;
		int count_value = 0;
		double ini_x, ini_y;
		int id = 0;
		for ( int i = 0; i < NUM_FILAS; ++i ) {
			ini_y = tam*(i+1) + (tam/2)*i;
			if (this.num_hexagonos_fila[i] == 3) {
				ini_x = 3*apotema;
			} else if (this.num_hexagonos_fila[i] == 4) {
				ini_x = 2*apotema;
			} else { // this.num_hexagonos_fila[i] == 5
				ini_x = apotema;
			}
			
			for (int j = 0; j < this.num_hexagonos_fila[i]; ++j) {
				aux = new Coordenadas(ini_x, ini_y);
				if ( this.vector_terrenos[id].esDesierto() ) {
					hexagonos.put(id, new Hexagonos(aux, this.vector_terrenos[id], -1));
				} else {
					hexagonos.put(id, new Hexagonos(aux, this.vector_terrenos[id], 
							this.vector_valores[count_value]));
					count_value++;
				}
				ini_x += 2*apotema;
				id++;
			}
		}
	}
	
	private void generarPuertos () {
		// Total de 9 puertos.
		// 5 de ellos son espec�ficos.
		// pueden tener puertos los hexagonos: 0(3),1(2),2(3),3(2),6(2),7(3),11(3),12(2),15(3),16(2),17(2),18(3).
		int i = 0;
		Collection<Aristas> aristasExteriores = Hexagonos.getAristasExteriores();
		List<Aristas> aristasNoPuertos = new ArrayList<Aristas>();
		for (Aristas a : aristasExteriores) {
			if (this.vector_puertos[i] == -1) {
				aristasNoPuertos.add(a);
			} else {
				a.setPuerto(this.vector_puertos[i]);
			}
			i++;
		}
		
		for (Aristas a : aristasNoPuertos) {
			Hexagonos.eliminarPuerto(a);
		}
	}
	
	public Hexagonos getPosicionLadron () {
		Hexagonos hexagonoLadron = null;
		for (Hexagonos h : hexagonos.values()) {
			if (h.tieneLadron()) {
				hexagonoLadron = h;
				break;
			}
		}
		return hexagonoLadron;
	}
	
	public void moverLadron (Integer nuevaPosicion) {
		Hexagonos posActualLadron = getPosicionLadron();
		
		Hexagonos nuevaPosLadron = hexagonos.get(nuevaPosicion);
		// Si el hexagono al que se puede mover existe
		if (nuevaPosLadron != null) {
			// Comprobamos que los dos hexagonos son adyacentes.
			if (posActualLadron.sonAdyacentes(nuevaPosLadron)) {
				posActualLadron.moverLadron();
				nuevaPosLadron.colocarLadron();
			}
		}
	} 
	
	public void producir (Integer valor) {	
		for (Hexagonos hex : hexagonos.values()) {
			if (hex.getValor() == valor) hex.producir();
		}
	}
	
	
	public void mostrarHexagonos () {
		Vertices v[];
		Aristas a[];
		for ( int i = 0; i < this.NUM_HEXAGONOS; ++i ) {
			v = hexagonos.get(i).getVertices();
			a = hexagonos.get(i).getAristas();
			System.out.println("Hexagono " + i + ": (" + hexagonos.get(i).getCentro().getX() + ", " + 
					hexagonos.get(i).getCentro().getY() + ")");
			for (int j = 0; j < v.length; ++j) {
				//System.out.println("\tid : " + v[j].getIdentificador() +"\t(" + v[j].getCoordenadas().getX() + ", " + v[j].getCoordenadas().getY() + ")");
				System.out.println("\tid : " + a[j].getIdentificador() + "\t[(" + a[j].getCoordenadasAristas().getX() + ", " + a[j].getCoordenadasAristas().getY() + 
						"),( " + a[j].getCoordenadasAristas().getFin_x() + ", " + a[j].getCoordenadasAristas().getFin_y() + ")]");
			}
		}
		System.out.println("Numero de vertices: " + Hexagonos.num_vertices());
		System.out.println("Numero de aristas: " + Hexagonos.num_aristas());
		System.out.println("Numero de puertos: " + Hexagonos.num_puertos());
	}
	
	public JSONArray tiposHexagonosToJSONArray () {
		JSONArray jsArray = new JSONArray ();
		
		for (Hexagonos h : hexagonos.values()) {
			jsArray.put(h.getTipo_terreno().toString());
		}
		
		return jsArray;
	}
	
	public JSONArray valorHexagonosToJSONArray () {
		JSONArray jsArray = new JSONArray ();
		
		for (Hexagonos h : hexagonos.values()) {
			jsArray.put(h.getValor());
		}
		
		return jsArray;
	}
	
	public JSONObject generarTablero () {
		JSONObject jsObject = new JSONObject();
		// Generar respuesta.
		return jsObject;
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
			construirAsentamiento(idHexagono,c, j);
		}
	}
	
	public void comerciar(Jugadores j) {
		
	}
	
	/*
	 * Guarda un tablero en la base de datos.
	 * */
	public void guardarTablero () {
		
	}
	
	public JSONObject infoHexagonosJSON () throws JSONException {
		String aux = "{";
		String auxTipo = "\"tipo\": [";
		String auxValor = "\"valor\": [";
		String auxLadron = "";
		Integer i = 0;
		Iterator<Hexagonos> it = hexagonos.values().iterator();
		
		Hexagonos h = it.next();
		auxTipo += h.getTipo_terreno().getStringTipoTerreno();
		auxValor += h.getValor().toString();
		if (h.tieneLadron())
			auxLadron = i.toString();
		i++;
		while(it.hasNext()) {
			h = it.next();
			auxTipo += "," + h.getTipo_terreno().getStringTipoTerreno();
			auxValor += "," + h.getValor().toString();
			if (h.tieneLadron())
				auxLadron = i.toString();
			i++;
		}
		aux += auxTipo + "]," + auxValor + "]," + "\"ladron\":" + auxLadron + "}";
		return new JSONObject(aux);
	}
	
	public JSONObject returnMessage () throws JSONException {
		JSONObject respuesta = new JSONObject ();
		
		respuesta.put("Message", "");

		JSONObject Tab_inf = new JSONObject();
		Tab_inf.put("hexagono", infoHexagonosJSON());
		
		JSONObject vertices = new JSONObject();
		vertices.put("asentamiento" , new JSONObject());
		vertices.put("posibles_asentamiento" , new JSONObject());
		
		JSONObject aristas = new JSONObject();
		aristas.put("camino" , new JSONObject());
		aristas.put("posibles_camino" , new JSONObject());
		aristas.put("puertos", Hexagonos.puertosToJSON());
		
		Tab_inf.put("vertices", vertices);
		Tab_inf.put("aristas", aristas);
		
		respuesta.put("Tab_inf", Tab_inf);
		
		JSONObject recursos = new JSONObject();
		recursos.put("Player_1", new JSONObject());
		recursos.put("Player_2", new JSONObject());
		recursos.put("Player_3", new JSONObject());
		recursos.put("Player_4", new JSONObject());
		respuesta.put("Recursos", recursos);
		
		JSONObject cartas = new JSONObject();
		cartas.put("Player_1", new JSONObject());
		cartas.put("Player_2", new JSONObject());
		cartas.put("Player_3", new JSONObject());
		cartas.put("Player_4", new JSONObject());
		respuesta.put("Cartas", cartas);
		
		JSONObject puntuacion = new JSONObject();
		puntuacion.put("Player_1", this.j[0].getPuntosVictoria());
		puntuacion.put("Player_2", this.j[1].getPuntosVictoria());
		puntuacion.put("Player_3", this.j[2].getPuntosVictoria());
		puntuacion.put("Player_4", this.j[3].getPuntosVictoria());
		respuesta.put("Puntuacion", puntuacion);
		
		respuesta.put("Resultado_Tirada", this.dados);
		respuesta.put("Turno", this.turno);
		return respuesta;
	}
	
	public JSONObject JSONmessage ( JSONObject jsObject ) throws JSONException {
		
		Integer id_jugador = jsObject.getInt("player");
		Jugadores jug = this.j[id_jugador - 1];
		
		JSONObject move = jsObject.getJSONObject("move");
		
		switch ( move.getString("name") ) {
		case "construir poblado":
			if ( jug.puedeConstruirPueblo() ) {
				int id_vertice = move.getInt("param");
				Vertices v = Hexagonos.getVerticePorId(id_vertice);
				if (v.getPosibleAsentamientoDeJugador(id_vertice)) {
					Hexagonos.construirAsentamiento(v, jug);
					jug.construirAsentamiento();
				}
			}
			break;
		case "mejorar poblado":
			if ( jug.puedeConstruirCiudad() ) {
				int id_vertice = move.getInt("param");
				Vertices v = Hexagonos.getVerticePorId(id_vertice);
				if ( v.tieneAsentamiento() && !v.tieneCiudad() && v.getPropietario().equals(jug) )
					Hexagonos.mejorarAsentamiento(v, jug);
					jug.mejorarAsentamiento();
			}
			break;
		case "crear carretera":
			if ( jug.puedeConstruirCarretera() ) {
				int id_arista = move.getInt("param");
				Aristas a = Hexagonos.getAristaPorId(id_arista);
				if (!a.tieneCamino() && a.getPosibleCaminoDeJugador(id_arista)) {
					Hexagonos.construirCarretera(a, jug);
				}
			}
			break;
		case "mover ladron":
			int id_hexagono = move.getInt("param");
			moverLadron(id_hexagono);
			break;
		case "finalizar turno":
			this.dados = generarNumero();
			turno++;
			break;
		case "comerciar":
			break;
		case "comerciar con puerto":
			break;
		case "primer asentamiento":
			int id_vertice = move.getInt("param");
			Vertices v = Hexagonos.getVerticePorId(id_vertice);
			Hexagonos.construirPrimerAsentamiento(v);
			break;
		case "primer camino":
			int id_arista = move.getInt("param");
			Aristas a = Hexagonos.getAristaPorId(id_arista);
			Hexagonos.construirPrimerCamino(a);
			break;
		default:
			// No existe la accion solicitada.
		}
		
		// RESPUESTA GENERAL.
		JSONObject respuesta = new JSONObject ();
		
		respuesta.put("Message", "");

		JSONObject Tab_inf = new JSONObject();
		Tab_inf.put("hexagono", infoHexagonosJSON());
		JSONObject vertices = new JSONObject();
		vertices.put("asentamiento" , Hexagonos.listAsentamientoToJSON());
		vertices.put("posibles_asentamiento" , Hexagonos.posibleAsentamientoToJSON());
		
		JSONObject aristas = new JSONObject();
		aristas.put("camino" , Hexagonos.listCaminoToJSON());
		aristas.put("posibles_camino" , Hexagonos.posibleCaminoToJSON());
		aristas.put("puertos", Hexagonos.puertosToJSON());
		
		Tab_inf.put("vertices", vertices);
		Tab_inf.put("aristas", aristas);
		
		respuesta.put("Tab_inf", Tab_inf);
		
		JSONObject recursos = new JSONObject();
		recursos.put("Player_1", this.j[0].recursosJugadorToJSON());
		recursos.put("Player_2", this.j[1].recursosJugadorToJSON());
		recursos.put("Player_3", this.j[2].recursosJugadorToJSON());
		recursos.put("Player_4", this.j[3].recursosJugadorToJSON());
		respuesta.put("Recursos", recursos);
		
		JSONObject cartas = new JSONObject();
		cartas.put("Player_1", this.j[0].cartasJugadorToJSON());
		cartas.put("Player_2", this.j[1].cartasJugadorToJSON());
		cartas.put("Player_3", this.j[2].cartasJugadorToJSON());
		cartas.put("Player_4", this.j[3].cartasJugadorToJSON());
		respuesta.put("Cartas", cartas);
		
		this.j[0].actualizarPuntosVictoria();
		this.j[1].actualizarPuntosVictoria();
		this.j[2].actualizarPuntosVictoria();
		this.j[3].actualizarPuntosVictoria();
		
		JSONObject puntuacion = new JSONObject();
		puntuacion.put("Player_1", this.j[0].getPuntosVictoria());
		puntuacion.put("Player_2", this.j[1].getPuntosVictoria());
		puntuacion.put("Player_3", this.j[2].getPuntosVictoria());
		puntuacion.put("Player_4", this.j[3].getPuntosVictoria());
		respuesta.put("Puntuacion", puntuacion);
		
		respuesta.put("Resultado_Tirada", this.dados);
		respuesta.put("Turno", this.turno);
		return respuesta;
	}
	
}
