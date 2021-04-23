package Test;

import logica.Coordenadas;

public class TestCoordenadas {
	
	private Boolean testHashCode (Coordenadas c1, Coordenadas c2) {
		int hasCode_c1 = c1.hashCode();
		int hasCode_c2 = c2.hashCode();
		return hasCode_c1 == hasCode_c2;
	}
	
	private Boolean testEquals (Coordenadas c1, Object o) {
		return c1.equals(o);
	}
	
	/*
	 * Esta función comprueba que la distancia de dos coordenadas es igual a la esperada.
	 * @param c1	coordenada 1.
	 * @param c2	coordenada 2.
	 * @param dist	distancia esperada entre la coordenada 1 y la coordenada 2.
	 * @return		true si y solo si la distancia esperada coincide con la distancia calculada.
	 * */
	private Boolean testGetDistancia (Coordenadas c1, Coordenadas c2, Double dist) {
		Double resul = c1.getDistancia(c2);
		return resul == dist;
	}
	
	public void ejecutarTest () {
		// Pruebas funcion hashCode.
		System.out.println("[test Coordenadas.hashCode] : Comprobar que c1(0,0) es igual a c2(2,3)");
		Boolean test1 = testHashCode(new Coordenadas(0,0), new Coordenadas(2,3));
		if (test1) {
			System.out.println("[test Coordenadas.hashCode] : con c1(0,0) y c2(2,3) el valor esperado era false");
		}
		System.out.println("[test Coordenadas.hashCode] : Comprobar que c1(1,1) es igual a c2(1,1)");
		Boolean test2 = testHashCode(new Coordenadas(1,1), new Coordenadas(1,1));
		if (!test2) {
			System.out.println("[test Coordenadas.hashCode] : con c1(1,1) y c2(1,1) el valor esperado era true");
		}
		System.out.println("[test Coordenadas.hashCode] : Comprobar que c1(2,0) es igual a c2(2,3)");
		Boolean test3 = testHashCode(new Coordenadas(0,0), new Coordenadas(2,3));
		if (test3) {
			System.out.println("[test Coordenadas.hashCode] : con c1(2,0) y c2(2,3) el valor esperado era false");
		}
		System.out.println("[test Coordenadas.hashCode] : Comprobar que c1(0,3) es igual a c2(2,3)");
		Boolean test4 = testHashCode(new Coordenadas(0,3), new Coordenadas(2,3));
		if (test4) {
			System.out.println("[test Coordenadas.hashCode] : con c1(0,3) y c2(2,3) el valor esperado era false");
		}
		System.out.println("[test Coordenadas.hashCode] : Comprobar que c1(0,0) es igual a c2(2,3)");
		Boolean test5 = testHashCode(new Coordenadas(9.2,8), new Coordenadas(9.3,8));
		if (test5) {
			System.out.println("[test Coordenadas.hashCode] : con c1(9.2,8) y c2(9.3,8) el valor esperado era false");
		}
		
		// Pruebas funcion equals.
	}
}
