package com.example.demo.fileGenerator;


import com.example.demo.dtos.OrderDTO;
import com.example.demo.entities.Order;
import com.itextpdf.text.DocumentException;


import java.io.IOException;

public interface FileGenerator {
    void generateFile(Order order) throws IOException, DocumentException;
}
