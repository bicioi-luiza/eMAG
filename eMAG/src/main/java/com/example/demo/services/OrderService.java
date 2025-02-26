package com.example.demo.services;

import com.example.demo.config.RabbitSender;
import com.example.demo.dtos.EmailRequestDto;
import com.example.demo.dtos.OrderDTO;
import com.example.demo.dtos.OrderItemDTO;
import com.example.demo.dtos.ProductDTO;
import com.example.demo.dtos.builders.OrderBuilder;
import com.example.demo.dtos.builders.UserBuilder;
import com.example.demo.entities.*;
import com.example.demo.entities.Order;
import com.example.demo.entities.Order;
import com.example.demo.fileGenerator.CsvFileGenerator;
import com.example.demo.fileGenerator.PdfFileGenerator;
import com.example.demo.fileGenerator.TextFileGenerator;
import com.example.demo.repositories.*;
import com.example.demo.validators.OrderValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final RabbitSender rabbitSender;
    private final OrderValidator orderValidator;
    private final TextFileGenerator textFileGenerator;
    private final PdfFileGenerator pdfFileGenerator;
    private final CsvFileGenerator csvFileGenerator;

    @Autowired
    public OrderService(OrderRepository orderRepository,ShoppingCartRepository shoppingCartRepository,UserRepository userRepository,ProductRepository productRepository,OrderItemRepository orderItemRepository,RabbitSender rabbitSender,OrderValidator orderValidator,TextFileGenerator textFileGenerator,PdfFileGenerator pdfFileGenerator,CsvFileGenerator csvFileGenerator) {
        this.orderRepository = orderRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.userRepository=userRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository=productRepository;
        this.rabbitSender=rabbitSender;
        this.orderValidator = orderValidator;
        this.pdfFileGenerator = pdfFileGenerator;
        this.csvFileGenerator = csvFileGenerator;
        this.textFileGenerator = textFileGenerator;
    }
    //@Autowired
    //private UserRepository userRepository;

    /**
     *Retrieves all orders.
     * @return List of OrderDTO objects representing all orders.
     */
    public List<OrderDTO> getAllOrders(){
        List<Order> personList = orderRepository.findAll();
        return personList.stream()
                .map(OrderBuilder::toOrderDTO)
                .collect(Collectors.toList());
    }

    /**
     *Retrieves an order by its ID.
     * @param id The ID of the order to retrieve.
     * @return The OrderDTO object representing the retrieved order.
     * @throws Exception if the order with the specified ID is not found.
     */
    public OrderDTO getOrderById(long id) throws Exception{
        Optional<Order> order = orderRepository.findById(id);
        if(!order.isPresent()) {
            LOGGER.error("Order with id {} was not found in db", id);

            throw new Exception(Order.class.getSimpleName() + " with id: " + id);
        }

        return OrderBuilder.toOrderDTO(order.get());
    }

    /**
     *
     * @param orderDTO The DTO representing the order to be created.
     * @return The created OrderDTO object.
     */
    public OrderDTO createOrder(OrderDTO orderDTO,String fileType)throws Exception{
        orderValidator.validateOrderCreation(orderDTO);
        Order order = OrderBuilder.toEntity(orderDTO);

        //Optional<User> user=userRepository.findById(orderDTO.getUsers_id());
        //order.setUser(user.get());
        order.setStatus("Pending");


       Optional<User> user=userRepository.findById(orderDTO.getUsers_id());
        order.setUser(user.get());
        ShoppingCart shoppingCart=user.get().getShoppingCart();
        List<OrderItem> orderItems = copyShoppingCartItems(shoppingCart);
        order=orderRepository.save(order);
        Product product;
        if (orderItems != null && !orderItems.isEmpty()) {
            for (OrderItem orderItem : orderItems) {
                product=orderItem.getProduct();
                product.setStock(product.getStock()-orderItem.getQuantity());
                productRepository.save(product);

                orderItem.setOrder(order);
                orderItemRepository.save(orderItem);

            }
        }
       // order.setOrderItems(orderItems);

       // double total=0;
     /*   List<OrderItemDTO> orderItems = orderDTO.getOrderItems();
        if (orderItems != null && !orderItems.isEmpty()) {
            for (OrderItemDTO orderItemDTO : orderItems) {
                // Set the order ID for each order item
                orderItemDTO.setOrderId(orderId);
                OrderItemDTO createdOrderItem = orderItemService.createOrderItem(orderItemDTO);
                // Set the ID of the created order item DTO
                orderItemDTO.setId(createdOrderItem.getId());
                ProductDTO product= null;
                try {
                    product = productService.getProductById(orderItemDTO.getProductId());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                total+=product.getPrice()* orderItemDTO.getQuantity();
            }
        }
        order.setTotal(total);*/


        LOGGER.debug("Order with id {} was inserted in db", order.getIdOrder());
        //email
        if(fileType.equals("txt"))
            textFileGenerator.generateFile(order);
        if(fileType.equals("pdf"))
            pdfFileGenerator.generateFile(order);
        if(fileType.equals("csv"))
            csvFileGenerator.generateFile(order);
        //email
        EmailRequestDto emailRequest = new EmailRequestDto();
        emailRequest.setId(user.get().getId());
        emailRequest.setFirstName(user.get().getFirstName());
        emailRequest.setLastName(user.get().getLastName());
        emailRequest.setRecipientEmail(user.get().getEmail()); // Assuming email is stored in UserDTO
        emailRequest.setSubject("Order Confirmation"); // Set your subject here

        StringBuilder bodyBuilder = new StringBuilder();
        bodyBuilder.append("<h1>Dear ").append(user.get().getFirstName()).append(",</h1><br>");
        bodyBuilder.append("<h1>Your order:</h1><br>");
        bodyBuilder.append("<p>Your order details:</p><br>");

// Iterate through each OrderItem and append its details to the email body
        for (OrderItem orderItem : orderItems) {
            bodyBuilder.append("<p>Product: ").append(orderItem.getProduct().getNameProd()).append("</p>");
            bodyBuilder.append("<p>Quantity: ").append(orderItem.getQuantity()).append("</p>");

            bodyBuilder.append("<br>");
        }
        bodyBuilder.append("<p>Total: ").append(shoppingCart.getTotal()).append("</p>");
        emailRequest.setBody(bodyBuilder.toString());
        emailRequest.setFileType(fileType);
        // Send the email request to the email service
        rabbitSender.send(emailRequest);
        return OrderBuilder.toOrderDTO(order);
    }

    /**
     *Updates an existing order.
     * @param id The ID of the order to be updated.
     * @param orderDTO The DTO representing the updated order data.
     * @return The updated OrderDTO object.
     * @throws Exception if the order with the specified ID is not found.
     */
    public OrderDTO updateOrder(long id, OrderDTO orderDTO) throws Exception{
        orderValidator.validateOrderUpdate(id, orderDTO);
        Optional<Order> optOrder = orderRepository.findById(id);
        if(!optOrder.isPresent()) {
            LOGGER.error("Order with id {} was not found in db", id);
            throw new Exception(Order.class.getSimpleName() + " with id: " + id);
        }

        Order existingOrder = optOrder.get();
        //LocalDateTime currentTimestamp = LocalDateTime.now();
       // existingOrder.setOrderDate(currentTimestamp);
        /*Optional<User> user=userRepository.findById(orderDTO.getUsers_id());
        existingOrder.setUser(user.get());*/
        existingOrder.setStatus(orderDTO.getStatus());


        Order updatedOrder = orderRepository.save(existingOrder);
        /*List<OrderItemDTO> orderItems = orderDTO.getOrderItems();
        if (orderItems != null && !orderItems.isEmpty()) {
            for (OrderItemDTO orderItemDTO : orderItems) {
                // Assuming order item ID is present in the DTO
                long orderItemId = orderItemDTO.getId();
                orderItemService.updateOrderItem(orderItemId, orderItemDTO);
            }
        }*/
        return OrderBuilder.toOrderDTO(updatedOrder);
    }

    /**
     *
     * @param id
     * @throws Exception
     */
    public void deleteOrder(long id)throws Exception{
        orderValidator.validateOrderExists(id);
        Optional<Order> optOrder = orderRepository.findById(id);


        List<OrderItem> orderItems =optOrder.get().getOrderItems();
        Product product;
        if (orderItems != null && !orderItems.isEmpty()) {
            for (OrderItem orderItem : orderItems) {
                product = orderItem.getProduct();
                product.setStock(product.getStock() + orderItem.getQuantity());
                productRepository.save(product);

            }
        }
        orderRepository.deleteById(id);
    }

    private List<OrderItem> copyShoppingCartItems(ShoppingCart shoppingCart) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItem cartItem : shoppingCart.getOrderItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            //orderItem.setShoppingCart(shoppingCart);
           
            orderItems.add(orderItem);
        }
        return orderItems;
    }
}
