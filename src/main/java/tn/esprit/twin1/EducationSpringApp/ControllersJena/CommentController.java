package tn.esprit.twin1.EducationSpringApp.ControllersJena;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.esprit.twin1.EducationSpringApp.servicesJena.CommentService;

import java.util.Random;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

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
    public ResponseEntity<String> createComment(@RequestParam String forumId, @RequestParam String content) {
        try {
            String id = generateRandomString(12);
            commentService.addComment(id ,forumId, content);
            return ResponseEntity.status(HttpStatus.CREATED).body("Comment created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating comment: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateComment(@PathVariable String id, @RequestParam String newContent) {
        try {
            commentService.updateComment(id, newContent);
            return ResponseEntity.ok("Comment updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating comment: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<String> getAllComments() {
        try {
            String comments = commentService.getAllComments();
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving comments: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getCommentById(@PathVariable String id) {
        try {
            String comment = commentService.getCommentById(id);
            if (comment != null) {
                return ResponseEntity.ok(comment);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("message"+ "Comment not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving comment: " + e.getMessage());
        }
    }

    @GetMapping("/getByForum/{forumId}")
    public ResponseEntity<String> getCommentByForumId(@PathVariable String forumId) {
        try {
            String comment = commentService.getCommentByForumId(forumId);
            if (comment != null) {
                return ResponseEntity.ok(comment);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("message"+ "Comment not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving comment: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable String id) {
        try {
            commentService.deleteComment(id);
            return ResponseEntity.ok("Comment deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting comment: " + e.getMessage());
        }
    }
}
