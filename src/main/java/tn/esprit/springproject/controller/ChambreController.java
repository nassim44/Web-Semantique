package tn.esprit.springproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springproject.Entity.Bloc;
import tn.esprit.springproject.Entity.Chambre;
import tn.esprit.springproject.Entity.Etudiant;
import tn.esprit.springproject.Entity.TypeChambre;
import tn.esprit.springproject.service.BlocService;
import tn.esprit.springproject.service.ChambreService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class ChambreController {

    @Autowired
    ChambreService chambreService;

    @PostMapping("/addcham")
    public Chambre addch(@RequestBody Chambre ch) {
        Chambre Cham = chambreService.addch(ch);
        return Cham;

    }
    @GetMapping("/dispch")
    public List<Chambre> getch(){return chambreService.getch();}

    @GetMapping("/chambre/{idChambre}")
    public Chambre getChambreById(@PathVariable("idChambre") Long idChambre) {

        return chambreService.getChambreById(idChambre);
    }
    @PutMapping("/updatech/{idChambre}")
    public Chambre updateChambre(@PathVariable Long idChambre, @RequestBody Chambre ch) {
        ch.setIdChambre(idChambre);
        return chambreService.updateChambre(ch);
    }
    @DeleteMapping("/deletech/{idChambre}")
    public void deleteChambre(@PathVariable Long idChambre) {
        chambreService.deleteChambre(idChambre);
    }

    @GetMapping("/getChambresParBlocEtType/{idBloc}/{typeC}")
    public List<Chambre> getChambresParBlocEtType(@PathVariable Long idBloc, @PathVariable TypeChambre typeC) {
        return chambreService.getChambresParBlocEtType(idBloc, typeC);
    }


}
