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
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE estadisticas SET partidas_jugadas = :partidas, total_de_victorias = :victorias, porcentaje_de_victorias = :porcentaje, racha_de_victorias_actual = :rachaActual, mayor_racha_de_victorias = :mayorRacha WHERE usuario_id = :usuarioId", nativeQuery = true)
	public void update(@Param("usuarioId") String usuarioId, @Param("partidas") int partidas, @Param("victorias") int victorias, @Param("porcentaje") float porcentaje, @Param("rachaActual") int rachaActual, @Param("mayorRacha") int mayorRacha);

}
