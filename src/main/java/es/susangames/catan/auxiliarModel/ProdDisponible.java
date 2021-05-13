package es.susangames.catan.auxiliarModel;

public class ProdDisponible {
	
	private String url;
	private String nombre;
	private Integer precio;
	private String tipo;
	private boolean adquirido;
	
	public ProdDisponible(String url, String nombre, Integer precio, String tipo, boolean adquirido) {
		this.url = url;
		this.nombre = nombre;
		this.precio = precio;
		this.tipo = tipo;
		this.adquirido = adquirido;
	}

	public String geturl() {
		return url;
	}

	public void seturl(String url) {
		this.url = url;
	}

	public String getnombre(){
		return this.nombre;
	}

	public void setnombre(String nombre){
		this.nombre = nombre;
	}

	public Integer getPrecio() {
		return precio;
	}

	public void setPrecio(Integer precio) {
		this.precio = precio;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public boolean isAdquirido() {
		return adquirido;
	}

	public void setAdquirido(boolean adquirido) {
		this.adquirido = adquirido;
	}
	
}
