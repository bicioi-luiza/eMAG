package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micrometer.common.lang.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name="shopping_cart")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_cart")
    private Long idCart;

    @OneToOne
    @JoinColumn(name="user_id")
    @Nullable
    private User user;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();
    private double total;



}
