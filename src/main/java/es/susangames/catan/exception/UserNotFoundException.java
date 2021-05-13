package es.susangames.catan.exception;

public class UserNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public UserNotFoundException(String usuarioId) {
		super("User by id " + usuarioId + " was not found");
	}

}
