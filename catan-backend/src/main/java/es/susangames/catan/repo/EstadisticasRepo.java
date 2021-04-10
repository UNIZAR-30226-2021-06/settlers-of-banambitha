package es.susangames.catan.repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.susangames.catan.model.Estadisticas;

@Repository
public interface EstadisticasRepo extends JpaRepository<Estadisticas, String> {
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO estadisticas (usuario_id) values (:usuarioId)", nativeQuery = true)
	public int newEstadisticas(@Param("usuarioId") String usuarioId);

}
