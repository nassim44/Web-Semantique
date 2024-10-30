package tn.esprit.twin1.EducationSpringApp.ControllersJena;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.twin1.EducationSpringApp.servicesJena.CategorieService;
import tn.esprit.twin1.EducationSpringApp.servicesJena.EventService;

import java.util.Map;
import java.util.Random;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Enable CORS for this controller
@RequestMapping("/categories") // Base URL for the controller
public class CategorieController {
    @Autowired
    private CategorieService categorieService;

    // Endpoint to get carbon footprint data from RDF file in JSON format
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCategories() {
        String result = categorieService.getCategories();
        return ResponseEntity.ok(result);
    }

    // Endpoint to add a new carbon footprint
   @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addEvents(@RequestBody Map<String, Object> newCategorie) {
        String id = generateRandomString(12);
        String categorieName = (String) newCategorie.get("Name");
        String categorieDescription = (String) newCategorie.get("Description");

        categorieService.addCategorie(id,categorieName,categorieDescription);
        return ResponseEntity.ok("Event footprint added successfully!");
    }
    private static final String CHARACTERS = "0123456789";
    private static final Random RANDOM = new Random();
    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
}
