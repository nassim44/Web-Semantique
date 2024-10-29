package tn.esprit.springproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springproject.Entity.Foyer;
import tn.esprit.springproject.Entity.Universite;
import tn.esprit.springproject.repository.FoyerRepository;
import tn.esprit.springproject.repository.UniversiteRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UniversiteServiceImpl implements UniversiteService {
    @Autowired
    UniversiteRepository universiterepository;
    @Autowired
    FoyerRepository foyerRepository;


    @Override
    public Universite addu(Universite uni) {
        return universiterepository.save(uni);

    }

    @Override
    public List<Universite> getuni() {
        return universiterepository.findAll();
    }

    @Override
    public Optional<Universite> getUniversiteById(Long idUniversite) {
        return universiterepository.findById(idUniversite) ;
    }

    @Override
    public Universite updateUniversite(Universite uni) {
        return universiterepository.save(uni);
    }

    @Override
    public void deleteUniversite(Long idUniversite) {
        universiterepository.deleteById(idUniversite);
    }
    public Universite affecterFoyerAUniversite(long idFoyer, String nomUniversite) {
        Universite universite = this.universiterepository.findByNomUniversite(nomUniversite);
        Foyer foy = this.foyerRepository.findById(idFoyer).orElse(null);
        universite.setFoyer(foy);
        return this.updateUniversite(universite);
    }
    @Override
    public Universite affecterFoyerAUniversite(Long idFoyer, String nomUniversite) {
        Foyer foyer = foyerRepository.findById(idFoyer).orElse(null);

        Universite universite = universiterepository.findByNomUniversite(nomUniversite);

        universite.setFoyer(foyer);
        return universiterepository.save(universite);
    }

    @Override
    public Universite desaffecterFoyerAUniversite(Long idUniversite) {
        Universite universite = universiterepository.findById(idUniversite).orElse(null);

        universite.setFoyer(null);
        return universiterepository.save(universite);
    }

}

