package com.example.Testing.strategy;

import com.example.Testing.Entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class UserFileGenerator{
    private static final Logger logger = LoggerFactory.getLogger(UserFileGenerator.class);
    private FileGeneratorStrategy strategy;

    public void setFileGenerationStrategy(FileGeneratorStrategy strategy) {
        this.strategy = strategy;
    }

    public void generateUserFile(Optional<User> userOptional) {
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String formattedUser;
            if (strategy instanceof PdfGeneratorStrategy) {
                formattedUser = formatUserForPDF(user);
            } else if (strategy instanceof TxtGeneratorStrategy) {
                formattedUser = formatUserForTXT(user);
            } else if (strategy instanceof CsvGeneratorStrategy) {
                formattedUser = formatUserForCSV(user);
            } else {
                logger.error("Unsupported file generation strategy");
                return;
            }
            strategy.generateFile(formattedUser);
        } else {
            logger.error("User not found");
        }
    }

    private String formatUserForTXT(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append("User Information for TXT File:\n")
                .append("***************************************\n")
                .append("User ID: ").append(user.getId()).append("\n")
                .append("User Name: ").append(user.getFirstName()).append(" ").append(user.getLastName()).append("\n\n")
                .append("\n")
                .append("Account Details:\n\n")
                .append("Role: ").append(user.getRole()).append("\n")
                .append("Email: ").append(user.getEmail()).append("\n\n")
                .append("Password: ").append(user.getPassword()).append("\n")
                .append("\n\n");

        return sb.toString();
    }

    private String formatUserForCSV(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append("User Information for CSV File:\n")
                .append("***************************************\n")
                .append("User ID, ").append(user.getId()).append("\n")
                .append("User Name, ").append(user.getFirstName()).append(" ").append(user.getLastName()).append("\n\n")
                .append("\n")
                .append("Account Details:\n\n")
                .append("Role, ").append(user.getRole()).append("\n")
                .append("Email, ").append(user.getEmail()).append("\n\n")
                .append("Password, ").append(user.getPassword()).append("\n")
                .append("\n\n");

        return sb.toString();
    }

    private String formatUserForPDF(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append("User Information for PDF File:<br/><br/>")
                .append("***************************************<br/>")
                .append("User ID: ").append(user.getId()).append("<br/>")
                .append("User Name: ").append(user.getFirstName()).append(" ").append(user.getLastName()).append("<br/><br/>")
                .append("<br/>")
                .append("Account Details:<br/><br/>")
                .append("Role: ").append(user.getRole()).append("<br/>")
                .append("Email: ").append(user.getEmail()).append("<br/>")
                .append("Password: ").append(user.getPassword()).append("<br/>")
                .append("<br/>");


        return sb.toString();
    }

}
