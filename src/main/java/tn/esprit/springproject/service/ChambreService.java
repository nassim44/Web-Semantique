package tn.esprit.springproject.service;

import org.springframework.http.ResponseEntity;
import tn.esprit.springproject.Entity.Bloc;
import tn.esprit.springproject.Entity.Chambre;
import tn.esprit.springproject.Entity.TypeChambre;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ChambreService {
    public Chambre addch(Chambre ch) ;

    public List<Chambre> getch();
    public Chambre getChambreById(Long idChambre);
    Chambre updateChambre(Chambre ch);

    public void deleteChambre(Long idChambre);
    List<Chambre> getChambresParBlocEtType (Long idBloc, TypeChambre typeC) ;
    // ResponseEntity<List<Chambre>> getChambresNonReserveesPourAnnee(Date date);

}
