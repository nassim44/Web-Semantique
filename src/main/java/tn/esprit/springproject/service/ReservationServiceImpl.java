package tn.esprit.springproject.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springproject.Entity.*;
import tn.esprit.springproject.repository.ChambreRepository;
import tn.esprit.springproject.repository.EtudiantRepository;
import tn.esprit.springproject.repository.FoyerRepository;
import tn.esprit.springproject.repository.ReservationRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
     EtudiantRepository etudiantRepository;
    @Autowired
     ChambreRepository chambreRepository;

    @Override
    public Reservation addr(Reservation res) {
        return reservationRepository.save(res);
    }

    @Override
    public List<Reservation> getres() {
        return reservationRepository.findAll();
    }

    @Override
    public Optional<Reservation> getReservationById(String idReservation) {
        return  reservationRepository.findById(idReservation) ;
    }

    @Override
    public Reservation updateReservation(Reservation res) {
        return  reservationRepository.save(res);
    }

    @Override
    public void deleteReservation(String idReservation) {
        reservationRepository.deleteById(idReservation);
    }

    @Override
    public Reservation ajouterReservation (Long idChambre, Long cin)  {
        Chambre chambre = chambreRepository.findById(idChambre).orElse(null);

        Etudiant etudiant = etudiantRepository.findByCin(cin);

        // Création de la réservation
        Reservation reservation = new Reservation();
        assert chambre != null;
        reservation.setNumReservation(chambre.getNumeroChambre() +"-"+ chambre.getBloc().getNomBloc().replace(" ", "") +"-"+ cin);
        reservation.setDebutAnneeUniv(LocalDate.of(LocalDate.now().getYear(), 9, 1));
        reservation.setFinAnneeUniv(LocalDate.of(LocalDate.now().getYear() + 1, 6, 1));
        reservation.setEstValide(true);

        // Déterminer la capacité maximale en fonction du type de chambre
        int capaciteMax = 0;
        if (TypeChambre.SIMPLE.equals(chambre.getTypeC())) {
            capaciteMax = 1;
        } else if (TypeChambre.DOUBLE.equals(chambre.getTypeC())) {
            capaciteMax = 2;
        } else if (TypeChambre.TRIPLE.equals(chambre.getTypeC())) {
            capaciteMax = 3;
        }

        // Vérifier si la capacité maximale de la chambre est atteinte
        long nombreReservations = chambre.getReservations().size();
        if (nombreReservations >= capaciteMax) {
            throw new IllegalStateException("La capacité maximale de la chambre est atteinte.");
        }

        // Gérer la relation ManyToMany
        Set<Etudiant> etudiants = new HashSet<>();
        etudiants.add(etudiant);
        reservation.setEtudiants(etudiants);

        // Sauvegarder la réservation
        Reservation savedReservation = reservationRepository.save(reservation);

        // Ajouter la réservation à la collection de réservations de la chambre et sauvegarder
        chambre.getReservations().add(savedReservation);
        chambreRepository.save(chambre);

        return savedReservation;
    }
    @Transactional
    public Reservation annulerReservation(long cinEtudiant) {
        Etudiant e =etudiantRepository.findByCin(cinEtudiant);
        List<Reservation> r = reservationRepository.findByEtudiants(e);
        for(Reservation re:r) {
            Chambre ch = chambreRepository.findChambreByReservations(re);
            ch.getReservations().remove(re);
            re.getEtudiants().remove(e);
            re.setEstValide(false);
            reservationRepository.save(re);
        }
        return null;
    }

    /*@Override
    public List<Reservation> getReservationParAnneeUniversitaireEtNomUniversite(String nomUniversite) {

    }*/

}
