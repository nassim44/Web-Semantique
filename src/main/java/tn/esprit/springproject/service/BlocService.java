package tn.esprit.springproject.service;

import tn.esprit.springproject.Entity.Bloc;
import tn.esprit.springproject.Entity.Etudiant;

import java.util.List;
import java.util.Optional;

public interface BlocService {
    public Bloc addbl(Bloc bl) ;

    public List<Bloc> getbl();
    public Optional<Bloc> getBlocById(Long idBloc);
    Bloc updateBloc(Bloc bl);

    public void deleteBloc(Long idBloc);
     Bloc affecterChambresABloc(List<Long> numChambre, long idBloc) ;

        //Bloc affecterBlocAFoyer(Long idBloc, Long idFoyer) ;
}
