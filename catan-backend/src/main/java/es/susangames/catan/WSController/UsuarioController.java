package es.susangames.catan.WSController;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import es.susangames.catan.WebSocketConfig;
import es.susangames.catan.model.Usuario;
import es.susangames.catan.service.AmigoService;
import es.susangames.catan.service.ProductoService;
import es.susangames.catan.service.UsuarioService;

@Controller
public class UsuarioController {

	private final SimpMessagingTemplate template;
	private final UsuarioService usuarioService;
	private final AmigoService amigoService;
	private final ProductoService productoService;
	
	@Autowired
	public UsuarioController(SimpMessagingTemplate template, UsuarioService usuarioService, AmigoService amigoService, ProductoService productoService) {
		this.template = template;
		this.usuarioService = usuarioService;
		this.amigoService = amigoService;
		this.productoService = productoService;
	}
	
	/* ******************************************************
	 * Maps: 	User Account Deletion
	 * 
	 * Expects: -JSON Message
	 * 			-Mapped point: 		/app/usuario/eliminar
	 * 			-Format:
	 * 				{
	 * 					"nombre" 	 : <playerId>,
	 * 					"contrasenya": <contrasenya>
	 * 				}
	 * 
	 * Returns: -JSON Message
	 * 			-Broadcast point:	/usuario-act/<playerId>
	 * 			-Format:
	 * 				{
	 * 					"status"	 : "DELETED" | "FAILED"
	 *				}
	 *
	 *			OPTIONAL
	 *			-Broadcast point:	/peticion/<amigos | peticiones enviadas | peticiones recibidas>
	 * 			-Format:
	 * 				{
	 * 					"status"	 : "TERMINATED",
	 * 					"from"		 : <playerId>
	 *				}
	****************************************************** */
	@MessageMapping("/usuario/eliminar")
	public void eliminarCuenta(String mensaje) {
		
		JSONObject message = new JSONObject(mensaje);
		String usuario = message.getString("nombre");
		String contrasenya = message.getString("contrasenya");
		
		JSONObject respuesta = new JSONObject();
		
		if(usuarioService.validarUsuario(new Usuario(usuario,contrasenya))) {
			
			List<String> aNotificar = amigoService.eliminarAmigos(usuario);
			
			if(!aNotificar.isEmpty()) {
				
				JSONObject notificacion = new JSONObject();
				notificacion.put("status", "TERMINATED");
				notificacion.put("from", usuario);
				
				for(String notificado : aNotificar) {
					template.convertAndSend(WebSocketConfig.TOPIC_PETICION + "/" + notificado, notificacion.toString());
				}
			}
			
			productoService.eliminarProductos(usuario);
			
			usuarioService.deleteUsuario(usuario);
			
			respuesta.put("status", "DELETED");
			
		} else {
			respuesta.put("status", "FAILED");
		}
		
		template.convertAndSend(WebSocketConfig.TOPIC_USUARIO_ACT + "/" + usuario, respuesta.toString());
	}
	
	/* ******************************************************
	 * Maps: 	User Reports
	 * 
	 * Expects: -JSON Message
	 * 			-Mapped point: 		/app/usuario/reportar
	 * 			-Format:
	 * 				{
	 * 					"from" 	: <jugador_que_reporta>,
	 * 					"to"	: <jugador_reportado>
	 * 				}
	 * 
	 * Returns: -JSON Message
	 * 			-Broadcast point:	/usuario-act/<jugador_que_reporta>
	 * 			-Format:
	 * 				{
	 * 					"status"	: "REPORT_SENT" | "REPORT_REJECTED",
	 * 					"player"	: <jugador_reportado>
	 *				}
	 *
	 *			-Broadcast point:	/usuario-act/<jugador_reportado>
	 * 			-Format:
	 * 				{
	 * 					"status"	: "REPORT_RECEIVED",
	 * 					"player"	: <jugador_que_reporta>
	 *				}
	****************************************************** */
	@MessageMapping("/usuario/reportar")
	public void reportarUsuario(String mensaje) {
		
		JSONObject message 	= new JSONObject(mensaje);
		String reportante 	= message.getString("from");
		String reportado 	= message.getString("to");
		
		JSONObject respuesta = new JSONObject();
		respuesta.put("player", reportado);
		
		if(usuarioService.reportJugador(reportante, reportado)) {
			respuesta.put("status", "REPORT_SENT");
			
			JSONObject notificacion = new JSONObject();
			notificacion.put("status", "REPORT_RECEIVED");
			notificacion.put("player", reportante);
			
			template.convertAndSend(WebSocketConfig.TOPIC_USUARIO_ACT + "/" + reportado, notificacion.toString());
		}
		else {
			respuesta.put("status", "REPORT_REJECTED");
		}
		
		template.convertAndSend(WebSocketConfig.TOPIC_USUARIO_ACT + "/" + reportante, respuesta.toString());
	}
	
}
