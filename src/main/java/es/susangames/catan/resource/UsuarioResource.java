package es.susangames.catan.resource;

import java.sql.Date;
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

import es.susangames.catan.auxiliarModel.ChangePassword;
import es.susangames.catan.model.Estadisticas;
import es.susangames.catan.model.Usuario;
import es.susangames.catan.service.UsuarioService;

@Controller
@RequestMapping(value = "/usuario")
public class UsuarioResource {
	
	private final UsuarioService usuarioService;

	public UsuarioResource(UsuarioService usuarioService) {
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
	 *					"partida"	 : NULL,
	 *					"bloqueado"	 : NULL,
	 *					"informes"	 : 0,
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
	 *					"partida"	 : <partidaId>,
	 *					"bloqueado"	 : <fechaBloqueo>,
	 *					"informes"	 : <informes>,
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
	 *						"partida"	 : <partidaId>,
	 *						"bloqueado"	 : <fechaBloqueo>,
	 *						"informes"	 : <informes>,
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
	 *					"partida"	 : <partidaId>,
	 *					"bloqueado"	 : <fechaBloqueo>,
	 *					"informes"	 : <informes>,
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
			
			Date fechaBloqueo = usuario.getBloqueado();
			Date fechaActual = new Date(System.currentTimeMillis());
			
			if(fechaBloqueo!=null) System.out.println(fechaBloqueo.toString());
			else System.out.println("null");
			System.out.println(fechaActual.toString());	
			if(fechaBloqueo!=null) System.out.println(fechaActual.after(fechaBloqueo));	
			
			if(fechaBloqueo!=null && fechaActual.after(fechaBloqueo)) {
				usuarioService.pardonJugador(valid_usuario);
				valid_usuario.setBloqueado(null);
				valid_usuario.setInformes(0);
			}
			
			session.setAttribute("username", valid_usuario.getNombre() );
			return new ResponseEntity<>(valid_usuario,HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new Usuario(),HttpStatus.NOT_FOUND);
		}
	
	}
	
	/* ******************************************************
	 * Maps: 	Update a user (avatar, apariencia,...) @put
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
	 * 					"idioma"	 : <nuevoIdioma (Español | English)>
	 * 		
	 * Returns: -JSON Message
	 * 			-Format:
	 * 				{
	 * 					"nombre" 	 : <nombre>,
	 * 					"email" 	 : <email>,
	 * 					"contrasenya": <contrasenya>,
	 *	 				"saldo"		 : <saldo>,
	 *				    "idioma"	 : <nuevoIdioma>,
	 *					"partida"	 : <partidaId>,
	 *					"bloqueado"	 : <fechaBloqueo>,
	 *					"informes"	 : <informes>,
	 *				    "avatar"	 : <nuevoAvatar>,
	 *				    "apariencia" : <nuevaApariencia>
	 *				}
	 *			-On failure all fields are null
	****************************************************** */
	@PutMapping("/update")
	public ResponseEntity<Usuario> updateUsuario(@RequestBody Usuario usuario) {
		
		Usuario updatedUsuario = usuarioService.updateUsuario(usuario);
		
		return new ResponseEntity<>(updatedUsuario, HttpStatus.OK);
		
	}
	
	/* ******************************************************
	 * Maps: 	Update a user's password @put
	 * 
	 * Expects: -New and Old Password in body
	 * 			-Mapped point: 	/usuario/new-password
	 * 			-Format: 
	 * 				{
	 * 					"userId"	 : <usuarioId>,
	 * 					"oldPassw" 	 : <anterior contraseña>,
	 * 					"newPassw"	 : <nueva contraseña>
	 * 				}
	 * 
	 * Returns: -JSON Message
	 * 			-Format:
	 * 				{
	 * 					"nombre" 	 : <usuarioId>,
	 * 					"email" 	 : <email>,
	 * 					"contrasenya": <nueva contraseña>,
	 *	 				"saldo"		 : <saldo>,
	 *				    "idioma"	 : <idioma>,
	 *					"partida"	 : <partidaId>,
	 *					"bloqueado"	 : <fechaBloqueo>,
	 *					"informes"	 : <informes>,
	 *				    "avatar"	 : <avatar>,
	 *				    "apariencia" : <apariencia>
	 *				}
	 *			-On failure all fields are null
	****************************************************** */
	@PutMapping("/new-password")
	public ResponseEntity<Usuario> newPassword(@RequestBody ChangePassword changePassword) {
		
		Usuario updated_usuario = usuarioService.changePassw(changePassword);
		
		return new ResponseEntity<>(updated_usuario, HttpStatus.OK);
		
	}
	
	
	/* ******************************************************
	 * Maps: 	Get User Stats @get
	 * 
	 * Expects: -User in body
	 * 			-Mapped point: 	/usuario/stats
	 * 			-Format: 		/usuario/stats/{usuarioId}
	 * 		
	 * Returns: -JSON Message
	 * 			-Format:
	 * 				{
	 * 					"usuario_id" 	 			: <nombre>,
	 * 					"mayor_racha_de_victorias" 	: <MaxRachVic>,
	 * 					"partidas_jugadas"			: <ParJugJ>,
	 *	 				"porcentaje_de_victorias"	: <PorVic>,
	 *				    "racha_de_victorias_actual"	: <RachVicAct>,
	 *				    "total_de_victorias"	 	: <TotVic>,
	 *				}
	****************************************************** */
	@GetMapping("/stats/{usuarioId}")
	public ResponseEntity<Estadisticas> getEstadisticas(@PathVariable("usuarioId") String usuarioId) {
		
		Estadisticas estadisticas = usuarioService.findEstadisticas(usuarioId);
		
		return new ResponseEntity<>(estadisticas,HttpStatus.OK);
	}
	
	/* ******************************************************
	 * Maps: 	Get a user by Id @get
	 * 
	 * Expects: -Parameter in url
	 * 			-Mapped point: 	/usuario/session
	 * 			-Format: 		/usuario/session
	 * 				
	 * Returns: -JSON Message
	 * 			-Format:
	 * 				{
	 * 					"nombre" 	 : <nombre>,
	 * 					"email" 	 : <email>,
	 * 					"contrasenya": <contrasenya>
	 *	 				"saldo"		 : <saldo>,
	 *				    "idioma"	 : <idioma>,
	 *					"partida"	 : <partidaId>,
	 *					"bloqueado"	 : <fechaBloqueo>,
	 *					"informes"	 : <informes>,
	 *				    "avatar"	 : <avatar>,
	 *				    "apariencia" : <apariencia>
	 *				}
	****************************************************** */
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

	//Invalida la sesión del usuario
	@GetMapping("/logout")
	public ResponseEntity<Boolean> logOut(HttpSession session){
		System.out.println("invalidando sesión de " + session.getAttribute("username")); 
		session.invalidate();
		return new ResponseEntity<>(true,HttpStatus.OK);
	}

}
