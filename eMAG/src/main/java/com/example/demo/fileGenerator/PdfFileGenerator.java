package com.example.demo.fileGenerator;


import com.example.demo.entities.Order;
import com.example.demo.entities.OrderItem;
import com.example.demo.fileGenerator.FileGenerator;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class PdfFileGenerator implements FileGenerator {
    @Override
    public void generateFile(Order order) throws IOException, DocumentException {
        Document document = new Document();
        try {
            Font titleFont = FontFactory.getFont("path_to_title_font", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 24); // Bigger font size for title
            Font regularFont = FontFactory.getFont("path_to_regular_font", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12); // Smaller font size for regular text


            PdfWriter.getInstance(document, new FileOutputStream("Order.pdf"));
            document.open();
            document.add(new Paragraph("Order Details:", titleFont));
            // Add title to the document with bigger font size
            Paragraph title = new Paragraph("Order Details:", titleFont);
            document.add(title);

            for (OrderItem orderItem : order.getUser().getShoppingCart().getOrderItems()) {
                String itemDetails = String.format("Product: %s, Quantity: %d, Price: %.2f",
                        orderItem.getProduct().getNameProd(), orderItem.getQuantity(), orderItem.getProduct().getPrice());
                Paragraph itemParagraph = new Paragraph(itemDetails, regularFont); // Apply smaller font size to regular text
                document.add(itemParagraph);
            }

            double totalPrice = order.getUser().getShoppingCart().getTotal();
            Paragraph totalPriceParagraph = new Paragraph(String.format("Total Price: %.2f", totalPrice), regularFont); // Apply smaller font size to regular text
            document.add(totalPriceParagraph);

        } catch (DocumentException | IOException e) {
            e.printStackTrace(); // Handle exceptions properly
        } finally {
            if (document != null) {
                document.close();
            }

        }
    }


}
