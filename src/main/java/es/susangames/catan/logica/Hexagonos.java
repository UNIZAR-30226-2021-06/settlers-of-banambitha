package es.susangames.catan.logica;


public class Hexagonos {
	private int tam = 1;
	private int NUM_VERTICES_ARISTAS = 6;
	
	private Coordenadas centro;
	private Vertices v[];
	private Aristas a[];
	
	private Boolean tieneLadron;
	
	private TipoTerreno tipo_terreno;
	private int valor;

	private int id;
	
	Hexagonos (Coordenadas c, TipoTerreno tipo_terreno, int valor, int id) {
		this.centro = c;
		this.tipo_terreno = tipo_terreno;
		this.valor = valor;
		this.tieneLadron = false;
		this.id = id;
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
			v[i] = new Vertices(aux);
			
			if ( i == 0 ) {v1 = aux; aux2 = aux;}
			else if ( i == (NUM_VERTICES_ARISTAS - 1) ) {
				auxCoordAristas = new CoordenadasAristas(aux.getX(), aux.getY(),aux2.getX(), aux2.getY());
				a[i-1] = new Aristas(auxCoordAristas);
				
				auxCoordAristas = new CoordenadasAristas(aux.getX(), aux.getY(),v1.getX(), v1.getY());
				a[i] = new Aristas(auxCoordAristas);
			} else {
				auxCoordAristas = new CoordenadasAristas(aux.getX(), aux.getY(),aux2.getX(), aux2.getY());
				a[i-1] = new Aristas(auxCoordAristas);
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
	
	public int getIdentificador () {
		return this.id;
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
			//System.out.println(this.v[i].getIdentificador() + " == " + v.getIdentificador());
			if (this.v[i].getIdentificador() == v.getIdentificador()) encontrado = true;
		}
		
		return encontrado;
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
	 * Este hexagono produce materiales, es decir, entrega una unidad de material del hexagono a 
	 * los jugadores que tengan un asentamiento. 
	 * */
	public void producir() {
		if (!tieneLadron()) {
			for (int i = 0 ; i < this.v.length; i++) {
				System.out.println("Vertice: " + v[i].getIdentificador());
				this.v[i].producir(this.tipo_terreno);
			}
		}
	}
	
	public TipoTerreno getTipo_terreno() {
		return tipo_terreno;
	}

	public void setTipo_terreno(TipoTerreno tipo_terreno) {
		this.tipo_terreno = tipo_terreno;
	}
}

