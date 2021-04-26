package es.susangames.catan.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import es.susangames.catan.auxiliarModel.ProdDisponible;
import es.susangames.catan.exception.ProductNotFoundException;
import es.susangames.catan.exception.UserNotFoundException;
import es.susangames.catan.model.Dispone;
import es.susangames.catan.model.Producto;
import es.susangames.catan.model.Usuario;
import es.susangames.catan.repo.DisponeRepo;
import es.susangames.catan.repo.ProductoRepo;
import es.susangames.catan.repo.UsuarioRepo;

@Service
public class ProductoService {
	
	private final ProductoRepo productoRepo;
	private final DisponeRepo disponeRepo;
	private final UsuarioRepo usuarioRepo;

	public ProductoService(ProductoRepo productoRepo, DisponeRepo disponeRepo, UsuarioRepo usuarioRepo) {
		this.productoRepo = productoRepo;
		this.disponeRepo = disponeRepo;
		this.usuarioRepo = usuarioRepo;
	}
	
	public List<Producto> getProductosTienda(){
		
		List<Producto> productos = productoRepo.getTienda();
		
		return productos;
	}
	
	public List<Producto> getProdAdquiridos(String usuarioId){
		
		List<Producto> productos = new ArrayList<Producto>();
		
		for(String producto : disponeRepo.getProdAdquiridos(usuarioId)) {
			String [] params = producto.split(",", 4);
			productos.add(new Producto(params[0], params[3], Integer.parseInt(params[1]), params[2]));
		}
		
		return productos;
	}
	
	public List<ProdDisponible> getProdDisp(String usuarioId){
		
		List<ProdDisponible> productosDisponibles = new ArrayList<ProdDisponible>();
		
		for (String s : productoRepo.getProdDisp(usuarioId)) {
			String[] params = s.split(",", 5);
			productosDisponibles.add(new ProdDisponible(params[0], params[3], Integer.parseInt(params[1]),params[2],params[4].contentEquals("true")));
		}

		return productosDisponibles;
	}
	
	public Map<String,Object> adquirir(String usuarioId, String productoId) {
		
		Map<String,Object> map = new HashMap<String, Object>();
		
		Usuario usuario = usuarioRepo.findById(usuarioId).orElseThrow(() -> new UserNotFoundException(usuarioId));
		Producto producto = productoRepo.findById(productoId).orElseThrow(() -> new ProductNotFoundException(productoId));
		
		String factible = disponeRepo.transaccionFactible(usuarioId, productoId);
		
		if(factible.contentEquals("true")) {
			
			int newSaldo = usuario.getSaldo()-producto.getPrecio();
			
			usuario.setSaldo(newSaldo);
			usuarioRepo.updateSaldo(usuarioId, newSaldo);
			
			Dispone compra = disponeRepo.save(new Dispone(usuarioId, productoId));
			
			map.put("usuario", usuario);
			map.put("compra", compra);
			
		} else {
			map.put("usuario", null);
			map.put("compra", null);
		}
		return map;
	}
}
