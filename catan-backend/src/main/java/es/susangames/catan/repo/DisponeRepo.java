package es.susangames.catan.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.susangames.catan.composite_keys.DisponePK;
import es.susangames.catan.model.Dispone;

public interface DisponeRepo extends JpaRepository<Dispone, DisponePK>{

	@Query(value = "SELECT p.* FROM producto p WHERE (p.producto_id IN (SELECT d.producto_id FROM dispone d WHERE d.usuario_id = :usuarioId)) OR (p.nombre = 'Original') OR (p.nombre = 'Clasica')", nativeQuery = true)
	public List<String> getProdAdquiridos(@Param("usuarioId") String usuarioId);
	

	@Query(value = "SELECT u.saldo >= p.precio FROM usuario u, producto p WHERE u.usuario_id = :usuarioId AND p.producto_id = :productoId", nativeQuery = true)
	public String transaccionFactible(@Param("usuarioId") String usuarioId, @Param("productoId") String productoId);

}
