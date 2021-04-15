package es.susangames.catan.WSController;

import java.sql.Timestamp;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import es.susangames.catan.WebSocketConfig;

@Controller
public class PartidaController {
	
	private static final String REQUEST = "REQUEST";
	private static final String ACCEPT 	= "ACCEPT";
	private static final String DECLINE = "DECLINE";

	private final SimpMessagingTemplate template;

	@Autowired
	public PartidaController(SimpMessagingTemplate template) {
		super();
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
	 * 
	 * Returns: -JSON Message
	 * 			-Broadcast point:	/partida-act/<partida>
	 * 			-Format:
	 * 				{
	 * 					TODO
	 *				}
	****************************************************** */
	@MessageMapping("/partida/jugada")
	public void realizarJugada(String mensaje) {
		
		
		
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
		int partida = message.getInt("game");
		
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
	 * 
	 * 			-Broadcast point:	/partida-act/<partida>
	 * 			-Format:
	 * 				{
	 * 					"Mensaje" : <mensaje [TODO A discutir]>,
	 * 					"Recursos": {
     *						"<Jugador que acepta la petición [1-4]>": [ "Mad", "Pied", "Ladr", "Lana", "Cereales" ],
     *						"<Jugador que hizo la petición [1-4]>"  : [ "Mad", "Pied", "Ladr", "Lana", "Cereales" ]
  	 *					}
	 *				}
	****************************************************** */
	@MessageMapping("/partida/comercio/aceptar")
	public void aceptarComercio(String mensaje) {
		
		JSONObject message = new JSONObject(mensaje);
		
		int remitente 		= message.getInt("from");
		int destinatario  	= message.getInt("to");
		
		String partida = message.getString("game");
		
		JSONObject answer = new JSONObject();
		answer.put("type", ACCEPT);
		answer.put("from", remitente);
		answer.put("to", destinatario);
		
		template.convertAndSend(WebSocketConfig.TOPIC_PARTIDA_COM + "/" + partida + "/" + destinatario, answer.toString());
		
		JSONObject res1 = message.getJSONObject("res1");
		String type1 = res1.getString("type");
		int cuan1 = res1.getInt("cuan");
		
		JSONObject res2 = message.getJSONObject("res2");
		String type2 = res2.getString("type");
		int cuan2 = res2.getInt("cuan");
		
		//TODO Llamar al controlador de la partida para que procecse el cambio de estado

		JSONObject resultado = null; //= Procesar Jugada
		
		template.convertAndSend(WebSocketConfig.TOPIC_PARTIDA_ACT + "/" + partida, resultado.toString());
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
		int partida = message.getInt("game");
		
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
