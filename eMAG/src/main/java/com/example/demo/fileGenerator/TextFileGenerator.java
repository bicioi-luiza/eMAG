package com.example.demo.fileGenerator;

import com.example.demo.dtos.OrderDTO;
import com.example.demo.entities.Order;
import com.example.demo.entities.OrderItem;
import com.example.demo.fileGenerator.FileGenerator;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class TextFileGenerator implements FileGenerator {
    @Override
    public void generateFile(Order order) throws IOException {
        File textOutputFile = new File("Order.txt");
        try (PrintWriter pw = new PrintWriter(textOutputFile)) {
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD); // Bigger font size for title
            Font regularFont = new Font(Font.FontFamily.HELVETICA, 12); // Smaller font size for regular text

            // Add title to the text file with bigger font size

            // Apply bigger font size to the title
            pw.println(String.format("%s", new Chunk("Order Details:", titleFont)));

            for (OrderItem orderItem : order.getUser().getShoppingCart().getOrderItems()) {
                String itemDetails = String.format("Product: %s, Quantity: %d, Price: %.2f",
                        orderItem.getProduct().getNameProd(), orderItem.getQuantity(), orderItem.getProduct().getPrice());
                // Apply smaller font size to regular text
                pw.println(String.format("%s", new Chunk(itemDetails, regularFont)));
            }

            // Apply smaller font size to regular text
            pw.println(String.format("%s", new Chunk("Total Price: " + order.getUser().getShoppingCart().getTotal(), regularFont)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
