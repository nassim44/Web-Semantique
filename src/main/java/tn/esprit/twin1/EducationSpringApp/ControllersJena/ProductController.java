package tn.esprit.twin1.EducationSpringApp.ControllersJena;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.twin1.EducationSpringApp.servicesJena.ProductService;

import java.util.Map;
import java.util.Random;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Endpoint to get all products in JSON format
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllProducts() {
        String result = productService.queryProducts();
        return ResponseEntity.ok(result);
    }

    // Endpoint to add a new product
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addProduct(@RequestBody Map<String, Object> newProduct) {
        String id = generateRandomString(12);
        String name = (String) newProduct.get("Name");
        String description = (String) newProduct.get("Description");
        String image = (String) newProduct.get("Image");
        Double price = Double.parseDouble(newProduct.get("Price").toString());
        Integer quantity = Integer.parseInt(newProduct.get("Quantity").toString());

        productService.addProduct(id, name, description, image, price, quantity);
        return ResponseEntity.ok("Product added successfully!");
    }

    // Method to generate a random ID
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

    // Endpoint to update an existing product
    @PutMapping(value = "/{productName}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateProduct(@PathVariable String productName,
                                                @RequestBody Map<String, Object> updatedProduct) {
        String newName = (String) updatedProduct.get("Name");
        String newDescription = (String) updatedProduct.get("Description");
        String newImage = (String) updatedProduct.get("Image");
        Double newPrice = Double.parseDouble(updatedProduct.get("Price").toString());
        Integer newQuantity = Integer.parseInt(updatedProduct.get("Quantity").toString());

        productService.updateProduct(productName, newName, newDescription, newImage, newPrice, newQuantity);
        return ResponseEntity.ok("Product updated successfully!");
    }

    // Endpoint to delete a product by ID
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully!");
    }

    // Endpoint to search product by name
    @GetMapping(value = "/search/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> searchProductByName(@PathVariable String name) {
        String result = productService.searchProductByName(name);
        return ResponseEntity.ok(result);
    }

    // Additional endpoint examples can be added based on further requirements
}
