package Test;

import logica.Aristas;
import logica.Coordenadas;
import logica.Jugadores;
import logica.Vertices;
import logica.Hexagonos;
import logica.Tablero;

public class TestHexagono {
	
	private Boolean testNum_vertices () {
		return Hexagonos.num_vertices() == 54;
	}
	
	private Boolean  testNum_aristas () {
		return Hexagonos.num_aristas() == 72;
	}
	
	private Boolean testNum_puertos () {
		return Hexagonos.num_puertos() == 9;
	}
	
	private Boolean testSonAdyacentes (Hexagonos h1, Hexagonos h2) {
		return h1.sonAdyacentes(h2);
	}
	
	private Boolean testContieneVertice (Hexagonos h, Vertices v) {
		return h.contieneVertice(v);
	}
	
	private Boolean testAristasDelVertice (Vertices v, Aristas a[]) {
		return false;
	}
	
	/*
	 * Comprueba que los vertices adyacentes a v1 son los vertices que se encuentran en el vector 
	 * de vertices vectorVertices.
	 * @param v	vertice sobre el que comprobaremos cuales son sus vertices adyacentes
	 * @return true si y solo si
	 * */
	private Boolean testGetVerticesAdyacentes (Vertices v, Vertices vectorVertices[]) {
		Vertices v_adyacentes[] = Hexagonos.getVerticesAdyacentes(v.getIdentificador());
		/*System.out.print("[" + v_adyacentes[0].getIdentificador());
		System.out.print(","+ v_adyacentes[1].getIdentificador());
		if (v_adyacentes[2] != null) System.out.print("," + v_adyacentes[2].getIdentificador());
		System.out.println("]");*/
		if (v_adyacentes.length != vectorVertices.length)	return false;
		Boolean tieneVertice = false;
		Boolean mismoVector = true;
		for (int i = 0; i < v_adyacentes.length; ++i) {
			tieneVertice = false;
			for (int j = 0; j < vectorVertices.length && !tieneVertice; ++j) {
				
				if (v_adyacentes[i] == null && vectorVertices[j] == null) tieneVertice = true;
				else if ((v_adyacentes[i] == null && vectorVertices[j] != null) || (
						v_adyacentes[i] != null && vectorVertices[j] == null ))
					tieneVertice = false;
				else {
					//System.out.println(v_adyacentes[i].getIdentificador() + " " + vectorVertices[j].getIdentificador());
					tieneVertice = v_adyacentes[i].equals(vectorVertices[j]);
				}
			}
			if (!tieneVertice)
				mismoVector &= false;
			else mismoVector &= true;
			//System.out.println(mismoVector);
		}
		//System.out.println();
		return mismoVector;
	}
	/*
	
	private Boolean testSePuedeConstruirAsentamiento(Coordenadas c, Jugadores j) {}
	
	private Boolean testConstruirAsentamiento (Vertices v, Jugadores j) {}
	
	private Boolean testConstruirCarretera (Aristas a, Jugadores j) {}
	
	private Boolean testMejorarAsentamiento (Vertices v, Jugadores j) {}
	*/
	
	public void ejecutarTest () {
		// Creamos el tablero.
		Tablero t = new Tablero(4);
		
		for (int i = 0; i < 19; ++i) {
			System.out.println("Hexagono " + i);
			Vertices v[] = t.getHexagono(i).getVertices();
			System.out.print("\t");
			System.out.print("Vertice " + v[0].getIdentificador());
			for (int j = 1; j < v.length; ++j) {
				System.out.print(", Vertice " + v[j].getIdentificador());
			}
			System.out.println();
		}
		
		System.out.println();
		
		// Prueba num_vertices
		System.out.println("[test Hexagonos.num_vertices()] : Comprobar que el numero de vertices creados son 54.");
		Boolean testVertices = this.testNum_vertices();
		if (!testVertices) {
			System.out.print("[Error: test Hexagonos.num_vertices()] : Numero de vertices creado es incorrecto.");
		}
		
		// Prueba num_aristas
		System.out.println("[test Hexagonos.num_vertices()] : Comprobar que el numero de aristas creadas son 72.");
		Boolean testAristas = this.testNum_aristas();
		if (!testAristas) {
			System.out.print("[Error: test Hexagonos.num_vertices()] : Numero de aristas creadas es incorrecto.");
		}
		
		// Prueba num_aristas
		System.out.println("[test Hexagonos.num_vertices()] : Comprobar que el numero de puertos creados son 9.");
		Boolean testPuertos = this.testNum_puertos();
		if (!testPuertos) {
			System.out.print("[Error: test Hexagonos.num_vertices()] : Numero de puertos creados es incorrecto.");
		}
		
		// Prueba funcion sonAdyacentes ()
		System.out.println("[test Hexagonos.sonAdyacentes] : Comprobar si hexagono con id 0 es adyacente al hexagono con id 1.");
		Boolean test1 = this.testSonAdyacentes(t.getHexagono(0), t.getHexagono(1));
		if (!test1) {
			System.out.println("[Error: test Hexagonos.sonAdyacentes] : Se ha obtenido que hexagono con id 0 no es adyacente al hexagono con id 1.");
		} else {
			System.out.println("[test Hexagonos.sonAdyacentes(0,1)] : Ejecución correcta.");
		}
		System.out.println("[test Hexagonos.sonAdyacentes] : Comprobar si hexagono con id 0 es adyacente al hexagono con id 2.");
		Boolean test2 = this.testSonAdyacentes(t.getHexagono(0), t.getHexagono(2));
		if (test2) {
			System.out.println("[Error: test Hexagonos.sonAdyacentes] : Se ha obtenido que hexagono con id 0 NO es adyacente al hexagono con id 2.");
		} else {
			System.out.println("[test Hexagonos.sonAdyacentes(0,2)] : Ejecución correcta.");
		}
		System.out.println("[test Hexagonos.sonAdyacentes] : Comprobar si hexagono con id 0 es adyacente al hexagono con id 4.");
		Boolean test3 = this.testSonAdyacentes(t.getHexagono(0), t.getHexagono(3));
		if (!test3) {
			System.out.println("[Error: test Hexagonos.sonAdyacentes] : Se ha obtenido que hexagono con id 0 es adyacente al hexagono con id 4.");
		} else {
			System.out.println("[test Hexagonos.sonAdyacentes(0,3)] : Ejecución correcta.");
		}
		System.out.println("[test Hexagonos.sonAdyacentes] : Comprobar si hexagono con id 9 es adyacente al hexagono con id 5.");
		Boolean test4 = this.testSonAdyacentes(t.getHexagono(9), t.getHexagono(5));
		if (!test4) {
			System.out.println("[Error: test Hexagonos.sonAdyacentes] : Se ha obtenido que hexagono con id 9 es adyacente al hexagono con id 2.");
		} else {
			System.out.println("[test Hexagonos.sonAdyacentes(9,5)] : Ejecución correcta.");
		}
		System.out.println("[test Hexagonos.sonAdyacentes] : Comprobar si hexagono con id 9 es adyacente al hexagono con id 0.");
		Boolean test5 = this.testSonAdyacentes(t.getHexagono(9), t.getHexagono(0));
		if (test5) {
			System.out.println("[Error: test Hexagonos.sonAdyacentes] : Se ha obtenido que hexagono con id 9 NO es adyacente al hexagono con id 0.");
		} else {
			System.out.println("[test Hexagonos.sonAdyacentes(9,0)] : Ejecución correcta.");
		}
		System.out.println("[test Hexagonos.sonAdyacentes] : Comprobar si hexagono con id 2 es adyacente al hexagono con id 18.");
		Boolean test6 = this.testSonAdyacentes(t.getHexagono(2), t.getHexagono(18));
		if (test6) {
			System.out.println("[Error: test Hexagonos.sonAdyacentes] : Se ha obtenido que hexagono con id 2 NO es adyacente al hexagono con id 18.");
		} else {
			System.out.println("[test Hexagonos.sonAdyacentes(2,18)] : Ejecución correcta.");
		}
		
		System.out.println("[test Hexagonos.sonAdyacentes] : Comprobar si hexagono con id 6 es adyacente al hexagono con id 12.");
		Boolean test7 = this.testSonAdyacentes(t.getHexagono(6), t.getHexagono(12));
		if (test7) {
			System.out.println("[Error: test Hexagonos.sonAdyacentes] : Se ha obtenido que hexagono con id 6 NO es adyacente al hexagono con id 12.");
		} else {
			System.out.println("[test Hexagonos.sonAdyacentes(6,12)] : Ejecución correcta.");
		}
		
		System.out.println("[test Hexagonos.sonAdyacentes] : Comprobar si hexagono con id 4 es adyacente al hexagono con id 14.");
		Boolean test8 = this.testSonAdyacentes(t.getHexagono(4), t.getHexagono(14));
		if (test8) {
			System.out.println("[Error: test Hexagonos.sonAdyacentes] : Se ha obtenido que hexagono con id 4 NO es adyacente al hexagono con id 14.");
		} else {
			System.out.println("[test Hexagonos.sonAdyacentes(4,14)] : Ejecución correcta.");
		}
		
		System.out.println("[test Hexagonos.sonAdyacentes] : Comprobar si hexagono con id 0 es adyacente al hexagono con id 0.");
		Boolean test9 = this.testSonAdyacentes(t.getHexagono(0), t.getHexagono(0));
		if (!test9) {
			System.out.println("[Error: test Hexagonos.sonAdyacentes] : Se ha obtenido que hexagono con id 0 es adyacente al hexagono con id 0.");
		} else {
			System.out.println("[test Hexagonos.sonAdyacentes(0,0)] : Ejecución correcta.");
		}
		
		System.out.println("[test Hexagonos.sonAdyacentes] : Comprobar si hexagono con id 3 es adyacente al hexagono con id 1.");
		Boolean test10 = this.testSonAdyacentes(t.getHexagono(2), t.getHexagono(0));
		if (test10) {
			System.out.println("[Error: test Hexagonos.sonAdyacentes] : Se ha obtenido que hexagono con id 2 NO es adyacente al hexagono con id 0.");
		} else {
			System.out.println("[test Hexagonos.sonAdyacentes(2,0)] : Ejecución correcta.");
		}
		
		System.out.println("[test Hexagonos.sonAdyacentes] : Comprobar si hexagono con id 0 es adyacente al hexagono con id 0.");
		Boolean test11 = this.testSonAdyacentes(t.getHexagono(1), t.getHexagono(0));
		if (!test11) {
			System.out.println("[Error: test Hexagonos.sonAdyacentes] : Se ha obtenido que hexagono con id 1 es adyacente al hexagono con id 0.");
		} else {
			System.out.println("[test Hexagonos.sonAdyacentes(1,0)] : Ejecución correcta.");
		}
		
		// Pruebas contieneVertice(h,v)
		System.out.println("[test Hexagonos.contieneVertice] : Comprobar si hexagono 0 contiene el vertice 0.");
		Boolean test12 = this.testContieneVertice(t.getHexagono(0), Hexagonos.getVerticePorId(0));
		if (!test12) {
			System.out.println("[Error: test Hexagonos.contieneVertice] : Se ha obtenido que hexagono 0 NO contiene el vertice 0.");
		} else {
			System.out.println("[test Hexagonos.contieneVertice(0,0)] : Ejecución correcta.");
		}
		
		System.out.println("[test Hexagonos.contieneVertice] : Comprobar si hexagono 0 contiene el vertice 1.");
		Boolean test16 = this.testContieneVertice(t.getHexagono(0), Hexagonos.getVerticePorId(1));
		if (!test16) {
			System.out.println("[Error: test Hexagonos.contieneVertice] : Se ha obtenido que hexagono 0 NO contiene el vertice 1.");
		} else {
			System.out.println("[test Hexagonos.contieneVertice(0,1)] : Ejecución correcta.");
		}
		
		System.out.println("[test Hexagonos.contieneVertice] : Comprobar si hexagono 0 contiene el vertice 2.");
		Boolean test17 = this.testContieneVertice(t.getHexagono(0), Hexagonos.getVerticePorId(2));
		if (!test17) {
			System.out.println("[Error: test Hexagonos.contieneVertice] : Se ha obtenido que hexagono 0 NO contiene el vertice 2.");
		} else {
			System.out.println("[test Hexagonos.contieneVertice(0,2)] : Ejecución correcta.");
		}
		
		System.out.println("[test Hexagonos.contieneVertice] : Comprobar si hexagono 0 contiene el vertice 3.");
		Boolean test18 = this.testContieneVertice(t.getHexagono(0), Hexagonos.getVerticePorId(3));
		if (!test18) {
			System.out.println("[Error: test Hexagonos.contieneVertice] : Se ha obtenido que hexagono 0 NO contiene el vertice 3.");
		} else {
			System.out.println("[test Hexagonos.contieneVertice(0,3)] : Ejecución correcta.");
		}
		
		System.out.println("[test Hexagonos.contieneVertice] : Comprobar si hexagono 0 contiene el vertice 4.");
		Boolean test19 = this.testContieneVertice(t.getHexagono(0), Hexagonos.getVerticePorId(4));
		if (!test19) {
			System.out.println("[Error: test Hexagonos.contieneVertice] : Se ha obtenido que hexagono 0 NO contiene el vertice 4.");
		} else {
			System.out.println("[test Hexagonos.contieneVertice(0,4)] : Ejecución correcta.");
		}
		
		System.out.println("[test Hexagonos.contieneVertice] : Comprobar si hexagono 0 contiene el vertice 5.");
		Boolean test20 = this.testContieneVertice(t.getHexagono(0), Hexagonos.getVerticePorId(5));
		if (!test20) {
			System.out.println("[Error: test Hexagonos.contieneVertice] : Se ha obtenido que hexagono 0 NO contiene el vertice 5.");
		} else {
			System.out.println("[test Hexagonos.contieneVertice(0,5)] : Ejecución correcta.");
		}
		
		System.out.println("[test Hexagonos.contieneVertice] : Comprobar si hexagono 0 contiene el vertice 30.");
		Boolean test13 = this.testContieneVertice(t.getHexagono(0), Hexagonos.getVerticePorId(30));
		if (test13) {
			System.out.println("[Error: test Hexagonos.contieneVertice] : Se ha obtenido que hexagono 0 contiene el vertice 30.");
		} else {
			System.out.println("[test Hexagonos.contieneVertice(0,30)] : Ejecución correcta.");
		}
		
		System.out.println("[test Hexagonos.contieneVertice] : Comprobar si hexagono 5 contiene el vertice 8.");
		Boolean test14 = this.testContieneVertice(t.getHexagono(5), Hexagonos.getVerticePorId(8));
		if (!test14) {
			System.out.println("[Error: test Hexagonos.contieneVertice] : Se ha obtenido que hexagono 5 NO contiene el vertice 8.");
		} else {
			System.out.println("[test Hexagonos.contieneVertice(5,8)] : Ejecución correcta.");
		}
		
		System.out.println("[test Hexagonos.contieneVertice] : Comprobar si hexagono 7 contiene el vertice 30.");
		Boolean test15 = this.testContieneVertice(t.getHexagono(7), Hexagonos.getVerticePorId(30));
		if (test15) {
			System.out.println("[Error: test Hexagonos.contieneVertice] : Se ha obtenido que hexagono 7 contiene el vertice 30.");
		} else {
			System.out.println("[test Hexagonos.contieneVertice(7,30)] : Ejecución correcta.");
		}
		
		// Prueba getVerticesAdyacentes.
		System.out.println();
		Vertices[] v1 = new Vertices[] { Hexagonos.getVerticePorId(4), Hexagonos.getVerticePorId(0), null};
		Boolean ady_test1 = this.testGetVerticesAdyacentes(Hexagonos.getVerticePorId(5), v1);
		if (ady_test1) System.out.println("Correcto");
		else System.out.println("MAL");
		
		System.out.println();
		Vertices[] v2 = new Vertices[] { Hexagonos.getVerticePorId(8), Hexagonos.getVerticePorId(21), Hexagonos.getVerticePorId(19)};
		Boolean ady_test2 = this.testGetVerticesAdyacentes(Hexagonos.getVerticePorId(18), v2);
		if (ady_test2) System.out.println("Correcto");
		else System.out.println("MAL");
		
		System.out.println();
		Vertices[] v3 = new Vertices[] { Hexagonos.getVerticePorId(25), Hexagonos.getVerticePorId(38), Hexagonos.getVerticePorId(29)};
		Boolean ady_test3 = this.testGetVerticesAdyacentes(Hexagonos.getVerticePorId(30), v3);
		if (ady_test3) System.out.println("Correcto");
		else System.out.println("MAL");
		
		System.out.println();
		Vertices[] v4 = new Vertices[] { Hexagonos.getVerticePorId(53), Hexagonos.getVerticePorId(46), null};
		Boolean ady_test4 = this.testGetVerticesAdyacentes(Hexagonos.getVerticePorId(52), v4);
		if (ady_test4) System.out.println("Correcto");
		else System.out.println("MAL");
		
		System.out.println();
		Vertices[] v5 = new Vertices[] { Hexagonos.getVerticePorId(29), Hexagonos.getVerticePorId(31), Hexagonos.getVerticePorId(41)};
		Boolean ady_test5 = this.testGetVerticesAdyacentes(Hexagonos.getVerticePorId(32), v5);
		if (ady_test5) System.out.println("Correcto");
		else System.out.println("MAL");
		
		System.out.println();
		Vertices[] v6 = new Vertices[] { Hexagonos.getVerticePorId(13), Hexagonos.getVerticePorId(11), null};
		Boolean ady_test6 = this.testGetVerticesAdyacentes(Hexagonos.getVerticePorId(10), v6);
		if (ady_test6) System.out.println("Correcto");
		else System.out.println("MAL");
	}
}
