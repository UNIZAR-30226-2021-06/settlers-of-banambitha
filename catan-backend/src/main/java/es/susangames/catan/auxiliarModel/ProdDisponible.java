package es.susangames.catan.auxiliarModel;

public class ProdDisponible {
	
	private String producto_id;
	private String url;
	private Integer precio;
	private String tipo;
	private boolean adquirido;
	
	public ProdDisponible(String producto_id, String url, Integer precio, String tipo, boolean adquirido) {
		this.producto_id = producto_id;
		this.url = url;
		this.precio = precio;
		this.tipo = tipo;
		this.adquirido = adquirido;
	}

	public String getProducto_id() {
		return producto_id;
	}

	public void setProducto_id(String producto_id) {
		this.producto_id = producto_id;
	}

	public String getUrl(){
		return this.url;
	}

	public void setUrl(String url){
		this.url = url;
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
