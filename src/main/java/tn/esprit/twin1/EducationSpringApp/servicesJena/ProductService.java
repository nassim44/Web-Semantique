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
import java.util.HashMap;
import java.util.Map;

@Component
public class ProductService {

    //private static final String RDF_FILE_PATH = "C:/Users/nassi/OneDrive/Bureau/antonio/webSemantique.rdf";
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

    // Method to add a new product
    public void addProduct(String id, String name, String description, String image, double price, int quantity) {
        if (model == null) {
            loadRDF();
        }

        Resource productResource = model.createResource(
                "http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id);
        productResource.addProperty(RDF.type, model.getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#Product"));
        productResource.addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasName"), model.createLiteral(name));
        productResource.addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasDescription"), model.createLiteral(description));
        productResource.addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasImage"), model.createLiteral(image));
        productResource.addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasPrice"), model.createTypedLiteral(price));
        productResource.addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasQuantity"), model.createTypedLiteral(quantity));

        saveRDF();
    }

    // Method to update an existing product
    public void updateProduct(String id, String newName, String newDescription, String newImage, double newPrice, int newQuantity) {
        if (model == null) {
            loadRDF();
        }

        Resource productResource = model.getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id);

        if (productResource != null) {
            productResource.removeAll(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasName"))
                    .addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasName"), newName);
            productResource.removeAll(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasDescription"))
                    .addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasDescription"), newDescription);
            productResource.removeAll(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasImage"))
                    .addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasImage"), newImage);
            productResource.removeAll(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasPrice"))
                    .addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasPrice"), model.createTypedLiteral(newPrice));
            productResource.removeAll(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasQuantity"))
                    .addProperty(model.getProperty("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#hasQuantity"), model.createTypedLiteral(newQuantity));

            saveRDF();
        }
    }

    // Method to delete a product
    public void deleteProduct(String id) {
        if (model == null) {
            loadRDF();
        }

        Resource productResource = model.getResource("http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#" + id);

        if (productResource != null) {
            model.removeAll(productResource, null, null);
            model.removeAll(null, null, productResource);

            saveRDF();
        }
    }

    // Method to query products
    public String queryProducts() {
        loadRDF();

        String queryString = "PREFIX ontology: <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#> "
                + "SELECT ?product ?hasName ?hasDescription ?hasImage ?hasPrice ?hasQuantity "
                + "WHERE { "
                + "  ?product a ontology:Product . "
                + "  ?product ontology:hasName ?hasName . "
                + "  ?product ontology:hasDescription ?hasDescription . "
                + "  ?product ontology:hasImage ?hasImage . "
                + "  ?product ontology:hasPrice ?hasPrice . "
                + "  ?product ontology:hasQuantity ?hasQuantity . "
                + "}";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();

            JSONArray productsArray = new JSONArray();

            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                JSONObject productObject = new JSONObject();
                productObject.put("id", solution.getResource("product").toString().split("#")[1]);
                productObject.put("name", solution.get("hasName").toString());
                productObject.put("description", solution.get("hasDescription").toString());
                productObject.put("image", solution.get("hasImage").toString());
                productObject.put("price", solution.get("hasPrice").toString().replaceAll("\\^\\^.*", ""));
                productObject.put("quantity", solution.get("hasQuantity").toString().replaceAll("\\^\\^.*", ""));
                productsArray.put(productObject);
            }

            JSONObject resultJson = new JSONObject();
            resultJson.put("products", productsArray);
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
    public String searchProductByName(String name) {
        if (model == null) {
            loadRDF();
        }

        String queryString = "PREFIX ontology: <http://www.semanticweb.org/ahinfo/ontologies/2024/9/untitled-ontology-3#> "
                + "SELECT ?product ?hasName ?hasDescription ?hasImage ?hasPrice ?hasQuantity "
                + "WHERE { "
                + "  ?product a ontology:Product . "
                + "  ?product ontology:hasName ?hasName . "
                + "  ?product ontology:hasDescription ?hasDescription . "
                + "  ?product ontology:hasImage ?hasImage . "
                + "  ?product ontology:hasPrice ?hasPrice . "
                + "  ?product ontology:hasQuantity ?hasQuantity . "
                + "  FILTER(CONTAINS(LCASE(?hasName), LCASE(\"" + name + "\"))) "
                + "}";

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();

            JSONArray productsArray = new JSONArray();

            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                JSONObject productObject = new JSONObject();
                productObject.put("id", solution.getResource("product").toString().split("#")[1]);
                productObject.put("name", solution.get("hasName").toString());
                productObject.put("description", solution.get("hasDescription").toString());
                productObject.put("image", solution.get("hasImage").toString());
                productObject.put("price", solution.get("hasPrice").toString());
                productObject.put("quantity", solution.get("hasQuantity").toString());
                productsArray.put(productObject);
            }

            JSONObject resultJson = new JSONObject();
            resultJson.put("products", productsArray);
            return resultJson.toString();
        }
    }

}
