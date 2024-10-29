package tn.esprit.springproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.springproject.Entity.Etudiant;
import tn.esprit.springproject.Entity.Reservation;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation,String> {
         List<Reservation> findByEtudiants(Etudiant etudiant);
     //List<Reservation> getReservationParAnneeUniversitaireEtNomUniversite(DateanneeUniversite, String nomUniversite) ;
}
