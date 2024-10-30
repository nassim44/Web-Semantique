package tn.esprit.twin1.EducationSpringApp.servicesJena;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDF;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component
public class CategorieService {

    private static final String RDF_FILE_PATH = "C:/Users/nassi/OneDrive/Bureau/antonio/webSemantique.rdf";
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
    public String getCategories() {
        loadRDF();
        System.out.println("Model size: " + model.size());

        // Query string updated to also retrieve optional properties and related instances
        String queryString = "PREFIX ontology: <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#> "
                + "SELECT ?categorie ?hasName ?hasDescription "
                + "WHERE { "
                + "  ?categorie a ontology:Categories_event . "
                + "  ?categorie ontology:hasName ?hasName . "
                + "  ?categorie ontology:hasDescription ?hasDescription . "
                + "}";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();

            JSONArray categoriessArray = new JSONArray();

            Map<String, JSONObject> eventMap = new HashMap<>();

            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();

                // Extract the event name
                String categorieName = solution.get("hasName").toString();
                String categorieDescription = solution.get("hasDescription").toString();
                String idURL = solution.getResource("categorie").toString();
                String id = idURL.split("#")[1];
                // Create or get the JSON object for this event
                JSONObject eventObject = eventMap.getOrDefault(categorieName, new JSONObject());
                if (!eventObject.has("categorie")) {
                    eventObject.put("id", id);
                    eventObject.put("categorie", categorieName);
                    eventObject.put("description", categorieDescription);
                    eventMap.put(categorieName, eventObject);
                }
                categoriessArray.put(eventObject);
                // Extract the dynamic relationship if present

            }

            // Add all footprints to the main array


            JSONObject resultJson = new JSONObject();
            resultJson.put("categories", categoriessArray);
            return resultJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error querying carbon footprints: " + e.getMessage());
        }
    }
    public void addCategorie(String id, String Name,String description) {
        if (model == null) {
            loadRDF();
        }
        // Create a new individual for the carbon footprint
        Resource eventResource = model.createResource(
                "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id);
        eventResource.addProperty(RDF.type, model
                .getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#Categories_event"));

// Add name and capacity properties
        eventResource.addProperty(
                model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasName"),
                model.createLiteral(Name));
        eventResource.addProperty(
                model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasDescription"),
                model.createLiteral(description));

        saveRDF();
    }
    private void saveRDF() {
        try (FileOutputStream out = new FileOutputStream(RDF_FILE_PATH)) {
            model.write(out, "RDF/XML");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
