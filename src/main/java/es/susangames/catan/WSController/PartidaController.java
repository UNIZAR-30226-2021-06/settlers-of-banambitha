package es.susangames.catan.WSController;

import java.sql.Timestamp;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import es.susangames.catan.WebSocketConfig;
import es.susangames.catan.gameDispatcher.MoveCarrierHeap;
import es.susangames.catan.service.UsuarioService;

@Controller
public class PartidaController {
	
	private static final String REQUEST = "REQUEST";
	private static final String ACCEPT 	= "ACCEPT";
	private static final String DECLINE = "DECLINE";

	private final SimpMessagingTemplate template;

	@Autowired
	public PartidaController(SimpMessagingTemplate template, UsuarioService usuarioService) {
		this.template = template;
	}
	
	
	/* ******************************************************
	 * Maps: 	Player moves
	 * 
	 * Expects: -JSON Message
	 * 			-Mapped point: 		/app/partida/jugada
	 * 			-Format:
	 * 				{
	 * 					"player": <número de Jugador [1-4]>,
	 * 					"game"	: <partida>,
	 * 					"move" : {
	 * 						"name" : <nombre de jugada [Por Definir]>,
	 * 						--Resto de Parámetros dependiendo del tipo de Jugada--
	 * 						}
	 * 				}
	****************************************************** */
	@MessageMapping("/partida/jugada")
	public void realizarJugada(String mensaje) {
		
		JSONObject jugada = new JSONObject(mensaje);
		
		String partida = jugada.getString("game");
		
		MoveCarrierHeap.newJugada(partida, jugada);
	}
	
	
	/* ******************************************************
	 * Maps: 	Player moves
	 * 
	 * Expects: -JSON Message
	 * 			-Mapped point: 		/app/partida/recargar
	 * 			-Format:
	 * 				{
	 * 					"player": <playerId>,
	 * 					"game"	: <partida>,
	 * 					"reload": true
	 * 				}
	****************************************************** */
	@MessageMapping("/partida/recargar")
	public void recargarPartida(String mensaje) {
		
		JSONObject jugada = new JSONObject(mensaje);
		
		String partida = jugada.getString("game");
		
		MoveCarrierHeap.newJugada(partida, jugada);
	}
	
	
	/* ******************************************************
	 * Maps: 	Trade proposals
	 * 
	 * Expects: -JSON Message
	 * 			-Mapped point: 		/app/comercio/proponer
	 * 			-Format:
	 * 				{
	 * 					"from"	: <Jugador que hace la petición [1-4]>,
	 * 					"to"	: <Jugador al que se le propone [1-4]>,
	 * 					"game"	: <partida>,
	 * 					"res1" : {
	 * 						"type" : <Tipo de Recurso Ofrecido>,
	 * 						"cuan" : <Cantidad de Recurso Ofrecido>
	 * 					},
	 * 					"res2" : {
	 * 						"type" : <Tipo de Recurso Deseado>,
	 * 						"cuan" : <Cantidad de Recurso Deseado>
	 * 					}
	 * 				}
	 * 
	 * Returns: -JSON Message
	 * 			-Broadcast point:	/partida-com/<partida>/<Jugador al que se le propone [1-4]>
	 * 			-Format:
	 * 				{
	 * 					"type"	: "REQUEST"
	 * 					"from"	: <Jugador que hace la petición>,
	 * 					"res1"  : {
	 * 						"type" : <Tipo de Recurso Ofrecido>,
	 * 						"cuan" : <Cantidad de Recurso Ofrecido>
	 * 					},
	 * 					"res2" : {
	 * 						"type" : <Tipo de Recurso Deseado>,
	 * 						"cuan" : <Cantidad de Recurso Deseado>
	 * 					},
	 * 					"time	: <marca de tiempo H:M>
	 * 				}
	****************************************************** */
	@SuppressWarnings("deprecation")
	@MessageMapping("/partida/comercio/proponer")
	public void proponerComercio(String mensaje) {
		
		JSONObject message = new JSONObject(mensaje);
		
		int destinatario = message.getInt("to");
		String partida = message.getString("game");
		
		message.remove("to");
		message.remove("game");
		
		message.put("type", REQUEST);
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		int Hours = timestamp.getHours();
		int Mins = timestamp.getMinutes();
		String H_M = String.format("%d:%02d",Hours,Mins);
		
		message.put("time", H_M);
		
		template.convertAndSend(WebSocketConfig.TOPIC_PARTIDA_COM + "/" + partida + "/" + destinatario, message.toString());
	}
	
	
	/* ******************************************************
	 * Maps: 	Traded proposal acceptance
	 * 
	 * Expects: -JSON Message
	 * 			-Mapped point: 		/app/comercio/aceptar
	 * 			-Format:
	 * 				{
	 * 					"from"	: <Jugador que acepta la petición [1-4]>,					
	 * 					"to"	: <Jugador que hizo la petición [1-4]>,
	 * 					"game"	: <partida>,
	 * 					"res1" : {
	 * 						"type" : <Tipo de Recurso Aceptada>,
	 * 						"cuan" : <Cantidad de Recurso Aceptada>
	 * 					},
	 * 					"res2" : {
	 * 						"type" : <Tipo de Recurso Ofrecido>,
	 * 						"cuan" : <Cantidad de Recurso Ofrecido>
	 * 					}
	 * 				}
	 * 
	 * Returns: -JSON Message
	 * 
	 * 			-Broadcast point:	/partida-com/<partida>/<Jugador que hizo la petición [1-4]>
	 * 			-Format:
	 * 				{
	 * 					"type"	: "ACCEPT",
	 * 					"from"	: <Jugador que acepta la petición [1-4]>,
	 * 					"time"	: <marca de tiempo H:M>
	 *				}
	****************************************************** */
	@MessageMapping("/partida/comercio/aceptar")
	public void aceptarComercio(String mensaje) {
		
		JSONObject message = new JSONObject(mensaje);
		
		int remitente 		= message.getInt("from");
		int destinatario  	= message.getInt("to");
		
		String partida = message.getString("game");
		JSONObject res1 = message.getJSONObject("res1");
		JSONObject res2 = message.getJSONObject("res2");
		
		JSONObject answer = new JSONObject();
		answer.put("type", ACCEPT);
		answer.put("from", remitente);
		answer.put("to", destinatario);
		
		template.convertAndSend(WebSocketConfig.TOPIC_PARTIDA_COM + "/" + partida + "/" + destinatario, answer.toString());

		JSONObject jugadaIntercambio = new JSONObject(); 
		jugadaIntercambio.put("player", destinatario); 
		jugadaIntercambio.put("game", partida); 

		JSONObject move = new JSONObject();
		move.put("name","comerciar"); 

		JSONArray param = new JSONArray();
		param.put(remitente);
		param.put(res1.getString("type"));
		param.put(res1.getInt("cuan"));
		param.put(res2.getString("type"));
		param.put(res2.getInt("cuan"));

		move.put("param", param);
		jugadaIntercambio.put("move", move); 

		moveCarrierHeap.newJugada(partida, jugadaIntercambio);
	}
	
	
	/* ******************************************************
	 * Maps: 	Trade proposal decline
	 * 
	 * Expects: -JSON Message
	 * 			-Mapped point: 		/app/comercio/rechazar
	 * 			-Format:
	 * 				{
	 * 					"from"	: <Jugador que acepta la petición [1-4]>,
	 * 					"to"	: <Jugador que hizo la petición [1-4]>,
	 * 					"game"  : <partida>
	 *				}
	 * 
	 * Returns: -JSON Message
	 * 			-Broadcast point:	/partida-com/<partida>/<Jugador que hizo la petición [1-4]>
	 * 			-Format:
	 * 				{
	 * 					"type"	: "DECLINE",
	 * 					"from"	: <Jugador que acepta la petición [1-4]>,
	 * 					"time"	: <marca de tiempo H:M>
	 *				}
	****************************************************** */
	@SuppressWarnings("deprecation")
	@MessageMapping("/partida/comercio/rechazar")
	public void rechazarComercio(String mensaje) {
		
		JSONObject message = new JSONObject(mensaje);
		
		int remitente = message.getInt("from");
		int destinatario = message.getInt("to");
		String partida = message.getString("game");
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		int Hours = timestamp.getHours();
		int Mins = timestamp.getMinutes();
		String H_M = String.format("%d:%02d",Hours,Mins); 
		
		JSONObject new_message = new JSONObject();
		new_message.put("type", DECLINE);
		new_message.put("from", remitente);
		new_message.put("time", H_M);
		
		template.convertAndSend(WebSocketConfig.TOPIC_PARTIDA_COM + "/" + partida + "/" + destinatario, new_message.toString());
	}
}
