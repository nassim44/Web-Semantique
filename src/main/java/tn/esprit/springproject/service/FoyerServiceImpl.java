package tn.esprit.springproject.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springproject.Entity.Bloc;
import tn.esprit.springproject.Entity.Etudiant;
import tn.esprit.springproject.Entity.Foyer;
import tn.esprit.springproject.Entity.Universite;
import tn.esprit.springproject.repository.BlocRepository;
import tn.esprit.springproject.repository.FoyerRepository;
import tn.esprit.springproject.repository.UniversiteRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FoyerServiceImpl implements FoyerService {

    @Autowired
    FoyerRepository foyerRepository;
    UniversiteRepository universiteRepository;
    BlocRepository blocRepository;

    @Override
    public Foyer addf(Foyer foy) {
        return foyerRepository.save(foy);
    }
    @Override
    @Transactional
    public Foyer ajouterFoyerEtAffecterAUniversite(Foyer foyer, Long idUniversite) {
        Universite universite = universiteRepository.findById(idUniversite).orElse(null);
        universite.setFoyer(foyer);
        for (Bloc bloc : foyer.getBloc()) {
            bloc.setFoyer(foyer);
            blocRepository.save(bloc);
        }

        return foyer;
    }
    @Override
    public List<Foyer> getfoy() {
        return foyerRepository.findAll();
    }

    @Override
    public Optional<Foyer> getFoyerById(Long idFoyer) {
        return foyerRepository.findById(idFoyer) ;
    }
    @Override
    public Foyer updateFoyer(Foyer foy) {
        return foyerRepository.save(foy);
    }
    @Override
    public void deleteFoyer(Long idFoyer) {
        foyerRepository.deleteById(idFoyer);
    }
}
