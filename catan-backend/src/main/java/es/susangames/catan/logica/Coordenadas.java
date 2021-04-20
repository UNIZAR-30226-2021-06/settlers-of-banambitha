package es.susangames.catan.logica;

/*
 * La clase coordenadas representa las coordenadas en un plano.
 * */
public class Coordenadas {
	/*
	 * Representa el valor de la coordenada x.
	 * */
	private double x;
	/*
	 * Representa el valor de la coordenada y.
	 * */
	private double y;
	
	/*
	 * Devuelve una clase Coordenadas que representa las coordenadas en un plano tomando como
	 * valores los parametros de entrada.
	 * 
	 * @param	x representa el valor de la coordenada x en el plano.
	 * @param	y representa el valor de la coordenada y en el plano.
	 * */
	public Coordenadas(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/*
	 * Actualiza el valor de la coordenada x.
	 * @param newX	nuevo valor de la coordenada x.
	 * */
	public void setX (int newX) {
		this.x = newX;
	}
	
	/*
	 * Devuelve el valor de la coordenada x en el plano.
	 * @return valor de la coordenada x
	 * */
	public double getX () {
		return this.x;
	}
	
	/*
	 * Actualiza el valor de la coordenada y.
	 * @param newY	nuevo valor de la coordenada y.
	 * */
	public void setY (int newY) {
		this.y = newY;
	}
	
	/*
	 * Devuelve el valor de la coordenada y en el plano.
	 * @return valor de la coordenada y.
	 * */
	public double getY () {
		return this.y;
	}
	
	/*
	 * Dados los valores de las coordenada x e y del plano, calcula el valor hash.
	 * @return valor que se obtiene al aplicar a las coordenadas x e y una función hash.
	 * */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/*
	 * Devuelve si el objeto obj es igual a un objeto de clase Coordenadas.
	 * 
	 * @return true si son el mismo objeto o si el valor de las coordenadas x e y de ambos objectos
	 * 			son iguales.
	 * */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coordenadas other = (Coordenadas) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}

	/*
	 * Dado un objeto coord, devuelve la distancia en un plano de las dos coordenadas.
	 * 
	 * @return	devuelve la distancia en un plano de dos coordenadas.
	 * */
	public double getDistancia (Coordenadas coord) {
		return Math.sqrt( Math.abs(coord.getX() - this.x) + Math.abs(coord.getY() - this.y) );
	}
}
