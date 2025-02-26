package com.example.demo.services;

/**
 * This class represents a service layer for shoppingCart-related operations
 */

import com.example.demo.dtos.ShoppingCartDTO;

import com.example.demo.dtos.builders.ShoppingCartBuilder;
import com.example.demo.entities.OrderItem;

import com.example.demo.entities.ShoppingCart;
import com.example.demo.repositories.ShoppingCartRepository;
import com.example.demo.validators.ShoppingCartValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class ShoppingCartService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SaleService.class);

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartValidator shoppingCartValidator;

    @Autowired
    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ShoppingCartValidator shoppingCartValidator) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartValidator = shoppingCartValidator;
    }

    public List<ShoppingCartDTO> getAllShoppingCarts() {
        List<ShoppingCart> cartsList = shoppingCartRepository.findAll();
        return cartsList.stream()
                .map(ShoppingCartBuilder::toShoppingCartDTO)
                .collect(Collectors.toList());
    }

    public ShoppingCartDTO getShoppingCartById(Long shoppingCartId) throws Exception {
        shoppingCartValidator.validateShoppingCartExists(shoppingCartId);
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findById(shoppingCartId);

            return ShoppingCartBuilder.toShoppingCartDTO(optionalShoppingCart.get());

    }

    /**
     *
     * @param shoppingCartDTO The DTO representing the cart to be created.
     * @return The createdShoppingCartDTO object.
     */
    public ShoppingCartDTO createShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        shoppingCartValidator.validateShoppingCartCreation(shoppingCartDTO);
        ShoppingCart shoppingCart = ShoppingCartBuilder.toEntity(shoppingCartDTO);
        // Calculate total based on order items
        double total = calculateTotal(shoppingCart.getOrderItems());
        shoppingCart.setTotal(total);
        shoppingCart = shoppingCartRepository.save(shoppingCart);
        LOGGER.debug("ShoppingCart with id {} was inserted in db", shoppingCart.getIdCart());
        return ShoppingCartBuilder.toShoppingCartDTO(shoppingCart);
    }

    /**
     *
     * @param id
     * @param shoppingCartDTO The DTO representing the cart to be updated.
     * @return The updatedShoppingCartDTO object.
     * @throws Exception
     */
    public ShoppingCartDTO updateShoppingCart(Long id, ShoppingCartDTO shoppingCartDTO) throws Exception {
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findById(id);
        if (optionalShoppingCart.isPresent()) {
            ShoppingCart shoppingCart = optionalShoppingCart.get();
            shoppingCart.setOrderItems(shoppingCartDTO.getOrderItems());
            double total = calculateTotal(shoppingCart.getOrderItems());
            shoppingCart.setTotal(total);
            shoppingCart = shoppingCartRepository.save(shoppingCart);
            return ShoppingCartBuilder.toShoppingCartDTO(shoppingCart);
        } else {
            LOGGER.error("Shopping cart with id {} was not found in db", id);
            throw new Exception("Shopping cart not found with id: " + id);
        }
    }

    /**
     *
     * @param id
     * @throws Exception
     */
    public void deleteShoppingCart(Long id) throws Exception {
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findById(id);
        if (optionalShoppingCart.isPresent()) {
            shoppingCartRepository.deleteById(id);
        } else {
            LOGGER.error("Shopping cart with id {} was not found in db", id);
            throw new Exception("Shopping cart not found with id: " + id);
        }
    }

    /**
     *
     * @param orderItems
     * @return
     */
    public double calculateTotal(List<OrderItem> orderItems) {
        double total = 0.0;
        if(orderItems==null||orderItems.isEmpty()) return total;
        else
        {for (OrderItem orderItem : orderItems) {
            total += orderItem.getProduct().getPrice() * orderItem.getQuantity();
        }
        return total;}
    }


}
