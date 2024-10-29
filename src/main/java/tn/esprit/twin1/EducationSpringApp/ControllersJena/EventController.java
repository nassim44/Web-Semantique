package tn.esprit.twin1.EducationSpringApp.ControllersJena;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.esprit.twin1.EducationSpringApp.servicesJena.EventService;

import java.util.Map; // Import the Map interface
import java.util.Random;

@RestController
@CrossOrigin(origins = "http://localhost:4200") // Enable CORS for this controller
@RequestMapping("/events") // Base URL for the controller
public class EventController {

    @Autowired
    private EventService eventService;

    // Endpoint to get carbon footprint data from RDF file in JSON format
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCarbonFootprintsData() {
        String result = eventService.queryCarbonFootprints();
        return ResponseEntity.ok(result);
    }

    // Endpoint to add a new carbon footprint
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addEvents(@RequestBody Map<String, Object> newCarbonFootprint) {
        String id = generateRandomString(12);
        String eventName = (String) newCarbonFootprint.get("Name");
        Integer capacity = (Integer) newCarbonFootprint.get("Capacity");
        String eventDescription = (String) newCarbonFootprint.get("Description");
        String eventLocation = (String) newCarbonFootprint.get("Location");
        eventService.addEvent(id,eventName, capacity,eventDescription,eventLocation);
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
    // Endpoint to update an existing carbon footprint
    @PutMapping(value = "/{eventName}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateEvents(@PathVariable String eventName,
            @RequestBody Map<String, Object> updatedevent) {
        String newName = updatedevent.get("Name").toString();
        Integer newCapacity = Integer.parseInt(updatedevent.get("Capacity").toString());

        eventService.updateEvent(eventName,newName, newCapacity);
        return ResponseEntity.ok("Carbon footprint updated successfully!");
    }

    // Endpoint to delete a carbon footprint
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteEvents(@PathVariable String id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok("Carbon footprint deleted successfully!");
    }

    // Endpoint to search carbon footprints by name or type
    @GetMapping(value = "/search/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> searchEventByID(@PathVariable String name) {
        String result = eventService.searchByID(name);
        return ResponseEntity.ok(result);
    }

    // Endpoint to search carbon footprints within a specified range of carbon
    // values
    @GetMapping(value = "/searchByRange", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> searchCarbonFootprintsByRange(
            @RequestParam("minValue") double minValue,
            @RequestParam("maxValue") double maxValue) {
        String result = eventService.searchCarbonFootprintsByRange(minValue, maxValue);
        return ResponseEntity.ok(result);
    }

    // Endpoint to get all relations between CarbonFootprint and
    // CarbonReductionStrategy
    @GetMapping("/relations")
    public ResponseEntity<String> getRelations() {
        // Call the service method to get relations
        String relations = eventService.getRelationsBetweenCarbonFootprintAndReductionStrategy();

        // Return the response
        return ResponseEntity.ok(relations);
    }

    @PostMapping("/addRelation")
    public ResponseEntity<String> addRelation(@RequestParam String relationName) {
        eventService.addRelation(relationName);
        return ResponseEntity.ok("Relation ajoutée avec succès : " + relationName);
    }

    @PostMapping("/addInstanceWithRelation")
    public ResponseEntity<String> addInstanceWithRelation(
            @RequestParam String footprintName,
            @RequestParam String reductionStrategyName,
            @RequestParam double carbonValue,
            @RequestParam String type,
            @RequestParam String relationName) {
        eventService.addInstanceWithRelation(footprintName, reductionStrategyName, carbonValue, type, relationName);
        return ResponseEntity.ok("Instance et relation ajoutées avec succès : " + relationName);
    }

}
