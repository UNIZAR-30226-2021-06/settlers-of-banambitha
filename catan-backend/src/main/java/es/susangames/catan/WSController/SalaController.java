package es.susangames.catan.WSController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import es.susangames.catan.WebSocketConfig;
import es.susangames.catan.gameDispatcher.Cola;
import es.susangames.catan.gameDispatcher.Matchmaker;
import es.susangames.catan.gameDispatcher.Sala;
import es.susangames.catan.service.UsuarioService;

@Controller
public class SalaController {
	
	private Matchmaker matchmaker;
	
	private Cola cola;
	private SimpMessagingTemplate template;
	private UsuarioService usuarioService;
	private Map<String,Sala> salas;
	
	@Autowired
	public SalaController(SimpMessagingTemplate template, UsuarioService usuarioService) {

		this.template = template;
		this.cola = new Cola();
		this.salas = new HashMap<String, Sala>();
		this.usuarioService = usuarioService;
		matchmaker = new Matchmaker(template, cola, usuarioService);
		
		Thread matchmakerThread = new Thread(matchmaker,"Matchmaker");
		
		matchmakerThread.start();
	
	}
	
	
	/* ******************************************************
	 * Maps: 	Game Room Creation
	 * 
	 * Expects: -JSON Message
	 * 			-Mapped point: 		/app/sala/crear
	 * 			-Format:
	 * 				{
	 * 					"leader"	: <playerId>
	 * 				}
	 * 
	 * Returns: -JSON Message
	 * 			-Broadcast point:	/sala-crear/<liderId>
	 * 			-Format:
	 * 				{
	 * 					"status"	: "CREATED",
	 * 					"leader"	: <liderId>,
	 * 					"room"		: <salaId>,
	 * 					"players" 	: [liderId],
	 * 					"invites"	: []
	 *				}
	****************************************************** */
	@MessageMapping("/sala/crear")
	public void crearSala(String mensaje) {

		JSONObject message = new JSONObject(mensaje);
		
		String lider = message.getString("leader");
		Sala sala = new Sala(lider);
		String salaId = sala.getId();

		salas.put(salaId, sala);
		
		JSONArray players = new JSONArray(sala.getPlayers());
		JSONArray invites = new JSONArray(sala.getInvites());
		
		message.put("status", "CREATED");
		message.put("room", salaId);
		message.put("players", players);
		message.put("invites", invites);
		
		usuarioService.setSala(lider, salaId);
		
		template.convertAndSend(WebSocketConfig.TOPIC_SALA_CREAR + "/" + lider, message.toString());
	}
	
	/* ******************************************************
	 * Maps: 	Game Room Erasure
	 * 
	 * Expects: -JSON Message
	 * 			-Mapped point: 		/app/sala/cerrar
	 * 			-Format:
	 * 				{
	 * 					"leader"	: <liderId>,
	 * 					"room"		: <salaId>
	 *				}
	 * 
	 * Returns: -JSON Message
	 * 			-Broadcast point:	/sala-act/<salaId> | /invitacion/<playerId>
	 * 			-Format:
	 * 				{
	 * 					"status"	: "CLOSED",
	 * 					"leader"	: <liderId>,
	 * 					"room"		: <salaId>
	 *				}
	****************************************************** */
	@MessageMapping("/sala/cerrar")
	public void cerrarSala(String mensaje) {
		
		JSONObject message = new JSONObject(mensaje);
		
		String salaId = message.getString("room");
		String liderId = message.getString("leader");
		
		Sala sala = salas.remove(salaId);

		if(sala != null && sala.getLeader().contentEquals(liderId)) {
			message.put("status", "CLOSED");
			
			for(String jugador : sala.getPlayers()) {
				usuarioService.leaveSala(jugador);
			}
			
			for(String invitado : sala.getInvites()) {
			
				template.convertAndSend(WebSocketConfig.TOPIC_INVITACION + "/" + invitado, message.toString());
			}
		} else {
			message.put("status", "FAILED");
		}
		
		template.convertAndSend(WebSocketConfig.TOPIC_SALA_ACT + "/" + salaId, message.toString());
	}
	
	/* ******************************************************
	 * Maps: 	Game Room Exit
	 * 
	 * Expects: -JSON Message
	 * 			-Mapped point: 		/app/sala/abandonar
	 * 			-Format:
	 * 				{
	 * 					"leader"	: <liderId>,
	 * 					"room"		: <salaId>,
	 * 					"player"	: <playerId>
	 *				}
	 * 
	 * Returns: -JSON Message
	 * 			-Broadcast point:	/sala-act/<salaId>
	 * 			-Format:
	 * 				{
	 * 					"status"	: "UPDATED-PLAYERS",
	 * 					"players"	: [previous players - <playerId>],
	 *				}
	 *				or
	 *				{
	 * 					"status"	: "CLOSED",
	 * 					"leader"	: <liderId>,
	 * 					"room"		: <salaId>
	 *				}
	 *
	 *			OPTIONAL
	 *			-Broadcast point:	/invitacion/<invitadosId>
	 * 			-Format:
	 * 				{
	 * 					"status"	: "OPEN" | "CLOSED",
	 * 					"leader"	: <liderId>,
	 * 					"room"		: <salaId>
	 *				}
	****************************************************** */
	@MessageMapping("/sala/abandonar")
	public void abandonarPartida(String mensaje) {
		
		JSONObject message = new JSONObject(mensaje);
		
		String salaId = message.getString("room");
		String liderId = message.getString("leader");
		String jugador = message.getString("player");
		
		JSONObject actualizacion = new JSONObject();
		JSONObject invitacion = new JSONObject();
		
		boolean existente = false;
		
		Sala sala = salas.get(salaId);
		
		existente = sala!=null && sala.getLeader().contentEquals(liderId) && sala.eliminarJugador(jugador);
			
		if(!existente) {
			
			synchronized (cola) {
				sala = cola.desencolar(salaId);
			}
			
			existente = sala!=null && sala.getLeader().contentEquals(liderId) && sala.eliminarJugador(jugador);
		}
		
		if(existente) {
			
			if(jugador.contentEquals(liderId)) {
				
				actualizacion.put("status", "CLOSED");
				actualizacion.put("leader", liderId);
				actualizacion.put("room", salaId);
				
				invitacion.put("status", "CLOSED");
				
				for(String player : sala.getPlayers()) {
					usuarioService.leaveSala(player);
				}
				
			} else {
				
				actualizacion.put("status", "UPDATED-PLAYERS");
				JSONArray players = new JSONArray(sala.getPlayers());
				actualizacion.put("players", players);
				
				invitacion.put("status", "OPEN");
				
				usuarioService.leaveSala(jugador);
			}
			
			template.convertAndSend(WebSocketConfig.TOPIC_SALA_ACT + "/" + salaId, actualizacion);
			
			invitacion.put("leader", liderId);
			invitacion.put("room", salaId);
			
			for(String invitado : sala.getInvites()) {
				template.convertAndSend(WebSocketConfig.TOPIC_INVITACION + "/" + invitado, invitacion);
			}
		}
	}
	
	/* ******************************************************
	 * Maps: 	Game Invites
	 * 
	 * Expects: -JSON Message
	 * 			-Mapped point: 		/app/invitacion/enviar
	 * 			-Format:
	 * 				{
	 * 					"leader"	: <liderId>,
	 * 					"room"		: <salaId>,
	 * 					"invite"	: <invitadoId>
	 *				}
	 * 
	 * Returns: -JSON Message
	 * 			-Broadcast point:	/invitacion/<invitadoId>
	 * 			-Format:
	 * 				{
	 * 					"status"	: "INVITED",
	 * 					"leader"	: <liderId>,
	 * 					"room"		: <salaId>
	 *				}
	 *
	 *			-Broadcast point:	/sala-act/<salaId>
	 *			-Format:
	 *				{
	 * 					"status"	: "UPDATED-INVITES",
	 * 					"invites"	: [previous invites , <invitadoId>]
	 *				}
	****************************************************** */
	@MessageMapping("/invitacion/enviar")
	public void enviarInvitacion(String mensaje) {
		
		JSONObject message = new JSONObject(mensaje);
		
		String liderId 	= message.getString("leader");
		String salaId 	= message.getString("room");
		String invitado = message.getString("invite");
		
		Sala sala = salas.get(salaId);
		
		if(sala != null && sala.getLeader().contentEquals(liderId)) {
			
			sala.invitar(invitado);
			
			JSONObject invitacion = new JSONObject();
			invitacion.put("status", "INVITED");
			invitacion.put("leader", liderId);
			invitacion.put("room", salaId);
			
			template.convertAndSend(WebSocketConfig.TOPIC_INVITACION + "/" + invitado, invitacion.toString());
			
			JSONObject actualizacion = new JSONObject();
			JSONArray invitados = new JSONArray(sala.getInvites());
			
			actualizacion.put("status", "UPDATED-INVITES");
			actualizacion.put("invites", invitados);
			
			template.convertAndSend(WebSocketConfig.TOPIC_SALA_ACT + "/" + salaId, actualizacion.toString());
		}
		
	}
	
	/* ******************************************************
	 * Maps: 	Game Invite Cancels and Rejections
	 * 
	 * Expects: -JSON Message
	 * 			-Mapped point: 		/app/invitacion/cancelar
	 * 			-Format:
	 * 				{
	 * 					"leader"	: <liderId>,
	 * 					"room"		: <salaId>,
	 * 					"invite"	: <invitadoId>
	 *				}
	 * 
	 * Returns: -JSON Message
	 * 			-Broadcast point:	/invitacion/<invitadoId>
	 * 			-Format:
	 * 				{
	 * 					"status"	: "CANCELLED",
	 * 					"leader"	: <liderId>,
	 * 					"room"		: <salaId>
	 *				}
	 *
	 *			-Broadcast point:	/sala-act/<salaId>
	 *			-Format:
	 *				{
	 * 					"status"	: "UPDATED-INVITES",
	 * 					"invites"	: [previous invites - <invitadoId>]
	 *				}
	****************************************************** */
	@MessageMapping("/invitacion/cancelar")
	public void cancelarInvitacion(String mensaje) {
		
		JSONObject message = new JSONObject(mensaje);
		
		String liderId 	= message.getString("leader");
		String salaId 	= message.getString("room");
		String invitado = message.getString("invite");
		
		Sala sala = salas.get(salaId);
		
		if(sala != null && sala.getLeader().contentEquals(liderId)) {
			
			sala.eliminarInvitacion(invitado);
			
			JSONObject invitacion = new JSONObject();
			invitacion.put("status", "CANCELLED");
			invitacion.put("leader", liderId);
			invitacion.put("room", salaId);
			
			template.convertAndSend(WebSocketConfig.TOPIC_INVITACION + "/" + invitado, invitacion.toString());
			
			JSONObject actualizacion = new JSONObject();
			JSONArray invitados = new JSONArray(sala.getInvites());
			
			actualizacion.put("status", "UPDATED-INVITES");
			actualizacion.put("invites", invitados);
			
			template.convertAndSend(WebSocketConfig.TOPIC_SALA_ACT + "/" + salaId, actualizacion.toString());
		}
	}
	
	/* ******************************************************
	 * Maps: 	Game Invite Acceptances
	 * 
	 * Expects: -JSON Message
	 * 			-Mapped point: 		/app/invitacion/aceptar
	 * 			-Format:
	 * 				{
	 * 					"leader"	: <liderId>,
	 * 					"room"		: <salaId>,
	 * 					"invite"	: <invitadoId>
	 *				}
	 * 
	 * Returns: -JSON Message
	 * 			-Broadcast point:	/invitacion/<invitadoId>
	 * 			-Format:
	 * 				{
	 * 					"status"	: "ACCEPTED" | "FULL",
	 * 					"leader"	: <liderId>,
	 * 					"room"		: <salaId>,
	 * 					"players"	: [previous players, <invitadoId>],
	 * 					"invites"	: [previous invites - <invitadoId>]
	 *				}
	 *
	 *			-Broadcast point:	/sala-act/<salaId>
	 *			-Format:
	 *				{
	 * 					"status"	: "UPDATED-PLAYERS",
	 * 					"players"	: [previous players, <invitadoId>],
	 * 					"invites"	: [previous invites - <invitadoId>]
	 *				}
	****************************************************** */
	@MessageMapping("/invitacion/aceptar")
	public void aceptarInvitacion(String mensaje) {
		
		JSONObject message = new JSONObject(mensaje);
		
		String liderId 	= message.getString("leader");
		String salaId 	= message.getString("room");
		String invitado = message.getString("invite");
		
		Sala sala = salas.get(salaId);
		
		if(sala != null && sala.getLeader().contentEquals(liderId)) {
			
			JSONObject invitacion = new JSONObject();
			invitacion.put("leader", liderId);
			invitacion.put("room", salaId);
			
			if(sala.aceptarInvitacion(invitado)) {
				
				usuarioService.setSala(invitado, salaId);
				
				if(sala.size()==4) {
					JSONObject actualizacionInvitacion = new JSONObject();
					
					actualizacionInvitacion.put("status", "FULL");
					actualizacionInvitacion.put("leader", liderId);
					actualizacionInvitacion.put("room", salaId);
					
					for(String i : sala.getInvites()) {
						template.convertAndSend(WebSocketConfig.TOPIC_INVITACION + "/" + i, actualizacionInvitacion.toString());
					}
				}
				
				JSONObject actualizacion = new JSONObject();
				JSONArray jugadores = new JSONArray(sala.getPlayers());
				JSONArray invitados = new JSONArray(sala.getInvites());
				
				invitacion.put("status", "ACCEPTED");
				invitacion.put("players", jugadores);
				invitacion.put("invites", invitados);
				template.convertAndSend(WebSocketConfig.TOPIC_INVITACION + "/" + invitado, invitacion.toString());
				
				actualizacion.put("status", "UPDATED-PLAYERS");
				actualizacion.put("players", jugadores);
				actualizacion.put("invites", invitados);
				
				template.convertAndSend(WebSocketConfig.TOPIC_SALA_ACT + "/" + salaId, actualizacion.toString());
				
			} else {
				invitacion.put("status", "FULL");
				template.convertAndSend(WebSocketConfig.TOPIC_INVITACION + "/" + invitado, invitacion.toString());
			}
		}
	}

	/* ******************************************************
	 * Maps: 	Search for Games
	 * 
	 * Expects: -JSON Message
	 * 			-Mapped point: 		/app/partida/busqueda/comenzar
	 * 			-Format:
	 * 				{
	 * 					"leader"	: <liderId>,
	 * 					"room"		: <salaId>
	 *				}
	 * 
	 * Returns: -JSON Message
	 * 			-Broadcast point:	/sala-act/<salaId>
	 * 			-Format:
	 * 				{
	 * 					"status"	: "SEARCHING" | "FAILED"
	 *				}
	 *
	 *			-Broadcast point:	/invitacion/<invitadoId>
	 * 			-Format:
	 * 				{
	 * 					"status"	: "QUEUING" | "CLOSED",
	 * 					"leader"	: <liderId>,
	 * 					"room"		: <salaId>
	 *				}
	****************************************************** */
	@MessageMapping("/partida/busqueda/comenzar")
	public void buscarPartidaGrupo(String mensaje) {
		
		JSONObject message = new JSONObject(mensaje);
		
		String liderId 	= message.getString("leader");
		String salaId 	= message.getString("room");
		
		JSONObject respuesta = new JSONObject();
		
		Sala sala = salas.remove(salaId);
		
		if(sala != null && sala.getLeader().contentEquals(liderId)) {
			
			List<String> invitados = sala.getInvites();
					
			if(sala.size() != 4) {
				
				synchronized (cola) {
					
					cola.encolar(sala);
					
					message.put("status", "QUEUING");
					for(String invitado : invitados) {
						
						template.convertAndSend(WebSocketConfig.TOPIC_INVITACION + "/" + invitado, message.toString());
					}
					
					respuesta.put("status", "SEARCHING");
					template.convertAndSend(WebSocketConfig.TOPIC_SALA_ACT + "/" + salaId, respuesta.toString());
					
					cola.notify();
				}
				
			} else {
				
				message.put("status", "CLOSED");
				for(String invitado : invitados) {
					
					template.convertAndSend(WebSocketConfig.TOPIC_INVITACION + "/" + invitado, message.toString());
				}
				
				matchmaker.privada(sala);
			}
		} else {
			respuesta.put("status", "FAILED");
			template.convertAndSend(WebSocketConfig.TOPIC_SALA_ACT + "/" + salaId, respuesta.toString());
		}
	}
	
	
	
	/* ******************************************************
	 * Maps: 	Game Search Cancels
	 * 
	 * Expects: -JSON Message
	 * 			-Mapped point: 		/app/partida/busqueda/cancelar
	 * 			-Format:
	 * 				{
	 * 					"leader"	: <liderId>,
	 * 					"room"		: <salaId>,
	 * 					"player"	: <playerId>
	 *				}
	 * 
	 * Returns: -JSON Message
	 * 			-Broadcast point:	/sala-act/<salaId>
	 * 			-Format:
	 * 				{
	 * 					"status"	: "CANCELLED" | "FAILED",
	 * 					"player"	: <playerId>
	 *				}
	 *
	 *			-Broadcast point:	/invitacion/<invitadoId>
	 * 			-Format:
	 * 				{
	 * 					"status"	: "OPEN" | "FULL",
	 * 					"leader"	: <liderId>,
	 * 					"room"		: <salaId>
	 *				}
	****************************************************** */
	@MessageMapping("/partida/busqueda/cancelar")
	public void cancelarBusquedaPartidaGrupo(String mensaje) {
		
		JSONObject message = new JSONObject(mensaje);
		
		String liderId 	= message.getString("leader");
		String salaId 	= message.getString("room");
		String playerId = message.getString("player");
		
		Sala sala;
		
		synchronized (cola) {
			sala = cola.desencolar(salaId);
		}
		
		if(sala!=null) salas.put(sala.getId(),sala);
		
		JSONObject respuesta = new JSONObject();
		
		String status = (sala!=null)?"CANCELLED":"FAILED";
		respuesta.put("status", status);
		respuesta.put("player", playerId);
		
		template.convertAndSend(WebSocketConfig.TOPIC_SALA_ACT + "/" + salaId, respuesta.toString());
		
		JSONObject invitacion = new JSONObject();
		
		status = (sala.size()<4)?"OPEN":"FULL";
		invitacion.put("status", status);
		invitacion.put("leader", liderId);
		invitacion.put("room", salaId);
		
		for(String invitado : sala.getInvites()) {
			template.convertAndSend(WebSocketConfig.TOPIC_INVITACION + "/" + invitado, invitacion.toString());
		}
	}
	
	
	/* ******************************************************
	 * Maps: 	Game Room Reload
	 * 
	 * Expects: -JSON Message
	 * 			-Mapped point: 		/app/sala/recargar
	 * 			-Format:
	 * 				{
	 * 					"room"		: <salaId>,
	 * 					"player"	: <jugadorId>
	 *				}
	 * 
	 * Returns: -JSON Message
	 * 			-Broadcast point:	/sala-rld/<playerId>
	 * 			-Format:
	 * 				{
	 * 					"status"	: "REJOINED" | "FAILED",
	 * 					"leader"	: <liderId>,
	 * 					"room"		: <salaId>,
	 * 					"players" 	: [<previousPlayers>],
	 * 					"invites"	: [<previousInvites>]
	 *				}
	 *
	 *			OPTIONAL
	 *			-JSON Message
	 * 			-Broadcast point:	/sala-act/<salaId>
	 * 			-Format:
	 * 				{
	 * 					"status"	: "CANCELLED",
	 * 					"player"	: <playerId>
	 *				}
	 *
	 *			OPTIONAL
	 *			-Broadcast point:	/invitacion/<invitadoId>
	 * 			-Format:
	 * 				{
	 * 					"status"	: "OPEN" | "FULL",
	 * 					"leader"	: <liderId>,
	 * 					"room"		: <salaId>
	 *				}
	****************************************************** */
	@MessageMapping("/sala/recargar")
	public void recargarSala(String mensaje) {
		
		JSONObject message = new JSONObject(mensaje);
		
		String salaId = message.getString("room");
		String jugador = message.getString("player");
		
		JSONObject reconexion = new JSONObject();
		JSONObject actualizacion = new JSONObject();
		JSONObject invitacion = new JSONObject();
		
		boolean queing = false;
		
		Sala sala = salas.get(salaId);
		
		if(sala==null) {
			
			synchronized (cola) {
				sala = cola.desencolar(salaId);
			}
			
			queing = sala!=null;
		}
		
		if(sala!=null && sala.comprobarJugador(jugador)) {
			
			String lider = sala.getLeader();
			List<String> players = sala.getPlayers();
			List<String> invites = sala.getInvites();
			JSONArray jugadores = new JSONArray(players);
			JSONArray invitados = new JSONArray(invites);
			
			reconexion.put("status", "REJOINED");
			reconexion.put("leader", lider);
			reconexion.put("room", salaId);
			reconexion.put("players", jugadores);
			reconexion.put("invites", invitados);
			template.convertAndSend(WebSocketConfig.TOPIC_SALA_CREAR + "/" + jugador, reconexion);
			
			if(queing) { 
			
				actualizacion.put("status", "CANCELLED");
				actualizacion.put("player", jugador);
				
				template.convertAndSend(WebSocketConfig.TOPIC_SALA_ACT + "/" + salaId, actualizacion);
				
				String status = (sala.size()<4)?"OPEN":"FULL";
				
				invitacion.put("status", status);
				invitacion.put("leader", lider);
				invitacion.put("room", salaId);
				
				for(String invitado : sala.getInvites()) {
					template.convertAndSend(WebSocketConfig.TOPIC_INVITACION + "/" + invitado, invitacion);
				}
			}
		} else {
			
			usuarioService.leaveSala(jugador);
			
			reconexion.put("status", "FAILED");
			template.convertAndSend(WebSocketConfig.TOPIC_SALA_CREAR + "/" + jugador, reconexion);
		}
	}
}
