package es.susangames.catan.service;

import java.util.ArrayList;

import java.util.List;

import org.javatuples.Pair;
import org.springframework.stereotype.Service;

import es.susangames.catan.model.Amigo;
import es.susangames.catan.model.PeticionAmistad;
import es.susangames.catan.repo.AmigoRepo;
import es.susangames.catan.repo.PeticionRepo;

@Service
public class AmigoService {

	private final AmigoRepo amigoRepo;
	private final PeticionRepo peticionRepo;

	
	public AmigoService(AmigoRepo amigoRepo, PeticionRepo peticionRepo) {
		this.amigoRepo = amigoRepo;
		this.peticionRepo = peticionRepo;
	}
	
	
	public PeticionAmistad addPeticionAmistad(PeticionAmistad peticionAmistad) {
		return peticionRepo.save(peticionAmistad);
	}

	public Amigo aceptarPeticionAmistad(PeticionAmistad peticionAmistad) {
		
		Pair<Amigo,Amigo> amigos = peticionAmistad.generarAmistad();

		peticionRepo.delete(peticionAmistad);
		amigoRepo.save(amigos.getValue1());
		return amigoRepo.save(amigos.getValue0());
	}
	
	public void rechazarPeticionAmistad(PeticionAmistad peticionAmistad) {
		peticionRepo.delete(peticionAmistad);
	}
	
	public List<Amigo> listaAmigos(String usuarioId) {
		
		List<Amigo> amigos = new ArrayList<Amigo>();
		
		for(String amigo : amigoRepo.listaAmigos(usuarioId)) {
			String [] parameters = amigo.split(",", 2);
			amigos.add(new Amigo(parameters[0],parameters[1]));
		}
		
		return amigos;
	}
	
	public List<PeticionAmistad> listaPendientesRecibidas(String usuarioId) {
		
		List<PeticionAmistad> pendientesRecibidas = new ArrayList<PeticionAmistad>();
		
		for(String peticion : amigoRepo.listaPendientesRecibidas(usuarioId)) {
			String [] parameters = peticion.split(",", 2);
			pendientesRecibidas.add(new PeticionAmistad(parameters[0],parameters[1]));
		}
		return pendientesRecibidas;
	}
	
	public List<PeticionAmistad> listaPendientesEnviadas(String usuarioId) {
		
		List<PeticionAmistad> pendientesEnviadas = new ArrayList<PeticionAmistad>();
		
		for(String peticion :amigoRepo.listaPendientesEnviadas(usuarioId)) {
			String [] parameters = peticion.split(",", 2);
			pendientesEnviadas.add(new PeticionAmistad(parameters[0],parameters[1]));
		}
		return pendientesEnviadas;
	}
}
