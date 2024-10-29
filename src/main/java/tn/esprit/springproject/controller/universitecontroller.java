package tn.esprit.springproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springproject.Entity.Reservation;
import tn.esprit.springproject.Entity.Universite;
import tn.esprit.springproject.service.UniversiteService;

import java.util.List;
import java.util.Optional;

@RestController
public class universitecontroller {
    @Autowired
     UniversiteService universiteService;

    @PostMapping("/addun")
    public Universite addu(@RequestBody Universite universite) {
         Universite Uni1 = universiteService.addu(universite);
        return Uni1;

    }


    @GetMapping("/reservation/{idUniversite}")
    public Optional<Universite> getReservationById(@PathVariable("idUniversite") Long idUniversite) {

        return universiteService.getUniversiteById(idUniversite);
    }

    @PutMapping("/updateres/{idUniversite}")
    public Universite updateUniversite(@PathVariable Long idUniversite, @RequestBody Universite uni) {
        uni.setIdUniversite(idUniversite);
        return universiteService.updateUniversite(uni);
    }
    @DeleteMapping("/deleteres/{idUniversite}")
    public void deleteUniversite(@PathVariable Long idUniversite) {
        universiteService.deleteUniversite(idUniversite);
    }
    @PutMapping("/affecterFoyer/{idFoyer}/{nomUniversite}")
    public Universite affecterFoyerAUniversite(@PathVariable Long idFoyer, @PathVariable String nomUniversite) {
        return universiteService.affecterFoyerAUniversite(idFoyer, nomUniversite);
    }
    @PutMapping("/desaffecterFoyer/{idUniversite}")
    public Universite desaffecterFoyerAUniversite(@PathVariable Long idUniversite) {
        return universiteService.desaffecterFoyerAUniversite(idUniversite);
    }
}
