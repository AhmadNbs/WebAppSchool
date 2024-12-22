package com.example.Testing.strategy;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PdfGeneratorStrategy implements FileGeneratorStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(PdfGeneratorStrategy.class);

    @Override
    public void generateFile(String data) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            PDFont font = PDType1Font.COURIER_BOLD_OBLIQUE;
            float fontSize = 14;

            contentStream.setFont(font, fontSize);

            String[] lines = data.split("<br/>");
            float y = 700;

            for (String line : lines) {
                contentStream.beginText();
                contentStream.newLineAtOffset(100, y);
                contentStream.showText(line);
                contentStream.endText();
                y -= fontSize;
            }

            contentStream.close();

            document.save("Account.pdf");
            LOGGER.info("PDF file has been generated successfully!");
        } catch (IOException e) {
            LOGGER.error("Error: PDF cannot be generated: {}", e.getMessage());
        }
    }
}
