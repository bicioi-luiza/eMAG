package com.example.demo.validators;

import com.example.demo.constants.SaleConstants;
import com.example.demo.dtos.SaleDTO;
import com.example.demo.entities.Product;
import com.example.demo.entities.Sale;
import com.example.demo.entities.User;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.repositories.SaleRepository;
import com.example.demo.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class SaleValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(SaleValidator.class);
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final SaleRepository saleRepository;

    public SaleValidator(UserRepository userRepository, ProductRepository productRepository, SaleRepository saleRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.saleRepository = saleRepository;
    }

    public void validateSaleCreation(SaleDTO saleDTO) {
        LOGGER.info(SaleConstants.VALIDATING_SALE_CREATION);

        if (saleDTO == null) {
            LOGGER.error(SaleConstants.SALE_CANNOT_BE_NULL);
            throw new IllegalArgumentException(SaleConstants.SALE_CANNOT_BE_NULL);
        }

        if (saleDTO.getUserId() == 0) {
            LOGGER.error(SaleConstants.USER_ID_CANNOT_BE_NULL);
            throw new IllegalArgumentException(SaleConstants.USER_ID_CANNOT_BE_NULL);
        }

        if (saleDTO.getProductId() == 0) {
            LOGGER.error(SaleConstants.PRODUCT_ID_CANNOT_BE_NULL);
            throw new IllegalArgumentException(SaleConstants.PRODUCT_ID_CANNOT_BE_NULL);
        }

        if (saleDTO.getPercent() < 0 || saleDTO.getPercent() > 100) {
            LOGGER.error(SaleConstants.PERCENT_INVALID);
            throw new IllegalArgumentException(SaleConstants.PERCENT_INVALID);
        }

        Optional<User> user = userRepository.findById(saleDTO.getUserId());
        if (!user.isPresent()) {
            LOGGER.error(SaleConstants.USER_NOT_FOUND_WITH_ID, saleDTO.getUserId());
            throw new IllegalArgumentException(SaleConstants.USER_NOT_FOUND_WITH_ID);
        }

        Optional<Product> product = productRepository.findById(saleDTO.getProductId());
        if (!product.isPresent()) {
            LOGGER.error(SaleConstants.PRODUCT_NOT_FOUND_WITH_ID, saleDTO.getProductId());
            throw new IllegalArgumentException(SaleConstants.PRODUCT_NOT_FOUND_WITH_ID);
        }

        LOGGER.info(SaleConstants.SALE_VALIDATION_SUCCESSFUL);
    }

    public void validateSaleExists(Long saleId) throws Exception {
        LOGGER.info(SaleConstants.VALIDATING_SALE_EXISTS, saleId);

        Optional<Sale> optionalSale = saleRepository.findById(saleId);
        if (!optionalSale.isPresent()) {
            LOGGER.error(SaleConstants.SALE_NOT_FOUND_WITH_ID, saleId);
            throw new Exception("Sale not found with id: " + saleId);
        }
    }

    public void validateSaleUpdate(SaleDTO saleDTO) {
        if (saleDTO.getPercent() < 0 || saleDTO.getPercent() > 100) {
            LOGGER.error(SaleConstants.PERCENT_INVALID);
            throw new IllegalArgumentException(SaleConstants.PERCENT_INVALID);
        }
    }
}