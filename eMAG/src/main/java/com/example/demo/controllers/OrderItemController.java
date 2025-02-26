package com.example.demo.controllers;

import com.example.demo.dtos.OrderItemDTO;
import com.example.demo.services.OrderItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/orderItems")
public class OrderItemController {

    private final OrderItemService orderItemService;

    @Autowired
    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    /**
     * Retrieves all order items.
     * @return ResponseEntity containing a list of OrderItemDTOs and HTTP status OK
     */
    @GetMapping
    public ResponseEntity<List<OrderItemDTO>> getAllOrderItems() {
        List<OrderItemDTO> dtos = orderItemService.getAllOrderItems();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * Retrieves an order item by ID.
     * @param id The ID of the order item to retrieve
     * @return ResponseEntity containing the OrderItemDTO if found, or HTTP status NOT_FOUND if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderItemDTO> getOrderItemById(@PathVariable Long id) {
        try {
            OrderItemDTO orderItemDTO = orderItemService.getOrderItemById(id);
            return new ResponseEntity<>(orderItemDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/insertOrderItem")
    public ModelAndView showCreateProductForm() {
        return new ModelAndView("customer");
    }
    /**
     * Creates a new order item.
     * @param orderItemDTO The OrderItemDTO containing the order item data to create
     * @return ResponseEntity containing the created OrderItemDTO and HTTP status CREATED
     */
    @PostMapping("/insertOrderItem")
    public ModelAndView createOrderItem( @ModelAttribute OrderItemDTO orderItemDTO) {
        ModelAndView mav=new ModelAndView("customer");
        try {
            OrderItemDTO createdOrderItem = orderItemService.createOrderItem(orderItemDTO);
            mav.addObject("orderItem",createdOrderItem);
            return mav;
        } catch (IllegalArgumentException e) {
            mav.addObject("error","S-a introdus gresit");
            return mav;
        }
    }
    @GetMapping("/updateOrderItem")
    public ModelAndView showUpdateOrderItemForm() {
        return new ModelAndView("customerCart");
    }
    /**
     * Updates an existing order item.
     * @param id The ID of the order item to update
     * @param orderItemDTO The updated OrderItemDTO containing the new order item data
     * @return ResponseEntity containing the updated OrderItemDTO if successful, or HTTP status NOT_FOUND if order item not found
     */
    @PostMapping("/updateOrderItem")
    public ModelAndView updateOrderItem(@RequestParam("id") Long id, @Valid @ModelAttribute OrderItemDTO orderItemDTO) {
        ModelAndView mav=new ModelAndView("customerCart");
        try {
            OrderItemDTO updatedOrderItem = orderItemService.updateOrderItem(id, orderItemDTO);
            mav.addObject("updatedOrderItem",updatedOrderItem);
            return mav;
        } catch (Exception e) {
            mav.addObject("error","Produsul cu id-ul respectiv nu exista");
            return mav;
        }
    }
    @GetMapping("/deleteOrderItem")
    public ModelAndView showDeleteProductForm() {
        return new ModelAndView("customerCart");
    }
    /**
     * Deletes an existing order item.
     * @param id The ID of the order item to delete
     * @return ResponseEntity with HTTP status OK if successful, or HTTP status NOT_FOUND if order item not found
     */
    @PostMapping("/deleteOrderItem")
    public ModelAndView deleteOrderItem(@RequestParam("id") Long id) {
        ModelAndView mav=new ModelAndView("customerCart");
        try {
            orderItemService.deleteOrderItem(id);
            return  mav.addObject("ok","Produsul cu id-ul respectiv s-a sters");
        } catch (Exception e) {
            return  mav.addObject("error","Produsul cu id-ul respectiv nu exista");
        }
    }

    // Implement other endpoints for updating, deleting, and retrieving order items as needed
}
