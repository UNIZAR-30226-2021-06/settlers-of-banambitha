package es.susangames.catan.logica;

import java.util.Map;
import java.util.stream.Collectors;

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
	private Integer NUM_HEXAGONOS = 19;
	
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
	private int ultimaJugada; 
	
	int turno;
	int dados;
	
	public Tablero () {
		this.num_jugadores = 4;
		j = new Jugadores[num_jugadores];
		this.turno_jugador = 0;
		this.haComerciado = false;
		this.exit_status = -1;
		this.message = "";
		this.seHaMovidoLadron = false;
		
		j[0] = new Jugadores(ColorJugador.Azul);
		j[1] = new Jugadores(ColorJugador.Rojo);
		j[2] = new Jugadores(ColorJugador.Amarillo);
		j[3] = new Jugadores(ColorJugador.Verde);
		
		turno = 0; dados = 0; ultimaJugada = 0;
		
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
		
		generarVerticesyAristas ();
		
		generarPuertos ();

		//mostrarHexagonos();
	}
	
	//----------------------------- Generar elementos tablero -----------------------------------\\

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
					hexagonos.put(id, new Hexagonos(aux, this.vector_terrenos[id], -1, id));
				} else {
					hexagonos.put(id, new Hexagonos(aux, this.vector_terrenos[id], 
							this.vector_valores[count_value],id));
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
		Collection<Aristas> aristasExteriores = getAristasExteriores();
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
			eliminarPuerto(a);
		}
	}
	
	public Hexagonos getHexagono (int identificador) {
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
	public Boolean existeHexagono (int id) {
		return hexagonos.containsKey(id);
	}
	
	/**
     * Método que devuelve el hexagono donde se encuentra el ladron
     * @return el hexagono donde se encuentra el ladron
     */
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
	
	/**
     * Método que mueve el ladron al hexagono indicado
     */
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
	
	/**
     * Método que entrega los recursos producidos por las ciudades y pueblos de los hexagonos activados a los jugadores propietarios de estos
     */
	public void producir (Integer valor) {	
		for (Hexagonos hex : hexagonos.values()) {
			if (hex.getValor() == valor) hex.producir();
		}
	}
	
	/**
     * Método que imprime informacion del tablero por pantalla
     */
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
		System.out.println("Numero de vertices: " + num_vertices());
		System.out.println("Numero de aristas: " + num_aristas());
		System.out.println("Numero de puertos: " + num_puertos());
	}
	
	/*
	 * Genera un numero aleatorio n del 1 al 12. Simula la accion de lanzar un dado.
	 * @return Devuelve n, siendo n un entero 1 <= n <= 12.
	 * */
	private int generarNumero () {
		return (int) Math.floor( Math.random()*11 + 1 );
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

	//------------------------------ Vertices y Aristas -----------------------------------------\\

	private Map<Coordenadas, Vertices> vertices = new HashMap<Coordenadas, Vertices>();
	private Map<Integer, Vertices> verticesPorID = new HashMap<Integer, Vertices>();
	private int next_id_vertice = 0;
	private Map<CoordenadasAristas, Aristas> aristas = new HashMap<CoordenadasAristas, Aristas>();
	private Map<Integer, Aristas> aristasPorID = new HashMap<Integer, Aristas>();
	private int next_id_arista = 0;
	private Map<CoordenadasAristas, Aristas> puertos = new HashMap<CoordenadasAristas, Aristas>();

	private int NUM_VERTICES_ARISTAS = 6;

	private void generarVerticesyAristas () {
		// Leer de cada uno de los vertices y aristas de un hexagono.
		Vertices verticesDelHexagono[];
		Aristas aristasDelHexagono[];
		
		int id_vertice = 0;
		int id_aristas = 0;

		for (Hexagonos h : hexagonos.values()) {
			verticesDelHexagono = h.getVertices();
			aristasDelHexagono = h.getAristas();

			for (int i = 0 ; i < NUM_VERTICES_ARISTAS ; ++i) {
				if (!vertices.containsKey(verticesDelHexagono[i].getCoordenadas())) {
					vertices.put(verticesDelHexagono[i].getCoordenadas(), verticesDelHexagono[i]);
					verticesDelHexagono[i].setIdentificador(id_vertice);
					verticesPorID.put(id_vertice, verticesDelHexagono[i]);
					id_vertice++;
				} else {
					Vertices aux = vertices.get(verticesDelHexagono[i].getCoordenadas());
					verticesDelHexagono[i] = aux;
				}

				if (aristas.containsKey(aristasDelHexagono[i].getCoordenadasAristas())) {
					Aristas aux = aristas.get(aristasDelHexagono[i].getCoordenadasAristas());
					aristasDelHexagono[i] = aux;
					puertos.remove(aristasDelHexagono[i].getCoordenadasAristas());
				} else {
					aristas.put(aristasDelHexagono[i].getCoordenadasAristas(), 
						aristasDelHexagono[i]);
					puertos.put(aristasDelHexagono[i].getCoordenadasAristas(), 
						aristasDelHexagono[i]);
					aristasDelHexagono[i].setIdentificador(id_aristas);
					aristasPorID.put(id_aristas, aristasDelHexagono[i]);
					id_aristas++;
				}
			}
		}
	}
	
	public Aristas[] getTodasAristas () {
		Collection<Aristas> collect = aristas.values();
		Object o[] = collect.toArray();
		return Arrays.copyOf(o, o.length, Aristas[].class);
	}
	
	public Vertices[] getTodosVertices () {
		Collection<Vertices> collect = vertices.values();
		Object o[] = collect.toArray();
		return Arrays.copyOf(o, o.length, Vertices[].class);
	}

	private void eliminarPuerto (Aristas p) {
		puertos.remove(p.getCoordenadasAristas());
	}

	public Vertices getVerticePorId (int id) {
		return this.verticesPorID.get(id);
	}

	public Aristas getAristaPorId (int id) {
		return this.aristasPorID.get(id);
	}

	public Jugadores[] getJugadores () {
		return this.j;
	}
	
	/**
	 * Dado un identificador id, devuelve true si corresponde con uno de los vertices existentes
	 * en el tablero.
	 * @param id entero que identifica a un vertice
	 * @return true si y solo si existe un vertice cuyo identificador sea id
	 */
	public Boolean existeVertice (int id) {
		return this.verticesPorID.containsKey(id);
	}

	/**
	 * Dado un identificador id, devuelve true si corresponde con una de las aristas existentes
	 * en el tablero.
	 * @param id entero que identifica a una arista.
	 * @return true si y solo si existe una aristas cuyo identificador sea id.
	 */
	public Boolean existeArista (int id) {
		return this.aristasPorID.containsKey(id);
	}

	private Boolean existeCoordenada (Coordenadas c) {
		return vertices.containsKey(c);
	}

	private Boolean existeCoordenadaArista (CoordenadasAristas c) {
		return aristas.containsKey(c);
	}

	public Vertices[] getVerticesAdyacentes (int id_vertice) {
		CoordenadasAristas coordAristas;
		Coordenadas coordAux;
		Vertices v_adyacentes[] = new Vertices[3];
		int ind = 0;
		
		Vertices vertice = getVerticePorId (id_vertice);
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

	public int num_vertices () {
		return this.vertices.size();
	}

	public int num_aristas () {
		return this.aristas.size();
	}

	public int num_puertos ()  {
		return this.puertos.size();
	}

	public int num_puertos_basicos () {
		int count = 0;
		for (Aristas p : puertos.values()) {
			if (p.getPuerto().esBasico()) count++;
		}
		return count;
	}
	
	public int num_puertos_madera () {
		int count = 0;
		for (Aristas p : puertos.values()) {
			if (p.getPuerto().esPuertoMadera()) count++;
		}
		return count;
	}
	
	public int num_puertos_lana () {
		int count = 0;
		for (Aristas p : puertos.values()) {
			if (p.getPuerto().esPuertoLana()) count++;
		}
		return count;
	}
	
	public int num_puertos_cereales () {
		int count = 0;
		for (Aristas p : puertos.values()) {
			if (p.getPuerto().esPuertoCereales()) count++;
		}
		return count;
	}
	
	public int num_puertos_arcilla () {
		int count = 0;
		for (Aristas p : puertos.values()) {
			if (p.getPuerto().esPuertoArcilla()) count++;
		}
		return count;
	}
	
	public int num_puertos_mineral () {
		int count = 0;
		for (Aristas p : puertos.values()) {
			if (p.getPuerto().esPuertoMineral()) count++;
		}
		return count;
	}
	
	public Collection<Aristas> getAristasExteriores () {
		return puertos.values();
	}

	public  Aristas[] aristasDelVertice (Vertices v) {
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

	//-------------------------------------------------------------------------------------------\\

	//---------------------------------- Construccion -------------------------------------------\\

	public void construirAsentamiento (Vertices v, Jugadores j) {
		Vertices verticesAdyacentes[] = getVerticesAdyacentes(v.getIdentificador());
		Aristas aristasAdyacentes[] = aristasDelVertice(v);
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

	public void construirPrimerAsentamiento (Vertices v, Jugadores j) {
		// Antes de comprobar si se puede construir vamos a comprobar que el vertice no tiene 
		// ning�n asentamiento construido.
		//System.out.println("Vamos a intentar construir en " + v.getIdentificador());
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
				
				//System.out.println("Aristas del vertice:");
				//System.out.println("\t" + posiblesCamino[0].getIdentificador());
				//System.out.println("\t" + posiblesCamino[1].getIdentificador());
				/*if (posiblesCamino.length == 3) {
					if (posiblesCamino[2] == null) System.out.println("\tNull");
					else System.out.println("\t" + posiblesCamino[2].getIdentificador());
				} else {
					System.out.println("\tNull");
				}*/
				
				for (int i = 0 ; i < posiblesCamino.length; ++i) {
					//System.out.println("Posible camino en " + posiblesCamino[i].getIdentificador());
					aristasPorID.get(posiblesCamino[i].getIdentificador()).posibleCaminoDeJugador(j.getColor().numeroColor());
					//posiblesCamino[i].posibleCaminoDeJugador(j.getColor().numeroColor());
					//System.out.println("\t" + posiblesCamino[i].getPosibleCaminoDeJugador(j.getColor().numeroColor()));
				}
			}
		}
	}

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

	public void construirCamino (Aristas a, Jugadores j) {
		if (j.puedeConstruirCamino()) {
			construirPrimerCamino(a,j);
		}
	}

	public void construirPrimerCamino (Aristas a, Jugadores j) {
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
					Vertices verticesAdyacentesV1[] = getVerticesAdyacentes(v1.getIdentificador());
					Vertices verticesAdyacentesV2[] = getVerticesAdyacentes(v2.getIdentificador());
					// Posibles Caminos.
					for (int i = 0; i < aristasAdyacentesAv1.length; ++i) {
						aristasAdyacentesAv1[i].posibleCaminoDeJugador(j.getColor().numeroColor());
					}
					for (int i = 0; i < aristasAdyacentesAv2.length; ++i) {
						aristasAdyacentesAv2[i].posibleCaminoDeJugador(j.getColor().numeroColor());
					}
					// Posibles Asentamientos. --> Respeta la regla de la distancia.
					for (int i = 0; i < verticesAdyacentesV1.length; ++i) {
						if (verticesAdyacentesV1[i] != null) {
							if (!verticesAdyacentesV1[i].equals(v2) && !verticesAdyacentesV1[i].tieneAsentamiento()) 
								v1.posibleAsentamientoDeJugador(j);
						}
						
					}
					for (int i = 0; i < verticesAdyacentesV2.length; ++i) {
						if (verticesAdyacentesV2 != null) {
							if (!verticesAdyacentesV2[i].equals(v1) && !verticesAdyacentesV2[i].tieneAsentamiento()) 
								v2.posibleAsentamientoDeJugador(j);
						}
					}
				}
			}
		}
	}
	
	private Boolean exitenConstruccionesAdyacentes (Vertices v) {
		Vertices vAdyacentes[] = this.getVerticesAdyacentes(v.getIdentificador());
		Boolean result = false;
		for (int i = 0; i < vAdyacentes.length && !result; ++i) {
			if (vAdyacentes[i] != null) {
				result = vAdyacentes[i].tieneAsentamiento();
			}
		}
		
		return result;
	}
	
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
	
	public void usarCartasConstruccionCarretera(Hexagonos hexagono, Aristas arista, Jugadores j) {
		if (j.hayCartasContruccionCarreteras()){
			j.usarCartasConstruccionCarretera();
			//TODO: arreglar este trozo de código
			//hexagono.construirCarretera(arista, j);
		}
	}
	//-------------------------------------------------------------------------------------------\\
	
	//---------------------------------- Comercio -----------------------------------------------\\
	private void actualizarMaterial (Jugadores j, String material, int numMaterial) {
		switch (material) {
		case "madera" : 
			j.setMadera( j.getMadera() + numMaterial);
			break;
		case "lana" : 
			j.setLana( j.getLana() + numMaterial );
			break;
		case "cereal" :
			j.setCereales( j.getCereales() + numMaterial);
			break;
		case "arcilla" :
			j.setArcilla( j.getArcilla() + numMaterial );
			break;
		case "mineral" : 
			j.setMineral( j.getMineral() + numMaterial );
			break;
		}
	}
	
	public void comerciar(Jugadores j1, String materialJ1, int numMaterialJ1, 
			Jugadores j2, String materialJ2, int numMaterialJ2) {
		
		actualizarMaterial(j1,materialJ1, -numMaterialJ1);
		actualizarMaterial(j1,materialJ2, numMaterialJ2);
		
		actualizarMaterial(j2,materialJ1, numMaterialJ1);
		actualizarMaterial(j2,materialJ2, -numMaterialJ1);
	}
	
	public void comercioMaritimo ( int materialEsperado, Jugadores j1, String materialRecibe, int madera, 
			int lana, int cereales, int arcilla, int mineral) {
		int totalMaterial = madera + lana +  cereales + arcilla + mineral; 
		if (materialEsperado < totalMaterial) {
			this.message = "El material ofrecido es mayor al esperado";
			this.exit_status = 28;
		} else if (materialEsperado > totalMaterial) {
			this.message = "El material ofrecido es menor al esperado";
			this.exit_status = 29;
		} else {
			if (madera > j1.getMadera() || lana > j1.getLana() || cereales > j1.getCereales()
					|| arcilla > j1.getArcilla() || mineral > j1.getMineral()) {
				this.message = "El jugador no dispone de los materiales requeridos";
				this.exit_status = 30;
			} else {
				j1.setMadera( j1.getMadera() - madera);
				j1.setLana( j1.getLana() - lana );
				j1.setCereales( j1.getCereales() - cereales );
				j1.setArcilla( j1.getArcilla() - arcilla);
				j1.setMineral( j1.getMineral() - mineral );
				
				this.actualizarMaterial(j1, materialRecibe, 1);
				this.message = "Se ha realizado el comercio maritimo correctamente.";
				this.exit_status = 0;
			}
			
		}
	}
	
	public void comercioEnPuertoEspecial (Aristas puerto, Jugadores j1, String materialRecibe, int madera, 
			int lana, int cereales, int arcilla, int mineral) {
		if (puerto.getTipoPuerto().esPuertoMadera()) {
			if (materialRecibe == "madera") {
				this.comercioMaritimo(2, j1, materialRecibe, madera, lana, cereales, arcilla, mineral);
			} else {
				this.comercioMaritimo(3, j1, materialRecibe, madera, lana, cereales, arcilla, mineral);
			}
		}
		else if (puerto.getTipoPuerto().esPuertoLana()) {
			if (materialRecibe == "lana") {
				this.comercioMaritimo(2, j1, materialRecibe, madera, lana, cereales, arcilla, mineral);
			} else {
				this.comercioMaritimo(3, j1, materialRecibe, madera, lana, cereales, arcilla, mineral);
			}
		}
		else if (puerto.getTipoPuerto().esPuertoCereales()) {
			if (materialRecibe == "cereales") {
				this.comercioMaritimo(2, j1, materialRecibe, madera, lana, cereales, arcilla, mineral);
			} else {
				this.comercioMaritimo(3, j1, materialRecibe, madera, lana, cereales, arcilla, mineral);
			}
		}
		else if (puerto.getTipoPuerto().esPuertoArcilla()) {
			if (materialRecibe == "arcilla") {
				this.comercioMaritimo(2, j1, materialRecibe, madera, lana, cereales, arcilla, mineral);
			} else {
				this.comercioMaritimo(3, j1, materialRecibe, madera, lana, cereales, arcilla, mineral);
			}
		} else {
			// Puerto mineral
			if (materialRecibe == "mineral") {
				this.comercioMaritimo(2, j1, materialRecibe, madera, lana, cereales, arcilla, mineral);
			} else {
				this.comercioMaritimo(3, j1, materialRecibe, madera, lana, cereales, arcilla, mineral);
			}
		}
	}
	
	//----------------------------------- JSON -----------------------------------\\
	
	/**
     * Método que devuelve los tipos de terreno de los hexagonos
     * @return los tipos de terreno de los hexagonos
     */
	public JSONArray tiposHexagonosToJSONArray () {
		String tipoTerreno[] = new String [19];

		int id = 0;
		for (Hexagonos h : hexagonos.values()) {
			id = h.getIdentificador();
			tipoTerreno[id] = h.getTipo_terreno().toString();
		}
		
		return new JSONArray(tipoTerreno);
	}
	
	/**
     * Método que devuelve los valores de los hexagonos
     * @return los valores de los hexagonos
     */
	private JSONArray valorHexagonosToJSONArray () {
		JSONArray jsArray = new JSONArray ();
		
		Integer valorHexagono[] = new Integer [19];

		int id = 0;
		for (Hexagonos h : hexagonos.values()) {
			id = h.getIdentificador();
			valorHexagono[id] = h.getValor();
		}
		
		return new JSONArray(valorHexagono);
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
	private JSONObject infoHexagonosJSON () throws JSONException {
		JSONObject jsObj =  new JSONObject();
		jsObj.put("tipo", this.tiposHexagonosToJSONArray());
		jsObj.put("valor", this.valorHexagonosToJSONArray());
		jsObj.put("ladron", this.getPosicionLadron().getIdentificador());

		return jsObj;
	}
	
	private String message;
	private int exit_status;
	private int turno_jugador;
	private Boolean haComerciado;
	private Boolean seHaMovidoLadron;

	/*
	 * Genera un JSONObject con la informacion de la partida
	 * @return JSONObject con la informacion de la partida
	 * TODO: hacer que esta función devuelva siempre el estado actual del tablero
	 * */
	public JSONObject returnMessage () throws JSONException {
		JSONObject respuesta = new JSONObject ();
		
		respuesta.put("Message", this.message);
		respuesta.put("Turno_Jugador", this.turno_jugador);
		respuesta.put("exit_status", this.exit_status);

		JSONObject Tab_inf = new JSONObject();
		Tab_inf.put("hexagono", infoHexagonosJSON());
		
		JSONObject vertices = new JSONObject();
		vertices.put("asentamiento" , listAsentamientoToJSON());
		vertices.put("posibles_asentamiento" , posibleAsentamientoToJSON());
		
		JSONObject aristas = new JSONObject();
		aristas.put("camino" , listCaminoToJSON());
		aristas.put("posibles_camino" , posibleCaminoToJSON());
		aristas.put("puertos", puertosToJSON());
		
		Tab_inf.put("vertices", vertices);
		Tab_inf.put("aristas", aristas);
		
		respuesta.put("Tab_inf", Tab_inf);
		
		JSONObject primerosAsentamiento = new JSONObject ();
		primerosAsentamiento.put("Player_1", this.j[0].getPrimerosAsentamientosConstruidos());
		primerosAsentamiento.put("Player_2", this.j[1].getPrimerosAsentamientosConstruidos());
		primerosAsentamiento.put("Player_3", this.j[2].getPrimerosAsentamientosConstruidos());
		primerosAsentamiento.put("Player_4", this.j[3].getPrimerosAsentamientosConstruidos());
		respuesta.put("primerosAsentamiento", primerosAsentamiento);
		
		JSONObject primerosCaminos = new JSONObject ();
		primerosCaminos.put("Player_1", this.j[0].getPrimerosCaminosConstruidos());
		primerosCaminos.put("Player_2", this.j[1].getPrimerosCaminosConstruidos());
		primerosCaminos.put("Player_3", this.j[2].getPrimerosCaminosConstruidos());
		primerosCaminos.put("Player_4", this.j[3].getPrimerosCaminosConstruidos());
		respuesta.put("primerosCaminos", primerosCaminos);
		
		JSONObject recursos = new JSONObject();
		recursos.put("Player_1", this.j[0].recursosJugadorToJSON());
		recursos.put("Player_2", this.j[1].recursosJugadorToJSON());
		recursos.put("Player_3", this.j[2].recursosJugadorToJSON());
		recursos.put("Player_4", this.j[2].recursosJugadorToJSON());
		respuesta.put("Recursos", recursos);
		
		JSONObject cartas = new JSONObject();
		cartas.put("Player_1", this.j[0].cartasJugadorToJSON());
		cartas.put("Player_2", this.j[0].cartasJugadorToJSON());
		cartas.put("Player_3", this.j[0].cartasJugadorToJSON());
		cartas.put("Player_4", this.j[0].cartasJugadorToJSON());
		respuesta.put("Cartas", cartas);
		
		JSONObject puntuacion = new JSONObject();
		puntuacion.put("Player_1", this.j[0].getPuntosVictoria());
		puntuacion.put("Player_2", this.j[1].getPuntosVictoria());
		puntuacion.put("Player_3", this.j[2].getPuntosVictoria());
		puntuacion.put("Player_4", this.j[3].getPuntosVictoria());
		respuesta.put("Puntuacion", puntuacion);
		respuesta.put("Clock", ultimaJugada);
		
		respuesta.put("Resultado_Tirada", this.dados);
		respuesta.put("Turno", this.turno);
		respuesta.put("Clock", ultimaJugada);
		
		respuesta.put("Ganador",this.ganador());
		return respuesta;
	}
	
	/*
	 * Atiende todas las posibles jugadas del juegador, las ejecuta y actualiza la informacion de la partida.
	 * @return JSONObject con la informacion de la partida
	 * TODO: Acciones de cartas (construir y uso), actualizar Carta del camino más largo y ejercito de caballería, comercio
	 * y comercio maritimo. Posibles problemas con JSON de lista caminos y lista asentamientos.
	 * */
	public JSONObject JSONmessage ( JSONObject jsObject ) throws JSONException {
		
		Integer id_jugador = jsObject.getInt("player");
		Jugadores jug = this.j[id_jugador - 1];
		
		// Comprobar que jugador debería de realizar el movimiento
		if (!jug.getColor().numeroColor().equals(turno_jugador)) {
			this.message = "El jugador " + (jug.getColor().numeroColor() + 1) + " no puede realizar"
					+ " ninguna acción. Por favor, espere a su turno.";
			this.exit_status = -1;
			return this.returnMessage();
			
		}
		
		JSONObject move = jsObject.getJSONObject("move");
		
		switch ( move.getString("name") ) {
		case "construir poblado":
			// Jugador uede construir pueblo
			if ( jug.puedeConstruirPueblo() ) {
				int id_vertice = move.getInt("param");
				// Comprobar si el identificador del vertice corresponde a algún vertice
				if (existeVertice(id_vertice)) {
					Vertices v = getVerticePorId(id_vertice);
					if (v.tieneAsentamiento()) {
						if (v.getPosibleAsentamientoDeJugador(id_vertice)) {
							construirAsentamiento(v, jug);
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
				if (existeVertice(id_vertice)) {
					Vertices v = getVerticePorId(id_vertice);
					if ( v.tieneAsentamiento() ) {
						if (v.getPropietario().equals(jug)) {
							if (!v.tieneCiudad()) {
							mejorarAsentamiento(v, jug);
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
				if (existeArista(id_arista)) {
					Aristas a = getAristaPorId(id_arista);
					if (!a.tieneCamino()) {
						if (a.getPosibleCaminoDeJugador(id_jugador)) {
							construirCamino(a, jug);
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
					if (this.dados == 7) {
						if (!seHaMovidoLadron) {
							this.seHaMovidoLadron = true;
							posActualLadron.moverLadron();
							nuevaPosLadron.colocarLadron();
							message = "Se ha movido correctamente el ladrón de hexagono";
							exit_status = 0;
						} else {
							message = "[Error] El ladrón ya ha sido movido en este turno.";
							exit_status = 32;
						}
						
					} else {
						this.message = "[Error] No se puede mover al ladrón de hexagono ya que "
								+ "el valor del dado no es de 7";
						this.exit_status = 16;
					}
				}
				else {
					message = "[Error] No se puede mover el ladrón ya que el hexagono elegido no "
							+ "es adyacente al hexagono que contiene el ladron";
					exit_status = 15;
				}
				
			} else {
				this.message = "";
				message = "[Error] El identificador del hexagono introducido no se corresponde"
						+ "con ninguno existente";
						exit_status = 14;
			}
			break;
		case "primer asentamiento":
			System.out.println("Intentando construir un primer asentamiento");
			int id_vertice = move.getInt("param");
			// Comprobamos que el jugador ya haya construido sus dos primeros asentamientos.
			if (!jug.getPrimerosAsentamientosConstruidos()) {
				// Comprobamos si el identificador corresponde con uno de los vertices disponibles
				if (existeVertice(id_vertice)) {
					// Tiene asentamiento el vertice
					Vertices v = this.getVerticePorId(id_vertice);
					if (!v.tieneAsentamiento()) {
						// Compribar que no existan construcciones adyacentes
						if (!this.exitenConstruccionesAdyacentes(v)) {
							this.construirPrimerAsentamiento(v, jug);
							jug.construirPrimerAsentamiento();
							this.message = "Se ha construido un primer poblado del jugador correctamente";
							this.exit_status = 0;
						} else {
							this.message = "[Error] La posición del asentamiento no está "
									+ "disponible para el jugador ya que no cumple con los requsitos";
							this.exit_status = 20;
						}
					} else {
						this.message = "[Error] Ya existe un poblado construido en ese vertice";
						this.exit_status = 19;
					}
				} else {
					this.message = "[Error] El identificador del vertice introducido no se "
							+ "corresponde con ninguno existente";
					this.exit_status = 18;
				}
			} else {
				this.message = "[Error] El jugador ya ha construido sus dos primeros asentamientos";
				this.exit_status = 17;
			}
			break;
		case "primer camino":
			int id_arista = move.getInt("param");
			if (!jug.getPrimerosCaminosConstruidos()) {
				if (existeArista(id_arista)) {
					Aristas a = getAristaPorId(id_arista);
					if (!a.tieneCamino()) {
						if (a.getPosibleCaminoDeJugador(jug.getColor().numeroColor())) {
							construirPrimerCamino(a,jug);
							jug.construirPrimerCamino();
							this.message = "Se ha construido un primer camino del jugador correctamente";
							this.exit_status = 0;
						} else {
							this.message = "[Error] La posición del camino no está disponible para"
									+ " el jugador ya que no cumple con los requsitos";
							this.exit_status = 24;
						}
					} else {
						this.message = "[Error] Ya existe un camino construido en esa arista";
						this.exit_status = 23;
					}
				} else {
					this.message = "[Error] El identificador de la arista introducido no se "
							+ "corresponde con ninguno existente";
					this.exit_status = 22;
				}
			} else {
				this.message = "[Error] El jugador ya ha construido sus dos primeros asentamientos";
				this.exit_status = 21;
			}
			
			break;
		case "finalizar turno":
			turno++;
			this.message = "Se ha finalizado el turno correctamente";
			this.exit_status = 0;
			this.haComerciado = false;
			Boolean finalizadoPrimerasConstrucciones = 
					this.j[0].getPrimerosAsentamientosConstruidos() 
					&& this.j[1].getPrimerosAsentamientosConstruidos() 
					&& this.j[2].getPrimerosAsentamientosConstruidos()
					&& this.j[3].getPrimerosAsentamientosConstruidos();
			if (finalizadoPrimerasConstrucciones) {
				this.seHaMovidoLadron = false;
				this.dados = generarNumero();
				this.producir(dados);
			}
			break;
		case "comerciar":
			JSONArray jsArray = move.getJSONArray("param");
			if (!this.haComerciado) {
				int id_jugador2 = jsArray.getInt(0);
				Jugadores j2 = this.j[id_jugador2 - 1];
				String materialJ1 = jsArray.getString(1);
				int numMaterialJ1 = jsArray.getInt(2);
				String materialJ2 = jsArray.getString(3);
				int numMaterialJ2 = jsArray.getInt(4);
				
				this.comerciar(jug, materialJ1, numMaterialJ1, j2, materialJ2, numMaterialJ2);
				this.haComerciado = true;
			} else {
				this.message = "El jugador ya ha comerciado durante su turno";
				this.exit_status = 25;
			}
			break;
		case "comerciar con puerto":
			JSONObject comercioMaritimo = move.getJSONObject("param");
			int id_puerto = comercioMaritimo.getInt("id_puerto");
			if (existeArista(id_puerto)) {
				Aristas p = getAristaPorId(id_puerto);
				if (p.tienePuerto()) {
					// Vamos a comprobar si el jugador tiene un asentamiento en la arista (puerto)
					Vertices v1 = vertices.get(p.getCoordenadasVertice1());
					Vertices v2 = vertices.get(p.getCoordenadasVertice2());
					JSONObject materiales = comercioMaritimo.getJSONObject("materiales");
					int madera = materiales.getInt("madera");
					int lana = materiales.getInt("lana");
					int cereales = materiales.getInt("cereales");
					int arcilla = materiales.getInt("arcilla");
					int mineral = materiales.getInt("mineral");
					String material_que_recibe = comercioMaritimo.getString("material_que_recibe");
					if (v1.tieneAsentamiento()) {
						if (jug.equals(v1.getPropietario())) {
							// Comprobar si es especial o basico.
							if (p.getTipoPuerto().esBasico()) {
								// 3 materiales a uno
								this.comercioMaritimo(3, jug, material_que_recibe, madera, 
										lana, cereales, arcilla, mineral);
							} else {
								// materiales a uno en concreto
								this.comercioEnPuertoEspecial(p, jug, material_que_recibe, 
										madera, lana, cereales, arcilla, mineral);
							}
						} else {
							// 4 materiales por 1
							this.comercioMaritimo(4, jug, material_que_recibe, madera, 
									lana, cereales, arcilla, mineral);
						}
					} else if (v2.tieneAsentamiento()) {
						if (jug.equals(v1.getPropietario())) {
							// Comprobar si es especial o basico.
							if (p.getTipoPuerto().esBasico()) {
								// 3 materiales a uno
								this.comercioMaritimo(3, jug, material_que_recibe, madera, 
										lana, cereales, arcilla, mineral);
							} else {
								// materiales a uno en concreto
								this.comercioEnPuertoEspecial(p, jug, material_que_recibe, 
										madera, lana, cereales, arcilla, mineral);
							}
						} else {
							// 4 materiales por 1
							this.comercioMaritimo(5, jug, material_que_recibe, madera, 
									lana, cereales, arcilla, mineral);
						}
					} else {
						// No hay asentamiento --> comercio por 5 materiales a uno.
						this.comercioMaritimo(5, jug, material_que_recibe, madera, lana, cereales, arcilla, mineral);
					}
				} else {
					this.message = "La arista seleccionada no corresponde a ningún puerto";
					this.exit_status = 27;
				}
			} else {
				this.message = "El identificador de arista introducido no corresponde a ninguno "
						+ "de los disponibles";
				this.exit_status = 26;
			}
			break;
			// Cartas
		default:
			// No existe la accion solicitada.
			message = "La acción solicitada no se encuentra disponible";
			exit_status = -1;
		}
		
		// RESPUESTA GENERAL.
		ultimaJugada++;
		return this.returnMessage();
	}

	private String[] listAsentamientoToJSON () {
		Iterator<Vertices> it = vertices.values().iterator();
		Vertices aux;
		//JSONArray jsArray = new JSONArray();
		String listAsentamiento[] = new String[num_vertices()];
		int id;
		while (it.hasNext()) {
			aux = it.next();
			id = aux.getIdentificador();
			listAsentamiento[id] = aux.getAsentamientoJugador();
		}
		
		return listAsentamiento;
	}

	private JSONArray posibleAsentamientoToJSON () {
		Iterator<Vertices> it = vertices.values().iterator();
		Vertices vAux;
		JSONArray jsArray = new JSONArray();
		Boolean posiblesAsentamientos[][] = new Boolean[4][num_vertices()];
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

	private String[] listCaminoToJSON () {
		Iterator<Aristas> it = aristas.values().iterator();
		//JSONArray jsArray = new JSONArray();
		Aristas aux;
		String caminos[] = new String[num_aristas()];
		int id;
		while (it.hasNext()) {
			aux = it.next();
			id = aux.getIdentificador();
			caminos[id] = aux.getCaminoJugador();
		}
		
		return caminos;
	}

	private JSONArray posibleCaminoToJSON () {
		Iterator<Aristas> it = aristas.values().iterator();
		Aristas aAux;
		JSONArray jsArray = new JSONArray();
		Boolean posiblesCaminos[][] = new Boolean [4][num_aristas()];
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
	
	private JSONObject puertosToJSON () {
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

