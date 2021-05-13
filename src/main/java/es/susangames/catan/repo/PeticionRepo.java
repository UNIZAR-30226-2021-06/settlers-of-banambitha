package es.susangames.catan.repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.susangames.catan.composite_keys.PeticionPK;
import es.susangames.catan.model.PeticionAmistad;

public interface PeticionRepo extends JpaRepository<PeticionAmistad, PeticionPK> {

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM peticion_amistad WHERE usuario1_id = :usuarioId OR usuario2_id = :usuarioId", nativeQuery = true)
	public void eliminarPeticiones(@Param("usuarioId") String usuarioId);
	
}
