package es.susangames.catan.service;

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
		
		return amigoRepo.listaAmigos(usuarioId);
	}
	
	public List<PeticionAmistad> listaPendientesRecibidas(String usuarioId) {
		
		return amigoRepo.listaPendientesRecibidas(usuarioId);
	}
	
	public List<PeticionAmistad> listaPendientesEnviadas(String usuarioId) {
		
		return amigoRepo.listaPendientesEnviadas(usuarioId);
	}
	
}
