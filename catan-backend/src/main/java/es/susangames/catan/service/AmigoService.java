package es.susangames.catan.service;

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
		super();
		this.amigoRepo = amigoRepo;
		this.peticionRepo = peticionRepo;
	}

	public Amigo addAmistad(PeticionAmistad peticionAmistad) {
		
		Pair<Amigo,Amigo> amigos = peticionAmistad.generarAmistad();
		
		peticionRepo.delete(peticionAmistad);;
		amigoRepo.save(amigos.getValue1());
		return amigoRepo.save(amigos.getValue0());
	}
	
	
	
}
