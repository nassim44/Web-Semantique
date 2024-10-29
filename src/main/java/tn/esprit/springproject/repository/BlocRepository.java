package tn.esprit.springproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.springproject.Entity.Bloc;
import tn.esprit.springproject.Entity.Chambre;

import java.util.List;
import java.util.Set;

@Repository
public interface BlocRepository  extends JpaRepository<Bloc,Long> {

}
