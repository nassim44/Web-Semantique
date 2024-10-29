package tn.esprit.springproject.service;

import tn.esprit.springproject.Entity.Foyer;
import tn.esprit.springproject.Entity.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationService {
    public Reservation addr(Reservation res) ;

    public List<Reservation> getres();
    public Optional<Reservation> getReservationById(String idReservation);
    Reservation updateReservation(Reservation res);

    public void deleteReservation(String idReservation);
    Reservation ajouterReservation (Long idChambre, Long cinEtudiant) ;
    Reservation annulerReservation (long cinEtudiant);
    //List<Reservation> getReservationParAnneeUniversitaireEtNomUniversite(DateanneeUniversite, String nomUniversite) ;

}
