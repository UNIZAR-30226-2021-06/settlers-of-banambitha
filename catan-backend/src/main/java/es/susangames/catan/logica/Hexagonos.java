package es.susangames.catan.logica;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Hexagonos {
	static private int tam = 1;
	static private int NUM_VERTICES_ARISTAS = 6;
	
	private Coordenadas centro;
	private Vertices v[];
	private Aristas a[];
	
	private Boolean tieneLadron;
	
	private TipoTerreno tipo_terreno;
	private int valor;
	
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
	
	Hexagonos (Coordenadas c, TipoTerreno tipo_terreno, int valor) {
		this.centro = c;
		this.tipo_terreno = tipo_terreno;
		this.valor = valor;
		this.tieneLadron = false;
		if(tipo_terreno.esDesierto()) {
			this.tieneLadron = true;
		}
		
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
					a[i-1] = new Aristas(auxCoordAristas, next_id_arista);
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
					a[i] = new Aristas(auxCoordAristas,next_id_arista);
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
					a[i-1] = new Aristas(auxCoordAristas,next_id_arista);
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
	public int getValor () {
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
		return this.centro.getDistancia(h.getCentro()) <= 2*calcularApotema();
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
		return centro.getDistancia(v.getCoordenadas()) <= tam;
	}
	
	/*
	 * Devuelve el valor de la variable booleana tieneLadron
	 * */
	public Boolean tieneLadron() {
		return this.tieneLadron;
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
	
	public static Boolean sePuedeConstruirAsentamiento(Coordenadas c, Jugadores j) {
		if (existeCoordenada(c)) {
			// Comprobar se existe carretera del jugador cercana.
			// Comprobar que se cumple la regla de la distancia.
		}
		return false;
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
	
	private Aristas[] aristasDelVertice (Vertices v) {
		Aristas a[] = null;
		Coordenadas c = v.getCoordenadas();
		// Se trata de un vertice que pertenece a un Hexagono.
		if (vertices.containsKey(v.getCoordenadas())) {
			 a= (Aristas[]) aristas.entrySet().stream()
					.filter(x -> x.getKey().contieneCoordenada(c))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).values().toArray();
		}
		return a;
	}
	
	public void construirAsentamiento (Vertices v, Jugadores j) {
		CoordenadasAristas coordAristas;
		Coordenadas coordAux;		
		Vertices v_adyacentes[] = new Vertices[3];
		int ind = 0;
		if (j.puedeConstruirPueblo() && !v.tieneAsentamiento()) {
			if (vertices.containsKey(v.getCoordenadas())) {
				Aristas a[] = aristasDelVertice(v);
				for (int i = 0; i < a.length; ++i) {
					coordAristas = a[i].getCoordenadasAristas();
					coordAux = new Coordenadas(coordAristas.getX() , coordAristas.getY());
					if (!v.getCoordenadas().equals(coordAux)) {
						v_adyacentes[ind] = vertices.get(coordAux); ind++;
					}
					coordAux = new Coordenadas(coordAristas.getFin_x() , coordAristas.getFin_y());
					if (!v.getCoordenadas().equals(coordAux)) {
						v_adyacentes[ind] = vertices.get(coordAux); ind++;
					}
				}
				// Se ha calculado aristas y vertices adyacentes.
				// Para cada una de las carreteras hay que mirar si hay una carretera aliada,
				// si la hay, hay que comprobar que el vertice adyacente esta vacio.
				
				if (a[0].tieneCamino() && a[0].getPropietario().equals(j)) {
					if (!v_adyacentes[0].tieneAsentamiento()) {
						v_adyacentes[0].construirAsentamiento(j);
						j.construirAsentamiento();
					}
				} else if (a[1].tieneCamino() && a[1].getPropietario().equals(j)) {
					if (!v_adyacentes[1].tieneAsentamiento()) {
						v_adyacentes[1].construirAsentamiento(j);
						j.construirAsentamiento();
					}
				} else { // a[2].tieneCarretera() && a[2].getPropietario().equals(j)
					if (!v_adyacentes[2].tieneAsentamiento()) {
						v_adyacentes[2].construirAsentamiento(j);
						j.construirAsentamiento();
					}
				}
				
				Aristas posiblesCarreteras[] = aristasDelVertice(v);
				
				for (int i = 0 ; i < posiblesCarreteras.length; ++i) {
					posiblesCarreteras[i].posibleCarreteraDeJugador(j);
				}
			}
		}
	}
	
	public void construirCarretera (Aristas a, Jugadores j) {
		Boolean sePuedeConstruir = false;
		Vertices v1 = vertices.get(a.getCoordenadasVertice1());
		Vertices v2 = vertices.get(a.getCoordenadasVertice2());
		
		Aristas aristasAdyacentesAv1[];
		Aristas aristasAdyacentesAv2[];
		
		if (j.puedeConstruirCarretera() && !a.tieneCamino()) {
			// Buscar asentamiento del jugador j en uno de los vertices.
			
			if (v1.tieneAsentamiento()) {
				if (v1.getPropietario().equals(j))
					sePuedeConstruir = true;
			} else {
				// Se comprueba que las aristas adyacentes tengan al menos un camino del jugador.
				aristasAdyacentesAv1 = aristasDelVertice(v1);
				for (int i = 0; i < aristasAdyacentesAv1.length && !sePuedeConstruir; ++i ) {
					if (aristasAdyacentesAv1[i].getPropietario().equals(j))
						sePuedeConstruir = true;
				}
			}
			
			if (!sePuedeConstruir) {
				if (v2.tieneAsentamiento()) {
					if (v2.getPropietario().equals(j) ) {
						sePuedeConstruir = true;
					}
				} else {
					// Se comprueba que las aristas adyacentes tengan al menos un camino del jugador.
					aristasAdyacentesAv2 = aristasDelVertice(v2);
					for (int i = 0; i < aristasAdyacentesAv2.length && !sePuedeConstruir; ++i ) {
						if (aristasAdyacentesAv2[i].getPropietario().equals(j))
							sePuedeConstruir = true;
					}
				}
			}
		}
		
		if (sePuedeConstruir) {
			// Construir camino.
			a.setCarretera(j);
			
			// Posibles nuevo caminos.
			aristasAdyacentesAv1 = aristasDelVertice(v1);
			for (int i = 0; i < aristasAdyacentesAv1.length; ++i ) {
				if (!aristasAdyacentesAv1[i].tieneCamino())
					aristasAdyacentesAv1[i].posibleCarreteraDeJugador(j);
			}
			
			aristasAdyacentesAv2 = aristasDelVertice(v2);
			for (int i = 0; i < aristasAdyacentesAv1.length; ++i ) {
				if (!aristasAdyacentesAv2[i].tieneCamino())
					aristasAdyacentesAv2[i].posibleCarreteraDeJugador(j);
			}
			
			// Actualizar posibles asentamientos.
			// Comprobar que v1 y v2 estan vacios.
				// comprobar que al menos uno de los vertices adyacentes a v1 y v2 tiene un 
				// asentamiento del jugador j.
			Boolean puedeConstruirseAsentamiento = false;
			if (!v1.tieneAsentamiento()) {
				Vertices v1Adyacentes[] = getVerticesAdyacentes(v1);
				for (int i = 0; i < v1Adyacentes.length; ++i) {
					puedeConstruirseAsentamiento |= v1Adyacentes[i].tieneAsentamiento() 
							&& v1Adyacentes[i].getPropietario().equals(j);
				}
				
				if (puedeConstruirseAsentamiento) v1.posibleAsentamientoDeJugador(j);
			}
			
			puedeConstruirseAsentamiento = false;
			if (!v2.tieneAsentamiento()) {
				Vertices v2Adyacentes[] = getVerticesAdyacentes(v2);
				for (int i = 0; i < v2Adyacentes.length; ++i) {
					puedeConstruirseAsentamiento |= v2Adyacentes[i].tieneAsentamiento() 
							&& v2Adyacentes[i].getPropietario().equals(j);
				}
				
				if (puedeConstruirseAsentamiento) v2.posibleAsentamientoDeJugador(j);
			}
		}
	}
	
	//
	public void mejorarAsentamiento (Vertices v, Jugadores j) {
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

	/*
	public JSONArray listAsentamientoToJSON () {}
	
	public JSONArray posibleAsentamientoToJSON () {}
	
	public JSONArray listCaminoToJSON () {}
	
	public JSONArray posibleCaminoToJSON () {}*/
	
	public static Vertices getVerticePorId (int id) {
		return verticesPorID.get(id);
	}
	
	public static Aristas getAristaPorId (int id) {
		return aristasPorID.get(id);
	}
	
	public Vertices[] getVerticesAdyacentes (int id) {
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
	
	public Vertices[] getVerticesAdyacentes (Vertices vertice) {
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
}
