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
	@Query(value = "UPDATE usuario SET apariencia = :apariencia WHERE usuario_id = :usuarioId", nativeQuery = true)
	public void updateApariencia(@Param("usuarioId") String usuarioId, @Param("apariencia") String apariencia);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE usuario SET avatar = :avatar WHERE usuario_id = :usuarioId", nativeQuery = true)
	public void updateAvatar(@Param("usuarioId") String usuarioId, @Param("avatar") String avatar);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE usuario SET idioma = :idioma WHERE usuario_id = :usuarioId", nativeQuery = true)
	public void updateIdioma(@Param("usuarioId") String usuarioId, @Param("idioma") String idioma);
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO usuario (usuario_id, email, contrasenya) values (:usuarioId, :mail, :passw)", nativeQuery = true)
	public void newUsuario(@Param("usuarioId") String usuarioId, @Param("mail") String mail, @Param("passw") String passw);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE usuario SET sala = :salaId WHERE usuario_id = :usuarioId", nativeQuery = true)
	public void setSala(@Param("usuarioId") String usuarioId, @Param("salaId") String salaId);

	@Modifying
	@Transactional
	@Query(value = "UPDATE usuario SET sala = NULL WHERE usuario_id = :usuarioId", nativeQuery = true)
	public void leaveSala(@Param("usuarioId") String usuarioId);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE usuario SET partida = :partidaId WHERE usuario_id = :usuarioId", nativeQuery = true)
	public void setPartida(@Param("usuarioId") String usuarioId, @Param("partidaId") String partidaId);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE usuario SET partida = NULL, saldo = :newSaldo  WHERE usuario_id = :usuarioId", nativeQuery = true)
	public void endPartida(@Param("usuarioId") String usuarioId, @Param("newSaldo") int newSaldo);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE usuario SET saldo = :newSaldo  WHERE usuario_id = :usuarioId", nativeQuery = true)
	public void updateSaldo(@Param("usuarioId") String usuarioId, @Param("newSaldo") int newSaldo);
	
}
