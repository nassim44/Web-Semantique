package tn.esprit.springproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springproject.Entity.Etudiant;
import tn.esprit.springproject.service.EtudiantService;

import java.util.List;
import java.util.Optional;

@RestController
public class EtudiantController {
    @Autowired
    EtudiantService etudiantService;

    @PostMapping("/addet")
        public Etudiant addet(@RequestBody Etudiant et) {
            Etudiant Etud = etudiantService.addet(et);
            return Etud;

    }
    @GetMapping("/dispet")
    public List<Etudiant> getet(){return etudiantService.getet();}

    @GetMapping("/etudiant/{idEtudiant}")
    public Optional<Etudiant> getEtudiantById(@PathVariable("idEtudiant") Long idEtudiant) {

        return etudiantService.getEtudiantById(idEtudiant);
    }
    @PutMapping("/updateet/{idEtudiant}")
    public Etudiant updateEtudiant(@PathVariable Long idEtudiant, @RequestBody Etudiant et) {
        et.setIdEtudiant(idEtudiant);
        return etudiantService.updateEtudiant(et);
    }
    @DeleteMapping("/deleteet/{idEtudiant}")
    public void deleteEtudiant(@PathVariable Long idEtudiant) {
        etudiantService.deleteEtudiant(idEtudiant);
    }
}
