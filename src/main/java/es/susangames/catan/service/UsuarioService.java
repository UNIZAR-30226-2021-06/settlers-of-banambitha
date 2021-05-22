package es.susangames.catan.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.susangames.catan.auxiliarModel.ChangePassword;
import es.susangames.catan.exception.UserNotFoundException;

import es.susangames.catan.model.Estadisticas;
import es.susangames.catan.model.Usuario;
import es.susangames.catan.repo.EstadisticasRepo;
import es.susangames.catan.repo.UsuarioRepo;

@Service
public class UsuarioService {
	
	private final UsuarioRepo usuarioRepo;
	private final EstadisticasRepo estadisticasRepo;
	
	private static final Integer 	maxInformes = 5;
	private static final long 		diaMilis = 86400000;
	private static final long 		diasBloqueo = 3;
	
	private static final Map<String, List<String>> InGameReports = new HashMap<String, List<String>>();
	
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
	
	public Usuario updateUsuario(Usuario usuario) {
		
		String usuarioId = usuario.getNombre();
		
		if(usuarioRepo.existsById(usuarioId)) {
			
			String apariencia = usuario.getApariencia();
			String avatar = usuario.getAvatar();
			String idioma = usuario.getIdioma();
	
			if(apariencia!=null) {
				usuarioRepo.updateApariencia(usuarioId,apariencia);
			}
			if(avatar!=null) {
				usuarioRepo.updateAvatar(usuarioId,avatar);
			}
			if(idioma!=null) {
				usuarioRepo.updateIdioma(usuarioId,idioma);
			}
			
			return usuarioRepo.findById(usuarioId).orElseThrow(() -> new UserNotFoundException(usuarioId));
		}
		
		return new Usuario();
	}
	
	public Usuario changePassw(ChangePassword changePassword) {
		
		String usuarioId 	= changePassword.getUserId();
		String oldPassw		= changePassword.getOldPassw();
		String newPassw 	= changePassword.getNewPassw();
		
		String EncryptedOldPassword = EncryptPassword(oldPassw);
		String EncryptedNewPassword = EncryptPassword(newPassw);
		String StoredPassword = usuarioRepo.getConstrasenya(usuarioId);
		
		if(usuarioRepo.existsById(usuarioId) && EncryptedOldPassword.contentEquals(StoredPassword)) {
			
			usuarioRepo.newPassw(usuarioId, EncryptedNewPassword);
			
			return usuarioRepo.findById(usuarioId).orElseThrow(() -> new UserNotFoundException(usuarioId));
		}
		
		return new Usuario();
	}
	
	public void deleteUsuario(String usuarioId) {
		estadisticasRepo.deleteById(usuarioId);
		usuarioRepo.deleteUsuario(usuarioId);
	}
	
	public Estadisticas findEstadisticas(String usuarioId) {
		return estadisticasRepo.findById(usuarioId).orElseThrow(() -> new  UserNotFoundException(usuarioId));
	}
	
	private void updateOnVictory(String usuarioId) {
		
		Estadisticas oldEstadisticas = estadisticasRepo.findById(usuarioId).orElseThrow(() -> new  UserNotFoundException(usuarioId));
		
		oldEstadisticas.onVictory();
		
		int partidasJugadas = oldEstadisticas.getPartidasJugadas();
		int totalVictorias 	= oldEstadisticas.getTotalDeVictorias();
		float porcentajeV 	= oldEstadisticas.getPorcentajeDeVictorias();
		int rachaActual 	= oldEstadisticas.getRachaDeVictoriasActual();
		int mayorRacha 		= oldEstadisticas.getMayorRachaDeVictorias();
		
		estadisticasRepo.update(usuarioId, partidasJugadas, totalVictorias, porcentajeV, rachaActual, mayorRacha);
		
	}
	
	private void updateOnDefeat(String usuarioId) {
		
		Estadisticas oldEstadisticas = estadisticasRepo.findById(usuarioId).orElseThrow(() -> new  UserNotFoundException(usuarioId));
		
		oldEstadisticas.onDefeat();
		
		int partidasJugadas = oldEstadisticas.getPartidasJugadas();
		int totalVictorias 	= oldEstadisticas.getTotalDeVictorias();
		float porcentajeV 	= oldEstadisticas.getPorcentajeDeVictorias();
		int rachaActual 	= oldEstadisticas.getRachaDeVictoriasActual();
		int mayorRacha 		= oldEstadisticas.getMayorRachaDeVictorias();
		
		estadisticasRepo.update(usuarioId, partidasJugadas, totalVictorias, porcentajeV, rachaActual, mayorRacha);
	}
	
	public boolean validarUsuario(Usuario usuario) {
		
		String contrasenya = usuarioRepo.getConstrasenya(usuario.getNombre());
		return (contrasenya==null)?false:EncryptPassword(usuario.getContrasenya()).contentEquals(contrasenya);
	}
	
	
	public void setPartida(String usuarioId, String partidaId) {
		
		synchronized (InGameReports) {
			InGameReports.put(usuarioId, new ArrayList<String>());
		}
		
		usuarioRepo.setPartida(usuarioId, partidaId);
		
	}
	
	public void endPartida(String usuarioId, int puntosVictoria) {
		
		Usuario usuario = usuarioRepo.findById(usuarioId).orElseThrow(() -> new UserNotFoundException(usuarioId));
		
		synchronized (InGameReports) {
			InGameReports.remove(usuarioId);
		}
		
		if(puntosVictoria == 10) updateOnVictory(usuarioId);
		else updateOnDefeat(usuarioId);
		
		int newSaldo = usuario.getSaldo() + puntosVictoria;
		
		usuarioRepo.endPartida(usuarioId, newSaldo);
	}
	
	public boolean reportJugador(String reportante_id, String reportado_id) {
		
		Usuario reportado = usuarioRepo.findById(reportado_id).orElseThrow(() -> new UserNotFoundException(reportado_id));
		
		synchronized (InGameReports) {
			List<String> reportantes = InGameReports.get(reportado_id);
			
			if(reportado.getBloqueado()==null && usuarioRepo.existsById(reportante_id) && !reportantes.contains(reportante_id)) {
				
				reportantes.add(reportante_id);
				InGameReports.put(reportado_id, reportantes);
				
				if(reportado.getInformes() + 1 < maxInformes) {
					usuarioRepo.reportar(reportado_id);
				}
				else {
					usuarioRepo.bloquear(reportado_id, new Date(System.currentTimeMillis() + diaMilis*diasBloqueo));
				}
				return true;
			}
			return false;
		}
	}
	
	public void pardonJugador(Usuario usuario) {
		
		usuarioRepo.desbloquear(usuario.getNombre());
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
