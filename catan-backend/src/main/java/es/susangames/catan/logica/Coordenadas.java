package es.susangames.catan.logica;

public class Coordenadas {
	private double x;
	private double y;
	
	public Coordenadas(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void setX (int newX) {
		this.x = newX;
	}
	
	public double getX () {
		return this.x;
	}
	
	public void setY (int newY) {
		this.y = newY;
	}
	
	public double getY () {
		return this.y;
	}
	
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

	public double getDistancia (Coordenadas coord) {
		return Math.sqrt( Math.abs(coord.getX() - this.x) + Math.abs(coord.getY() - this.y) );
	}
}
