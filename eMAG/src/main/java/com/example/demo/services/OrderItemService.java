package com.example.demo.services;


import com.example.demo.dtos.OrderItemDTO;

import com.example.demo.dtos.builders.OrderItemBuilder;

import com.example.demo.entities.*;
import com.example.demo.repositories.*;
import com.example.demo.validators.OrderItemValidator;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class OrderItemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderItemService.class);

    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductRepository productRepository;
    private final OrderItemValidator orderItemValidator;

    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository, ShoppingCartRepository shoppingCartRepository, ProductRepository productRepository, OrderItemValidator orderItemValidator) {
        this.orderItemRepository = orderItemRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.productRepository = productRepository;
        this.orderItemValidator = orderItemValidator;
    }

    /**
     *Retrieves all orders.
     * @return List of OrderDTO objects representing all orders.
     */
    public List<OrderItemDTO> getAllOrderItems(){
        List<OrderItem> personList = orderItemRepository.findAll();
        return personList.stream()
                .map(OrderItemBuilder::toOrderItemDTO)
                .collect(Collectors.toList());
    }

    /**
     *Retrieves an order by its ID.
     * @param id The ID of the order to retrieve.
     * @return The OrderDTO object representing the retrieved order.
     * @throws Exception if the order with the specified ID is not found.
     */
    public OrderItemDTO getOrderItemById(long id) throws Exception{
        orderItemValidator.validateOrderItemExists(id);
        Optional<OrderItem> orderItem = orderItemRepository.findById(id);

        return OrderItemBuilder.toOrderItemDTO(orderItem.get());
    }


    /**
     *
     * @param orderItemDTO The DTO representing the orderItem to be created.
     * @return The created OrderItemDTO object.
     * @throws IllegalArgumentException
     */
    public OrderItemDTO createOrderItem(OrderItemDTO orderItemDTO) throws IllegalArgumentException {
        orderItemValidator.validateOrderItemCreation(orderItemDTO);
        Long cartId = orderItemDTO.getCartId();
        Long productId = orderItemDTO.getProductId();
        int quantity = orderItemDTO.getQuantity();




        Optional<ShoppingCart> cartOptional = shoppingCartRepository.findById(cartId);


        Optional<Product> productOptional = productRepository.findById(productId);



        ShoppingCart shoppingCart = cartOptional.get();
        Product product = productOptional.get();


        OrderItem orderItem = new OrderItem();
        orderItem.setShoppingCart(shoppingCart);
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);

        OrderItem savedOrderItem = orderItemRepository.save(orderItem);
        shoppingCart.setTotal(calculateTotal(shoppingCart.getOrderItems()));
        shoppingCartRepository.save(shoppingCart);

        LOGGER.debug("OrderItem with ID {} created", savedOrderItem.getId());

        return OrderItemBuilder.toOrderItemDTO(savedOrderItem);
    }

    /**
     *
     * @param orderItemId
     * @param updatedOrderItemDTO
     * @return The updatedOrderDTO object.
     * @throws Exception
     */
    public OrderItemDTO updateOrderItem(long orderItemId, OrderItemDTO updatedOrderItemDTO) throws Exception {
        LOGGER.debug("Request to update OrderItem with ID {}", orderItemId);
        LOGGER.debug("Updated OrderItemDTO: {}", updatedOrderItemDTO);
        orderItemValidator.validateOrderItemExists(orderItemId);
        orderItemValidator.validateOrderItemUpdate(updatedOrderItemDTO);


        // Retrieve existing order item
        Optional<OrderItem> optOrderItem = orderItemRepository.findById(orderItemId);

        OrderItem existingOrderItem = optOrderItem.get();

        LOGGER.debug("Existing OrderItem: {}", existingOrderItem);

        // Retrieve and update product
        //Optional<Product> productOptional = productRepository.findById(updatedOrderItemDTO.getProductId());
        //if (productOptional.isEmpty()) {
        //    throw new IllegalArgumentException("Product with ID " + updatedOrderItemDTO.getProductId() + " not found");
        //}
        //existingOrderItem.setProduct(productOptional.get());

        // Update quantity
        existingOrderItem.setQuantity(updatedOrderItemDTO.getQuantity());
        LOGGER.debug("Updated quantity: {}", updatedOrderItemDTO.getQuantity());



        // Update shopping cart total
        ShoppingCart shoppingCart = existingOrderItem.getShoppingCart();
        shoppingCart.setTotal(calculateTotal(shoppingCart.getOrderItems()));
        shoppingCartRepository.save(shoppingCart);
        LOGGER.debug("ShoppingCart total updated: {}", shoppingCart.getTotal());
        // Save updated order item
        OrderItem updatedOrderItem = orderItemRepository.save(existingOrderItem);
        LOGGER.debug("OrderItem saved with new quantity: {}", updatedOrderItem);
        return OrderItemBuilder.toOrderItemDTO(updatedOrderItem);
    }


    /**
     *
     * @param id
     * @throws Exception
     */
    public void deleteOrderItem(long id) throws Exception {
        orderItemValidator.validateOrderItemExists(id);

        Optional<OrderItem> optOrderItem = orderItemRepository.findById(id);

        long cartId=optOrderItem.get().getId();
        ShoppingCart cartOptional = optOrderItem.get().getShoppingCart();

        double total=cartOptional.getTotal();
        total-=optOrderItem.get().getProduct().getPrice()*optOrderItem.get().getQuantity();
        cartOptional.setTotal(total);
        shoppingCartRepository.save(cartOptional);
        orderItemRepository.deleteById(id);
    }



    public double calculateTotal(List<OrderItem> orderItems) {
        double total = 0.0;
        for (OrderItem orderItem : orderItems) {
            total += orderItem.getProduct().getPrice() * orderItem.getQuantity();
        }
        return total;
    }

}
