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


/**
 * Esta clase define el tablero de la partida
 * @author: Fabian Conde Lafuente y Víctor García García
 */
public class Tablero {
//Campos de la clase

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
	
	public Tablero () {
		this.num_jugadores = 4;
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

		//mostrarHexagonos();
	}
	
	/**
     * Método que genera los hexagonos del tablero
     */
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
					hexagonos.put(id, new Hexagonos(aux, this.vector_terrenos[id], -1, this.num_jugadores));
				} else {
					hexagonos.put(id, new Hexagonos(aux, this.vector_terrenos[id], 
							this.vector_valores[count_value], this.num_jugadores));
					count_value++;
				}
				ini_x += 2*apotema;
				id++;
			}
		}
	}
	
	/**
     * Método que genera los puertos del tablero
     */
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
	
	private Hexagonos getHexagono (int identificador) {
		try {
			return this.hexagonos.get(identificador);
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}
	/**
	 * Dado un identificador id, devuelve true si corresponde con uno de los hexagonos existentes
	 * en el tablero.
	 * @param id entero que identifica a un hexagono
	 * @return true si y solo si existe un hexagono cuyo identificador sea id
	 */
	private Boolean existeHexagono (int id) {
		return hexagonos.containsKey(id);
	}
	
	/**
     * Método que devuelve el hexagono donde se encuentra el ladron
     * @return el hexagono donde se encuentra el ladron
     */
	private Hexagonos getPosicionLadron () {
		Hexagonos hexagonoLadron = null;
		for (Hexagonos h : hexagonos.values()) {
			if (h.tieneLadron()) {
				hexagonoLadron = h;
				break;
			}
		}
		return hexagonoLadron;
	}
	
	/**
     * Método que mueve el ladron al hexagono indicado
     */
	private void moverLadron (Integer nuevaPosicion) {
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
	
	/**
     * Método que entrega los recursos producidos por las ciudades y pueblos de los hexagonos activados a los jugadores propietarios de estos
     */
	private void producir (Integer valor) {	
		for (Hexagonos hex : hexagonos.values()) {
			if (hex.getValor() == valor) hex.producir();
		}
	}
	
	/**
     * Método que imprime informacion del tablero por pantalla
     */
	private void mostrarHexagonos () {
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
	
	/*
	 * Genera un numero aleatorio n del 1 al 6. Simula la accion de lanzar un dado.
	 * @return Devuelve n, siendo n un entero 1 <= n <= 6.
	 * */
	private int generarNumero () {
		return (int) Math.floor( Math.random()*5 + 1 );
	}
	
	//--------------------------- Partida pausada -----------------------------------------------\\
	/*
	 * Carga un tablero ubicado en la base de datos.
	 * */
	public void cargarTablero () {
		
	}
	/*
	 * Guarda un tablero en la base de datos.
	 * */
	public void guardarTablero () {
		
	}
	//-------------------------------------------------------------------------------------------\\
	
	//---------------------------------- Cartas -------------------------------------------------\\
	public void usarCartaCaballero(Integer nuevaPosicion, Jugadores j) {
		if (j.hayCartasCartasCaballeros()){
			j.usarCartaCaballero();
			this.moverLadron(nuevaPosicion);
		}
	}
	
	public void usarCartaMonopolio (TipoTerreno tipo, Jugadores j) {
		//TODO: arreglar este trozo de código
		// if (j.numCartasMonopolio() > 0){
		// 	j.usarCartaMonopolio();
		// 	for (int i = 0; i < 4; ++i ) {
		// 		if(!j.mismoJugador(this.Jugadores[i])){
		// 			switch (tipo) {
		// 				case 0:
		// 					j.setMadera(j.getMadera() + this.j[i].getMadera()); // j.madera += this.Jugadores[i].madera;
		// 					this.j[i].setMadera(0); // this.Jugadores[i].madera = 0;
		// 				case 1:
		// 					j.setLana(j.getLana() + this.j[i].getLana()); // j.lana += this.Jugadores[i].lana;
		// 					this.j[i].setLana(0); // this.Jugadores[i].lana = 0;
		// 				case 2:
		// 					j.setCereales(j.getCereales() + this.j[i].getCereales()); // j.cereales += this.Jugadores[i].cereales;
		// 					this.j[i].setCereales(0); // this.Jugadores[i].cereales = 0;
		// 				case 3:
		// 					j.setArcilla(j.getArcilla() + this.j[i].getArcilla()); // j.arcilla += this.Jugadores[i].arcilla;
		// 					this.j[i].setArcilla(0); // this.Jugadores[i].arcilla = 0;
		// 				case 4:
		// 					j.setMineral(j.getMineral() + this.j[i].getMineral()); // j.mineral += this.Jugadores[i].mineral;
		// 					this.j[i].setMineral(0);// this.Jugadores[i].mineral = 0;
		// 			}
		// 		}
		// 	}		
		// }
	}
	
	private void usarCartasConstruccionCarretera(Hexagonos hexagono, Aristas arista, Jugadores j) {
		if (j.hayCartasContruccionCarreteras()){
			j.usarCartasConstruccionCarretera();
			//TODO: arreglar este trozo de código
			//hexagono.construirCarretera(arista, j);
		}
	}
	//-------------------------------------------------------------------------------------------\\
	
	//---------------------------------- Comercio -----------------------------------------------\\
	public void comerciar(Jugadores j1, Jugadores j2, int maderaJ1, int lanaJ1, int cerealesJ1,
		int arcillaJ1, int mineralJ1, int maderaJ2, int lanaJ2, int cerealesJ2,
		int arcillaJ2, int mineralJ2) {
		
	}
	
	//----------------------------------- JSON -----------------------------------\\
	
	/**
     * Método que devuelve los tipos de terreno de los hexagonos
     * @return los tipos de terreno de los hexagonos
     */
	private JSONArray tiposHexagonosToJSONArray () {
		JSONArray jsArray = new JSONArray ();
		
		for (Hexagonos h : hexagonos.values()) {
			jsArray.put(h.getTipo_terreno().toString());
		}
		
		return jsArray;
	}
	
	/**
     * Método que devuelve los valores de los hexagonos
     * @return los valores de los hexagonos
     */
	private JSONArray valorHexagonosToJSONArray () {
		JSONArray jsArray = new JSONArray ();
		
		for (Hexagonos h : hexagonos.values()) {
			jsArray.put(h.getValor());
		}
		
		return jsArray;
	}
	
	/**
     * Método que tranforma el JSONObject a jsObject
     * @return jsObject
     */
	private JSONObject generarTablero () {
		JSONObject jsObject = new JSONObject();
		// Generar respuesta.
		return jsObject;
	}

	/*
	 * Genera un JSONObject con informacion de los hexagonos
	 * @return devuelve informacion de los hexagonos
	 * */
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

	/*
	 * Genera un JSONObject con la informacion de la partida
	 * @return JSONObject con la informacion de la partida
	 * TODO: hacer que esta función devuelva siempre el estado actual del tablero
	 * */
	public JSONObject returnMessage () throws JSONException {
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
	
	/*
	 * Atiende todas las posibles jugadas del juegador, las ejecuta y actualiza la informacion de la partida.
	 * @return JSONObject con la informacion de la partida
	 * */
	public JSONObject JSONmessage ( JSONObject jsObject ) throws JSONException {
		
		Integer id_jugador = jsObject.getInt("player");
		Jugadores jug = this.j[id_jugador - 1];

		String message="";
		int exit_status = 0;
		
		JSONObject move = jsObject.getJSONObject("move");
		
		switch ( move.getString("name") ) {
		case "construir poblado":
			// Jugador uede construir pueblo
			if ( jug.puedeConstruirPueblo() ) {
				int id_vertice = move.getInt("param");
				// Comprobar si el identificador del vertice corresponde a algún vertice
				if (Hexagonos.existeVertice(id_vertice)) {
					Vertices v = Hexagonos.getVerticePorId(id_vertice);
					if (v.tieneAsentamiento()) {
						if (v.getPosibleAsentamientoDeJugador(id_vertice)) {
							Hexagonos.construirAsentamiento(v, jug);
							jug.construirAsentamiento();
							message = "Se ha construido el poblado correctamente";
							exit_status = 0;
						}
						else {
							message = "[Error] La posición del asentamiento no está disponible para el "
							+ "jugador ya que no cumple con los requsitos";
							exit_status = 4;
						}
					} else {
						message = "[Error] Ya existe un poblado construido en ese vertice";
						exit_status = 3;
					}
					
				}
				else {
					message = "[Error] El identificador del vertice introducido no se corresponde "
					+ "con ninguno existente";
					exit_status = 2;
				}
			} else {
				message = "[Error] El jugador no dispone de los recursos necesarios para construir"
				+ " un poblado";
				exit_status = 1;
			}
			break;
		case "mejorar poblado":
			if ( jug.puedeConstruirCiudad() ) {
				int id_vertice = move.getInt("param");
				if (Hexagonos.existeVertice(id_vertice)) {
					Vertices v = Hexagonos.getVerticePorId(id_vertice);
					if ( v.tieneAsentamiento() ) {
						if (v.getPropietario().equals(jug)) {
							if (!v.tieneCiudad()) {
							Hexagonos.mejorarAsentamiento(v, jug);
							jug.mejorarAsentamiento();
							message = "Se ha construido la ciudad correctamente";
							exit_status = 0;
							} else {
								message = "[Error] Ya existe una ciudad construida en ese vertice";
								exit_status = 9;
							}
						} else {
							message = "[Error] El asentamiento construido en el vertice no " + 
							"corresponde con un asentamiento del jugador";
							exit_status = 8;
						}
					} else {
						message = "[Error] En el vertice con id " + id_vertice + 
						" no se encuentra ningún poblado sobre el que construir una ciudad";
						exit_status = 7;
					}
				} else {
					message = "[Error] El identificador del vertice introducido no se corresponde con ninguno existente";
					exit_status = 6;
				}	
			} else {
				message = "[Error] El jugador no dispone de los recursos necesarios para construir una ciudad";
				exit_status = 5;
			}
			break;
		case "construir camino":
			if ( jug.puedeConstruirCamino() ) {
				int id_arista = move.getInt("param");
				if (Hexagonos.existeArista(id_arista)) {
					Aristas a = Hexagonos.getAristaPorId(id_arista);
					if (!a.tieneCamino()) {
						if (a.getPosibleCaminoDeJugador(id_jugador)) {
							Hexagonos.construirCamino(a, jug);
							jug.construirCamino();
							message = "Se ha construido el camino correctamente";
							exit_status = 0;
						} else {
							message = "[Error] La posición del camino no está disponible para" + 
							" el jugador ya que no cumple con los requsitos";
							exit_status = 13;
						}
					} else {
						message = "[Error] Ya existe un camino construido en esa arista";
						exit_status = 12;
					}
				} else {
					message = "[Error] El identificador de la arista introducido no se corresponde con ninguno existente";
					exit_status = 11;
				}
			} else {
				message = "[Error] El jugador no dispone de los recursos necesarios para construir un camino";
				exit_status = 10;
			}
			break;
		case "mover ladron":
			int id_hexagono = move.getInt("param");
			// Si el hexagono al que se puede mover existe
			if (existeHexagono(id_hexagono)) {
				Hexagonos posActualLadron = getPosicionLadron();
				Hexagonos nuevaPosLadron = hexagonos.get(id_hexagono);
				// Comprobamos que los dos hexagonos son adyacentes.
				if (posActualLadron.sonAdyacentes(nuevaPosLadron)) {
					posActualLadron.moverLadron();
					nuevaPosLadron.colocarLadron();
					message = "Se ha movido correctamente el ladrón de hexagono";
					exit_status = 0;
				}
				else {
					message = "[Error] El identificador del hexagono introducido no se corresponde"
					+ "con ninguno existente";
					exit_status = 14;
				}
				
			}
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
			Hexagonos.construirPrimerAsentamiento(v,jug);
			break;
		case "primer camino":
			int id_arista = move.getInt("param");
			Aristas a = Hexagonos.getAristaPorId(id_arista);
			Hexagonos.construirPrimerCamino(a,jug);
			break;
		default:
			// No existe la accion solicitada.
		}
		
		// RESPUESTA GENERAL.
		JSONObject respuesta = new JSONObject ();
		
		respuesta.put("exit_status",exit_status);
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
		Integer ganador = ganador(); 
		if ( ganador != null){
			respuesta.put("Ganador",ganador);
		}
		return respuesta;
	}

	//-------------------------------------------------------------------------------------------\\
	
	public Boolean hayGanador() {
		int pVJ1 = this.j[0].getPuntosVictoria();
		int pVJ2 = this.j[1].getPuntosVictoria();
		int pVJ3 = this.j[2].getPuntosVictoria();
		int pVJ4 = this.j[3].getPuntosVictoria();
		return pVJ1 >= 10 || pVJ2 >= 10 || pVJ3 >= 10 || pVJ4 >= 10;
	}

	public Integer ganador () {
		if (hayGanador()) {
			int pVJ1 = this.j[0].getPuntosVictoria();
			int pVJ2 = this.j[1].getPuntosVictoria();
			int pVJ3 = this.j[2].getPuntosVictoria();
			int pVJ4 = this.j[3].getPuntosVictoria();
			if (pVJ1 >= 10) return 1;
			else if (pVJ2 >= 10) return 2;
			else if (pVJ3 >= 10) return 3;
			else if (pVJ4 >= 10) return 4;
			else return -1;
		}
		else return null;
	}
}

