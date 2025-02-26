package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.example.demo.controllers.OrderItemController;
import com.example.demo.dtos.OrderItemDTO;
import com.example.demo.entities.OrderItem;
import com.example.demo.entities.Product;
import com.example.demo.entities.ShoppingCart;
import com.example.demo.repositories.OrderItemRepository;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.repositories.ShoppingCartRepository;
import com.example.demo.services.OrderItemService;
import com.example.demo.validators.OrderItemValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class OrderItemCreateTest {
    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderItemValidator orderItemValidator;

    @InjectMocks
    private OrderItemService orderItemService;  // Replace with the actual service class name

    private OrderItemDTO orderItemDTO;
    private ShoppingCart shoppingCart;
    private Product product;
    private OrderItem orderItem;

    @BeforeEach
    public void setUp() {
        orderItemDTO = new OrderItemDTO();
        orderItemDTO.setCartId(20L);
        orderItemDTO.setProductId(2L);
        orderItemDTO.setQuantity(2);

        shoppingCart = new ShoppingCart();
        shoppingCart.setIdCart(20L);

        product = new Product();
        product.setIdProd(2L);

        orderItem = new OrderItem();
        //orderItem.setId(1L);
        orderItem.setShoppingCart(shoppingCart);
        orderItem.setProduct(product);
        orderItem.setQuantity(2);
    }

    @Test
    public void testCreateOrderItem() {
        // Mocking the repository calls
        when(shoppingCartRepository.findById(20L)).thenReturn(Optional.ofNullable(shoppingCart));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

        // Calling the actual method
        OrderItemDTO result = orderItemService.createOrderItem(orderItemDTO);

        // Verifying the interactions and asserting the result
        verify(orderItemValidator).validateOrderItemCreation(orderItemDTO);
        verify(shoppingCartRepository).findById(20L);
        verify(productRepository).findById(2L);
        verify(orderItemRepository).save(any(OrderItem.class));
        verify(shoppingCartRepository).save(shoppingCart);

        assertNotNull(result);
        assertEquals(orderItem.getId(), result.getId());
        assertEquals(orderItem.getShoppingCart().getIdCart(), result.getCartId());
        assertEquals(orderItem.getProduct().getIdProd(), result.getProductId());
        assertEquals(orderItem.getQuantity(), result.getQuantity());
    }
}
