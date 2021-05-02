package es.susangames.catan.WSController;

import java.sql.Timestamp;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import es.susangames.catan.WebSocketConfig;
import es.susangames.catan.model.PeticionAmistad;
import es.susangames.catan.service.AmigoService;

@Controller
public class AmigoController {
	
	private static final String REQUEST = "REQUEST";
	private static final String ACCEPT 	= "ACCEPT";
	private static final String DECLINE = "DECLINE";

	private final SimpMessagingTemplate template;
	private final AmigoService amigoService;

	@Autowired
	public AmigoController(SimpMessagingTemplate template, AmigoService amigoService) {
		this.template = template;
		this.amigoService = amigoService;
	}
	
	
	/* ******************************************************
	 * Maps: 	Friendship requests
	 * 
	 * Expects: -JSON Message
	 * 			-Mapped point: 		/app/enviar/peticion
	 * 			-Format:
	 * 				{
	 * 					"from" 	: <el que hace la peticion>,
	 * 					"to"	: <el que recibe la peticion>,
	 * 				}
	 * 
	 * Returns: -JSON Message
	 * 			-Broadcast point:	/peticion/<el que acepta la peticion>
	 * 			-Format:
	 * 				{
	 * 					"type"	: "REQUEST",
	 * 					"from"	: <el que hace la peticion>,
	 * 					"time"	: <marca de tiempo H:M>
	 *				}
	****************************************************** */
	@SuppressWarnings("deprecation")
	@MessageMapping("/enviar/peticion")
	public void enviarPeticion(String mensaje) {
		
		JSONObject obj = new JSONObject(mensaje);
		
		String remitente = obj.getString("from");
		String destinatario = obj.getString("to");
		
		amigoService.addPeticionAmistad(new PeticionAmistad(remitente, destinatario));
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		int Hours = timestamp.getHours();
		int Mins = timestamp.getMinutes();
		String H_M = String.format("%d:%02d",Hours,Mins); 
		
		JSONObject nuevoMensaje = new JSONObject();
		nuevoMensaje.put("type", REQUEST);
		nuevoMensaje.put("from", remitente);
		nuevoMensaje.put("time", H_M);
		
		template.convertAndSend(WebSocketConfig.TOPIC_PETICION + "/" + destinatario, nuevoMensaje.toString());
		
	}
	
	
	/* ******************************************************
	 * Maps: 	Friendship requests acceptance
	 * 
	 * Expects: -JSON Message
	 * 			-Mapped point: 		/app/aceptar/peticion
	 * 			-Format:
	 * 				{
	 * 					"from" 	: <el que acepta la peticion>,
	 * 					"to"	: <el que hizo la peticion>
	 * 				}
	 * 
	 * Returns: -JSON Message
	 * 			-Broadcast point:	/peticion/<el que hizo la peticion>
	 * 			-Format:
	 * 				{
	 * 					"type"	: "ACCEPT",
	 * 					"from"	: <el que acepta la peticion>,
	 * 					"time"	: <marca de tiempo H:M>
	 *				}
	****************************************************** */
	@SuppressWarnings("deprecation")
	@MessageMapping("/aceptar/peticion")
	public void aceptarPeticion(String mensaje) {
		
		JSONObject obj = new JSONObject(mensaje);
		
		//El que acepta la petición
		String remitente = obj.getString("from");
		//El que hizo la petición
		String destinatario = obj.getString("to");
		
		amigoService.aceptarPeticionAmistad(new PeticionAmistad(destinatario,remitente));
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		int Hours = timestamp.getHours();
		int Mins = timestamp.getMinutes();
		String H_M = String.format("%d:%02d",Hours,Mins); 
		
		JSONObject nuevoMensaje = new JSONObject();
		nuevoMensaje.put("type", ACCEPT);
		nuevoMensaje.put("from", remitente);
		nuevoMensaje.put("time", H_M);
		
		template.convertAndSend(WebSocketConfig.TOPIC_PETICION + "/" + destinatario, nuevoMensaje.toString());
		
	}
	
	
	/* ******************************************************
	 * Maps: 	Friendship requests decline
	 * 
	 * Expects: -JSON Message
	 * 			-Mapped point: 		/app/rechazar/peticion
	 * 			-Format:
	 * 				{
	 * 					"from" 	: <el que rechaza la peticion>,
	 * 					"to"	: <el que hizo la peticion>
	 * 				}
	 * 
	 * Returns: -JSON Message
	 * 			-Broadcast point:	/peticion/<el que hizo la peticion>
	 * 			-Format:
	 * 				{
	 * 					"type"	: "DECLINE",
	 * 					"from"	: <el que rechaza la peticion>,
	 * 					"time"	: <marca de tiempo H:M>
	 *				}
	****************************************************** */
	@SuppressWarnings("deprecation")
	@MessageMapping("/rechazar/peticion")
	public void rechazarPeticion(String mensaje) {
		
		JSONObject obj = new JSONObject(mensaje);
		
		//El que acepta la petición
		String remitente = obj.getString("from");
		//El que hizo la petición
		String destinatario = obj.getString("to");
		
		amigoService.rechazarPeticionAmistad(new PeticionAmistad(destinatario,remitente));
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		int Hours = timestamp.getHours();
		int Mins = timestamp.getMinutes();
		String H_M = String.format("%d:%02d",Hours,Mins); 
		
		JSONObject nuevoMensaje = new JSONObject();
		nuevoMensaje.put("type", DECLINE);
		nuevoMensaje.put("from", remitente);
		nuevoMensaje.put("time", H_M);
		
		template.convertAndSend(WebSocketConfig.TOPIC_PETICION + "/" + destinatario, nuevoMensaje.toString());
		
	}
	
}
