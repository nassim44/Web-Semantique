package tn.esprit.springproject.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import tn.esprit.springproject.Entity.Bloc;
import tn.esprit.springproject.Entity.Chambre;
import tn.esprit.springproject.Entity.Etudiant;
import tn.esprit.springproject.Entity.TypeChambre;
import tn.esprit.springproject.repository.ChambreRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor

public class ChambreServiceImpl implements ChambreService {

    @Autowired
    ChambreRepository chambreRepository;
    @Override
    public Chambre addch(Chambre ch) {
        return chambreRepository.save(ch);
    }


    @Override
    public List<Chambre> getch() {
        return chambreRepository.findAll();
    }

    @Override
        public Chambre getChambreById(Long idChambre) {
        return chambreRepository.findById(idChambre).orElse(null);
    }
    @Override
    public Chambre updateChambre(Chambre ch) {
        return chambreRepository.save(ch);
    }

    @Override
    public void deleteChambre(Long idChambre) {
        chambreRepository.deleteById(idChambre);
    }
    @Override
    public List<Chambre> getChambresParBlocEtType(Long idBloc, TypeChambre typeC) {
        return chambreRepository.getChambresParBlocEtType(idBloc, typeC);  //Solution 1
    }




    //@Override Sol 2
    //public List<Chambre> getChambresParBlocEtType(long idBloc, TypeChambre typeC) {
       // Bloc b = blocRepository.findById(idBloc).orElse(null);

        //return  chambreRepository.findByBloc2AndTypeChambre(b,typeC);  }
}
