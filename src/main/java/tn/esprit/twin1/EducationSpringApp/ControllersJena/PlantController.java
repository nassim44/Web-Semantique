package tn.esprit.twin1.EducationSpringApp.ControllersJena;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.esprit.twin1.EducationSpringApp.servicesJena.PlantService; // Update to use PlantService

import java.util.Map;
import java.util.Random;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Enable CORS for this controller
@RequestMapping("/plants") // Change the request mapping to "/plants"
public class PlantController {

    @Autowired
    private PlantService plantService; // Update to use PlantService

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addPlant(@RequestBody Map<String, Object> newPlant) {
        String id = generateRandomString(12); // Generates a random ID for the plant
        String plantName = (String) newPlant.get("name");
        String plantingDate = (String) newPlant.get("planting_date");
        String species = (String) newPlant.get("species");

        plantService.addPlant(id, plantName, plantingDate, species); // Call the addPlant method
        return ResponseEntity.ok("Plant added successfully!");
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

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPlantsData() {
        String result = plantService.queryPlants(); // Call the updated method for plants
        return ResponseEntity.ok(result);
    }
/*
    @PutMapping(value = "/{plantId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updatePlant(@PathVariable String plantId,
                                              @RequestBody Map<String, Object> updatedPlant) {
        String newName = updatedPlant.get("name").toString();
        String newPlantingDate = updatedPlant.get("planting_date").toString();
        String newSpecies = updatedPlant.get("species").toString();

        plantService.updatePlant(plantId, newName, newPlantingDate, newSpecies); // Call the updatePlant method
        return ResponseEntity.ok("Plant updated successfully!");
    }

    // Endpoint to delete a plant
    @DeleteMapping(value = "/{plantId}")
    public ResponseEntity<String> deletePlant(@PathVariable String plantId) {
        plantService.deletePlant(plantId); // Call the deletePlant method
        return ResponseEntity.ok("Plant deleted successfully!");
    }

 */
}
