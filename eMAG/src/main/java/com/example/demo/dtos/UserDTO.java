package com.example.demo.dtos;

import com.example.demo.entities.Order;
import com.example.demo.entities.Review;
import com.example.demo.entities.Sale;
import com.example.demo.entities.ShoppingCart;
import io.micrometer.common.lang.Nullable;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class UserDTO {

    private Long id;
    private String lastName;
    private String firstName;
    private String username;
    private String password;
    private String email;
    private String address;
    private LocalDate dateOfBirth;
    private boolean isAdmin;
    private List<Order> orders=new ArrayList<>();
    private List<Review> reviews;
    private List<Sale> sales;
    private ShoppingCart shoppingcart;
}
