package com.example.demo.fileGenerator;

import com.example.demo.entities.Order;
import com.example.demo.fileGenerator.FileGenerator;
import com.itextpdf.text.DocumentException;

import java.io.IOException;

public class FileGeneratorContext {
    private FileGenerator fileGenerator;

    public FileGeneratorContext(FileGenerator fileGenerator) {
        this.fileGenerator = fileGenerator;
    }

    public void generateFile(Order order) throws IOException, DocumentException {
        fileGenerator.generateFile(order);
    }
}
