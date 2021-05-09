package es.susangames.catan.Test;

import logica.*;

public class TestLogica {
	
	private Tablero t;
	private Jugadores j[];
	private Boolean posiblesCaminos[][];
	private Boolean posiblesAsentamientos[][];
	private TestLogicaAuxiliar tLA;
	
	public TestLogica () {
		this.t = new Tablero ();
		this.j = t.getJugadores();
		j[0].setArcilla(99);
		j[0].setCereales(99);
		j[0].setLana(99);
		j[0].setMadera(99);
		j[0].setMineral(99);
		
		j[1].setArcilla(99);
		j[1].setCereales(99);
		j[1].setLana(99);
		j[1].setMadera(99);
		j[1].setMineral(99);
		
		j[2].setArcilla(99);
		j[2].setCereales(99);
		j[2].setLana(99);
		j[2].setMadera(99);
		j[2].setMineral(99);
		
		j[3].setArcilla(99);
		j[3].setCereales(99);
		j[3].setLana(99);
		j[3].setMadera(99);
		j[3].setMineral(99);
		
		this.posiblesCaminos = new Boolean[4][t.num_aristas()];
		for (int i = 0; i < t.num_aristas(); ++i) {
			posiblesCaminos[0][i] = false;
			posiblesCaminos[1][i] = false;
			posiblesCaminos[2][i] = false;
			posiblesCaminos[3][i] = false;
		}
		
		this.posiblesAsentamientos = new Boolean[4][t.num_vertices()];
		for (int i = 0; i < t.num_vertices(); ++i) {
			posiblesAsentamientos[0][i] = false;
			posiblesAsentamientos[1][i] = false;
			posiblesAsentamientos[2][i] = false;
			posiblesAsentamientos[3][i] = false;
		}
		
		tLA = new TestLogicaAuxiliar (t);
	}
	
	public Boolean ejecutarTest () {
		Boolean test_numVertices = test_numVertices();
		if (!test_numVertices) {
			System.out.println("El numero de verices de tablero es erroneo, hay " + 
					t.num_vertices() + "cuando debería haber 54.");
			return false;
		}
		
		Boolean test_numAristas = test_numAristas ();
		if (!test_numAristas) {
			System.out.println("El numero de aristas de tablero es erroneo, hay " + 
					t.num_vertices() + "cuando debería haber 72.");
			return false;
		}
		
		Boolean test_numPuertos = test_numPuertos ();
		if (!test_numPuertos) {
			System.out.println("El numero de puertos es de " + t.num_puertos() + 
					"  deberia ser de 9");
			return false;
		}
		
		Boolean test_numPuertosBasicos = test_numPuertosBasicos();
		if (!test_numPuertosBasicos) {
			System.out.println("El numero de puertos basico es incorrecto");
			return false;
		}
		
		Boolean test_numPuertosMadera = test_numPuertosMadera();
		if (!test_numPuertosMadera) {
			System.out.println("El numero de puertos de madera es incorrecto");
			return false;
		}
		
		Boolean test_numPuertosLana = test_numPuertosLana();
		if (!test_numPuertosLana) {
			System.out.println("El numero de puertos de lana es incorrecto");
			return false;
		}
		
		Boolean test_numPuertosCereales = test_numPuertosCereales();
		if (!test_numPuertosCereales) {
			System.out.println("El numero de puertos de cereales es incorrecto");
			return false;
		}
		
		Boolean test_numPuertosArcilla = test_numPuertosArcilla();
		if (!test_numPuertosArcilla) {
			System.out.println("El numero de puertos de arcilla es incorrecto");
			return false;
		}
		
		Boolean test_numPuertosMineral = test_numPuertosMineral();
		if (!test_numPuertosMineral) {
			System.out.println("El numero de puertos de mineral es incorrecto");
			return false;
		}
		
		Boolean test_sonAdyacentes = test_sonAdyacentes();
		if (!test_sonAdyacentes) {
			System.out.println("Error en test sonAdyacentes de Hexagonos");
			return false;
		}
		
		Boolean test_contieneVertice = test_contieneVertice();
		if (!test_contieneVertice) {
			System.out.println("Error en test contieneVertice de Hexagonos");
			return false;
		}
		
		Boolean testAristasDelVertice = testAristasDelVertice();
		if (!testAristasDelVertice) {
			System.out.println("Error en test aristasDelVertice");
			return false;
		}
		
		Boolean test_getVerticesAdyacentes = test_getVerticesAdyacentes();
		if (!test_getVerticesAdyacentes) {
			System.out.println("Error en test getVerticesAdyacentes");
			return false;
		}
		
		Boolean test_construcciones = test_construcciones ();
		if (!test_construcciones) {
			System.out.println("Error en el test de construcciones");
			return false;
		}
		
		return true;
	}
	
	private Boolean test_numVertices () {
		return t.num_vertices() == 54;
	}
	
	private Boolean test_numAristas () {
		return t.num_aristas() == 72;
	}
	
	private Boolean test_numPuertos () {
		return t.num_puertos() == 9;
	}
	
	private Boolean test_numPuertosBasicos () {
		return t.num_puertos_basicos() == 4;
	}
	
	private Boolean test_numPuertosMadera () {
		return t.num_puertos_madera() == 1;
	}
	
	private Boolean test_numPuertosLana () {
		return t.num_puertos_lana() == 1;
	}
	
	private Boolean test_numPuertosCereales () {
		return t.num_puertos_cereales() == 1;
	}
	
	private Boolean test_numPuertosArcilla () {
		return t.num_puertos_arcilla() == 1;
	}
	
	private Boolean test_numPuertosMineral () {
		return t.num_puertos_mineral() == 1;
	}
	
	private Boolean test_sonAdyacentes () {
		Boolean test1 = tLA.testSonAdyacentes(t.getHexagono(0), t.getHexagono(1)); // true
		if (!test1) {
			System.out.println("Error Test 1 sonAdyacentes");
			return false;
		}
		
		Boolean test2 = tLA.testSonAdyacentes(t.getHexagono(0), t.getHexagono(2)); // false
		if (test2) {
			System.out.println("Error Test 2 sonAdyacentes");
			return false;
		}
		
		Boolean test3 = tLA.testSonAdyacentes(t.getHexagono(0), t.getHexagono(3));
		if (!test3) {
			System.out.println("Error Test 3 sonAdyacentes");
			return false;
		}
		
		Boolean test4 = tLA.testSonAdyacentes(t.getHexagono(9), t.getHexagono(5));
		if (!test4) {
			System.out.println("Error Test 1 sonAdyacentes");
			return false;
		}
		
		Boolean test5 = tLA.testSonAdyacentes(t.getHexagono(9), t.getHexagono(0));
		if (test5) {
			System.out.println("Error Test 5 sonAdyacentes");
			return false;
		}
		
		Boolean test6 = tLA.testSonAdyacentes(t.getHexagono(2), t.getHexagono(18));
		if (test6) {
			System.out.println("Error Test 6 sonAdyacentes");
			return false;
		}
		
		Boolean test7 = tLA.testSonAdyacentes(t.getHexagono(6), t.getHexagono(12));
		if (test7) {
			System.out.println("Error Test 7 sonAdyacentes");
			return false;
		}
		
		Boolean test8 = tLA.testSonAdyacentes(t.getHexagono(4), t.getHexagono(14));
		if (test8) {
			System.out.println("Error Test 8 sonAdyacentes");
			return false;
		}
		
		Boolean test9 = tLA.testSonAdyacentes(t.getHexagono(0), t.getHexagono(0));
		if (!test9) {
			System.out.println("Error Test 9 sonAdyacentes");
			return false;
		}
		
		Boolean test10 = tLA.testSonAdyacentes(t.getHexagono(2), t.getHexagono(0));
		if (test10) {
			System.out.println("Error Test 10 sonAdyacentes");
			return false;
		}
		
		Boolean test11 = tLA.testSonAdyacentes(t.getHexagono(1), t.getHexagono(0));
		if (!test11) {
			System.out.println("Error Test 11 sonAdyacentes");
			return false;
		}
		
		return true;
	}
	
	private Boolean test_contieneVertice () {
		Boolean test1 = tLA.testContieneVertice(t.getHexagono(0), t.getVerticePorId(0));
		if (!test1) {
			System.out.println("Error Test 1 contieneVertice(h,v)");
			return false;
		} 
		
		Boolean test2 = tLA.testContieneVertice(t.getHexagono(0), t.getVerticePorId(1));
		if (!test2) {
			System.out.println("Error Test 2 contieneVertice(h,v)");
			return false;
		} 
		
		Boolean test3 = tLA.testContieneVertice(t.getHexagono(0), t.getVerticePorId(2));
		if (!test3) {
			System.out.println("Error Test 3 contieneVertice(h,v)");
			return false;
		} 
		
		Boolean test4 = tLA.testContieneVertice(t.getHexagono(0), t.getVerticePorId(3));
		if (!test4) {
			System.out.println("Error Test 4 contieneVertice(h,v)");
			return false;
		} 
		
		Boolean test5 = tLA.testContieneVertice(t.getHexagono(0), t.getVerticePorId(4));
		if (!test5) {
			System.out.println("Error Test 5 contieneVertice(h,v)");
			return false;
		} 
		
		Boolean test6 = tLA.testContieneVertice(t.getHexagono(0), t.getVerticePorId(5));
		if (!test6) {
			System.out.println("Error Test 6 contieneVertice(h,v)");
			return false;
		}
			
		Boolean test7 = tLA.testContieneVertice(t.getHexagono(0), t.getVerticePorId(30));
		if (test7) {
			System.out.println("Error Test 7 contieneVertice(h,v)");
			return false;
		}	
		
		Boolean test8 = tLA.testContieneVertice(t.getHexagono(5), t.getVerticePorId(8));
		if (!test8) {
			System.out.println("Error Test 8 contieneVertice(h,v)");
			return false;
		}
		
		Boolean test9 = tLA.testContieneVertice(t.getHexagono(7), t.getVerticePorId(30));
		if (test9) {
			System.out.println("Error Test 9 contieneVertice(h,v)");
			return false;
		}
		
		return true;
	}
	
	private Boolean testAristasDelVertice () {
		
		
		return true;
	}
	
	private Boolean test_getVerticesAdyacentes () {
		
		Vertices[] v1 = new Vertices[] { t.getVerticePorId(4), t.getVerticePorId(0), null};
		Boolean test1 = tLA.testGetVerticesAdyacentes(t.getVerticePorId(5), v1);
		if (!test1) {
			System.out.println("Error Test 1 getVerticesAdyacentes(v,vect_v[])");
			return false;
		}
		
		Vertices[] v2 = new Vertices[] { t.getVerticePorId(8), t.getVerticePorId(21), t.getVerticePorId(19)};
		Boolean test2 = tLA.testGetVerticesAdyacentes(t.getVerticePorId(18), v2);
		if (!test2) {
			System.out.println("Error Test 2 getVerticesAdyacentes(v,vect_v[])");
			return false;
		}
		
		Vertices[] v3 = new Vertices[] { t.getVerticePorId(25), t.getVerticePorId(38), t.getVerticePorId(29)};
		Boolean test3 = tLA.testGetVerticesAdyacentes(t.getVerticePorId(30), v3);
		if (!test3) {
			System.out.println("Error Test 3 getVerticesAdyacentes(v,vect_v[])");
			return false;
		}
		
		Vertices[] v4 = new Vertices[] { t.getVerticePorId(53), t.getVerticePorId(46), null};
		Boolean test4 = tLA.testGetVerticesAdyacentes(t.getVerticePorId(52), v4);
		if (!test4) {
			System.out.println("Error Test 4 getVerticesAdyacentes(v,vect_v[])");
			return false;
		}
		
		Vertices[] v5 = new Vertices[] { t.getVerticePorId(29), t.getVerticePorId(31), t.getVerticePorId(41)};
		Boolean test5 = tLA.testGetVerticesAdyacentes(t.getVerticePorId(32), v5);
		if (!test5) {
			System.out.println("Error Test 5 getVerticesAdyacentes(v,vect_v[])");
			return false;
		}

		Vertices[] v6 = new Vertices[] { t.getVerticePorId(13), t.getVerticePorId(11), null};
		Boolean test6 = tLA.testGetVerticesAdyacentes(t.getVerticePorId(10), v6);
		if (!test6) {
			System.out.println("Error Test 6 getVerticesAdyacentes(v,vect_v[])");
			return false;
		}
		
		return true;
	}
	
	private Boolean test_construcciones () {
		posiblesCaminos[0][0] = true;
		posiblesCaminos[0][5] = true;
		posiblesCaminos[0][9] = true;
		Boolean testPosiblesCaminos1 = tLA.testConstruirPrimerAsentamiento(t.getVerticePorId(0), j[0], posiblesCaminos);
		if (!testPosiblesCaminos1) {
			System.out.println("Mal 1");
			return false;
		}
		
		posiblesCaminos[1][8] = true;
		posiblesCaminos[1][7] = true;
		posiblesCaminos[1][21] = true;
		Boolean testPosiblesCaminos2 = tLA.testConstruirPrimerAsentamiento(t.getVerticePorId(8), j[1], posiblesCaminos);
		if (!testPosiblesCaminos2) {
			System.out.println("Mal 2");
			return false;
		}
		
		posiblesCaminos[2][69] = true;
		posiblesCaminos[2][70] = true;
		Boolean testPosiblesCaminos3 = tLA.testConstruirPrimerAsentamiento(t.getVerticePorId(52), j[2], posiblesCaminos);
		if (!testPosiblesCaminos3) {
			System.out.println("Mal 3");
			return false;
		}
		
		posiblesCaminos[3][32] = true;
		posiblesCaminos[3][31] = true;
		posiblesCaminos[3][38] = true;
		Boolean testPosiblesCaminos4 = tLA.testConstruirPrimerAsentamiento(t.getVerticePorId(25), j[3], posiblesCaminos);
		if (!testPosiblesCaminos4) {
			System.out.println("Mal 4");
			return false;
		}
		
		// Si pudiesemos construir en el vertice 1 las aristas 1, 8 y 0 estaria a true. 
		// Como no se puede deben de quedarse en el valor esperado.  
		Boolean testPosiblesCaminos5 = tLA.testConstruirPrimerAsentamiento(t.getVerticePorId(1), j[0], posiblesCaminos);
		if (!testPosiblesCaminos5) {
			System.out.println("Mal 5");
			return false;
		}
		
		posiblesCaminos[0][0] = false;
		posiblesCaminos[0][5] = true;
		posiblesCaminos[0][9] = true;
		posiblesCaminos[0][1] = true;
		posiblesCaminos[0][8] = true;
		Boolean testPosibles1 = tLA.testConstruirPrimerCamino(t.getAristaPorId(0), j[0], posiblesAsentamientos, posiblesCaminos);
		if (!testPosibles1) {
			System.out.println("Mal 6");
			return false;
		}
		
		posiblesCaminos[1][1] = true;
		posiblesCaminos[1][21] = true;
		posiblesCaminos[1][7] = true;
		posiblesCaminos[0][8] = false;
		posiblesCaminos[1][0] = false;
		posiblesCaminos[1][8] = false;
		Boolean testPosibles2 = tLA.testConstruirPrimerCamino(t.getAristaPorId(8), j[1], posiblesAsentamientos, posiblesCaminos);
		if (!testPosibles2) {
			System.out.println("Mal 7");
			return false;
		}
		
		posiblesCaminos[2][61] = true;
		posiblesCaminos[2][70] = true;
		posiblesCaminos[2][60] = true;
		posiblesCaminos[2][69] = false;
		Boolean testPosibles3 = tLA.testConstruirPrimerCamino(t.getAristaPorId(69), j[2], posiblesAsentamientos, posiblesCaminos);
		if (!testPosibles3) {
			System.out.println("Mal 8");
			return false;
		} 
		
		posiblesCaminos[3][33] = true;
		posiblesCaminos[3][52] = true;
		posiblesCaminos[3][31] = true;
		posiblesCaminos[3][38] = true;
		posiblesCaminos[3][32] = false;
		Boolean testPosibles4 = tLA.testConstruirPrimerCamino(t.getAristaPorId(32), j[3], posiblesAsentamientos, posiblesCaminos);
		if (!testPosibles4) {
			System.out.println("Mal 9");
			return false;
		}
		
		Boolean testPosibles5 = tLA.testConstruirPrimerCamino(t.getAristaPorId(1), j[2], posiblesAsentamientos, posiblesCaminos);
		if (!testPosibles5) {
			System.out.println("Mal 10");
			return false;
		}
		
		Boolean testPosibles6 = tLA.testConstruirPrimerCamino(t.getAristaPorId(8), j[0], posiblesAsentamientos, posiblesCaminos);
		if (!testPosibles6) {
			System.out.println("Mal 11");
			return false;
		}
		
		Boolean testConstAsent1 = tLA.testConstruirAsentamiento(t.getVerticePorId(5), j[0], posiblesAsentamientos, posiblesCaminos);
		if (!testConstAsent1) {
			System.out.println("Mal 12");
			return false;
		}
		
		
		posiblesCaminos[0][1] = false;
		posiblesCaminos[1][1] = false;
		posiblesCaminos[2][1] = false;
		posiblesCaminos[3][1] = false;
		posiblesCaminos[0][2] = true;
		posiblesCaminos[0][16] = true;
		posiblesAsentamientos[0][2] = true;
		Boolean testConstAsent2 = tLA.testConstruirPrimerCamino(t.getAristaPorId(1), j[0], posiblesAsentamientos, posiblesCaminos);
		if (!testConstAsent2) {
			System.out.println("Mal 13");
			return false;
		}
		
		posiblesCaminos[0][2] = true;
		posiblesCaminos[0][16] = true;
		posiblesAsentamientos[0][2] = false;
		posiblesAsentamientos[0][1] = false;
		posiblesAsentamientos[0][3] = false;
		Boolean testConstAsent3 = tLA.testConstruirAsentamiento(t.getVerticePorId(2), j[0], posiblesAsentamientos, posiblesCaminos);
		if (!testConstAsent3) {
			System.out.println("Mal 14");
			return false;
		}
		
		return true;
	}
}
