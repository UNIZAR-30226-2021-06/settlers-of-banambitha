package es.susangames.catan.logica;

import org.json.JSONException;
import org.json.JSONObject;

public class Jugadores {
	private ColorJugador color;
	
	private Integer puntosVictoria;
	
	// RECURSOS
	private Integer madera;
	private Integer lana;
	private Integer cereales;
	private Integer arcilla;
	private Integer mineral;

	// CARTAS DESARROLLO
	private Integer numCartasCaballeros;
	private Integer numCartasPuntoVictoria;
	private Integer numCartasContruccionCarreteras;
	private Integer numCartasDescubrimiento;
	private Integer numCartasMonopolio;
	
	// CARTAS ESPECIALES
	private Boolean granRutaComercial;
	private Boolean granEjecitoCaballeria;
	
	// ASENTAMIENTO
	private int MAX_NUM_POBLADOS = 5;
	private int MAX_NUM_CIUDADES = 4;
	private int MAX_NUM_CARRETERAS = 15;
	
	private int numPobladosConstruidos;
	private int numCiudadesConstruidos;
	private int numCaminosConstruidos;
	
	public Jugadores (ColorJugador color) {
		this.color = color;
		this.puntosVictoria = 0;
		this.madera = 0; this.lana = 0; this.cereales = 0; this.arcilla = 0; this.mineral = 0;
		
		this.numCartasCaballeros = 0;
		this.numCartasPuntoVictoria = 0;
		this.numCartasContruccionCarreteras = 0;
		this.numCartasDescubrimiento = 0;
		this.numCartasMonopolio = 0;
		
		this.granRutaComercial = false;
		this.granEjecitoCaballeria = false;
		
		this.numPobladosConstruidos = 0;
		this.numCiudadesConstruidos = 0;
		this.numCaminosConstruidos = 0;
	}
	
	public void anyadirRecurso (TipoTerreno tipo, Boolean produceCiudad) {
		int numProducidos = 1;
		if (produceCiudad) numProducidos = 2;
		switch (tipo) {
		case Bosque:
			madera+=numProducidos;
			break;
		case Pasto:
			lana+=numProducidos;
			break;
		case Sembrado:
			cereales+=numProducidos;
			break;
		case Cerro:
			arcilla+=numProducidos;
			break;
		case Montanya:
			mineral+=numProducidos;
			break;
		}
	}
	
	public Boolean puedeConstruirPueblo () {
		return madera >= 1 && lana >= 1 && cereales >= 1 && arcilla >= 1;
	}
	
	public Boolean puedeConstruirCiudad () {
		// Materiales necesarios: mineral x3, cereales x2.
		return mineral >= 3 && cereales >= 2;
	}
	
	public Boolean puedeConstruirCarretera () {
		return madera >= 1 && arcilla >= 1;
	}
	
	public void actualizarPuntosVictoria () {
		this.puntosVictoria = this.numCartasPuntoVictoria + this.numPobladosConstruidos +
				this.numCiudadesConstruidos * 2;
	}
	
	public Integer getPuntosVictoria () {
		return this.puntosVictoria;
	}
	
	public Boolean mismoJugador (Jugadores j) {
		return this == j;
	}
	
	public void eliminarRecursos() {
		int num_recursos = madera + lana + cereales + arcilla + mineral;
		// Quitamos la mitad de los recursos solamente si tiene m�s de 7 en total.
		if (num_recursos > 7) {
			int recursos_eliminar = num_recursos / 2;
			for (int i = 0; i < recursos_eliminar; i++) {
				switch(i%5) {
				case 0:
					if(madera>0) {madera--; break;}
				case 1:
					if(lana>0) {lana--; break;}
				case 2:
					if(cereales>0) {cereales--; break;}
				case 3:
					if(arcilla>0) {arcilla--; break;}
				case 4:
					if(mineral>0) {mineral--; break;}
				}
			}
		}
	}
	
	public ColorJugador getColor () {
		return color;
	}
	
	public Boolean puedeConstruirCartasDesarrollo () {
		// Material: lana x1, mineral x1, cereales x1
		return lana>=1 && mineral >=1 && cereales>=1;
	}
	
	public void construirAsentamiento () {
		// Eliminar recursos. --> madera >= 1 && lana >= 1 && cereales >= 1 && arcilla >= 1;
		this.madera--;
		this.lana--;
		this.cereales--;
		this.arcilla--;
		this.numPobladosConstruidos++;
	}
	
	public void mejorarAsentamiento () {
		// Eliminar recursos. -->  mineral x3, cereales x2.
		this.mineral -= 3;
		this.cereales -= 2;
		this.numPobladosConstruidos--;
		this.numCiudadesConstruidos++;
	}
	
	public void construirCarretera () {
		// Eliminar recursos. --> madera >= 1 && arcilla >= 1;
		this.madera--;
		this.arcilla--;
		this.numCaminosConstruidos++;
	}
	
	public void construirCartasDesarrollo () {
		// Caballero, Puntos de victoria, Construcci�n de carreteras, Descubrimiento o Monopolio.
		int carta = (int) Math.floor(Math.random()*4);
		switch(carta) {
		case 0:
			this.numCartasCaballeros++;
			break;
		case 1:
			this.numCartasContruccionCarreteras++;
			break;
		case 2:
			this.numCartasDescubrimiento++;
			break;
		case 3:
			this.numCartasMonopolio++;
			break;
		case 4:
			this.numCartasPuntoVictoria++;
			break;
		}
	}
	
	public void usarCartaCaballero() {
		// mover Ladron 
		
	}
	
	public void usarCartasPuntosVictoria() {
		this.puntosVictoria++;
	}
	
	public void usarCartaMonopolio () {}
	
	public void usarCartaDescubrimiento() {}
	
	public void usarCartasConstruccionCarretera() {}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
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
		Jugadores other = (Jugadores) obj;
		if (color != other.color)
			return false;
		return true;
	}
	
	public JSONObject recursosJugadorToJSON () throws JSONException {
		return new JSONObject ("\"Player_" + this.color.numeroColor() + 1 + "\": [" + this.madera + "," + 
				this.lana + "," + this.cereales + "," + this.arcilla + "," + this.mineral + "]");
	}
	
	public JSONObject cartasJugadorToJSON () throws JSONException {
		Integer numGranEjecitoCaballeria = 1;
		if (this.granEjecitoCaballeria) numGranEjecitoCaballeria = 0;
		Integer numGranRutaComercial = 1;
		if (this.granRutaComercial) numGranRutaComercial = 0;
		return new JSONObject ("\"Player_" + this.color.numeroColor() + 1 + "\": [" + this.numCartasCaballeros + "," +
				this.numCartasPuntoVictoria + "," +this.numCartasContruccionCarreteras + "," + this.numCartasDescubrimiento +
				"," + this.numCartasMonopolio + "," + numGranEjecitoCaballeria + "," + numGranRutaComercial + "]");
	}
}
