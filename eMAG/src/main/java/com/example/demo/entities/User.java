package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micrometer.common.lang.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String lastName;
    @Column
    private String firstName;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private String email;
    @Column
    private String address;
    @Column
    private LocalDate dateOfBirth;
    @Column
    private boolean isAdmin;
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Sale> sales;
    @JsonIgnore
    @OneToOne(mappedBy="user",cascade= CascadeType.ALL)
    private ShoppingCart shoppingCart;

}
