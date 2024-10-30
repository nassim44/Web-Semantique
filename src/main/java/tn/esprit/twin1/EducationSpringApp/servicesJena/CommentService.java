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
public class CommentService {

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

    public void addComment(String id, String forumId, String content) {
        if (model == null) {
            loadRDF();
        }

        Resource commentResource = model.createResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id);
        commentResource.addProperty(RDF.type, model.getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#Comment"));
        commentResource.addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasContent"), model.createLiteral(content));
        commentResource.addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#creationDate"), model.createLiteral(LocalDate.now().toString()));
        commentResource.addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#isCommentOf"), model.getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + forumId));

        saveRDF();
    }

    public void updateComment(String id, String newContent) {
        if (model == null) {
            loadRDF();
        }

        Resource commentResource = model.getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id);

        if (commentResource != null) {
            commentResource.removeAll(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasContent"));
            commentResource.addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasContent"), newContent);
            commentResource.removeAll(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#creationDate"));
            commentResource.addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#creationDate"), model.createLiteral(LocalDate.now().toString()));

            saveRDF();
        }
    }

    public String getAllComments() {
        // Load the RDF model if it's not already loaded
        if (model == null) {
            loadRDF();
        }
        System.out.println("Model size: " + model.size());

        // Query string updated to use ontology prefix
        String queryString = "PREFIX ontology: <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#> "
                + "SELECT ?comment ?content ?date ?forum "
                + "WHERE { "
                + "  ?comment a ontology:Comment . "
                + "  ?comment ontology:hasContent ?content . "
                + "  ?comment ontology:creationDate ?date . "
                + "  ?comment ontology:isCommentOf ?forum . "
                + "}";

        Query query = QueryFactory.create(queryString);
        JSONArray commentsArray = new JSONArray(); // Initialize comments array

        try (QueryExecution qe = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qe.execSelect();
            Map<String, JSONObject> commentMap = new HashMap<>(); // To prevent duplicates

            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();

                // Extract comment attribute
                String commentId = solution.getResource("comment").toString().split("#")[1];
                String content = solution.getLiteral("content").getString();
                String creationDate = solution.getLiteral("date").getString();
                String forumId = solution.getResource("forum").toString().split("#")[1];

                // Create or get the JSON object for this comment
                JSONObject commentObject = commentMap.getOrDefault(commentId, new JSONObject());
                if (!commentObject.has("id")) {
                    commentObject.put("id", commentId);
                    commentObject.put("content", content);
                    commentObject.put("creationDate", creationDate);
                    commentObject.put("forumId", forumId);
                    commentMap.put(commentId, commentObject);
                }
                commentsArray.put(commentObject); // Add the comment to the JSON array
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error querying comments: " + e.getMessage());
        }

        // Return the result as a JSON string
        JSONObject resultJson = new JSONObject();
        resultJson.put("comments", commentsArray);
        return resultJson.toString();
    }


    public String getCommentById(String id) {
        if (model == null) {
            loadRDF();
        }

        // Construct the SPARQL query to retrieve the comment by its ID
        String queryString = "PREFIX ontology: <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#> "
                + "SELECT ?content ?date ?forum "
                + "WHERE { "
                + "  <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id + "> a ontology:Comment . "
                + "  <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id + "> ontology:hasContent ?content . "
                + "  <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id + "> ontology:creationDate ?date . "
                + "  <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id + "> ontology:isCommentOf ?forum . "
                + "}";

        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            if (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                JSONObject commentJson = new JSONObject();
                commentJson.put("id", id);
                commentJson.put("content", solution.getLiteral("content").getString());
                commentJson.put("creationDate", solution.getLiteral("date").getString());
                commentJson.put("forumId", solution.getResource("forum").toString().split("#")[1]);
                return commentJson.toString(); // Return JSON as a string
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions and log errors
        }
        return "{\"error\": \"Comment not found\"}"; // Return a JSON error message if not found
    }

    public String getCommentByForumId(String forumId) {
        if (model == null) {
            loadRDF();
        }

        // Construct the SPARQL query to retrieve comments by forum ID
        String queryString = "PREFIX ontology: <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#> "
                + "SELECT ?comment ?content ?date "
                + "WHERE { "
                + "  ?comment a ontology:Comment . "
                + "  ?comment ontology:hasContent ?content . "
                + "  ?comment ontology:creationDate ?date . "
                + "  ?comment ontology:isCommentOf <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + forumId + "> . "
                + "}";

        Query query = QueryFactory.create(queryString);
        JSONArray commentsArray = new JSONArray(); // Initialize comments array

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                JSONObject commentJson = new JSONObject();
                String commentId = solution.getResource("comment").toString().split("#")[1];
                commentJson.put("id", commentId);
                commentJson.put("content", solution.getLiteral("content").getString());
                commentJson.put("creationDate", solution.getLiteral("date").getString());
                commentJson.put("forumId", forumId); // Use the passed forumId
                commentsArray.put(commentJson); // Add each comment to the JSON array
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions and log errors
            return "{\"error\": \"Error retrieving comments: " + e.getMessage() + "\"}";
        }

        // Return the result as a JSON string
        JSONObject resultJson = new JSONObject();
        resultJson.put("comments", commentsArray);
        return resultJson.toString();
    }


    public void deleteComment(String id) {
        if (model == null) {
            loadRDF();
        }

        Resource commentResource = model.getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id);
        if (commentResource != null) {
            model.removeAll(commentResource, null, null);
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
