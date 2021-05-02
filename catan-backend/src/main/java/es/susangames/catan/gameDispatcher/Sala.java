package es.susangames.catan.gameDispatcher;

import java.util.ArrayList;
import java.util.List;

public class Sala {

	private static AutoId salaId = new AutoId('<','>');
	
	private String Id;
	private String leader;
	private List<String> players;
	private List<String> invites;
	
	public Sala(String leader) {
		
		this.Id = salaId.nextId();
		this.leader = leader;
		this.players = new ArrayList<String>();
		players.add(leader);
		this.invites = new ArrayList<String>();
		
	}
	
	public void invitar(String invitado) {
		invites.add(invitado);
	}
	
	public void eliminarInvitacion(String invitado) {
		invites.remove(invitado);
	}
	
	public boolean eliminarJugador(String jugador) {
		
		if (leader.contentEquals(jugador)) {
			
			players.remove(jugador);
			leader = players.get(0);
			return true;
			
		} else return players.remove(jugador);
	}
	
	public boolean comprobarJugador(String jugador) {
		return players.contains(jugador);
	}
	
	public boolean aceptarInvitacion(String invitado) {
		return invites.remove(invitado) && players.size()<4 && players.add(invitado);
	}
	
	public int size() {
		return players.size();
	}
	
	public String getId() {
		return Id;
	}

	public String getLeader() {
		return leader;
	}

	public List<String> getPlayers() {
		return players;
	}

	public List<String> getInvites() {
		return invites;
	}
	
}
