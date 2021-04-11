package es.susangames.catan.logica;


import java.util.Map;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
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
	
	Tablero () {
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
		Collection<Aristas> aristasExteriores = hexagonos.get(0).getAristasExteriores();
		Object a[] = aristasExteriores.toArray();
		Aristas aux;
		for (int i = 0; i < a.length; ++i) {
			aux = (Aristas) a[i];
			aux.setPuerto(i);
		}
		
	}
	
	public void moverLadron () {
		
	} 
	
	public void producir (Integer valor) {	
		for (Hexagonos hex : hexagonos.values()) {
			if (hex.getValor() == valor) hex.producir();
		}
	}
	
	public void construirAsentamiento (Integer idHexagono, Coordenadas c, Jugadores j) {
		if (hexagonos.get(idHexagono).sePuedeConstruirAsentamiento(c,j)) {
			
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
				System.out.println("\t(" + v[j].getCoordenadas().getX() + ", " + v[j].getCoordenadas().getY() + ")");
				System.out.println("\t[(" + a[j].getCoordenadasAristas().getX() + ", " + a[j].getCoordenadasAristas().getY() + 
						"),( " + a[j].getCoordenadasAristas().getFin_x() + ", " + a[j].getCoordenadasAristas().getFin_y() + ")]");
			}
		}
		System.out.println("Numero de vertices: " + hexagonos.get(0).num_vertices());
		System.out.println("Numero de aristas: " + hexagonos.get(0).num_aristas());
		System.out.println("Numero de puertos: " + hexagonos.get(0).num_puertos());
	}
}
