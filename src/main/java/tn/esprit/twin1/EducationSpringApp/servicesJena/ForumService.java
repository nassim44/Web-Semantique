package tn.esprit.twin1.EducationSpringApp.servicesJena;

import org.apache.jena.rdf.model.*;
import org.apache.jena.query.*;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDF;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
public class ForumService {

    private static final String RDF_FILE_PATH = "C:/Users/zied loukil/OneDrive/Documents/web semantique/web.rdf";
    private Model model;

    public Model loadRDF() {
        model = ModelFactory.createDefaultModel();
        InputStream in = FileManager.get().open(RDF_FILE_PATH);
        if (in == null) {
            throw new IllegalArgumentException("File not found: " + RDF_FILE_PATH);
        }
        model.read(in, null);
        return model;
    }

    public void addForum(String id, String title, String content) {
        if (model == null) {
            loadRDF();
        }

        Resource forumResource = model.createResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id);
        forumResource.addProperty(RDF.type, model.getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#Forum"));
        forumResource.addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasTitle"), model.createLiteral(title));
        forumResource.addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasContent"), model.createLiteral(content));
        forumResource.addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#creationDate"), model.createLiteral(LocalDate.now().toString()));

        saveRDF();
    }

    public void updateForum(String id, String newTitle, String newContent) {
        if (model == null) {
            loadRDF();
        }

        Resource forumResource = model.getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id);

        if (forumResource != null) {
            forumResource.removeAll(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasTitle"));
            forumResource.addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasTitle"), newTitle);
            forumResource.removeAll(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasContent"));
            forumResource.addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasContent"), newContent);
            forumResource.removeAll(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#creationDate"));
            forumResource.addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#creationDate"), model.createLiteral(LocalDate.now().toString()));

            saveRDF();
        }
    }

    public String getAllForums() {
        // Load the RDF model if it's not already loaded
        if (model == null) {
            loadRDF();
        }
        System.out.println("Model size: " + model.size());

        // Query string updated to use ontology prefix
        String queryString = "PREFIX ontology: <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#> "
                + "SELECT ?forum ?title ?content ?date "
                + "WHERE { "
                + "  ?forum a ontology:Forum . "
                + "  ?forum ontology:hasTitle ?title . "
                + "  ?forum ontology:hasContent ?content . "
                + "  ?forum ontology:creationDate ?date . "
                + "}";

        Query query = QueryFactory.create(queryString);
        JSONArray forumsArray = new JSONArray(); // Initialize forums array

        try (QueryExecution qe = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qe.execSelect();
            Map<String, JSONObject> forumMap = new HashMap<>(); // To prevent duplicates

            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();

                // Extract forum attribute
                System.out.println(solution);
                String forumId = solution.getResource("forum").toString().split("#")[1];
                String title = solution.getLiteral("title").getString();
                String content = solution.getLiteral("content").getString();
                String creationDate = solution.getLiteral("date").getString();

                // Create or get the JSON object for this forum
                JSONObject forumObject = forumMap.getOrDefault(forumId, new JSONObject());
                if (!forumObject.has("id")) {
                    forumObject.put("id", forumId);
                    forumObject.put("title", title);
                    forumObject.put("content", content);
                    forumObject.put("creationDate", creationDate);
                    forumMap.put(forumId, forumObject);
                }
                forumsArray.put(forumObject); // Add the forum to the JSON array
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error querying forums: " + e.getMessage());
        }

        // Return the result as a JSON string
        JSONObject resultJson = new JSONObject();
        resultJson.put("forums", forumsArray);
        return resultJson.toString();
    }


    public String getForumById(String id) {
        if (model == null) {
            loadRDF();
        }

        // Construct the SPARQL query to retrieve the forum by its ID
        String queryString = "PREFIX ontology: <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#> "
                + "SELECT ?title ?content ?creationDate "
                + "WHERE { "
                + "  <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id + "> a ontology:Forum . "
                + "  <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id + "> ontology:hasTitle ?title . "
                + "  <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id + "> ontology:hasContent ?content . "
                + "  <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id + "> ontology:creationDate ?creationDate . "
                + "}";

        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            if (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                JSONObject forumJson = new JSONObject();
                forumJson.put("id", id);
                forumJson.put("title", solution.getLiteral("title").getString());
                forumJson.put("content", solution.getLiteral("content").getString());
                forumJson.put("creationDate", solution.getLiteral("creationDate").getString());
                return forumJson.toString(); // Return JSON as a string
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions and log errors
        }
        return "{\"error\": \"Forum not found\"}"; // Return a JSON error message if not found
    }


    public void deleteForum(String id) {
        if (model == null) {
            loadRDF();
        }

        Resource forumResource = model.getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id);
        if (forumResource != null) {
            model.removeAll(forumResource, null, null);
            saveRDF();
        }
    }

    private void saveRDF() {
        try (FileOutputStream out = new FileOutputStream(RDF_FILE_PATH)) {
            model.write(out, "RDF/XML");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
