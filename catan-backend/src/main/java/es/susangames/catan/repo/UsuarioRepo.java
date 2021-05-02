package es.susangames.catan.repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.susangames.catan.model.Usuario;

@Repository
public interface UsuarioRepo extends JpaRepository<Usuario, String> {
	
	@Query(value = "SELECT contrasenya FROM Usuario WHERE usuario_id = :usuarioId", nativeQuery = true)
	public String getConstrasenya(@Param("usuarioId") String usuarioId);

	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO usuario (usuario_id, email, contrasenya) values (:usuarioId, :mail, :passw)", nativeQuery = true)
	public void newUsuario(@Param("usuarioId") String usuarioId, @Param("mail") String mail, @Param("passw") String passw);

}
