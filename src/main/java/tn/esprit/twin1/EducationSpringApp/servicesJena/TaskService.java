package tn.esprit.twin1.EducationSpringApp.servicesJena;

import org.apache.jena.rdf.model.*;
import org.apache.jena.query.*;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component
public class TaskService {

    private static final String RDF_FILE_PATH = "C:/Users/malek/OneDrive/Documents/Websemantique/webSemantique.rdf";
    private Model model;

    // Method to load the RDF file
    public Model loadRDF() {
        model = ModelFactory.createDefaultModel();
        InputStream in = FileManager.get().open(RDF_FILE_PATH);
        if (in == null) {
            throw new IllegalArgumentException("File not found: " + RDF_FILE_PATH);
        }
        model.read(in, null);
        return model;
    }

    private void saveRDF() {
        try (FileOutputStream out = new FileOutputStream(RDF_FILE_PATH)) {
            model.write(out, "RDF/XML");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addTask(String id, String name, String description, String status, String dueDate) {
        if (model == null) {
            loadRDF();
        }

        // Create a new individual for the task
        Resource taskResource = model.createResource(
                "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id);
        taskResource.addProperty(RDF.type, model
                .getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#Task"));

        // Add name, description, status, and dueDate properties
        taskResource.addProperty(
                model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasName"),
                model.createLiteral(name));
        taskResource.addProperty(
                model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasDescription"),
                model.createLiteral(description));
        taskResource.addProperty(
                model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasStatus"),
                model.createLiteral(status));
        taskResource.addProperty(
                model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasDueDate"),
                model.createLiteral(dueDate, "http://www.w3.org/2001/XMLSchema#date"));

        // Save the updated model back to the RDF file
        saveRDF();
    }




    public String queryTasks() {
        loadRDF();
        System.out.println("Model size: " + model.size());

        // Query string updated to retrieve task properties
        String queryString = "PREFIX ontology: <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#> "
                + "SELECT ?task ?hasName ?hasDescription ?hasStatus ?hasDueDate "
                + "WHERE { "
                + "  ?task a ontology:Task . "
                + "  ?task ontology:hasName ?hasName . "
                + "  ?task ontology:hasDescription ?hasDescription . "
                + "  ?task ontology:hasStatus ?hasStatus . "
                + "  OPTIONAL { ?task ontology:hasDueDate ?hasDueDate . }" // Use OPTIONAL for due date if it might not be present
                + "}";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();

            JSONArray tasksArray = new JSONArray();

            Map<String, JSONObject> taskMap = new HashMap<>();

            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();

                // Extract task properties
                String taskName = solution.get("hasName").toString();
                String taskDescription = solution.get("hasDescription").toString();
                String taskStatus = solution.get("hasStatus").toString();
                String dueDateWithType = solution.get("hasDueDate").toString();
                String dueDate = dueDateWithType.split("@")[0]; // Strip out datatype
                String idURL = solution.getResource("task").toString();
                String id = idURL.split("#")[1];

                // Create or get the JSON object for this task
                JSONObject taskObject = taskMap.getOrDefault(taskName, new JSONObject());
                if (!taskObject.has("task")) {
                    taskObject.put("id", id);
                    taskObject.put("task", taskName);
                    taskObject.put("description", taskDescription);
                    taskObject.put("status", taskStatus);
                    taskObject.put("dueDate", dueDate); // Use the stripped date

                    taskMap.put(taskName, taskObject);
                }
                tasksArray.put(taskObject);
            }

            // Create the final result JSON
            JSONObject resultJson = new JSONObject();
            resultJson.put("tasks", tasksArray);
            return resultJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error querying tasks: " + e.getMessage());
        }
    }



    // Method to update an existing task
    public void updateTask(String taskId, String newName, String newDescription, String newStatus, String newDueDate) {
        if (model == null) {
            loadRDF();
        }

        // Find the existing task resource by ID
        Resource taskResource = model.getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + taskId);

        if (taskResource != null) {
            // Update the properties
            taskResource.removeAll(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasName"));
            taskResource.addProperty(
                    model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasName"),
                    newName);

            taskResource.removeAll(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasDescription"));
            taskResource.addProperty(
                    model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasDescription"),
                    newDescription);

            taskResource.removeAll(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasStatus"));
            taskResource.addProperty(
                    model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasStatus"),
                    newStatus);

            taskResource.removeAll(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasDueDate"));
            taskResource.addProperty(
                    model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasDueDate"),
                    model.createTypedLiteral(newDueDate));

            // Save changes
            saveRDF();
        } else {
            throw new RuntimeException("Task with ID " + taskId + " not found.");
        }
    }

    // Method to delete a task
    public void deleteTask(String taskId) {
        if (model == null) {
            loadRDF();
        }

        // Find the existing task resource by ID
        Resource taskResource = model.getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + taskId);

        if (taskResource != null) {
            // Remove the resource from the model
            model.removeAll(taskResource, null, null);
            model.removeAll(null, null, taskResource);

            // Save changes
            saveRDF();
        } else {
            throw new RuntimeException("Task with ID " + taskId + " not found.");
        }
    }





}
