package tn.esprit.twin1.EducationSpringApp.ControllersJena;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.twin1.EducationSpringApp.servicesJena.OrderService;

import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Endpoint to get all orders in JSON format
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllOrders() {
        String result = orderService.queryOrders();
        return ResponseEntity.ok(result);
    }

    // Endpoint to add a new order
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addOrder(@RequestBody Map<String, Object> newOrder) {
        String id = generateRandomString(12);
        String address = (String) newOrder.get("Address");
        String status = (String) newOrder.get("Status");  // Get status from request

        // Convert the contain_Products from List<String> to JSONArray
        List<String> productIds = (List<String>) newOrder.get("contain_Products");
        JSONArray jsonProducts = new JSONArray();
        for (String productId : productIds) {
            jsonProducts.put(productId);
        }

        orderService.addOrder(id, address, status, jsonProducts); // Pass status to the service
        return ResponseEntity.ok("Order added successfully!");
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

    // Endpoint to update an existing order
    @PutMapping(value = "/{orderId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateOrder(@PathVariable String orderId,
                                              @RequestBody Map<String, Object> updatedOrder) {
        String newAddress = (String) updatedOrder.get("Address");
        String newStatus = (String) updatedOrder.get("Status");  // Get new status from request

        // Convert the contain_Products from List<String> to JSONArray
        List<String> productIds = (List<String>) updatedOrder.get("contain_Products");
        JSONArray jsonProducts = new JSONArray();
        for (String productId : productIds) {
            jsonProducts.put(productId);
        }

        orderService.updateOrder(orderId, newAddress, newStatus, jsonProducts); // Pass new status to the service
        return ResponseEntity.ok("Order updated successfully!");
    }

    // Endpoint to delete an order by ID
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable String id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted successfully!");
    }

    // Additional endpoint examples can be added based on further requirements
}
