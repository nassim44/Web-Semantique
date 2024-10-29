package tn.esprit.springproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.springproject.Entity.Etudiant;
import tn.esprit.springproject.Entity.Universite;

@Repository
public interface EtudiantRepository  extends JpaRepository<Etudiant,Long> {
    Etudiant findByCin(Long cin);

}
