package com.example.demo.controllers;

import com.example.demo.dtos.OrderDTO;
import com.example.demo.dtos.UserDTO;
import com.example.demo.entities.Order;

import com.example.demo.services.OrderService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;



    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     *
     * @return
     */
    @GetMapping
    public ModelAndView getAllOrders() {
        ModelAndView mav=new ModelAndView("orders");
        List<OrderDTO> dtos = orderService.getAllOrders();
        mav.addObject("orders",dtos);
        return mav;
    }

    @GetMapping("/byId")
    public ModelAndView getUserById(@RequestParam("id") Long id) {
        return getOrderById(id, "orders");
    }

    @GetMapping("/orderUpdateDetails")
    public ModelAndView getUserByIdForUpdate(@RequestParam("id") Long id) {
        return getOrderById(id, "orderUpdate");
    }
    /**
     *Retrieves an order by its ID.
     * @param id The ID of the order to retrieve.
     * @return ResponseEntity containing the OrderDTO if found, otherwise HTTP status NOT_FOUND.
     */

    public ModelAndView getOrderById(@RequestParam("id") Long id,String viewName) {
        ModelAndView mav=new ModelAndView(viewName);
        try {
            OrderDTO orderDTO = orderService.getOrderById(id);
            mav.addObject("orders",orderDTO);
            return mav;
        } catch (Exception e) {
            mav.addObject("error","Orderul cu id ul respectiv nu exista");
            return mav;
        }
    }
    @GetMapping("/insertOrder")
    public ModelAndView showCreateOrderForm() {
        return new ModelAndView("orderCreate");
    }
    /**
     *Creates a new order.
     * @param orderDTO The OrderDTO representing the order to be created.
     * @return ResponseEntity containing the created OrderDTO with HTTP status CREATED.
     */
    @PostMapping( "/insertOrder")
    public ModelAndView createOrder(@RequestParam("fileType") String fileType,@Valid @ModelAttribute OrderDTO orderDTO) throws Exception {
        ModelAndView mav=new ModelAndView("orderCreate");
        OrderDTO createdOrder = orderService.createOrder(orderDTO,fileType);
        mav.addObject("order",createdOrder);
        return mav;
    }

    @PostMapping( "/insertOrderCustomer")
    public ModelAndView createOrderCustomer(@RequestParam("fileType") String fileType,@Valid @ModelAttribute OrderDTO orderDTO) throws Exception {
        ModelAndView mav=new ModelAndView("customerCart");
        OrderDTO createdOrder = orderService.createOrder(orderDTO,fileType);
        mav.addObject("order",createdOrder);
        return mav;
    }
    @GetMapping("/updateOrder")
    public ModelAndView showUpdateOrderForm() {
        return new ModelAndView("orderUpdate");
    }
    /**
     *Updates an existing order.
     * @param id The ID of the order to be updated.
     * @param orderDTO The OrderDTO representing the updated order data.
     * @return ResponseEntity containing the updated OrderDTO if successful, otherwise HTTP status NOT_FOUND.
     */
    @PostMapping("/updateOrder")
    public ModelAndView updateOrder(@RequestParam("id") Long id, @Valid @ModelAttribute OrderDTO orderDTO) {
        ModelAndView mav=new ModelAndView("orderUpdate");
        try {
            OrderDTO updatedOrder = orderService.updateOrder(id,orderDTO);
            mav.addObject("products",updatedOrder);
            return mav;
        } catch (Exception e) {
            return mav.addObject("error","Produsul cu id-ul respectiv nu exista");
        }
    }
    @GetMapping("/deleteOrder")
    public ModelAndView showDeleteProductForm() {
        return new ModelAndView("orderDelete");
    }
    /**
     *Deletes an order by its ID.
     * @param id The ID of the order to be deleted.
     * @return ResponseEntity with HTTP status OK if successful, otherwise HTTP status NOT_FOUND.
     */
    @PostMapping("/deleteOrder")
    public ModelAndView deleteOrder(@RequestParam("id") Long id) {
        ModelAndView mav=new ModelAndView("orderDelete");
        try {
            orderService.deleteOrder(id);
            return mav.addObject("ok","Orderul cu id-ul respectiv s-a sters");
        } catch (Exception e) {
            return mav.addObject("error","Orderul cu id-ul respectiv nu exista");
        }
    }

}
