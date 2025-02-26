package com.example.demo.services;

import com.example.demo.dtos.ProductDTO;
import com.example.demo.dtos.builders.ProductBuilder;
import com.example.demo.dtos.builders.UserBuilder;
import com.example.demo.entities.Product;
import com.example.demo.entities.User;
import com.example.demo.repositories.ProductRepository;

import com.example.demo.validators.ProductValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
/**
 * This class represents a service layer for product-related operations.
 * It provides methods for retrieving, creating, updating, and deleting product entities.
 */
@Service
public class ProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;
    private final ProductValidator productValidator;
    @Autowired
    public ProductService(ProductRepository productRepository,ProductValidator productValidator) {
      this.productRepository = productRepository;
        this.productValidator = productValidator;
    }


    /**
     *Retrieves a list of all products
     * @return the list with all the products
     */
    public List<ProductDTO> getAllProducts(){
        List<Product> personList = productRepository.findAll();
        return personList.stream()
                .map(ProductBuilder::toProductDTO)
                .collect(Collectors.toList());
    }

    /**
     *Retrieves a product by its ID.
     * @param id the id of the product we want to find
     * @return the product we found
     * @throws Exception if the product was not found
     */
    public ProductDTO getProductById(long id) throws Exception{
        Optional<Product> product = productRepository.findById(id);
        if(!product.isPresent()) {
            LOGGER.error("Product with id {} was not found in db", id);

            throw new Exception(Product.class.getSimpleName() + " with id: " + id);
        }

        return ProductBuilder.toProductDTO(product.get());
    }

    /**
     * Retrieves all products belonging to a specific category.
     * @param category The category of products to retrieve.
     * @return A list of {@link ProductDTO} objects representing the products.
     * @throws Exception If no products are found for the given category.
     */
    public List<ProductDTO> getAllProductsByCategory(String category,String sort) throws Exception {
        List<Product> productList = productRepository.findByCategory(category);
        if(productList.isEmpty()) {
            LOGGER.error("No products found for category {}", category);

            throw new Exception("No products found for category: " + category);
        }
        // Sort the products based on the sort parameter
        if ("ASC".equalsIgnoreCase(sort)) {
            productList.sort(Comparator.comparing(Product::getPrice));
        } else if ("DESC".equalsIgnoreCase(sort)) {
            productList.sort(Comparator.comparing(Product::getPrice).reversed());
        }
        return productList.stream()
                .map(ProductBuilder::toProductDTO)
                .collect(Collectors.toList());
    }

    /**
     *Creates a new product.
     * @param productDTO The DTO representing the product to be created.
     * @return The DTO representing the created product.
     */
    public ProductDTO createProduct(ProductDTO productDTO){
        productValidator.validateProduct(productDTO);
        Product product = ProductBuilder.toEntity(productDTO);
        product = productRepository.save(product);
        LOGGER.debug("Product with id {} was inserted in db", product.getIdProd());
        return ProductBuilder.toProductDTO(product);
    }

    /**
     *Updates an existing product with the given ID.
     * @param id The ID of the product to update.
     * @param productDTO The DTO representing the updated product data.
     * @return The DTO representing the updated product.
     * @throws Exception Exception If the product with the given ID is not found.
     */
    public ProductDTO updateProduct(long id, ProductDTO productDTO) throws Exception{
        productValidator.validateProduct(productDTO);
        productValidator.validateProductId(id);
        Optional<Product> optProduct = productRepository.findById(id);

        Product existingProduct = optProduct.get();

        existingProduct.setNameProd(productDTO.getNameProd());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setCategory(productDTO.getCategory());
        existingProduct.setStock(productDTO.getStock());


        Product updatedProduct = productRepository.save(existingProduct);

        return ProductBuilder.toProductDTO(updatedProduct);
    }

    /**
     *Deletes a product with the specified ID.
     * @param id The ID of the product to delete.
     * @throws Exception If the product with the given ID is not found.
     */
    public void deleteProduct(long id) throws Exception{
        productValidator.validateProductId(id);
        Optional<Product> optProduct = productRepository.findById(id);
        productRepository.deleteById(id);
    }

}
