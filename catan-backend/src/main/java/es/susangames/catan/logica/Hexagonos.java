package es.susangames.catan.logica;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Hexagonos {
	static private int tam = 1;
	static private int NUM_VERTICES_ARISTAS = 6;
	
	private Coordenadas centro;
	private Vertices v[];
	private Aristas a[];
	
	private Boolean tieneLadron;
	
	private TipoTerreno tipo_terreno;
	private int valor;
	
	private static Integer numJugadores;
	
	/*
	 * Vertices de todos los hexagonos creados.
	 * */
	private static Map<Coordenadas, Vertices> vertices = new HashMap<Coordenadas, Vertices>();
	private static Map<Integer, Vertices> verticesPorID = new HashMap<Integer, Vertices>();
	private static int next_id_vertice = 0;
	private static Map<CoordenadasAristas, Aristas> aristas = new HashMap<CoordenadasAristas, Aristas>();
	private static Map<Integer, Aristas> aristasPorID = new HashMap<Integer, Aristas>();
	private static int next_id_arista = 0;
	private static Map<CoordenadasAristas, Aristas> puertos = new HashMap<CoordenadasAristas, Aristas>();
	
	Hexagonos (Coordenadas c, TipoTerreno tipo_terreno, int valor, Integer numJugadores) {
		this.centro = c;
		this.tipo_terreno = tipo_terreno;
		this.valor = valor;
		this.tieneLadron = false;
		if(tipo_terreno.esDesierto()) {
			this.tieneLadron = true;
		}
		
		this.numJugadores = numJugadores;
		
		v = new Vertices[NUM_VERTICES_ARISTAS];
		a = new Aristas[NUM_VERTICES_ARISTAS];
		generarVerticesyAristas();
	}
	
	/*
	 * Genera todos los vertices de un hexagono dado su centro.
	 * */
	private void generarVerticesyAristas () {
		Coordenadas aux;
		Coordenadas aux2 = null;
		CoordenadasAristas auxCoordAristas;
		Coordenadas v1 = null;
		for ( int i = 0; i < NUM_VERTICES_ARISTAS; ++i ) {
			aux = calcularVertice(i);
			
			// El vertice con Coordenadas aux ya ha sido creado.
			if (!vertices.containsKey(aux)) {
				v[i] = new Vertices(aux,next_id_vertice);
				vertices.put(aux, v[i]);
				verticesPorID.put(next_id_vertice, v[i]);
				next_id_vertice++;
			} else {
				v[i] = vertices.get(aux);
			}
			
			if ( i == 0 ) {v1 = aux; aux2 = aux;}
			else if ( i == (NUM_VERTICES_ARISTAS - 1) ) {
				auxCoordAristas = new CoordenadasAristas(aux.getX(), aux.getY(),aux2.getX(), aux2.getY());
				if (!aristas.containsKey(auxCoordAristas)) {
					a[i-1] = new Aristas(auxCoordAristas, next_id_arista, this.numJugadores);
					aristas.put(auxCoordAristas, a[i-1]);
					puertos.put(auxCoordAristas, a[i-1]);
					aristasPorID.put(next_id_arista, a[i-1]);
					next_id_arista++;
				} else {
					a[i-1] = aristas.get(auxCoordAristas);
					puertos.remove(auxCoordAristas);
				}
				auxCoordAristas = new CoordenadasAristas(aux.getX(), aux.getY(),v1.getX(), v1.getY());
				if (!aristas.containsKey(auxCoordAristas)) {
					a[i] = new Aristas(auxCoordAristas,next_id_arista, this.numJugadores);
					aristas.put(auxCoordAristas, a[i]);
					puertos.put(auxCoordAristas, a[i]);
					aristasPorID.put(next_id_arista, a[i]);
					next_id_arista++;
				} else {
					a[i] = aristas.get(auxCoordAristas);
					puertos.remove(auxCoordAristas);
				}
			} else {
				auxCoordAristas = new CoordenadasAristas(aux.getX(), aux.getY(),aux2.getX(), aux2.getY());
				if (!aristas.containsKey(auxCoordAristas)) {
					a[i-1] = new Aristas(auxCoordAristas,next_id_arista, this.numJugadores);
					aristas.put(auxCoordAristas, a[i-1]);
					puertos.put(auxCoordAristas, a[i-1]);
					aristasPorID.put(next_id_arista, a[i-1]);
					next_id_arista++;
				} else {
					a[i-1] = aristas.get(auxCoordAristas);
					puertos.remove(auxCoordAristas);
				}
				aux2 = aux;
			}
		}
	}
	
	/*
	 * Calcula las coordenadas de un vertice dado un entero i, este entero indica cual de los 
	 * vertices del hexagono vamos a calcular.
	 * */
	private Coordenadas calcularVertice (int i) {
		double angle_deg = 60 * i - 30;
		double angle_rad = Math.PI / 180 * angle_deg;
		
		double coordX = centro.getX() + tam * Math.cos(angle_rad);
		double coordY = centro.getY() + tam * Math.sin(angle_rad);
		
		return new Coordenadas(Math.round(coordX * 100000d) / 100000d, Math.round(coordY * 10d) / 10d);
	}
	
	/*
	 * Devuelve las coordenadas del centro
	 * */
	public Coordenadas getCentro () {
		return this.centro;
	}
	
	/*
	 * Devuelve el valor del hexagono
	 * */
	public Integer getValor () {
		return this.valor;
	} 
	
	/*
	 * Devuelve un vector con todos los v�rtices que forman el hexagono
	 * */
	public Vertices[] getVertices() {
		return this.v;
	}
	
	public Aristas[] getAristas() {
		return this.a;
	}
	
	/*
	 * Comprueba si el hexagono h es adyacente.
	 * */
	public Boolean sonAdyacentes (Hexagonos h) {
		try {
			if (h == null) {
				throw new Exception ("El hexagono h no puede ser nulo.");
			}
			return this.centro.getDistancia(h.getCentro()) <= 2*calcularApotema();
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
	}
	
	/*
	 * Calcula el valor de la apotema del hexagono
	 * */
	private double calcularApotema () {
		return Math.sin(Math.PI/3) ;
	}
	
	/*
	 * Comprueba si el Vertice v forma parte de los v�rtices del hexagono.
	 * */
	public Boolean contieneVertice (Vertices v) {
		Boolean encontrado = false;
		for (int i = 0; i < this.NUM_VERTICES_ARISTAS && !encontrado; ++i) {
			if (this.v[i] == v) encontrado = true;
		}
		
		return true;
	}
	
	/*
	 * Devuelve el valor de la variable booleana tieneLadron
	 * */
	public Boolean tieneLadron() {
		return this.tieneLadron;
	}
	
	public void moverLadron () {
		this.tieneLadron = false;
	}
	
	/*
	 * Asigna el valor del booleano tieneLadron a Verdadero. 
	 * */
	public void colocarLadron() {
		this.tieneLadron = true;
	}
	
	/*
	 * Dado un tipo Coordenadas c pasada como paramentro, comprueba si existe alg�n vertice cuyo
	 * Coordenadas sean iguales
	 * */
	public static Boolean existeCoordenada (Coordenadas c) {
		return vertices.containsKey(c);
	}
	
	/**
	 * Dado un identificador id, devuelve true si corresponde con uno de los vertices existentes
	 * en el tablero.
	 * @param id entero que identifica a un vertice
	 * @return true si y solo si existe un vertice cuyo identificador sea id
	 */
	public static Boolean existeVertice (int id) {
		return verticesPorID.containsKey(id);
	}
	/**
	 * Dado un identificador id, devuelve true si corresponde con una de las aristas existentes
	 * en el tablero.
	 * @param id entero que identifica a una arista.
	 * @return true si y solo si existe una aristas cuyo identificador sea id.
	 */
	public static Boolean existeArista (int id) {
		return aristasPorID.containsKey(id);
	}
	
	public static int num_vertices () {
		return vertices.size();
	}
	
	public static int num_aristas () {
		return aristas.size();
	}
	
	public static int num_puertos () {
		return puertos.size();
	}
	
	public static Collection<Aristas> getAristasExteriores () {
		return puertos.values();
	}
	
	public static void eliminarPuerto (Aristas p) {
		puertos.remove(p.getCoordenadasAristas());
	}
	
	/*
	 * Este hexagono produce materiales, es decir, entrega una unidad de material del hexagono a 
	 * los jugadores que tengan un asentamiento. 
	 * */
	public void producir() {
		if (!tieneLadron()) {
			for (int i = 0 ; i < NUM_VERTICES_ARISTAS; i++) {
				v[i].producir(this.tipo_terreno);
			}
		}
	}
	
	private static Aristas[] aristasDelVertice (Vertices v) {
		Aristas a[] = new Aristas[3];
		Coordenadas c = v.getCoordenadas();
		// Se trata de un vertice que pertenece a un Hexagono.
		if (vertices.containsKey(v.getCoordenadas())) {
			 Object o[] = aristas.entrySet().stream()
					.filter(x -> x.getKey().contieneCoordenada(c))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).values().toArray();
			 a = Arrays.copyOf(o, o.length, Aristas[].class);
		}
		return a;
	}
	
	public static void construirAsentamiento (Vertices v, Jugadores j) {
		Vertices verticesAdyacentes[] = Hexagonos.getVerticesAdyacentes(v);
		Aristas aristasAdyacentes[] = Hexagonos.aristasDelVertice(v);
		if (j.puedeConstruirPueblo() && !v.tieneAsentamiento()) {
			// No existe asentamiento adyacente.
			Boolean existeAsentamiento = false;
			for (int i = 0; i < verticesAdyacentes.length && !existeAsentamiento; ++i) {
				if (verticesAdyacentes[i] != null) {
					if (verticesAdyacentes[i].tieneAsentamiento()) existeAsentamiento = true;
				}
			}
			
			if (!existeAsentamiento) {
				// Comprobamos que existe un camino del jugador en alguno de las aristas adyacentes.
				Boolean existeCaminoDeJugador = false;
				for (int i = 0; i < aristasAdyacentes.length && !existeCaminoDeJugador; ++i) {
					if (aristasAdyacentes[i] != null) {
						if (aristasAdyacentes[i].tieneCamino() && aristasAdyacentes[i].getPropietario().equals(j)) {
							existeCaminoDeJugador = true;
						}
					}
				}
				
				if (existeCaminoDeJugador) {
					v.construirAsentamiento(j);
					
					for (int i = 0; i < verticesAdyacentes.length; ++i) {
						if (verticesAdyacentes[i] != null)
							verticesAdyacentes[i].asentamientoAdyacente();
					}
					for (int i = 0; i < aristasAdyacentes.length; ++i) {
						if (aristasAdyacentes[i] != null) {
							aristasAdyacentes[i].posibleCaminoDeJugador(j.getColor().numeroColor());
						}
					}
				}
			}
		}
	}
	
	public static void construirCamino (Aristas a, Jugadores j) {
		if (j.puedeConstruirCamino()) {
			construirPrimerCamino(a,j);
		}
	}
	
	//
	public static void mejorarAsentamiento (Vertices v, Jugadores j) {
		if (j.puedeConstruirCiudad()) {
			if (vertices.containsKey(v.getCoordenadas())) {
				if (v.tieneAsentamiento() && !v.tieneCiudad() && v.getPropietario().equals(j)) {
					v.mejorarAsentamiento();
					j.mejorarAsentamiento();
				}
			}
		}
	}
	
	
	public TipoTerreno getTipo_terreno() {
		return tipo_terreno;
	}

	public void setTipo_terreno(TipoTerreno tipo_terreno) {
		this.tipo_terreno = tipo_terreno;
	}

	public static JSONArray listAsentamientoToJSON () throws JSONException {
		Iterator<Vertices> it = vertices.values().iterator();
		Vertices aux;
		JSONArray jsArray = new JSONArray();
		String listAsentamiento[] = new String[Hexagonos.num_vertices()];
		int id;
		while (it.hasNext()) {
			aux = it.next();
			id = aux.getIdentificador();
			listAsentamiento[id] = aux.getAsentamientoJugador();
		}
		
		jsArray.put(listAsentamiento);
		
		return jsArray;
	}
	
	public static JSONArray posibleAsentamientoToJSON () throws JSONException {
		Iterator<Vertices> it = vertices.values().iterator();
		Vertices vAux;
		JSONArray jsArray = new JSONArray();
		Boolean posiblesAsentamientos[][] = new Boolean[numJugadores][Hexagonos.num_vertices()];
		int id;
		while (it.hasNext()) {
			vAux = it.next();
			id = vAux.getIdentificador();
			posiblesAsentamientos[0][id] = vAux.getPosibleAsentamientoDeJugador(0);
			posiblesAsentamientos[1][id] = vAux.getPosibleAsentamientoDeJugador(1);
			posiblesAsentamientos[2][id] = vAux.getPosibleAsentamientoDeJugador(2);
			posiblesAsentamientos[3][id] = vAux.getPosibleAsentamientoDeJugador(3);
		}
		
		jsArray.put(posiblesAsentamientos[0]);
		jsArray.put(posiblesAsentamientos[1]);
		jsArray.put(posiblesAsentamientos[2]);
		jsArray.put(posiblesAsentamientos[3]);
		
		return jsArray;
	}
	
	public static JSONArray listCaminoToJSON () throws JSONException {
		Iterator<Aristas> it = aristas.values().iterator();
		JSONArray jsArray = new JSONArray();
		Aristas aux;
		String caminos[] = new String[Hexagonos.num_aristas()];
		int id;
		while (it.hasNext()) {
			aux = it.next();
			id = aux.getIdentificador();
			caminos[id] = aux.getCaminoJugador();
		}
		
		jsArray.put(caminos);
		
		return jsArray;
	}
	
	public static JSONArray posibleCaminoToJSON () throws JSONException {
		Iterator<Aristas> it = aristas.values().iterator();
		Aristas aAux;
		JSONArray jsArray = new JSONArray();
		Boolean posiblesCaminos[][] = new Boolean [4][Hexagonos.num_aristas()];
		int id;
		while (it.hasNext()) {
			aAux = it.next();
			id = aAux.getIdentificador();
			posiblesCaminos[0][id] = aAux.getPosibleCaminoDeJugador(0);
			posiblesCaminos[1][id] = aAux.getPosibleCaminoDeJugador(1);
			posiblesCaminos[2][id] = aAux.getPosibleCaminoDeJugador(2);
			posiblesCaminos[3][id] = aAux.getPosibleCaminoDeJugador(3);
		}
		
		jsArray.put(posiblesCaminos[0]);
		jsArray.put(posiblesCaminos[1]);
		jsArray.put(posiblesCaminos[2]);
		jsArray.put(posiblesCaminos[3]);
		
		return jsArray;
	}
	
	public static JSONObject puertosToJSON () throws JSONException {
		JSONObject jsObj = new JSONObject();
		JSONArray puertosBasicos = new JSONArray();
		Iterator<Aristas> it = puertos.values().iterator();
		Aristas aAux;
		Integer puertoMadera, puertoLana, puertoArcilla, puertoCereales, puertoMineral;
		puertoMadera = puertoLana = puertoArcilla = puertoCereales = puertoMineral = -1;
		while (it.hasNext()) {
			aAux = it.next();
			if (aAux.getTipoPuerto().esBasico())
				puertosBasicos.put(aAux.getIdentificador());
			else if (aAux.getTipoPuerto().esPuertoMadera())
				puertoMadera = aAux.getIdentificador();
			else if (aAux.getTipoPuerto().esPuertoLana())
				puertoLana = aAux.getIdentificador();
			else if (aAux.getTipoPuerto().esPuertoArcilla())
				puertoArcilla = aAux.getIdentificador();
			else if (aAux.getTipoPuerto().esPuertoCereales())
				puertoCereales = aAux.getIdentificador();
			else if (aAux.getTipoPuerto().esPuertoMineral())
				puertoMineral = aAux.getIdentificador();
		}
		
		jsObj.put("puertosBasicos", puertosBasicos);
		jsObj.put("puertoMadera", puertoMadera);
		jsObj.put("puertoLana", puertoLana);
		jsObj.put("puertoCereales", puertoCereales);
		jsObj.put("puertoArcilla", puertoArcilla);		
		jsObj.put("puertoMineral", puertoMineral);
		return jsObj;
	}
	
	public static Vertices getVerticePorId (int id) {
		return verticesPorID.get(id);
	}
	
	public static Aristas getAristaPorId (int id) {
		return aristasPorID.get(id);
	}
	
	public static Vertices[] getVerticesAdyacentes (int id) {
		CoordenadasAristas coordAristas;
		Coordenadas coordAux;
		Vertices v_adyacentes[] = new Vertices[3];
		int ind = 0;
		
		Vertices vAux = getVerticePorId (id);		
		Aristas aristasAdyacentes[] = aristasDelVertice (vAux);
		
		for (int i = 0; i < aristasAdyacentes.length; ++i) {
			coordAristas = aristasAdyacentes[i].getCoordenadasAristas();
			coordAux = new Coordenadas(coordAristas.getX() , coordAristas.getY());
			if (!vAux.getCoordenadas().equals(coordAux)) {
				v_adyacentes[ind] = vertices.get(coordAux); ind++;
			}
			coordAux = new Coordenadas(coordAristas.getFin_x() , coordAristas.getFin_y());
			if (!vAux.getCoordenadas().equals(coordAux)) {
				v_adyacentes[ind] = vertices.get(coordAux); ind++;
			}
		}
		
		return v_adyacentes;
	}
	
	public static Vertices[] getVerticesAdyacentes (Vertices vertice) {
		CoordenadasAristas coordAristas;
		Coordenadas coordAux;
		Vertices v_adyacentes[] = new Vertices[3];
		int ind = 0;
		
		Aristas aristasAdyacentes[] = aristasDelVertice (vertice);
		
		for (int i = 0; i < aristasAdyacentes.length; ++i) {
			coordAristas = aristasAdyacentes[i].getCoordenadasAristas();
			coordAux = new Coordenadas(coordAristas.getX() , coordAristas.getY());
			if (!vertice.getCoordenadas().equals(coordAux)) {
				v_adyacentes[ind] = vertices.get(coordAux); ind++;
			}
			coordAux = new Coordenadas(coordAristas.getFin_x() , coordAristas.getFin_y());
			if (!vertice.getCoordenadas().equals(coordAux)) {
				v_adyacentes[ind] = vertices.get(coordAux); ind++;
			}
		}
		
		return v_adyacentes;
	}
	
	public static void construirPrimerAsentamiento (Vertices v, Jugadores j) {
		// Antes de comprobar si se puede construir vamos a comprobar que el vertice no tiene 
		// ning�n asentamiento construido.
		System.out.println("Vamos a intentar construir en " + v.getIdentificador());
		if (!v.tieneAsentamiento()) {
			// Comprobamos que no hay ningun asentamiento construido en los vertices adyacentes.
			Vertices vAdyacentes[] = getVerticesAdyacentes(v.getIdentificador());
			Boolean existeAsentamiento = false;
			for (int i = 0; i < vAdyacentes.length && !existeAsentamiento; ++i) {
				if (vAdyacentes[i] != null) {
					if (vAdyacentes[i].tieneAsentamiento()) {
						existeAsentamiento = true;
					}
				}
			}
			
			if (!existeAsentamiento) {
				v.construirPrimerAsentamiento(j);
				
				Aristas posiblesCamino[] = aristasDelVertice(v);
				
				System.out.println("Aristas del vertice:");
				System.out.println("\t" + posiblesCamino[0].getIdentificador());
				System.out.println("\t" + posiblesCamino[1].getIdentificador());
				if (posiblesCamino.length == 3) {
					if (posiblesCamino[2] == null) System.out.println("\tNull");
					else System.out.println("\t" + posiblesCamino[2].getIdentificador());
				} else {
					System.out.println("\tNull");
				}
				
				for (int i = 0 ; i < posiblesCamino.length; ++i) {
					System.out.println("Posible camino en " + posiblesCamino[i].getIdentificador());
					aristasPorID.get(posiblesCamino[i].getIdentificador()).posibleCaminoDeJugador(j.getColor().numeroColor());
					//posiblesCamino[i].posibleCaminoDeJugador(j.getColor().numeroColor());
					System.out.println("\t" + posiblesCamino[i].getPosibleCaminoDeJugador(j.getColor().numeroColor()));
				}
			}
		}
	}
	
	public static void construirPrimerCamino (Aristas a, Jugadores j) {
		Boolean sePuedeConstruir = false;
		Vertices v1 = vertices.get(a.getCoordenadasVertice1());
		Vertices v2 = vertices.get(a.getCoordenadasVertice2());
		
		Aristas aristasAdyacentesAv1[] = aristasDelVertice(v1);
		Aristas aristasAdyacentesAv2[] = aristasDelVertice(v2);
		
		// Solo se puede construir en una arista si esta no tiene un camino ya construido
		if (!a.tieneCamino()) {
			// Para que se pueda construir hay que comprobar que si alguno de los dos vertices que
			// forman la arista tienen construido una asentamiento o hay un camino construido en 
			// las aristas adyacentes.
			
			// V1 tiene asentamiento del jugador
			if ( (v1.tieneAsentamiento() && v1.getPropietario().equals(j) ) || 
					( v2.tieneAsentamiento() && v2.getPropietario().equals(j) ) ){
				// No podemos marcar ningun vertice como posible asentamiento ya que no cumplir�a 
				// la regla de la distancia
				Boolean resul = a.setCamino(j);
				//System.out.println(resul);
				// Solo podemos marcar aristas adyacentes como camino
				for (int i = 0; i < aristasAdyacentesAv1.length; ++i) {
					//System.out.println(aristasAdyacentesAv1[i].getIdentificador());
					aristasAdyacentesAv1[i].posibleCaminoDeJugador(j.getColor().numeroColor());
				}
				for (int i = 0; i < aristasAdyacentesAv2.length; ++i) {
					//System.out.println(aristasAdyacentesAv2[i].getIdentificador());
					aristasAdyacentesAv2[i].posibleCaminoDeJugador(j.getColor().numeroColor());
				}
				
			}
			else if (!v1.tieneAsentamiento() && !v2.tieneAsentamiento()){
				// Existe camino en alguno de las aristas adyacentes a v1 y esta arista no es a.
				for (int i = 0; i < aristasAdyacentesAv1.length && !sePuedeConstruir; ++i) {
					if (!aristasAdyacentesAv1[i].equals(a) && aristasAdyacentesAv1[i].tieneCamino() && 
							j.equals(aristasAdyacentesAv1[i].getPropietario())) {
						sePuedeConstruir = true;
					}
				}
				
				for (int i = 0; i < aristasAdyacentesAv2.length && !sePuedeConstruir; ++i) {
					if (!aristasAdyacentesAv2[i].equals(a) && aristasAdyacentesAv2[i].tieneCamino() && 
							j.equals(aristasAdyacentesAv2[i].getPropietario())) {
						sePuedeConstruir = true;
					}
				}
				
				if (sePuedeConstruir) {
					// Construir
					a.setCamino(j);
					Vertices verticesAdyacentesV1[] = Hexagonos.getVerticesAdyacentes(v1);
					Vertices verticesAdyacentesV2[] = Hexagonos.getVerticesAdyacentes(v2);
					// Posibles Caminos.
					for (int i = 0; i < aristasAdyacentesAv1.length; ++i) {
						aristasAdyacentesAv1[i].posibleCaminoDeJugador(j.getColor().numeroColor());
					}
					for (int i = 0; i < aristasAdyacentesAv2.length; ++i) {
						aristasAdyacentesAv2[i].posibleCaminoDeJugador(j.getColor().numeroColor());
					}
					// Posibles Asentamientos. --> Respeta la regla de la distancia.
					for (int i = 0; i < verticesAdyacentesV1.length; ++i) {
						if (!verticesAdyacentesV1[i].equals(v2) && !verticesAdyacentesV1[i].tieneAsentamiento()) 
							v1.posibleAsentamientoDeJugador(j);
					}
					for (int i = 0; i < verticesAdyacentesV2.length; ++i) {
						if (!verticesAdyacentesV2[i].equals(v1) && !verticesAdyacentesV2[i].tieneAsentamiento()) 
							v2.posibleAsentamientoDeJugador(j);
					}
				}
			}
		}
	}
	
	public static Aristas[] getTodasAristas () {
		Collection<Aristas> collect = aristas.values();
		Object o[] = collect.toArray();
		return Arrays.copyOf(o, o.length, Aristas[].class);
	}
	
	public static Vertices[] getTodosVertices () {
		Collection<Vertices> collect = vertices.values();
		Object o[] = collect.toArray();
		return Arrays.copyOf(o, o.length, Vertices[].class);
	}
}
