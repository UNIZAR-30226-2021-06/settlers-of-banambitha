package es.susangames.catan.resource;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.susangames.catan.model.Usuario;
import es.susangames.catan.service.UsuarioService;

@RestController
@RequestMapping(value = "/usuario")
public class UsuarioResourcee {
	
	private final UsuarioService usuarioService;

	public UsuarioResourcee(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	@PostMapping("/add")
	public ResponseEntity<Usuario> newUsuario(@RequestBody Usuario usuario) {
		
		Usuario newUsuario = usuarioService.newUsuario(usuario);
		
		return new ResponseEntity<>(newUsuario, HttpStatus.CREATED);
	}
	
	@GetMapping("/find/{usuarioId}")
	public ResponseEntity<Usuario> findUsuario(@PathVariable("usuarioId") String usuarioId){
		Usuario usuario = usuarioService.findUsuario(usuarioId);
		
		return new ResponseEntity<>(usuario, HttpStatus.OK);
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<Usuario>> findAllUsers() {
		List<Usuario> usuarios = usuarioService.findAllUsuarios();
		return new ResponseEntity<>(usuarios, HttpStatus.OK);
	}
	
	@GetMapping("/validate")
	public ResponseEntity<Usuario> validateUsuario(@RequestBody Usuario usuario) {

		boolean valid = usuarioService.validarUsuario(usuario);
		
		if(valid) {
			Usuario valid_usuario = usuarioService.findUsuario(usuario.getNombre());
			return new ResponseEntity<>(valid_usuario,HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new Usuario(),HttpStatus.NOT_FOUND);
		}
	
	}
}
