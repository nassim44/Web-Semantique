package tn.esprit.springproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springproject.Entity.Foyer;
import tn.esprit.springproject.Entity.Reservation;
import tn.esprit.springproject.service.ReservationService;

import java.util.List;
import java.util.Optional;

@RestController
public class ReservationController {
    @Autowired
    ReservationService reservationService;
    @PostMapping("/addres")
    public Reservation addr(@RequestBody Reservation res) {
        Reservation resr = reservationService.addr(res);
        return resr;

    }
    @GetMapping("/dispr")
    public List<Reservation> getres(){return reservationService.getres();}

    @GetMapping("/reservation/{idReservation}")
    public Optional<Reservation> getReservationById(@PathVariable("idReservation") String idReservation) {

        return reservationService.getReservationById(idReservation);
    }
    @PutMapping("/updateres/{idReservation}")
    public Reservation updateReservation(@PathVariable String idReservation, @RequestBody Reservation res) {
        res.setIdReservation(idReservation);
        return reservationService.updateReservation(res);
    }
    @DeleteMapping("/deleteres/{idReservation}")
    public void deleteReservation(@PathVariable String idReservation) {
        reservationService.deleteReservation(idReservation);
    }
    @PostMapping("/add/{idChambre}/{cin}")
    public Reservation ajouterReservation(@PathVariable Long idChambre, @PathVariable Long cin) {
        return reservationService.ajouterReservation(idChambre, cin);
    }
    @PutMapping("/annulerReservation/{cin}")
    public Reservation annulerReservation(@PathVariable Long cin) {
        return reservationService.annulerReservation(cin);
    }
}




