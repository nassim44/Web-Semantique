package tn.esprit.springproject.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.springproject.Entity.Universite;
import tn.esprit.springproject.repository.UniversiteRepository;
@Repository
public interface UniversiteRepository extends JpaRepository<Universite,Long> {

    Universite findByNomUniversite(String nomUniversite);
}
