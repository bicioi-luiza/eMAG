package com.example.demo.controllers;

import com.example.demo.dtos.ProductDTO;
import com.example.demo.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;


    //private ProductService productService;
    @Autowired
    public ProductController(ProductService productService) {
      this.productService = productService;
    }

    /**
     *Retrieves all products.
     * @return ResponseEntity containing a list of ProductDTOs and HTTP status OK
     */
    @GetMapping()
    public ModelAndView getAllProducts() {
        ModelAndView mav=new ModelAndView("products");
        List<ProductDTO> dtos = productService.getAllProducts();
        mav.addObject("products",dtos);
        return mav;
    }

    @GetMapping("/customer")
    public ModelAndView getAllProductsForCustomer() {
        ModelAndView mav=new ModelAndView("customer");
        List<ProductDTO> dtos = productService.getAllProducts();
        mav.addObject("products",dtos);
        return mav;
    }

    /**
     * Retrieves products by category.
     * @param category The category to filter products
     * @return ResponseEntity containing a list of ProductDTOs if successful, or HTTP status NOT_FOUND if no products found
     */
    @GetMapping("/byCategory")
    public ModelAndView getProductsByCategory(@RequestParam("category") String category,@RequestParam("sort") String sort) {
        ModelAndView mav=new ModelAndView("customer");
        try {
            List<ProductDTO> productsDTO = productService.getAllProductsByCategory(category,sort);
            mav.addObject("products",productsDTO);
            return mav;
        } catch (Exception e) {
            mav.addObject("error","Nu exista niciun produs in aceasta categorie");
            return mav;
        }
    }
    @GetMapping("/byId")
    public ModelAndView getUserById(@RequestParam("id") Long id) {
        return getProductById(id, "products");
    }

    @GetMapping("/productUpdateDetails")
    public ModelAndView getUserByIdForUpdate(@RequestParam("id") Long id) {
        return getProductById(id, "productUpdate");
    }
    /**
     *Retrieves a product by ID.
     * @param id The ID of the product to retrieve
     * @return ResponseEntity containing the ProductDTO if found, or HTTP status NOT_FOUND if not found
     */

    public ModelAndView getProductById(@RequestParam("id") Long id,String viewName) {
        ModelAndView mav=new ModelAndView(viewName);
        try {
            ProductDTO productDTO = productService.getProductById(id);
            mav.addObject("products",productDTO);
            return mav;
        } catch (Exception e) {
            mav.addObject("error","Produsul cu id ul respectiv nu exista");
            return mav;
        }
    }
    @GetMapping("/insertProduct")
    public ModelAndView showCreateProductForm() {
        return new ModelAndView("productCreate");
    }
    /**
     * Creates a new product.
     * @param productDTO productDTO The ProductDTO containing the product data to create
     * @return ResponseEntity containing the created ProductDTO and HTTP status CREATED
     */
    @PostMapping( "/insertProduct")
    public ModelAndView createProduct(@Valid  @ModelAttribute ProductDTO productDTO) {
        ModelAndView mav=new ModelAndView("productCreate");
        ProductDTO createdProduct = productService.createProduct(productDTO);
        mav.addObject("products",createdProduct);
        return mav;
    }
    @GetMapping("/updateProduct")
    public ModelAndView showUpdateProductForm() {
        return new ModelAndView("productUpdate");
    }
    /**
     *
     * @param id The ID of the product to update
     * @param productDTO he updated ProductDTO containing the new product data
     * @return containing the updated ProductDTO if successful, or HTTP status NOT_FOUND if product not found
     */
    @PostMapping("/updateProduct")
    public ModelAndView updateProduct(@RequestParam("id") Long id, @Valid @ModelAttribute ProductDTO productDTO) {
        ModelAndView mav=new ModelAndView("productUpdate");
        try {
            ProductDTO updatedProduct = productService.updateProduct(id,productDTO);
            mav.addObject("products",updatedProduct);
            return mav;
        } catch (Exception e) {
            mav.addObject("error","Produsul cu id-ul respectiv nu exista");
            return mav;
        }
    }
    @GetMapping("/deleteProduct")
    public ModelAndView showDeleteProductForm() {
        return new ModelAndView("productDelete");
    }
    /**
     *Deletes a product by ID.
     * @param id The ID of the product to delete
     * @return ResponseEntity with HTTP status OK if successful, or HTTP status NOT_FOUND if product not found
     */
    @PostMapping("/productDelete")
    public ModelAndView deleteProduct(@RequestParam("id") Long id) {
        ModelAndView mav=new ModelAndView("productDelete");
        try {
            productService.deleteProduct(id);
            return  mav.addObject("ok","Produsul cu id-ul respectiv s-a sters");
        } catch (Exception e) {
            return  mav.addObject("error","Produsul cu id-ul respectiv nu exista");
        }
    }

}
