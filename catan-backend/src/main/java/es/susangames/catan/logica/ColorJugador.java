package es.susangames.catan.logica;

public enum ColorJugador {
	Azul(0), Rojo(1), Amarillo(2), Verde(3);
	
	private int color;
	
	private ColorJugador( int color ) {
		this.color = color;
	}
	
	public Integer numeroColor () {
		return this.color;
	}
	
	public Boolean mismoColor(ColorJugador j) {
		return this.color == j.numeroColor();
	}
	
	public String getStringColor () {
		switch (color) {
		case 0:
			return "Azul";
		case 1:
			return "Rojo";
		case 2:
			return "Amarillo";
		case 3:
			return "Verde";
		default:
			return "Null";
		}
	}
}