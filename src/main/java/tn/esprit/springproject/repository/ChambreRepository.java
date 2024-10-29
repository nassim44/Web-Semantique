package tn.esprit.springproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.springproject.Entity.Chambre;
import tn.esprit.springproject.Entity.Reservation;
import tn.esprit.springproject.Entity.TypeChambre;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface ChambreRepository extends JpaRepository<Chambre,Long> {

    @Query("SELECT c FROM Chambre c WHERE c.bloc.idBloc = :idBloc AND c.typeC = :typeC")
    List<Chambre> getChambresParBlocEtType(Long idBloc, TypeChambre typeC);
    Chambre findChambreByReservations(Reservation reservation);

    //Sol2
    //List<Chambre> findByBlocIdBlocAndTypeC(Long idBloc, TypeChambre typeC);

    List<Chambre> findByNumeroChambreIn(List<Long> numChambre );
    Chambre findByReservationsContains(Reservation reservation);

    @Query("SELECT c FROM Chambre c LEFT JOIN c.reservations r WHERE r.idReservation IS NULL OR r.finAnneeUniv != :annee")
    List<Chambre> findChambresNonReserveesPourAnnee(@Param("annee") Date annee);




}
