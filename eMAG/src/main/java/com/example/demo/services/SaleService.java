package com.example.demo.services;

import com.example.demo.dtos.SaleDTO;
import com.example.demo.dtos.builders.SaleBuilder;
import com.example.demo.dtos.builders.UserBuilder;
import com.example.demo.entities.Product;
import com.example.demo.entities.Sale;
import com.example.demo.entities.User;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.repositories.SaleRepository;
import com.example.demo.validators.SaleValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class represents a service layer for sale-related operations
 */
@Service
public class SaleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SaleService.class);
    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final SaleValidator saleValidator;


    @Autowired
    public SaleService(SaleRepository saleRepository, ProductRepository productRepository,SaleValidator saleValidator) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
        this.saleValidator = saleValidator;
    }

    public List<SaleDTO> getAllSales() {
        List<Sale> saleList = saleRepository.findAll();
        return saleList.stream()
                .map(SaleBuilder::toSaleDTO)
                .collect(Collectors.toList());
    }

    public SaleDTO getSaleById(Long id) throws Exception {
        saleValidator.validateSaleExists(id);
        Optional<Sale> sale = saleRepository.findById(id);
        return SaleBuilder.toSaleDTO(sale.get());
    }

    /**
     *
     * @param saleDTO The DTO representing the sale to be created.
     * @return The createdSaleDTO
     * @throws Exception
     */
    public SaleDTO createSale(SaleDTO saleDTO) throws Exception {
        saleValidator.validateSaleCreation(saleDTO);
        Optional<Product> productOptional = productRepository.findById(saleDTO.getProductId());

        Product product = productOptional.get();

        double oldPrice = product.getPrice();

        double percent = saleDTO.getPercent();

        double newPrice = oldPrice - (oldPrice * percent / 100);

        Sale sale = SaleBuilder.toEntity(saleDTO);
        sale.setOldPrice(oldPrice);
        sale.setNewPrice(newPrice);
        sale = saleRepository.save(sale);
        product.setPrice(newPrice);
        productRepository.save(product);
        LOGGER.debug("Sale with id {} was inserted in db", sale.getIdSale());
        return SaleBuilder.toSaleDTO(sale);
    }

    /**
     *
     * @param id
     * @param saleDTO The DTO representing the sale to be updated.
     * @return
     * @throws Exception
     */
    public SaleDTO updateSale(Long id, SaleDTO saleDTO) throws Exception {
        saleValidator.validateSaleExists(id);
        saleValidator.validateSaleUpdate(saleDTO);
        Optional<Sale> optSale = saleRepository.findById(id);


        Sale existingSale = optSale.get();

        // Update other fields of the sale if needed
        existingSale.setPercent(saleDTO.getPercent());

        // Calculate newPrice based on the updated percent
        double oldPrice = existingSale.getOldPrice();
        double percent = saleDTO.getPercent();
        double newPrice = oldPrice - (oldPrice * percent / 100);
        existingSale.setOldPrice(oldPrice);
        existingSale.setNewPrice(newPrice);


        Product product =optSale.get().getProduct();
        product.setPrice(newPrice);
        Sale updatedSale = saleRepository.save(existingSale);
        productRepository.save(product);
        return SaleBuilder.toSaleDTO(updatedSale);
    }

    /**
     *
     * @param id
     * @throws Exception
     */
    public void deleteSale(Long id) throws Exception {
        saleValidator.validateSaleExists(id);
        Optional<Sale> optSale = saleRepository.findById(id);

        Optional<Product> productOptional = Optional.ofNullable(optSale.get().getProduct());

        Product product = productOptional.get();
        product.setPrice(optSale.get().getOldPrice());
        productRepository.save(product);
        saleRepository.deleteById(id);
    }
}
