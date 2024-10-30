package tn.esprit.twin1.EducationSpringApp.ControllersJena;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.twin1.EducationSpringApp.servicesJena.CoursesService;

import java.util.Map;
import java.util.Random;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Enable CORS for this controller
@RequestMapping("/courses") // Base URL for the controller
public class CourseController {

    @Autowired
    private CoursesService coursesService;

    // Endpoint to get carbon footprint data from RDF file in JSON format
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllCourses() {
        String result = coursesService.getCourses();
        return ResponseEntity.ok(result);
    }

    // Endpoint to add a new carbon footprint
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addCourses(@RequestBody Map<String, Object> newCarbonFootprint) {
        String id = generateRandomString(12);
        String courseName = (String) newCarbonFootprint.get("Name");
        String courseDescription = (String) newCarbonFootprint.get("Description");
        String Added_date = (String) newCarbonFootprint.get("Added_date");
        coursesService.addCourse(id,courseName,courseDescription,Added_date);
        return ResponseEntity.ok("Course added successfully!");
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
    public ResponseEntity<String> updateCourses(@PathVariable String id,
            @RequestBody Map<String, Object> updatedcourse) {
        String name = updatedcourse.get("Name").toString();
        String Description = updatedcourse.get("Description").toString();
        String Added_date = updatedcourse.get("Added_date").toString();

        coursesService.updateCourse(id,name,Description, Added_date);
        return ResponseEntity.ok("Course updated successfully!");
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteCourses(@PathVariable String id) {
        coursesService.deleteCourse(id);
        return ResponseEntity.ok("Course deleted successfully!");
    }

    // Endpoint to search carbon footprints by name or type
    @GetMapping(value = "/search/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> searchCourseByID(@PathVariable String name) {
        String result = coursesService.searchByName(name);
        return ResponseEntity.ok(result);
    }

    // Endpoint to search carbon footprints within a specified range of carbon
    // values
    @GetMapping(value = "/searchByRange", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> searchCarbonFootprintsByRange(
            @RequestParam("minValue") double minValue,
            @RequestParam("maxValue") double maxValue) {
        String result = coursesService.searchCarbonFootprintsByRange(minValue, maxValue);
        return ResponseEntity.ok(result);
    }

    // Endpoint to get all relations between CarbonFootprint and
    // CarbonReductionStrategy
    @GetMapping("/relations")
    public ResponseEntity<String> getRelations() {
        // Call the service method to get relations
        String relations = coursesService.getRelationsBetweenCarbonFootprintAndReductionStrategy();

        // Return the response
        return ResponseEntity.ok(relations);
    }

    @PostMapping("/addRelation")
    public ResponseEntity<String> addRelation(@RequestParam String relationName) {
        coursesService.addRelation(relationName);
        return ResponseEntity.ok("Relation ajoutée avec succès : " + relationName);
    }

    @PostMapping("/addInstanceWithRelation")
    public ResponseEntity<String> addInstanceWithRelation(
            @RequestParam String footprintName,
            @RequestParam String reductionStrategyName,
            @RequestParam double carbonValue,
            @RequestParam String type,
            @RequestParam String relationName) {
        coursesService.addInstanceWithRelation(footprintName, reductionStrategyName, carbonValue, type, relationName);
        return ResponseEntity.ok("Instance et relation ajoutées avec succès : " + relationName);
    }

}
