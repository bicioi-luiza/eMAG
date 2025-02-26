package com.example.demo.fileGenerator;


import com.example.demo.dtos.OrderDTO;
import com.example.demo.entities.Order;
import com.example.demo.entities.OrderItem;
import com.example.demo.entities.User;
import com.example.demo.fileGenerator.FileGenerator;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CsvFileGenerator implements FileGenerator {
    public String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }
    public String escapeSpecialCharacters(String data) {
        if (data == null) {
            throw new IllegalArgumentException("Input data cannot be null");
        }
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

    @Override
    public void generateFile(Order order) {
        File csvOutputFile = new File("Order.csv");
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            // Write header row
            pw.println("Product,Quantity,Price");

            // Write order items
            for (OrderItem orderItem : order.getUser().getShoppingCart().getOrderItems()) {
                String productName = escapeSpecialCharacters(orderItem.getProduct().getNameProd());
                int quantity = orderItem.getQuantity();
                double price = orderItem.getProduct().getPrice();

                // Quote fields containing special characters or leading/trailing spaces
                pw.println(String.format("\"%s\",%d,%.2f", productName, quantity, price));
            }

            // Write total price as a separate line
            double totalPrice = order.getUser().getShoppingCart().getTotal();
            pw.println(String.format("Total Price,%.2f", totalPrice));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
