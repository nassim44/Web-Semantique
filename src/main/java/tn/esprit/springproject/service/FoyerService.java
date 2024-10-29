package tn.esprit.springproject.service;

import org.springframework.stereotype.Service;
import tn.esprit.springproject.Entity.Etudiant;
import tn.esprit.springproject.Entity.Foyer;

import java.util.List;
import java.util.Optional;

public interface FoyerService {
    public Foyer addf(Foyer foy) ;

    public List<Foyer> getfoy();
    public Optional<Foyer> getFoyerById(Long idFoyer);
    Foyer updateFoyer(Foyer foy);

    public void deleteFoyer(Long idFoyer);
    Foyer ajouterFoyerEtAffecterAUniversite (Foyer foyer, Long idUniversite) ;

}
