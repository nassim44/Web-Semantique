package tn.esprit.springproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springproject.Entity.Bloc;
import tn.esprit.springproject.Entity.Chambre;
import tn.esprit.springproject.Entity.Etudiant;
import tn.esprit.springproject.service.BlocService;
import tn.esprit.springproject.service.ChambreService;
import tn.esprit.springproject.service.EtudiantService;

import java.util.List;
import java.util.Optional;

@RestController
public class BlocController {

    @Autowired
    BlocService blocService;
    ChambreService chambreService;

    @PostMapping("/addbl")
    public Bloc addbl(@RequestBody Bloc bl) {
        Bloc blo = blocService.addbl(bl);
        return blo;

    }
    @GetMapping("/dispbl")
    public List<Bloc> getet(){return blocService.getbl();}

    @GetMapping("/bloc/{idBloc}")
    public Optional<Bloc> getBloctById(@PathVariable("idBloc") Long idBloc) {

        return blocService.getBlocById(idBloc);
    }
    @PutMapping("/updatebl/{idBloc}")
    public Bloc updateBloc(@PathVariable Long idBloc, @RequestBody Bloc bl) {
        bl.setIdBloc(idBloc);
        return blocService.updateBloc(bl);
    }
    @DeleteMapping("/deletebl/{idBloc}")
    public void deleteBloc(@PathVariable Long idBloc) {
        blocService.deleteBloc(idBloc);
    }


    @PutMapping("/affecterChambres/{idBloc}")
    public Bloc affecterChambresABloc(@RequestBody List<Long> idChambre, @PathVariable Long idBloc) {
        return blocService.affecterChambresABloc(idChambre, idBloc);
    }
}
