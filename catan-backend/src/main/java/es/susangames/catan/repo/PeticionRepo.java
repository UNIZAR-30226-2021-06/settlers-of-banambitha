package es.susangames.catan.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import es.susangames.catan.composite_keys.PeticionPK;
import es.susangames.catan.model.PeticionAmistad;

public interface PeticionRepo extends JpaRepository<PeticionAmistad, PeticionPK> {

}
