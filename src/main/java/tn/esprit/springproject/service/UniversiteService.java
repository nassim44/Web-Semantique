package tn.esprit.springproject.service;

import tn.esprit.springproject.Entity.Foyer;
import tn.esprit.springproject.Entity.Universite;

import java.util.List;
import java.util.Optional;

public interface UniversiteService {

    public Universite addu(Universite uni) ;

    public List<Universite> getuni();
    public Optional<Universite> getUniversiteById(Long idUniversite);
    Universite updateUniversite(Universite uni);

    public void deleteUniversite(Long idUniversite);
    Universite affecterFoyerAUniversite(Long idFoyer, String nomUniversite);
    Universite desaffecterFoyerAUniversite(Long idUniversite);
}

