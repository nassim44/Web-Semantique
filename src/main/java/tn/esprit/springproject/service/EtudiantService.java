package tn.esprit.springproject.service;

import tn.esprit.springproject.Entity.Etudiant;

import java.util.List;
import java.util.Optional;

public interface EtudiantService {
    public Etudiant addet(Etudiant et) ;

    public List<Etudiant> getet();
    public Optional<Etudiant> getEtudiantById(Long idEtudiant);
    Etudiant updateEtudiant(Etudiant et);

    public void deleteEtudiant(Long idEtudiant);

}
