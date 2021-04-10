package es.susangames.catan.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import es.susangames.catan.composite_keys.AmigoPK;
import es.susangames.catan.model.Amigo;

public interface AmigoRepo extends JpaRepository<Amigo, AmigoPK> {

}
