package com.example.demo.controllers;

import com.example.demo.dtos.ShoppingCartDTO;
import com.example.demo.services.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/shopping-carts")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping
    public ModelAndView getAllShoppingCarts() {
        ModelAndView mav=new ModelAndView("shopping-carts");
        List<ShoppingCartDTO> dtos = shoppingCartService.getAllShoppingCarts();
        mav.addObject("shoppingCarts",dtos);
        return mav;
    }
    @GetMapping("/customerCart")
    public ModelAndView getCartByIdForCustomer(@RequestParam("id") Long id) {
        return getShoppingCartById(id, "customerCart");
    }

    @GetMapping("/byId")
    public ModelAndView getCartById(@RequestParam("id") Long id) {
        return getShoppingCartById(id, "shopping-carts");
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/cartUpdateDetails")
    public ModelAndView getCartByIdForUpdate(@RequestParam("id") Long id) {
        return getShoppingCartById(id, "shopping-cartsUpdate");
    }
    public ModelAndView getShoppingCartById(@RequestParam("id") Long id,String viewName) {
        ModelAndView mav=new ModelAndView(viewName);
        try {
            ShoppingCartDTO shoppingCartDTO = shoppingCartService.getShoppingCartById(id);
            mav.addObject("shoppingCarts",shoppingCartDTO);
            return mav;
        } catch (Exception e) {
          mav.addObject("error","Cosul cu id ul respectiv nu exista");
          return mav;
        }
    }

    /**
     *
     * @return
     */
    @GetMapping("/insertShoppingCart")
    public ModelAndView showCreateShoppingCartForm() {
        return new ModelAndView("userCreate");
    }
    @PostMapping("/insertShoppingCart")
    public ModelAndView createShoppingCart(@Valid @ModelAttribute ShoppingCartDTO shoppingCartDTO) {
        ModelAndView mav=new ModelAndView("userCreate");
        ShoppingCartDTO createdShoppingCart = shoppingCartService.createShoppingCart(shoppingCartDTO);
        mav.addObject("products",createdShoppingCart);
        return mav;
    }

    /**
     *
     * @param id
     * @param shoppingCartDTO
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShoppingCartDTO> updateShoppingCart(@PathVariable Long id, @Valid @RequestBody ShoppingCartDTO shoppingCartDTO) {
        try {
            ShoppingCartDTO updatedShoppingCart = shoppingCartService.updateShoppingCart(id, shoppingCartDTO);
            return new ResponseEntity<>(updatedShoppingCart, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteShoppingCart(@PathVariable Long id) {
        try {
            shoppingCartService.deleteShoppingCart(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
        }
    }
}
