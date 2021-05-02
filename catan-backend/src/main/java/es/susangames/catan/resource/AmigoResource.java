package es.susangames.catan.resource;

import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.susangames.catan.model.Amigo;
import es.susangames.catan.model.PeticionAmistad;
import es.susangames.catan.service.AmigoService;

@RestController
@RequestMapping(value = "/amigo")
public class AmigoResource {

	private final AmigoService amigoService;

	public AmigoResource(AmigoService amigoService) {
		this.amigoService = amigoService;
	}
	
	
	/* ******************************************************
	 * Maps: 	Friends List @Get
	 * 
	 * Expects: -Parameter in url
	 * 			-Mapped point: 	/amigo/list
	 * 			-Format: 		/amigo/list/{usuarioId}
	 * 
	 * Returns: -JSON Message
	 * 			-Format:
	 * 				[
	 * 					{
	 * 						"usuario1_id"	: <usuarioId>,
	 * 						"usuario2_id"	: <id_amigo>
	 *					}
	 *				]
	****************************************************** */
	@GetMapping("/list/{usuarioId}")
	public ResponseEntity<List<Amigo>> findAllAmigos(@PathVariable("usuarioId") String usuarioId) {
		
		List<Amigo> listaAmigos = amigoService.listaAmigos(usuarioId);
		
		return new ResponseEntity<>(listaAmigos, HttpStatus.OK);
		
	}
	
	
	/* ******************************************************
	 * Maps: 	Pending Received Friend Request List @Get
	 * 
	 * Expects: -Parameter in url
	 * 			-Mapped point: 	/amigo/pending-r
	 * 			-Format: 		/amigo/pending-r/{usuarioId}
	 * 
	 * Returns: -JSON Message
	 * 			-Format:
	 * 				[
	 * 					{
	 * 						"usuario1_id"	: <invitante_id>,
	 * 						"usuario2_id"	: <usuarioId>
	 *					}
	 *				]
	****************************************************** */
	@GetMapping("/pending-r/{usuarioId}")
	public ResponseEntity<List<PeticionAmistad>> findSolicitudesPendientesRecibidas(@PathVariable("usuarioId") String usuarioId) {
		
		List<PeticionAmistad> listaPendientes = amigoService.listaPendientesRecibidas(usuarioId);
		
		return new ResponseEntity<>(listaPendientes, HttpStatus.OK);
	}
	
	
	/* ******************************************************
	 * Maps: 	Pending Sent Friend Request List @Get
	 * 
	 * Expects: -Parameter in url
	 * 			-Mapped point: 	/amigo/pending-s
	 * 			-Format: 		/amigo/pending-s/{usuarioId}
	 * 
	 * Returns: -JSON Message
	 * 			-Format:
	 * 				[
	 * 					{
	 * 						"usuario1_id"	: <usuarioId>,
	 * 						"usuario2_id"	: <invitado_id>
	 *					}
	 *				]
	****************************************************** */
	@GetMapping("/pending-s/{usuarioId}")
	public ResponseEntity<List<PeticionAmistad>> findSolicitudesPendientesEnviadas(@PathVariable("usuarioId") String usuarioId) {
		
		List<PeticionAmistad> listaPendientes = amigoService.listaPendientesEnviadas(usuarioId);
		
		return new ResponseEntity<>(listaPendientes, HttpStatus.OK);
	}
	
}
