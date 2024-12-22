package com.example.Testing.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;

public class CsvGeneratorStrategy implements FileGeneratorStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(CsvGeneratorStrategy.class);

    @Override
    public void generateFile(String data) {
        try (FileWriter writer = new FileWriter("Account.csv")) {
            String formattedData = formatData(data);
            writer.write(formattedData);
            LOGGER.info("CSV file has been generated successfully!");
        } catch (IOException e) {
            LOGGER.error("Error: CSV cannot be generated: {}", e.getMessage());
        }
    }
    private String formatData(String data) {
        return data.toUpperCase();
    }
}
