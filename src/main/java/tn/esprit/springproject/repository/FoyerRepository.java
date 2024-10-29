package tn.esprit.springproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.springproject.Entity.Foyer;
@Repository
public interface FoyerRepository extends JpaRepository<Foyer,Long > {
}
