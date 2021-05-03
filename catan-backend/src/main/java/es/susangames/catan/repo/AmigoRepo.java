package es.susangames.catan.repo;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.susangames.catan.composite_keys.AmigoPK;
import es.susangames.catan.model.Amigo;

public interface AmigoRepo extends JpaRepository<Amigo, AmigoPK> {
	
	@Query(value = "SELECT * FROM amigo WHERE usuario1_id = :usuarioId", nativeQuery = true)
	public List<String> listaAmigos(@Param(value = "usuarioId") String usuarioId);


	@Query(value = "SELECT * FROM peticion_amistad WHERE usuario2_id = :usuarioId", nativeQuery = true)
	public List<String> listaPendientesRecibidas(@Param(value = "usuarioId") String usuarioId);
	
	@Query(value = "SELECT * FROM peticion_amistad WHERE usuario1_id = :usuarioId", nativeQuery = true)
	public List<String> listaPendientesEnviadas(@Param(value = "usuarioId") String usuarioId);
}
