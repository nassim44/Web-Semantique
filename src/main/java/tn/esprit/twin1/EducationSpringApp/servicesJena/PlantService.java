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
import java.util.HashMap;
import java.util.Map;

@Component
public class PlantService {

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

    // Method to add a new plant
    public void addPlant(String id, String name, String plantingDate, String species) {
        // Validate inputs
        if (id == null || name == null || plantingDate == null || species == null) {
            throw new IllegalArgumentException("All parameters must be non-null");
        }

        if (model == null) {
            loadRDF();
        }

        // Create a new individual for the plant
        Resource plantResource = model.createResource(
                "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id);
        plantResource.addProperty(RDF.type, model
                .getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#Plant"));

        // Add name, plantingDate, and species properties
        plantResource.addProperty(
                model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasName"),
                model.createLiteral(name));
        plantResource.addProperty(
                model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasPlantingDate"),
                model.createLiteral(plantingDate, "http://www.w3.org/2001/XMLSchema#date"));
        plantResource.addProperty(
                model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasSpecies"),
                model.createLiteral(species));

        // Save the updated model back to the RDF file
        saveRDF();
    }

   // Method to query all plants
    public String queryPlants() {
        loadRDF();
        System.out.println("Model size: " + model.size());

        // Query string updated to retrieve plant properties
        String queryString = "PREFIX ontology: <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#> "
                + "SELECT ?plant ?hasName ?hasPlantingDate ?hasSpecies "
                + "WHERE { "
                + "  ?plant a ontology:Plant . "
                + "  ?plant ontology:hasName ?hasName . "
                + "  OPTIONAL { ?plant ontology:hasPlantingDate ?hasPlantingDate . } "
                + "  ?plant ontology:hasSpecies ?hasSpecies . "
                + "}";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();

            JSONArray plantsArray = new JSONArray();

            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();

                // Extract plant properties
                String plantName = solution.get("hasName").toString();
                String plantingDateWithType = solution.get("hasPlantingDate") != null ? solution.get("hasPlantingDate").toString() : null;
                String plantingDate = plantingDateWithType != null ? plantingDateWithType.split("@")[0] : null; // Strip out datatype
                String species = solution.get("hasSpecies").toString();
                String idURL = solution.getResource("plant").toString();
                String id = idURL.split("#")[1];

                // Create a JSON object for this plant
                JSONObject plantObject = new JSONObject();
                plantObject.put("id", id);
                plantObject.put("name", plantName);
                plantObject.put("plantingDate", plantingDate); // Use the stripped date
                plantObject.put("species", species);

                plantsArray.put(plantObject);
            }

            // Create the final result JSON
            JSONObject resultJson = new JSONObject();
            resultJson.put("plants", plantsArray);
            return resultJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error querying plants: " + e.getMessage());
        }
    }
 /*
    // Method to update an existing plant
    public void updatePlant(String plantId, String newName, String newPlantingDate, String newSpecies) {
        if (model == null) {
            loadRDF();
        }

        // Find the existing plant resource by ID
        Resource plantResource = model.getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + plantId);

        if (plantResource != null) {
            // Update the properties
            plantResource.removeAll(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasName"));
            plantResource.addProperty(
                    model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasName"),
                    newName);

            plantResource.removeAll(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasPlantingDate"));
            plantResource.addProperty(
                    model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasPlantingDate"),
                    model.createTypedLiteral(newPlantingDate));

            plantResource.removeAll(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasSpecies"));
            plantResource.addProperty(
                    model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasSpecies"),
                    newSpecies);

            // Save changes
            saveRDF();
        } else {
            throw new RuntimeException("Plant with ID " + plantId + " not found.");
        }
    }

    // Method to delete a plant
    public void deletePlant(String plantId) {
        if (model == null) {
            loadRDF();
        }

        // Find the existing plant resource by ID
        Resource plantResource = model.getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + plantId);

        if (plantResource != null) {
            // Remove the resource from the model
            model.removeAll(plantResource, null, null);
            model.removeAll(null, null, plantResource);

            // Save changes
            saveRDF();
        } else {
            throw new RuntimeException("Plant with ID " + plantId + " not found.");
        }
    }
    */
}
