package es.susangames.catan.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//import es.susangames.catan.auxiliarModel.ProdDisponible;
import es.susangames.catan.model.Producto;

public interface ProductoRepo extends JpaRepository<Producto, String> {
	
	@Query(value = "SELECT * FROM producto WHERE NOT (producto_id = 'Original' OR producto_id = 'Clasica')", nativeQuery = true)
	public List<Producto> getTienda();
	
	@Query(value = "SELECT p.*, EXISTS(SELECT * FROM dispone d WHERE d.producto_id = p.producto_id) FROM producto p WHERE NOT (p.producto_id = 'Original' OR p.producto_id = 'Clasica')", nativeQuery = true)
	public List<String> getProdDisp(@Param("usuarioId") String usuarioId);

}
