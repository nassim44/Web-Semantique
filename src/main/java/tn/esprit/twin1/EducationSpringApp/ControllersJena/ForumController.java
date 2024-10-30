package tn.esprit.twin1.EducationSpringApp.ControllersJena;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.esprit.twin1.EducationSpringApp.servicesJena.ForumService;

import java.util.Random;

@RestController
@RequestMapping("/api/forums")
public class ForumController {

    @Autowired
    private ForumService forumService;

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

    @PostMapping
    public ResponseEntity<String> createForum( @RequestParam String title, @RequestParam String content) {
        try {
            String id = generateRandomString(12);
            forumService.addForum(id, title, content);
            return ResponseEntity.status(HttpStatus.CREATED).body("Forum created successfully"+id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating forum: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateForum(@PathVariable String id, @RequestParam String newTitle, @RequestParam String newContent) {
        try {
            forumService.updateForum(id, newTitle, newContent);
            return ResponseEntity.ok("Forum updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating forum: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<String> getAllForums() {
        try {
            String forumsJson = forumService.getAllForums(); // Get JSON string from service
            return ResponseEntity.ok(forumsJson); // Return the JSON string directly
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Error retrieving forums: " + e.getMessage() + "\"}");
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<String> getForumById(@PathVariable String id) {
        try {
            String forumJson = forumService.getForumById(id); // Get JSON string from the service
            if (forumJson != null && !forumJson.contains("error")) { // Check if forum is found
                return ResponseEntity.ok(forumJson); // Return the forum JSON
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JSONObject().put("message", "Forum not found").toString()); // JSON error response
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new JSONObject().put("Error retrieving forum:", e.getMessage()).toString()); // JSON error response
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteForum(@PathVariable String id) {
        try {
            forumService.deleteForum(id);
            return ResponseEntity.ok("Forum deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting forum: " + e.getMessage());
        }
    }
}
