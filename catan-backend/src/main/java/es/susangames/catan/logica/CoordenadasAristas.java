package logica;


/*
 * Clase hija de Coordenadas.
 * Esta clase representa las coordenadas un punto de un segmento.
 * */
public class CoordenadasAristas extends Coordenadas {
	/*
	 * */
	private double fin_x;
	private double fin_y;
	
	/*
	 * Constructor de la clase CoordenadasAristas, representa las coordenadas segmento en un plano
	 * @param ini_x coordenadas del eje x del primer punto que forma el segmento.
	 * @param ini_y coordenadas del eje y del primer punto que forma el segmento.
	 * @param fin_x coordenadas del eje x del segundo punto que forma el segmento.
	 * @param fin_y coordenadas del eje y del segundo punto que forma el segmento. 
	 * */
	CoordenadasAristas (double ini_x, double ini_y, double fin_x, double fin_y) {
		super(ini_x, ini_y);
		this.fin_x = fin_x;
		this.fin_y = fin_y;
	}

	/*
	 * @return la coordenada x del segundo punto que forma el segmento.
	 * */
	public double getFin_x() {
		return fin_x;
	}
	
	/*
	 * Actualiza el valor de la variable fin_x.
	 * @param fin_x nuevo valor de la variable fin_x.
	 * */
	public void setFin_x(double fin_x) {
		this.fin_x = fin_x;
	}

	/*
	 * @return la coordenada x del segundo punto que forma el segmento.
	 * */
	public double getFin_y() {
		return fin_y;
	}

	/*
	 * Actualiza el valor de la variable fin_y.
	 * @param fin_y nuevo valor de la variable fin_x.
	 * */
	public void setFin_y(double fin_y) {
		this.fin_y = fin_y;
	}

	/*
	 * @return dados los valores de x e y del punto inicial y del punto final se aplica una 
	 * función hash y se suma el resultado del punto inicial y final
	 * */
	@Override
	public int hashCode() {
		final int prime = 31;
		int from_result = super.hashCode();
		int to_result = 1;
		long temp;
		temp = Double.doubleToLongBits(fin_x);
		to_result = prime * to_result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(fin_y);
		to_result = prime * to_result + (int) (temp ^ (temp >>> 32));
		return to_result + from_result;
	}

	/*
	 * Devuelve si el objeto obj es igual a un objeto de clase Coordenadas.
	 * 
	 * @return true si son el mismo objeto o si el valor de los punto inicial y final de las 
	 * 	clases son iguales o si el punto inicial de la primera clase es el mismo que el punto
	 * 	final de la segunad clase y viceversa.
	 * */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CoordenadasAristas other = (CoordenadasAristas) obj;
		long x11 = Double.doubleToLongBits(this.getX());
		long y11 = Double.doubleToLongBits(this.getY());
		long x12 = Double.doubleToLongBits(this.fin_x);
		long y12 = Double.doubleToLongBits(this.fin_y);
		
		long x21 = Double.doubleToLongBits(other.getX());
		long y21 = Double.doubleToLongBits(other.getY());
		long x22 = Double.doubleToLongBits(other.fin_x);
		long y22 = Double.doubleToLongBits(other.fin_y);
		
		if ( ( (x11 == x21) && (y11 == y21) ) &&
				( (x12 == x22 ) && (y12 == y22) ) )
			return true;
		if ( ( (x11 == x22) && (y11 == y22) ) &&
				( (x21 == x12) && (y21 == y12) ) )
			return true;
		return false;
	}
	
	public Boolean contieneCoordenada (Coordenadas c) {
		try {
			if (c == null) {
				throw new Exception ("function contieneCoordenada (Coordenadas c): El valor del parametro c no puede ser nulo.");
			}
			return (c.getX() == this.getX() && c.getY() == this.getY() ) ||
					( c.getX() == this.getFin_x() && c.getY() == this.getFin_y() );
		} catch (Exception e) {
			System.out.print(e.toString());
			return false;
		}
	}
}
