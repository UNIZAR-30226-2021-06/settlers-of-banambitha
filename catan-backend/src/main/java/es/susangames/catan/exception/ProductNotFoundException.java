package es.susangames.catan.exception;

public class ProductNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public ProductNotFoundException(String productId) {
		super("Product with id " + productId + " was not found");
	}

}
