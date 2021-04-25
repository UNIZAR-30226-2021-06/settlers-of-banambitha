package es.susangames.catan.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.susangames.catan.exception.UserNotFoundException;

import es.susangames.catan.model.Estadisticas;
import es.susangames.catan.model.Usuario;
import es.susangames.catan.repo.EstadisticasRepo;
import es.susangames.catan.repo.UsuarioRepo;

@Service
public class UsuarioService {
	
	private final UsuarioRepo usuarioRepo;
	private final EstadisticasRepo estadisticasRepo;
	
	@Autowired
	public UsuarioService(UsuarioRepo usuarioRepo, EstadisticasRepo estadisticasRepo) {
		this.usuarioRepo = usuarioRepo;
		this.estadisticasRepo = estadisticasRepo;
	}
	
	
	public Usuario newUsuario(Usuario usuario) {
		
		//Se Guarda el usuario con la contraseña encriptada
		
		usuarioRepo.newUsuario(usuario.getNombre(),usuario.getEmail(),EncryptPassword(usuario.getContrasenya()));
		estadisticasRepo.newEstadisticas(usuario.getNombre());
		
		Usuario newUsuario = usuarioRepo.findById(usuario.getNombre()).orElseThrow(() -> new UserNotFoundException(usuario.getNombre()));
		return newUsuario;
	}
	
	public Usuario findUsuario(String usuarioId) {
		return usuarioRepo.findById(usuarioId).orElseThrow(() -> new UserNotFoundException(usuarioId));
	}
	
	public List<Usuario> findAllUsuarios() {
		return usuarioRepo.findAll();
	}
	
	public void deleteUsuario(String usuarioId) {
		usuarioRepo.deleteById(usuarioId);
	}
	
	public Estadisticas findEstadisticas(String usuarioId) {
		return estadisticasRepo.findById(usuarioId).orElseThrow(() -> new  UserNotFoundException(usuarioId));
	}
	
	public Estadisticas updateOnVictory(String usuarioId) {
		
		Estadisticas oldEstadisticas = estadisticasRepo.findById(usuarioId).orElseThrow(() -> new  UserNotFoundException(usuarioId));
		
		oldEstadisticas.onVictory();
		
		return estadisticasRepo.save(oldEstadisticas);
	}
	
	public Estadisticas updateOnDefeat(String usuarioId) {
		
		Estadisticas oldEstadisticas = estadisticasRepo.findById(usuarioId).orElseThrow(() -> new  UserNotFoundException(usuarioId));
		
		oldEstadisticas.onDefeat();
		
		return estadisticasRepo.save(oldEstadisticas);
	}
	
	public boolean validarUsuario(Usuario usuario) {
		
		String contrasenya = usuarioRepo.getConstrasenya(usuario.getNombre());
		System.out.println(contrasenya);
		return (contrasenya==null)?false:EncryptPassword(usuario.getContrasenya()).contentEquals(contrasenya);
	}
	
	public void setPartida(String usuarioId, String partidaId) {
		
		usuarioRepo.setPartida(usuarioId, partidaId);
		
	}
	
	public void endPartida(String usuarioId) {
		
		usuarioRepo.endPartida(usuarioId);
	}
	
	//Función que devuelve el resultado de encriptar mediante MD5 una cadena de caracteres 
	private static String EncryptPassword(String plain_password) {

        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plain_password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } 
        catch (NoSuchAlgorithmException e) 
        {
            e.printStackTrace();
        }
        return generatedPassword;
	}

}
