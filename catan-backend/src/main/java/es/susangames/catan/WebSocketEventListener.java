package es.susangames.catan;

import es.susangames.catan.WSController.SalaController;
import es.susangames.catan.gameDispatcher.MoveCarrierHeap;
import es.susangames.catan.gameDispatcher.Session;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Component
public class WebSocketEventListener {
	
	private static Map<String,Session> sesiones = new HashMap<String, Session>();
	
	@EventListener
	private void onConnectedEventHandler(SessionConnectedEvent event ) {
		
		StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(event.getMessage());
		
		String sessionId = stompHeaderAccessor.getSessionId();
		String userId = getUserId(stompHeaderAccessor);
		
		synchronized (sesiones) {
			sesiones.put(sessionId, new Session(userId));
		}
		
		System.out.println("\u001B[1m " + sessionId + "\u001B[0m connected with userId : \u001B[1m " + userId + " \u001B[0m\n");
	}
	
	@EventListener
	private void onDisconnectEventHandler(SessionDisconnectEvent event) {
		
		StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(event.getMessage());
		
		String sessionId = stompHeaderAccessor.getSessionId();
		
		Session sesion;
		
		synchronized (sesiones) {
			sesion = sesiones.remove(sessionId);
		}
		
		if(sesion!=null) {
			
			String salaId 	 = sesion.getSalaId();
			String partidaId = sesion.getPartidaId();
			String usuarioId = sesion.getUsuarioId();
			
			if(salaId!=null) {
				
				JSONObject mensaje = new JSONObject();
				mensaje.put("room", salaId);
				mensaje.put("player", usuarioId);
				
				SalaController.abandonarPartida(mensaje.toString());
			
			} else if(partidaId!=null) {
				
				JSONObject jugada = new JSONObject();
				jugada.put("left", true);
				jugada.put("player", usuarioId);
				
				MoveCarrierHeap.newJugada(partidaId, jugada);
			}
			
			System.out.println("\u001B[1m " + sessionId + "\u001B[0m disconnected \n");
			
		} else {
			
			System.out.println("Unknown session with sessionId: \u001B[1m " + sessionId + "\u001B[0m disconnected \n");
		}
	}
	
	@EventListener
	private void onSuscribeEventHandler(SessionSubscribeEvent event) {
		
		StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(event.getMessage());
		
		String sessionId = stompHeaderAccessor.getSessionId();
		String suscription = stompHeaderAccessor.getDestination();
		
		if(suscription.contains(WebSocketConfig.TOPIC_SALA_ACT)) {
			
			String salaId = suscription.split("/",3)[2];
			
			synchronized (sesiones) {
				Session sesion = sesiones.get(sessionId);
				if(sesion!=null) sesion.setSalaId(salaId);
			}
			
		} else if (suscription.contains(WebSocketConfig.TOPIC_PARTIDA_ACT)) {
			
			String partidaId = suscription.split("/",3)[2];
			
			synchronized (sesiones) {
				Session sesion = sesiones.get(sessionId);
				if(sesion!=null) sesion.setPartidaId(partidaId);
			}
		}

		System.out.println("\u001B[1m " + sessionId + "\u001B[0m suscribed to \u001B[1m " + suscription + " \u001B[0m\n");
	}
	
	@EventListener
	private void onUnsuscribeEventHandler(SessionUnsubscribeEvent event) {
		
		StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(event.getMessage());
		
		String sessionId = stompHeaderAccessor.getSessionId();
		String suscription = stompHeaderAccessor.getSubscriptionId();
		
		if(suscription.contains(WebSocketConfig.TOPIC_SALA_ACT)) {
			
			synchronized (sesiones) {
				Session sesion = sesiones.get(sessionId);
				if(sesion!=null) sesion.setSalaId(null);
			}
			
		} else if (suscription.contains(WebSocketConfig.TOPIC_PARTIDA_ACT)) {
			
			synchronized (sesiones) {
				Session sesion = sesiones.get(sessionId);
				if(sesion!=null) sesion.setPartidaId(null);
			}
		}

		System.out.println("\u001B[1m " + sessionId + "\u001B[0m unsuscribed from \u001B[1m " + suscription + " \u001B[0m\n");
	}
	
	
	
	
	private String getUserId(StompHeaderAccessor accessor) {
	    GenericMessage<?> generic = (GenericMessage<?>) accessor.getHeader(SimpMessageHeaderAccessor.CONNECT_MESSAGE_HEADER);
	    if (generic!=null) {
	        SimpMessageHeaderAccessor nativeAccessor = SimpMessageHeaderAccessor.wrap(generic);
	        List<String> userIdValue = nativeAccessor.getNativeHeader("user-id");

	        return (userIdValue==null) ? null : userIdValue.stream().findFirst().orElse(null);
	    }

	    return null;
	}
}