package tn.esprit.springproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springproject.Entity.Bloc;
import tn.esprit.springproject.Entity.Chambre;
import tn.esprit.springproject.Entity.Foyer;
import tn.esprit.springproject.repository.BlocRepository;
import tn.esprit.springproject.repository.ChambreRepository;
import tn.esprit.springproject.repository.FoyerRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BlocServiceImpl implements BlocService {
    @Autowired
    BlocRepository blocRepository ;
    @Autowired
    FoyerRepository foyerRepository;
    ChambreRepository chambreRepository;
    @Override
    public Bloc addbl(Bloc bl) {
        return blocRepository.save(bl);
    }

    @Override
    public List<Bloc> getbl() {
        return blocRepository.findAll();
    }

    @Override
    public Optional<Bloc> getBlocById(Long idBloc) {
        return blocRepository.findById(idBloc) ;
    }
    @Override
    public Bloc updateBloc(Bloc bl) {
        return blocRepository.save(bl);
    }
    @Override
    public void deleteBloc(Long idBloc) {
        blocRepository.deleteById(idBloc);
    }

    @Override
public Bloc affecterChambresABloc(List<Long> numChambre, long idBloc) {
        Bloc bloc = blocRepository.findById(idBloc).orElse(null);
        List<Chambre> chambreList =chambreRepository.findByNumeroChambreIn(numChambre);
        for(Chambre chambre: chambreList) {
            chambre.setBloc(bloc);
            chambreRepository.save(chambre);
        }
        return bloc;
    }


}
