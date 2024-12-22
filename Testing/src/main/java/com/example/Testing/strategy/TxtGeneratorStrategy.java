package com.example.Testing.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class TxtGeneratorStrategy implements FileGeneratorStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(TxtGeneratorStrategy.class);

    @Override
    public void generateFile(String data) {
        PrintWriter writer = null;
        try {
            String fileName = "Account.txt";
            writer = new PrintWriter(new FileWriter(fileName));

            String formattedData = formatData(data);
            writer.println(formattedData);

            LOGGER.info("Text file has been generated successfully! {}", fileName);
        } catch (IOException e) {
            LOGGER.error("Error: Text file cannot be generated: {}", e.getMessage());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
    private String formatData(String data) {
        return data.toUpperCase();
    }
}
