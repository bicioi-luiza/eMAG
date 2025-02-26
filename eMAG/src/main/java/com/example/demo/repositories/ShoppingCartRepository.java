package com.example.demo.repositories;

import com.example.demo.entities.OrderItem;
import com.example.demo.entities.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {

}
