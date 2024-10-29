package tn.esprit.springproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springproject.Entity.Etudiant;
import tn.esprit.springproject.Entity.Foyer;
import tn.esprit.springproject.service.EtudiantService;
import tn.esprit.springproject.service.FoyerService;

import java.util.List;
import java.util.Optional;

@RestController
public class FoyerController {
    @Autowired
    FoyerService foyerService;
    @PostMapping("/addfoy")
    public Foyer addf(@RequestBody Foyer foy) {
        Foyer foye = foyerService.addf(foy);
        return foye;

    }
    @GetMapping("/dispf")
    public List<Foyer> getet(){return foyerService.getfoy();}
    @PostMapping("/add/{idUniversite}")
    public Foyer ajouterFoyerEtAffecterAUniversite(@RequestBody Foyer foyer, @PathVariable Long idUniversite) {
        return foyerService.ajouterFoyerEtAffecterAUniversite(foyer, idUniversite);
    }

    @GetMapping("/foyer/{idFoyer}")
    public Optional<Foyer> getFoyerById(@PathVariable("idFoyer") Long idFoyer) {

        return foyerService.getFoyerById(idFoyer);
    }
    @PutMapping("/updatef/{idFoyer}")
    public Foyer updateFoyer(@PathVariable Long idFoyer, @RequestBody Foyer foy) {
        foy.setIdFoyer(idFoyer);
        return foyerService.updateFoyer(foy);
    }
    @DeleteMapping("/deletef/{idFoyer}")
    public void deleteFoyer(@PathVariable Long idFoyer) {
        foyerService.deleteFoyer(idFoyer);
    }

    @PutMapping("/ajouterFoyerEtAffecterAUniversite/{idUniversite}")
    public Foyer ajouterFoyerEtAffecterAUniversite(@RequestBody Foyer
                                                           foyer, @PathVariable("idUniversite") long idUniversite) {
        return foyerService.ajouterFoyerEtAffecterAUniversite(foyer, idUniversite);
    }
}
