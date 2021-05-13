package es.susangames.catan.WSController;

import java.sql.Timestamp;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


import es.susangames.catan.WebSocketConfig;

@Controller
public class ChatController{

	private final SimpMessagingTemplate template;
	
	@Autowired
	public ChatController(SimpMessagingTemplate template) {
		this.template = template;
	}
	
	/* ******************************************************
	 * Maps: 	Private chat messages
	 * 
	 * Expects: -JSON Message
	 * 			-Mapped point: 		/app/enviar/privado
	 * 			-Format:
	 * 				{
	 * 					"from" 	: <remitente>,
	 * 					"to"	: <destinatario>,
	 * 					"body"	: <cuerpo>
	 * 				}
	 * 
	 * Returns: -JSON Message
	 * 			-Broadcast point:	/chat/<destinatario>
	 * 			-Format:
	 * 				{
	 * 					"from"	: <remitente>,
	 * 					"body"	: <cuerpo>,
	 * 					"time"	: <marca de tiempo H:M>
	 *				}
	****************************************************** */
	@SuppressWarnings("deprecation")
	@MessageMapping("/enviar/privado")
	public void enviarMensajePrivado(String mensaje) {
		
		JSONObject obj = new JSONObject(mensaje);
		
		String remitente = obj.getString("from");
		String destinatario = obj.getString("to");
		String cuerpo = obj.getString("body");
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		int Hours = timestamp.getHours();
		int Mins = timestamp.getMinutes();
		String H_M = String.format("%d:%02d",Hours,Mins); 
		
		JSONObject nuevoMensaje = new JSONObject();
		nuevoMensaje.put("from", remitente);
		nuevoMensaje.put("body", cuerpo);
		nuevoMensaje.put("time", H_M);
		
		template.convertAndSend(WebSocketConfig.TOPIC_CHAT + "/" + destinatario, nuevoMensaje.toString());
	
	}
	
	
	/* ******************************************************
	 * Maps: 	In-game chat messages
	 * 
	 * Expects: -JSON Message
	 * 			-Mapped point: 		/app/enviar/partida
	 * 			-Format:
	 * 				{
	 * 					"from" 	: <remitente>,
	 * 					"game"	: <partida>,
	 * 					"body"	: <cuerpo>
	 * 				}
	 * 
	 * Returns: -JSON Message
	 * 			-Broadcast point:	/partida-chat/<partida>
	 * 			-Format:
	 * 				{
	 * 					"from"	: <remitente>,
	 * 					"body"	: <cuerpo>,
	 * 					"time"	: <marca de tiempo H:M>
	 *				}
	****************************************************** */
	@SuppressWarnings("deprecation")
	@MessageMapping("/enviar/partida")
	public void enviarMensajePartida(String mensaje) {
		
		JSONObject obj = new JSONObject(mensaje);
		
		Integer remitente = obj.getInt("from");
		String partida = obj.getString("game");
		String cuerpo = obj.getString("body");
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		int Hours = timestamp.getHours();
		int Mins = timestamp.getMinutes();
		String H_M = String.format("%d:%02d",Hours,Mins); 
		
		JSONObject nuevoMensaje = new JSONObject();
		nuevoMensaje.put("from", remitente);
		nuevoMensaje.put("body", cuerpo);
		nuevoMensaje.put("time", H_M);
		
		template.convertAndSend(WebSocketConfig.TOPIC_PARTIDA_CHAT + "/" + partida, nuevoMensaje.toString());
	}
	
}
