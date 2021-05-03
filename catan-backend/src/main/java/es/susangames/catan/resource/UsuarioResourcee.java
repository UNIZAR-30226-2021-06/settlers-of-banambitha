package es.susangames.catan.resource;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import es.susangames.catan.model.Usuario;
import es.susangames.catan.service.UsuarioService;

@Controller
@RequestMapping(value = "/usuario")
public class UsuarioResourcee {
	
	private final UsuarioService usuarioService;

	public UsuarioResourcee(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	
	/* ******************************************************
	 * Maps: 	Add a new user @Post
	 * 
	 * Expects: -User in body
	 * 			-Mapped point: 	/usuario/add
	 * 			-Format: 
	 * 				{
	 * 					"nombre" 	 : <nombre>,
	 * 					"email" 	 : <email>,
	 * 					"contrasenya": <contrasenya>
	 * 				}
	 * 			-Los parámetros restantes no son necesarios
	 * 
	 * Returns: -JSON Message
	 * 			-Format:
	 * 				{
	 * 					"nombre" 	 : <nombre>,
	 * 					"email" 	 : <email>,
	 * 					"contrasenya": <contrasenya>
	 *	 				"saldo"		 : 0,
	 *				    "idioma"	 : "Español",
	 *				    "avatar"	 : "Original",
	 *				    "apariencia" : "Clasica"
	 *				}
	****************************************************** */
	@PostMapping("/add")
	public ResponseEntity<Usuario> newUsuario(@RequestBody Usuario usuario, HttpSession session) {
		
		Usuario newUsuario = usuarioService.newUsuario(usuario);
		session.setAttribute("username", newUsuario.getNombre() );
		return new ResponseEntity<>(newUsuario, HttpStatus.CREATED);
	}
	
	
	/* ******************************************************
	 * Maps: 	Get a user by Id @get
	 * 
	 * Expects: -Parameter in url
	 * 			-Mapped point: 	/usuario/find
	 * 			-Format: 		/usuario/find/{usuarioId}
	 * 				
	 * Returns: -JSON Message
	 * 			-Format:
	 * 				{
	 * 					"nombre" 	 : <nombre>,
	 * 					"email" 	 : <email>,
	 * 					"contrasenya": <contrasenya>
	 *	 				"saldo"		 : <saldo>,
	 *				    "idioma"	 : <idioma>,
	 *				    "avatar"	 : <avatar>,
	 *				    "apariencia" : <apariencia>
	 *				}
	****************************************************** */
	@GetMapping("/find/{usuarioId}")
	public ResponseEntity<Usuario> findUsuario(@PathVariable("usuarioId") String usuarioId){
		Usuario usuario = usuarioService.findUsuario(usuarioId);
		
		return new ResponseEntity<>(usuario, HttpStatus.OK);
	}
	
	
	/* ******************************************************
	 * Maps: 	Get all users @get
	 * 
	 * Expects: -Nothing
	 * 			-Mapped point: 	/usuario/all
	 * 				
	 * Returns: -JSON Message
	 * 			-Format:
	 * 				[
	 * 					{
	 * 						"nombre" 	 : <nombre>,
	 * 						"email" 	 : <email>,
	 * 						"contrasenya": <contrasenya>
	 *	 					"saldo"		 : <saldo>,
	 *				    	"idioma"	 : <idioma>,
	 *				    	"avatar"	 : <avatar>,
	 *				    	"apariencia" : <apariencia>
	 *					}
	 *				]
	****************************************************** */
	@GetMapping("/all")
	public ResponseEntity<List<Usuario>> findAllUsers() {
		List<Usuario> usuarios = usuarioService.findAllUsuarios();
		return new ResponseEntity<>(usuarios, HttpStatus.OK);
	}
	
	/* ******************************************************
	 * Maps: 	Validate a user using Id and password @post
	 * 
	 * Expects: -User in body
	 * 			-Mapped point: 	/usuario/validate
	 * 			-Format: 
	 * 				{
	 * 					"nombre" 	 : <nombre>,
	 * 					"contrasenya": <contrasenya>
	 * 				}
	 * 			-Los parámetros restantes no son necesarios		
	 * 		
	 * Returns: -JSON Message
	 * 			-Format:
	 * 				{
	 * 					"nombre" 	 : <nombre>,
	 * 					"email" 	 : <email>,
	 * 					"contrasenya": <contrasenya>,
	 *	 				"saldo"		 : <saldo>,
	 *				    "idioma"	 : <idioma>,
	 *				    "avatar"	 : <avatar>,
	 *				    "apariencia" : <apariencia>
	 *				}
	 *			-On failure all fields are null
	****************************************************** */
	@PostMapping("/validate")
	public ResponseEntity<Usuario> validateUsuario(@RequestBody Usuario usuario, HttpSession session) {

		boolean valid = usuarioService.validarUsuario(usuario);
		
		if(valid) {
			Usuario valid_usuario = usuarioService.findUsuario(usuario.getNombre());
			session.setAttribute("username", valid_usuario.getNombre() );
			return new ResponseEntity<>(valid_usuario,HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new Usuario(),HttpStatus.NOT_FOUND);
		}
	
	}
	
	/* ******************************************************
	 * Maps: 	Update a user (avatar, apariencia,...)
	 * 
	 * Expects: -User in body
	 * 			-Mapped point: 	/usuario/validate
	 * 			-Format: 
	 * 				{
	 * 					"nombre" 	 : <nombre>,
	 * 					campo_act	 : <nuevoValor>
	 * 				}
	 * 			-Los parámetros restantes no son necesarios	
	 * 			-Se pueden especificar varios parámetros a actualizar	
	 * 			-Parámetros aceptables para actualizar:
	 * 				{
	 * 					"avatar" 	 : <nuevoAvatar>,
	 * 					"apariencia" : <nuevaApariencia>,
	 * 					"idioma"	 : <Español | English>
	 * 		
	 * Returns: -JSON Message
	 * 			-Format:
	 * 				{
	 * 					"nombre" 	 : <nombre>,
	 * 					"email" 	 : <email>,
	 * 					"contrasenya": <contrasenya>,
	 *	 				"saldo"		 : <saldo>,
	 *				    "idioma"	 : <idioma>,
	 *				    "avatar"	 : <avatar>,
	 *				    "apariencia" : <apariencia>
	 *				}
	 *			-On failure all fields are null
	****************************************************** */
	@PutMapping("/update")
	public ResponseEntity<Usuario> updateUsuario(@RequestBody Usuario usuario) {
		
		Usuario updatedUsuario = usuarioService.updateUsuario(usuario);
		
		return new ResponseEntity<>(updatedUsuario, HttpStatus.OK);
		
	}

	@GetMapping("/session")
	public ResponseEntity<Usuario> checkSession(HttpSession session){
		String username = (String) session.getAttribute("username");
		if (username != null ){
			Usuario user = usuarioService.findUsuario(username);
			return new ResponseEntity<>(user,HttpStatus.OK);
		}else{
			return new ResponseEntity<>(new Usuario(),HttpStatus.NOT_FOUND);
		}
	}

}
