package tn.esprit.twin1.EducationSpringApp.servicesJena;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
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
public class ChapterService {

    private static final String RDF_FILE_PATH = "C:\\Users\\yassine\\Desktop\\9raya\\Web Semantique\\WebSemantique.rdf";
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

    public void addChapter(String id, String Name,String Description, String hours_toComplete) {
        if (model == null) {
            loadRDF();
        }

        Resource ChapterResource = model.createResource(
                "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id);
        ChapterResource.addProperty(RDF.type, model
                .getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#Chapter"));


        ChapterResource.addProperty(
                model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasName"),
                Name);
        ChapterResource.addProperty(
                model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasDescription"),
                Description);
        ChapterResource.addProperty(
                model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hashours_toComplete"),
                hours_toComplete);


// Save the updated model back to the RDF file
        saveRDF();
    }

    // Method to update an existing carbon footprint
    public void updateChapter(String id, String Name,String Description, String hours_toComplete) {
        if (model == null) {
            loadRDF();
        }

        // Find the existing carbon footprint resource by name
        Resource ChapterRessource = model
                .getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id);

        if (ChapterRessource != null) {
            // Update the properties
            ChapterRessource.removeAll(model
                    .getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasName"));
            ChapterRessource.addProperty(
                    model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasName"),
                    Name);

            ChapterRessource.removeAll(model
                    .getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasDescription"));
            ChapterRessource.addProperty(
                    model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasDescription"),
                    Description);

            ChapterRessource.removeAll(model
                    .getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hashours_toComplete"));
            ChapterRessource.addProperty(
                    model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hashours_toComplete"),
                    hours_toComplete);

            // Save changes
            saveRDF();
        }
    }


    // Method to delete a carbon footprint
    public void deleteChapter(String id) {
        if (model == null) {
            loadRDF();
        }

        // Find the existing carbon footprint resource by name
        Resource ChapterResource = model
                .getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id);

        if (ChapterResource != null) {
            // Remove the resource from the model
            model.removeAll(ChapterResource, null, null);
            model.removeAll(null, null, ChapterResource);

            // Save changes
            saveRDF();
        }
    }

    // Helper method to save the RDF model to file
    private void saveRDF() {
        try (FileOutputStream out = new FileOutputStream(RDF_FILE_PATH)) {
            model.write(out, "RDF/XML");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to query carbon footprints with dynamic object properties
    public String getChapters() {
        loadRDF();
        System.out.println("Model size: " + model.size());

        // Query string updated to also retrieve optional properties and related instances
        String queryString = "PREFIX ontology: <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#> "
                + "SELECT ?Chapter ?hasName ?hashours_toComplete ?hasDescription "
                + "WHERE { "
                + "  ?Chapter a ontology:Chapter . "
                + "  ?Chapter ontology:hasName ?hasName . "
                + "  ?Chapter ontology:hashours_toComplete ?hashours_toComplete . "
                + "  ?Chapter ontology:hasDescription ?hasDescription . "
                + "}";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();

            JSONArray ChaptersArray = new JSONArray();

            Map<String, JSONObject> ChapterMap = new HashMap<>();

            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();

                // Extract the Chapter name
                String ChapterName = solution.get("hasName").toString();
                String hashours_toComplete = solution.get("hashours_toComplete").toString();
                String ChapterDescription = solution.get("hasDescription").toString();
                String idURL = solution.getResource("Chapter").toString();
                String id = idURL.split("#")[1];
                // Create or get the JSON object for this Chapter
                JSONObject ChapterObject = ChapterMap.getOrDefault(ChapterName, new JSONObject());
                if (!ChapterObject.has("Chapter")) {
                    ChapterObject.put("id", id);
                    ChapterObject.put("Name", ChapterName);
                    ChapterObject.put("hours_toComplete", hashours_toComplete);
                    ChapterObject.put("Description", ChapterDescription);
                    ChapterMap.put(ChapterName, ChapterObject);
                }
                ChaptersArray.put(ChapterObject);
                // Extract the dynamic relationship if present

            }

            // Add all footprints to the main array


            JSONObject resultJson = new JSONObject();
            resultJson.put("Chapters", ChaptersArray);
            return resultJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error querying carbon footprints: " + e.getMessage());
        }
    }

    // ***** Recherche et filtrage ******/

    public String searchByName(String Name) {
        if (model == null) {
            loadRDF();
        }

        String escapedName = Name.replaceAll("[\"']", "\\\\$0");

        String queryString = "PREFIX ontology: <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#> "
                + "SELECT ?Chapter ?hasName ?hashours_toComplete ?hasDescription "
                + "WHERE { "
                + "  ?Chapter a ontology:Chapter . "
                + "  ?Chapter ontology:hasName ?hasName . "
                + "  ?Chapter ontology:hashours_toComplete ?hashours_toComplete . "
                + "  ?Chapter ontology:hasDescription ?hasDescription . "
                + "  FILTER (REGEX(STR(?hasName), \"" + escapedName + "\", \"i\")) "
                + "}";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();

            JSONArray ChaptersArray = new JSONArray();

            Map<String, JSONObject> ChapterMap = new HashMap<>();

            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();

                // Extract the Chapter name
                String ChapterName = solution.get("hasName").toString();
                String hashours_toComplete = solution.get("hashours_toComplete").toString();
                String ChapterDescription = solution.get("hasDescription").toString();
                String idURL = solution.getResource("Chapter").toString();
                String id = idURL.split("#")[1];
                // Create or get the JSON object for this Chapter
                JSONObject ChapterObject = ChapterMap.getOrDefault(ChapterName, new JSONObject());
                if (!ChapterObject.has("Chapter")) {
                    ChapterObject.put("id", id);
                    ChapterObject.put("Name", ChapterName);
                    ChapterObject.put("hours_toComplete", hashours_toComplete);
                    ChapterObject.put("Description", ChapterDescription);
                    ChapterMap.put(ChapterName, ChapterObject);
                }
                ChaptersArray.put(ChapterObject);

            }

            // Add all footprints to the main array


            JSONObject resultJson = new JSONObject();
            resultJson.put("Chapters", ChaptersArray);
            return resultJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error querying carbon footprints: " + e.getMessage());
        }
    }

    // Méthode pour rechercher des empreintes carbone entre deux valeurs de carbone
    public String searchCarbonFootprintsByRange(double minValue, double maxValue) {
        if (model == null) {
            loadRDF();
        }

        // Construire la requête SPARQL pour filtrer les empreintes carbone par valeur
        // de carbone
        String queryString = "PREFIX ontology: <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#> "
                + "SELECT ?carbonFootprint ?hasCarbonValue ?hasType ?property ?relatedInstance "
                + "WHERE { "
                + "  ?carbonFootprint a ontology:CarbonFootprint . "
                + "  ?carbonFootprint ontology:hasCarbonValue ?hasCarbonValue . "
                + "  ?carbonFootprint ontology:hasType ?hasType . "
                + "  OPTIONAL { ?carbonFootprint ?property ?relatedInstance . } "
                + "  FILTER (?hasCarbonValue >= " + minValue + " && ?hasCarbonValue <= " + maxValue + ") "
                + "}";

        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            JSONArray carbonFootprintsArray = new JSONArray();

            // Map to hold footprints and their relations
            Map<String, JSONObject> footprintsMap = new HashMap<>();

            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                // Extraire les détails de chaque empreinte carbone
                String carbonFootprintUrl = solution.getResource("carbonFootprint").toString();
                String carbonFootprintName = carbonFootprintUrl.split("#")[1];

                String hasCarbonValue = solution.get("hasCarbonValue").toString().replaceAll("\\^\\^.*", "");
                String hasType = solution.get("hasType").toString();

                // Create or get the JSON object for this footprint
                JSONObject carbonFootprintObject = footprintsMap.getOrDefault(carbonFootprintName, new JSONObject());
                if (!carbonFootprintObject.has("Chapter")) {
                    carbonFootprintObject.put("Chapter", carbonFootprintName);
                    carbonFootprintObject.put("hasCarbonValue", hasCarbonValue);
                    carbonFootprintObject.put("hasType", hasType);
                    carbonFootprintObject.put("relations", new JSONArray()); // Initialize relations as empty
                    footprintsMap.put(carbonFootprintName, carbonFootprintObject);
                }

                // Extract the dynamic relationship if present
                if (solution.contains("property") && solution.contains("relatedInstance")) {
                    RDFNode propertyNode = solution.get("property");
                    RDFNode relatedInstanceNode = solution.get("relatedInstance");

                    if (propertyNode.isResource() && relatedInstanceNode.isResource()) {
                        String property = propertyNode.asResource().getLocalName();
                        String relatedInstanceUrl = relatedInstanceNode.asResource().toString();
                        String relatedInstanceName = relatedInstanceUrl.split("#")[1];

                        JSONObject relationObject = new JSONObject();
                        relationObject.put("relation", property);
                        relationObject.put("relatedInstance", relatedInstanceName);

                        carbonFootprintObject.getJSONArray("relations").put(relationObject);
                    }
                }
            }

            // Add all footprints to the main array
            for (JSONObject footprintObject : footprintsMap.values()) {
                // Ensure relations are initialized to empty if not added
                if (footprintObject.getJSONArray("relations").length() == 0) {
                    footprintObject.put("relations", new JSONArray());
                }
                carbonFootprintsArray.put(footprintObject);
            }

            JSONObject resultJson = new JSONObject();
            resultJson.put("carbonFootprints", carbonFootprintsArray);
            return resultJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error searching carbon footprints by range: " + e.getMessage());
        }
    }

    // *** fonctions lier a la relation ***/

    public String getRelationsBetweenCarbonFootprintAndReductionStrategy() {
        if (model == null) {
            loadRDF();
        }

        String queryString = "PREFIX ontology: <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#> "
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
                + "PREFIX owl: <http://www.w3.org/2002/07/owl#> "
                + "SELECT ?property "
                + "WHERE { "
                + "  ?property a owl:ObjectProperty ; "
                + "           rdfs:domain ontology:CarbonFootprint ; "
                + "           rdfs:range ontology:CarbonReductionStrategy . "
                + "}";

        Query query = QueryFactory.create(queryString);
        JSONArray relationsArray = new JSONArray();

        synchronized (model) { // Synchronisation sur le modèle
            try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
                ResultSet results = qexec.execSelect();

                while (results.hasNext()) {
                    QuerySolution solution = results.nextSolution();
                    String property = solution.getResource("property").getLocalName();
                    relationsArray.put(property);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error retrieving relations: " + e.getMessage());
            }
        }

        JSONObject resultJson = new JSONObject();
        resultJson.put("relations", relationsArray);
        return resultJson.toString();
    }

    // Méthode pour ajouter une relation dynamique entre CarbonFootprint et
    // CarbonReductionStrategy
    public void addRelation(String relationName) {
        if (model == null) {
            loadRDF();
        }

        // On récupère les ressources pour CarbonFootprint et CarbonReductionStrategy
        Resource carbonFootprint = model
                .getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#CarbonFootprint");
        Resource carbonReductionStrategy = model.getResource(
                "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#CarbonReductionStrategy");

        // Créer une propriété avec le nom de relation dynamique
        Property relationProperty = model.createProperty(
                "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + relationName);

        // Ajouter les propriétés de type, de domaine et de range
        model.add(relationProperty, RDF.type, OWL.ObjectProperty);
        model.add(relationProperty, RDFS.domain, carbonFootprint);
        model.add(relationProperty, RDFS.range, carbonReductionStrategy);

        // Sauvegarde du modèle RDF
        saveRDF();
    }

    public void addInstanceWithRelation(String Chapter, String reductionStrategyName, double carbonValue,
            String type, String relationName) {
        if (model == null) {
            loadRDF();
        }

        // Créer l'instance CarbonFootprint
        Resource footprintInstance = model.createResource(
                "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + Chapter);
        footprintInstance.addProperty(RDF.type, model
                .getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#CarbonFootprint"));
        footprintInstance.addProperty(
                model.getProperty(
                        "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasCarbonValue"),
                model.createTypedLiteral(carbonValue));
        footprintInstance.addProperty(
                model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasType"),
                type);

        // Créer l'instance CarbonReductionStrategy
        Resource reductionStrategyInstance = model.getResource(
                "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + reductionStrategyName);

        // Définir la relation dynamique entre CarbonFootprint et
        // CarbonReductionStrategy
        Property relationProperty = model.createProperty(
                "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + relationName);
        footprintInstance.addProperty(relationProperty, reductionStrategyInstance);

        // Sauvegarde du modèle RDF
        saveRDF();
    }

}
