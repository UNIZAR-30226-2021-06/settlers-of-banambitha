package es.susangames.catan.resource;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.susangames.catan.auxiliarModel.ProdDisponible;
import es.susangames.catan.model.Dispone;
import es.susangames.catan.model.Producto;
import es.susangames.catan.service.ProductoService;

@RestController
@RequestMapping(value = "/producto")
public class ProductoResource {
	
	private final ProductoService productoService;
	
	public ProductoResource(ProductoService productoService) {
		this.productoService = productoService;
	}


	/* ******************************************************
	 * Maps: 	Get Every Product Available on the Store @Get
	 * 
	 * Expects: -Nothing
	 * 			-Mapped point: 	/producto/all
	 * 
	 * Returns: -JSON Message
	 * 			-Format:
	 * 				[
     *					{
	 * 				        "nombre": <nombre>,
	 *				        "precio": <precio>,
	 *				        "tipo"	: <tipo [APARIENCIA | AVATAR]>
	 *				    }
	 *				]
	****************************************************** */
	@GetMapping("/all")
	public ResponseEntity<List<Producto>> getAllProductos(){
		
		List<Producto> productos = productoService.getProductosTienda();
		
		return new ResponseEntity<>(productos, HttpStatus.OK);
	}
	
	/* ******************************************************
	 * Maps: 	Get Every Product and whether or not
	 * 			they have already been purchased by the user @Get
	 * 
	 * Expects: -Parameter in url
	 * 			-Mapped point	: /producto/disponibles
	 * 			-Format			: /producto/disponibles/{usuarioId}
	 * 
	 * Returns: -JSON Message
	 * 			-Format:
	 * 				[
     *					{
     *    					"producto_id"	: <producto>,
	 * 						"url"			: <url>
     * 						"precio"		: <precio>,
     *   					"tipo"			: <tipo [APARIENCIA | AVATAR]>,
     *   					"adquirido"		: <[true | false]>
     *					}
	 *				]
	****************************************************** */
	@GetMapping("/disponibles/{usuarioId}")
	public ResponseEntity<List<ProdDisponible>> findProductosDisponibles(@PathVariable("usuarioId") String usuarioId) {
		List<ProdDisponible> productosDisponibles = productoService.getProdDisp(usuarioId);
		
		return new ResponseEntity<>(productosDisponibles, HttpStatus.OK);
	}
	
	/* ******************************************************
	 * Maps: 	Get Every Product Owned by a User @Get
	 * 
	 * Expects: -Parameter in url
	 * 			-Mapped point	: /producto/adquiridos
	 * 			-Format			: /producto/adquiridos/{usuarioId}
	 * 
	 * Returns: -JSON Message
	 * 			-Format:
	 * 				[
     *					{
     *    					"nombre"	: <producto>,
     * 						"precio"	: <precio>,
     *   					"tipo"		: <tipo [APARIENCIA | AVATAR]>,
     *					}
	 *				]
	****************************************************** */
	@GetMapping("/adquiridos/{usuarioId}")
	public ResponseEntity<List<Producto>> findProductosAdquiridos(@PathVariable("usuarioId") String usuarioId) {
		List<Producto> productos = productoService.getProdAdquiridos(usuarioId);
		
		return new ResponseEntity<>(productos, HttpStatus.OK);
	}

	

	/* ******************************************************
	 * Maps: 	Purchase of a Product @Put
	 * 
	 * Expects: -User Id and Product Id on body
	 * 			-Mapped point	: /producto/adquirir
	 * 			-Format:
	 * 				{
	 *		       		"usuario_id"	: <nombre>,
	 *		       		"producto_id"	: <producto>
	 *			   	}
	 * 
	 * 
	 * Returns: -JSON Message
	 * 			-Format:
	 * 				{
	 * 					"compra": {
	 *		        		"usuario_id"	: <nombre>,
	 *		        		"producto_id"	: <producto>
	 *			    	},
	 *			    	"usuario": {
	 *			        	"nombre"		: <nombre>,
	 *			        	"email"			: <email>,
	 *			        	"contrasenya"	: <contrasenya (Encriptada)>,
	 *			        	"saldo"			: <saldo>,
	 *			        	"idioma"		: <idioma>,
	 *			        	"avatar"		:"<avatar>",
	 *			        	"apariencia"	: <apariencia>
	 *			    	}
	 *				}
	****************************************************** */
	@PutMapping("/adquirir")
	public ResponseEntity<Map<String,Object>> adquirirProducto(@RequestBody Dispone dispone, HttpSession session){
		
		Map<String,Object> compra = productoService.adquirir(dispone.getUsuario_id(), dispone.getProducto_id());
		
		return (compra.containsKey("compra"))?new ResponseEntity<>(compra, HttpStatus.OK):new ResponseEntity<>(compra, HttpStatus.NOT_ACCEPTABLE);
	}
}
