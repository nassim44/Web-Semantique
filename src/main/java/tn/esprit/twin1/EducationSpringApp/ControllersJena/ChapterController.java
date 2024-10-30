package tn.esprit.twin1.EducationSpringApp.ControllersJena;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.twin1.EducationSpringApp.servicesJena.ChapterService;

import java.util.Map;
import java.util.Random;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Enable CORS for this controller
@RequestMapping("/chapter") // Base URL for the controller
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    // Endpoint to get carbon footprint data from RDF file in JSON format
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllChapters() {
        String result = chapterService.getChapters();
        return ResponseEntity.ok(result);
    }

    // Endpoint to add a new carbon footprint
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addChapters(@RequestBody Map<String, Object> newCarbonFootprint) {
        String id = generateRandomString(12);
        String courseName = (String) newCarbonFootprint.get("Name");
        String courseDescription = (String) newCarbonFootprint.get("Description");
        String hours_toComplete = (String) newCarbonFootprint.get("hours_toComplete");
        chapterService.addChapter(id,courseName,courseDescription,hours_toComplete);
        return ResponseEntity.ok("Chapter added successfully!");
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

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateChapters(@PathVariable String id,
            @RequestBody Map<String, Object> updatedcourse) {
        String name = updatedcourse.get("Name").toString();
        String Description = updatedcourse.get("Description").toString();
        String hours_toComplete = updatedcourse.get("hours_toComplete").toString();

        chapterService.updateChapter(id,name,Description, hours_toComplete);
        return ResponseEntity.ok("Chapter updated successfully!");
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteChapters(@PathVariable String id) {
        chapterService.deleteChapter(id);
        return ResponseEntity.ok("Chapter deleted successfully!");
    }

    // Endpoint to search carbon footprints by name or type
    @GetMapping(value = "/search/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> searchChapterByID(@PathVariable String name) {
        String result = chapterService.searchByName(name);
        return ResponseEntity.ok(result);
    }

    // Endpoint to search carbon footprints within a specified range of carbon
    // values
    @GetMapping(value = "/searchByRange", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> searchCarbonFootprintsByRange(
            @RequestParam("minValue") double minValue,
            @RequestParam("maxValue") double maxValue) {
        String result = chapterService.searchCarbonFootprintsByRange(minValue, maxValue);
        return ResponseEntity.ok(result);
    }

    // Endpoint to get all relations between CarbonFootprint and
    // CarbonReductionStrategy
    @GetMapping("/relations")
    public ResponseEntity<String> getRelations() {
        // Call the service method to get relations
        String relations = chapterService.getRelationsBetweenCarbonFootprintAndReductionStrategy();

        // Return the response
        return ResponseEntity.ok(relations);
    }

    @PostMapping("/addRelation")
    public ResponseEntity<String> addRelation(@RequestParam String relationName) {
        chapterService.addRelation(relationName);
        return ResponseEntity.ok("Relation ajoutée avec succès : " + relationName);
    }

    @PostMapping("/addInstanceWithRelation")
    public ResponseEntity<String> addInstanceWithRelation(
            @RequestParam String footprintName,
            @RequestParam String reductionStrategyName,
            @RequestParam double carbonValue,
            @RequestParam String type,
            @RequestParam String relationName) {
        chapterService.addInstanceWithRelation(footprintName, reductionStrategyName, carbonValue, type, relationName);
        return ResponseEntity.ok("Instance et relation ajoutées avec succès : " + relationName);
    }

}
