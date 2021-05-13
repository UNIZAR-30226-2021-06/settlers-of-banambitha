package es.susangames.catan.gameDispatcher;

public class Session {

	private String usuarioId;
	private String salaId;
	private String partidaId;
	
	public Session(String usuarioId) {
		this.usuarioId = usuarioId;
		salaId = null;
		partidaId = null;
	}

	public String getUsuarioId() {
		return usuarioId;
	}

	public String getSalaId() {
		return salaId;
	}

	public void setSalaId(String salaId) {
		this.salaId = salaId;
	}

	public String getPartidaId() {
		return partidaId;
	}

	public void setPartidaId(String partidaId) {
		this.partidaId = partidaId;
	}
}
