package tn.esprit.twin1.EducationSpringApp.ControllersJena;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.esprit.twin1.EducationSpringApp.servicesJena.EventService;
import tn.esprit.twin1.EducationSpringApp.servicesJena.TaskService;

import java.util.Map; // Import the Map interface
import java.util.Random;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Enable CORS for this controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addTask(@RequestBody Map<String, Object> newTask) {
        String id = generateRandomString(12); // Generates a random ID for the task
        String taskName = (String) newTask.get("name");
        String taskDescription = (String) newTask.get("description");
        String taskStatus = (String) newTask.get("status");
        String taskDueDate = (String) newTask.get("dueDate");

        taskService.addTask(id, taskName, taskDescription, taskStatus, taskDueDate);
        return ResponseEntity.ok("Task added successfully!");
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
    public ResponseEntity<String> getTasksData() {
        String result = taskService.queryTasks(); // Call the updated method for tasks
        return ResponseEntity.ok(result);
    }



    @PutMapping(value = "/{taskId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateTask(@PathVariable String taskId,
                                             @RequestBody Map<String, Object> updatedTask) {
        String newName = updatedTask.get("name").toString();
        String newDescription = updatedTask.get("description").toString();
        String newStatus = updatedTask.get("status").toString();
        String newDueDate = updatedTask.get("dueDate").toString();

        taskService.updateTask(taskId, newName, newDescription, newStatus, newDueDate);
        return ResponseEntity.ok("Task updated successfully!");
    }

    // Endpoint to delete a task
    @DeleteMapping(value = "/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable String taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok("Task deleted successfully!");
    }


    @GetMapping("/relations/tasks-plants")
    public ResponseEntity<String> getTaskPlantRelations() {
        try {
            String relations = taskService.getRelationsBetweenTasksAndPlants();
            return ResponseEntity.ok(relations);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving relations: " + e.getMessage());
        }
    }


    @PostMapping("/{taskId}/relations")
    public ResponseEntity<String> addRelation(
            @PathVariable String taskId,
            @RequestBody Map<String, String> relationData) {

        String plantId = relationData.get("plantId");
        String relationName = relationData.get("relationName");

        if (plantId == null || relationName == null) {
            return ResponseEntity.badRequest().body("plantId and relationName are required.");
        }

        try {
            taskService.addRelation(taskId, plantId, relationName);
            return ResponseEntity.ok("Relation added successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error adding relation: " + e.getMessage());
        }
    }

}









