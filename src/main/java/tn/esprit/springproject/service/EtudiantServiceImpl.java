package tn.esprit.springproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springproject.Entity.Etudiant;
import tn.esprit.springproject.repository.EtudiantRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EtudiantServiceImpl implements EtudiantService {

    @Autowired
    EtudiantRepository etudiantRepository;

    @Override
    public Etudiant addet(Etudiant et) {
        return etudiantRepository.save(et);
    }

    @Override
    public List<Etudiant> getet() {
        return etudiantRepository.findAll();
    }

    @Override
    public Optional<Etudiant> getEtudiantById(Long idEtudiant) {
        return etudiantRepository.findById(idEtudiant) ;
    }
    @Override
    public Etudiant updateEtudiant(Etudiant et) {
        return etudiantRepository.save(et);
    }
    @Override
    public void deleteEtudiant(Long idEtudiant) {
        etudiantRepository.deleteById(idEtudiant);
    }
}

