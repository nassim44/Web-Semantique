package tn.esprit.twin1.EducationSpringApp.servicesJena;

import org.apache.jena.rdf.model.*;
import org.apache.jena.query.*;
import org.apache.jena.util.FileManager;
import org.springframework.stereotype.Component;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.jena.vocabulary.RDF;

import java.io.FileOutputStream;
import java.io.InputStream;

@Component
public class OrderService {

    // Path to the RDF file
    private static final String RDF_FILE_PATH = "C:/Users/seif/Desktop/web.rdf";
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

    // Method to add a new order
    public void addOrder(String id, String address, String status, JSONArray productIds) {
        if (model == null) {
            loadRDF();
        }

        Resource orderResource = model.createResource(
                "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id);
        orderResource.addProperty(RDF.type, model.getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#Order"));
        orderResource.addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasAddress"), model.createLiteral(address));
        orderResource.addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasStatus"), model.createLiteral(status));

        // Adding products to the order
        for (int i = 0; i < productIds.length(); i++) {
            String productId = productIds.getString(i);
            Resource productResource = model.getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + productId);
            orderResource.addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#contain_Products"), productResource);
        }

        saveRDF();
    }

    // Method to update an existing order
    public void updateOrder(String id, String newAddress, String newStatus, JSONArray newProductIds) {
        if (model == null) {
            loadRDF();
        }

        Resource orderResource = model.getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id);

        if (orderResource != null) {
            orderResource.removeAll(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasAddress"))
                    .addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasAddress"), newAddress);

            orderResource.removeAll(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasStatus"))
                    .addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasStatus"), newStatus);

            // Clear existing products
            orderResource.removeAll(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#contain_Products"));

            // Add new products
            for (int i = 0; i < newProductIds.length(); i++) {
                String productId = newProductIds.getString(i);
                Resource productResource = model.getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + productId);
                orderResource.addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#contain_Products"), productResource);
            }

            saveRDF();
        }
    }

    // Method to delete an order
    public void deleteOrder(String id) {
        if (model == null) {
            loadRDF();
        }

        Resource orderResource = model.getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id);

        if (orderResource != null) {
            model.removeAll(orderResource, null, null);
            model.removeAll(null, null, orderResource);
            saveRDF();
        }
    }

    // Method to query orders
    public String queryOrders() {
        loadRDF();

        String queryString = "PREFIX ontology: <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#> "
                + "SELECT ?order ?hasAddress ?hasStatus ?contain_Products "
                + "WHERE { "
                + "  ?order a ontology:Order . "
                + "  ?order ontology:hasAddress ?hasAddress . "
                + "  ?order ontology:hasStatus ?hasStatus . "
                + "  ?order ontology:contain_Products ?contain_Products . "
                + "}";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();

            JSONArray ordersArray = new JSONArray();

            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                JSONObject orderObject = new JSONObject();
                orderObject.put("id", solution.getResource("order").toString().split("#")[1]);
                orderObject.put("address", solution.get("hasAddress").toString());
                orderObject.put("status", solution.get("hasStatus").toString());
                // Add logic to include product details if necessary
                ordersArray.put(orderObject);
            }

            JSONObject resultJson = new JSONObject();
            resultJson.put("orders", ordersArray);
            return resultJson.toString();
        }
    }

    private void saveRDF() {
        try (FileOutputStream out = new FileOutputStream(RDF_FILE_PATH)) {
            model.write(out, "RDF/XML");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Additional methods can be added as needed
}
