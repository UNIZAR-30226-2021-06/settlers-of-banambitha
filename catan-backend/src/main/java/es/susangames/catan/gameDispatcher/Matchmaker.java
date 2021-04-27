package es.susangames.catan.gameDispatcher;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import es.susangames.catan.WebSocketConfig;
import es.susangames.catan.service.UsuarioService;

public class Matchmaker implements Runnable {

	private SimpMessagingTemplate template;
	private UsuarioService usuarioService;
	private Cola cola;
	private MoveCarrierHeap moveCarrierHeap;
	
	public Matchmaker(SimpMessagingTemplate template, UsuarioService usuarioService) {
		this.template = template;
		this.cola = new Cola();
		this.usuarioService = usuarioService;
		this.moveCarrierHeap = new MoveCarrierHeap(template,usuarioService);
	}

	
	
	/* ******************************************************
	 * Returns: -JSON Message
	 * 			-Broadcast point:	/sala-act/<salaId>
	 * 			-Format:
	 * 				{
	 * 					"status"	: "FOUND",
	 * 					"game"		: <partidaId>,
	 * 					"players"	: [<player1Id>,<player2Id>,<player3Id>,<player4Id>]
	 *				}
	 *
	 *			-Broadcast point:	/invitacion/<invitadoId>
	 * 			-Format:
	 * 				{
	 * 					"status"	: "CLOSED",
	 * 					"leader"	: <liderId>,
	 * 					"room"		: <salaId>
	 *				}
	****************************************************** */
	@Override
	public void run() {
		
		List<Sala> salas = null;

		while(true) {
			
			do {
				salas = cola.emparejar();
				
				if (salas != null) {
					
					List<String> listaJugadores = new ArrayList<String>();
					
					for(Sala sala : salas) {
						listaJugadores.addAll(sala.getPlayers());
					}
					
					String partidaId = moveCarrierHeap.newGame(listaJugadores);
					
					JSONObject mensaje = new JSONObject();
					JSONArray jugadores = new JSONArray(listaJugadores);
					
					mensaje.put("status", "FOUND");
					mensaje.put("game", partidaId);
					mensaje.put("players", jugadores);
					
					for(Sala sala : salas) {
						template.convertAndSend(WebSocketConfig.TOPIC_SALA_ACT + "/" + sala.getId(), mensaje.toString());
						
						JSONObject invitacion = new JSONObject();
						invitacion.put("status", "CLOSED");
						invitacion.put("leader", sala.getLeader());
						invitacion.put("room", sala.getId());
						
						for(String invitado : sala.getInvites()) {
							template.convertAndSend(WebSocketConfig.TOPIC_INVITACION + "/" + invitado, invitacion.toString());
						}
					}
		 				
	 				for(String player : listaJugadores) {
	 					usuarioService.setPartida(player, partidaId);
	 					usuarioService.leaveSala(player);
	 				}
		 		}
				
			} while (salas != null); 
			
			cola.waitOnCola();
		}	
	}
	

	/* ******************************************************
	 * Returns: -JSON Message
	 * 			-Broadcast point:	/sala-act/<salaId>
	 * 			-Format:
	 * 				{
	 * 					"status"	: "FOUND",
	 * 					"game"		: <partidaId>,
	 * 					"players"	: [<player1Id>,<player2Id>,<player3Id>,<player4Id>]
	 *				}
	****************************************************** */
	public void privada(Sala sala) {
		
		List<String> jugadores = sala.getPlayers();
		
		String partidaId = moveCarrierHeap.newGame(jugadores);
		
		JSONObject mensaje = new JSONObject();
		JSONArray jugadoresArr = new JSONArray(jugadores);
		
		mensaje.put("status", "FOUND");
		mensaje.put("game", partidaId);
		mensaje.put("players", jugadoresArr);
		
		template.convertAndSend(WebSocketConfig.TOPIC_SALA_ACT + "/" + sala.getId(), mensaje.toString());	
		
		for(String player : jugadores) {
			usuarioService.setPartida(player, partidaId);
			usuarioService.leaveSala(player);
		}
	}
	
}
