package Test;

import logica.*;

public class TestLogicaAuxiliar {
	
	Tablero t;
	
	public TestLogicaAuxiliar (Tablero t) {
		this.t = t;
	}
	
	public Boolean testSonAdyacentes (Hexagonos h1, Hexagonos h2) {
		return h1.sonAdyacentes(h2);
	}
	
	public Boolean testContieneVertice (Hexagonos h, Vertices v) {
		return h.contieneVertice(v);
	}
	
	public Boolean testAristasDelVertice (Vertices v, Aristas a[]) {
		return false;
	}
	
	/*
	 * Comprueba que los vertices adyacentes a v1 son los vertices que se encuentran en el vector 
	 * de vertices vectorVertices.
	 * @param v	vertice sobre el que comprobaremos cuales son sus vertices adyacentes
	 * @return true si y solo si
	 * */
	public Boolean testGetVerticesAdyacentes (Vertices v, Vertices vectorVertices[]) {
		Vertices v_adyacentes[] = t.getVerticesAdyacentes(v.getIdentificador());
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
	
	public Boolean testConstruirPrimerAsentamiento (Vertices v, Jugadores j, Boolean posiblesCaminos[][]) {
		t.construirPrimerAsentamiento(v, j);
		Aristas a[] = t.getTodasAristas();
		Boolean resul[][] = new Boolean[4][t.num_aristas()];
		
		for (int i = 0; i < t.num_aristas(); ++i) {
			resul[0][i] = false;
			resul[1][i] = false;
			resul[2][i] = false;
			resul[3][i] = false;
		}
		int identificador;
		for (int i = 0; i < t.num_aristas(); ++i) {
			identificador = a[i].getIdentificador();
			resul[0][identificador] = a[i].getPosibleCaminoDeJugador(0);
			resul[1][identificador] = a[i].getPosibleCaminoDeJugador(1);
			resul[2][identificador] = a[i].getPosibleCaminoDeJugador(2);
			resul[3][identificador] = a[i].getPosibleCaminoDeJugador(3);
		}
		
		/*System.out.println("Posibles caminos obtenidos");
		
		for (int i = 0; i < 4; ++i) {
			System.out.println("Jugador " + i);
			System.out.print(0 + " : " + resul[i][0]);
			for (int k = 1; k < t.num_aristas(); ++k) {
				System.out.print(", " + k + " : " + resul[i][k]);
			}
			System.out.println();
		}
		
		System.out.println("\nPosibles caminos esperados");
		for (int i = 0; i < 4; ++i) {
			System.out.println("Jugador " + i);
			System.out.print(0 + " : " + posiblesCaminos[i][0]);
			for (int k = 1; k < t.num_aristas(); ++k) {
				System.out.print(", " + k + " : " + posiblesCaminos[i][k]);
			}
			System.out.println();
		}*/
		
		Boolean equal = true;
		for (int i = 0 ; i < 4 && equal; ++i) {
			for (int k = 1; k < t.num_aristas() && equal; ++k) {
				equal = resul[i][k] == posiblesCaminos[i][k];
				if (!equal) System.out.println("resul[" + i + "][" + k + "] = " + resul[i][k] + "; posiblesCaminos[" + i + "][" + k + "]" + posiblesCaminos[i][k]);
			}
		}
		return equal;
	}
	
	public Boolean testConstruirPrimerCamino (Aristas a, Jugadores j, Boolean posiblesAsentamientos[][], 
			Boolean posiblesCaminos[][]) {
		t.construirPrimerCamino(a, j);
		Vertices vs[] = t.getTodosVertices();
		Aristas as[] = t.getTodasAristas();
		
		Boolean resulV[][] = new Boolean[4][t.num_vertices()];
		for (int i = 0; i < t.num_vertices(); ++i) {
			resulV[0][i] = false;
			resulV[1][i] = false;
			resulV[2][i] = false;
			resulV[3][i] = false;
		}
		Boolean resulA[][] = new Boolean[4][t.num_aristas()];
		for (int i = 0; i < t.num_aristas(); ++i) {
			resulA[0][i] = false;
			resulA[1][i] = false;
			resulA[2][i] = false;
			resulA[3][i] = false;
		}
		
		int identificador;
		for (int i = 0; i < t.num_vertices(); ++i) {
			identificador = vs[i].getIdentificador();
			resulV[0][identificador] = vs[i].getPosibleAsentamientoDeJugador(0);
			resulV[1][identificador] = vs[i].getPosibleAsentamientoDeJugador(1);
			resulV[2][identificador] = vs[i].getPosibleAsentamientoDeJugador(2);
			resulV[3][identificador] = vs[i].getPosibleAsentamientoDeJugador(3);
		}
		for (int i = 0; i < t.num_aristas(); ++i) {
			identificador = as[i].getIdentificador();
			resulA[0][identificador] = as[i].getPosibleCaminoDeJugador(0);
			resulA[1][identificador] = as[i].getPosibleCaminoDeJugador(1);
			resulA[2][identificador] = as[i].getPosibleCaminoDeJugador(2);
			resulA[3][identificador] = as[i].getPosibleCaminoDeJugador(3);
		}
		/*
		System.out.println("Posibles caminos calculados");
		for (int i = 0; i < 4; ++i) {
			System.out.println("Jugador " + i);
			System.out.print(0 + " : " + resulA[i][0]);
			for (int k = 1; k < t.num_aristas(); ++k) {
				System.out.print(", " + k + " : " + resulA[i][k]);
			}
			System.out.println();
		}*/
		
		/*
		for (int i = 0; i < t.num_aristas(); ++i) {
			System.out.println("A[" + as[i].getIdentificador() + "] : tieneCamino = " + as[i].tieneCamino());
		}*/
		
		Boolean equalV = true;
		for (int i = 0 ; i < 4 && equalV; ++i) {
			for (int k = 1; k < t.num_vertices() && equalV; ++k) {
				equalV = resulV[i][k] == posiblesAsentamientos[i][k];
				if (!equalV) {
					System.out.println("resulV[" + i + "][" + k + "] = " + resulV[i][k] + "; "
							+ "posiblesAsentamientos[" + i + "][" + k + "]" + posiblesAsentamientos[i][k]);
				}
			}
		}
		Boolean equalA = true;
		for (int i = 0 ; i < 4 && equalA; ++i) {
			for (int k = 1; k < t.num_aristas() && equalA; ++k) {
				equalA = resulA[i][k] == posiblesCaminos[i][k];
				if (!equalA) {
					System.out.println("resulA[" + i + "][" + k + "] = " + resulA[i][k] + "; "
							+ "posiblesCaminos[" + i + "][" + k + "]" + posiblesCaminos[i][k]);
				}
			}
		}
		return equalV && equalA;
	}
	
	public Boolean testConstruirAsentamiento (Vertices v, Jugadores j, 
			Boolean posiblesAsentamientos[][], Boolean posiblesCaminos[][]) {
		//System.out.println("testConstruirAsentamiento");
		t.construirAsentamiento(v, j);
		Vertices vs[] = t.getTodosVertices();
		Aristas as[] = t.getTodasAristas();
		
		Boolean resulV[][] = new Boolean[4][t.num_vertices()];
		for (int i = 0; i < t.num_vertices(); ++i) {
			resulV[0][i] = false;
			resulV[1][i] = false;
			resulV[2][i] = false;
			resulV[3][i] = false;
		}
		Boolean resulA[][] = new Boolean[4][t.num_aristas()];
		for (int i = 0; i < t.num_aristas(); ++i) {
			resulA[0][i] = false;
			resulA[1][i] = false;
			resulA[2][i] = false;
			resulA[3][i] = false;
		}
		
		int identificador;
		for (int i = 0; i < t.num_vertices(); ++i) {
			identificador = vs[i].getIdentificador();
			resulV[0][identificador] = vs[i].getPosibleAsentamientoDeJugador(0);
			resulV[1][identificador] = vs[i].getPosibleAsentamientoDeJugador(1);
			resulV[2][identificador] = vs[i].getPosibleAsentamientoDeJugador(2);
			resulV[3][identificador] = vs[i].getPosibleAsentamientoDeJugador(3);
		}
		for (int i = 0; i < t.num_aristas(); ++i) {
			identificador = as[i].getIdentificador();
			resulA[0][identificador] = as[i].getPosibleCaminoDeJugador(0);
			resulA[1][identificador] = as[i].getPosibleCaminoDeJugador(1);
			resulA[2][identificador] = as[i].getPosibleCaminoDeJugador(2);
			resulA[3][identificador] = as[i].getPosibleCaminoDeJugador(3);
		}
		
		Boolean equalV = true;
		for (int i = 0 ; i < 4 && equalV; ++i) {
			for (int k = 1; k < t.num_vertices() && equalV; ++k) {
				equalV = resulV[i][k] == posiblesAsentamientos[i][k];
				if (!equalV) {
					System.out.println("resulV[" + i + "][" + k + "] = " + resulV[i][k] + "; "
							+ "posiblesAsentamientos[" + i + "][" + k + "]" + posiblesAsentamientos[i][k]);
				}
			}
		}
		Boolean equalA = true;
		for (int i = 0 ; i < 4 && equalA; ++i) {
			for (int k = 1; k < t.num_aristas() && equalA; ++k) {
				equalA = resulA[i][k] == posiblesCaminos[i][k];
				if (!equalA) {
					System.out.println("resulA[" + i + "][" + k + "] = " + resulA[i][k] + "; "
							+ "posiblesCaminos[" + i + "][" + k + "]" + posiblesCaminos[i][k]);
				}
			}
		}
		return equalV && equalA;
	}
	
	/*private Boolean testConstruirCamino (Aristas a, Jugadores j) {}
	
	private Boolean testMejorarAsentamiento (Vertices v, Jugadores j) {}
	*/
}
