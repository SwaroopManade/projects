package edu.swaroop.servesmart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.swaroop.servesmart.entity.Order;
import edu.swaroop.servesmart.entity.Response;
import edu.swaroop.servesmart.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins ="*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    
    @PostMapping
    protected ResponseEntity<Response<Order>> addOrder(@RequestBody Order order) {
        Order createdOrder = orderService.addOrder(order);
        Response<Order> response = new Response<>();
        response.setMessage("Order added successfully.");
        response.setStatus(HttpStatus.CREATED.value());
        response.setData(createdOrder);

        return new ResponseEntity<>(response, HttpStatus.CREATED); 
    }

    
    @GetMapping
    protected ResponseEntity<Response<List<Order>>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        Response<List<Order>> response = new Response<>();
        response.setMessage("Orders retrieved successfully.");
        response.setStatus(HttpStatus.OK.value());
        response.setData(orders);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/table/{tableId}")
    protected ResponseEntity<Response<List<Order>>> getOrdersByTableId(@PathVariable int tableId) {
        List<Order> orders = orderService.getOrdersByTableId(tableId);
        Response<List<Order>> response = new Response<>();

        if (!orders.isEmpty()) {
            response.setMessage("Orders retrieved successfully.");
            response.setStatus(HttpStatus.OK.value());
            response.setData(orders);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.setMessage("No orders found for this table.");
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    
    @GetMapping("/order/{id}")
    protected ResponseEntity<Response<Order>> getOrderById(@PathVariable int id) {
        Order order = orderService.getOrderById(id);
        Response<Order> response = new Response<>();

        if (order != null) {
            response.setMessage("Order retrieved successfully.");
            response.setStatus(HttpStatus.OK.value());
            response.setData(order);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.setMessage("Order not found.");
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

   
    @PutMapping("/update/table/{tableId}")
    protected ResponseEntity<Response<Order>> updateOrderByTableId(@PathVariable int tableId, @RequestBody Order order) {
        Response<Order> response = new Response<>();

        try {
            // Check if there is an existing order for the given tableId
//            Order existingOrder = orderService.findByTableId(tableId);
//            if (existingOrder == null) {
//                response.setMessage("No order found for the given table ID.");
//                response.setStatus(HttpStatus.NOT_FOUND.value());
//                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//            }

          
            Order updatedOrder = orderService.updateOrderByTableId(tableId, order);
            response.setMessage("Order updated successfully.");
            response.setStatus(HttpStatus.OK.value());
            response.setData(updatedOrder);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            // Handle any exceptions
            response.setMessage("An error occurred: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





    
    @DeleteMapping("/delete/{id}")
    protected ResponseEntity<Response<String>> deleteOrder(@PathVariable int id) throws Exception {
        orderService.deleteOrder(id);
        Response<String> response = new Response<>();
        response.setMessage("Order deleted successfully.");
        response.setStatus(HttpStatus.OK.value());
        response.setData("Order with ID " + id + " has been deleted.");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @DeleteMapping("/clear/{tableId}")
    public ResponseEntity<String> clearOrdersByTableId(@PathVariable("tableId") int tableId) {
        try {
            orderService.deleteOrdersByTableId(tableId);
            return ResponseEntity.ok("All orders cleared for table " + tableId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while clearing orders.");
        }
    }

}
