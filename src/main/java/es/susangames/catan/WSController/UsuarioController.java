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
	
}
