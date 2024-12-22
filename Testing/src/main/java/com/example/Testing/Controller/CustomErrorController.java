package com.example.Testing.Controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller class for handling custom error pages.
 */
@Controller
public class CustomErrorController implements ErrorController {

    private static final String PATH = "/error";

    /**
     * Handles errors and renders a custom error page.
     *
     * @return The name of the HTML template for the custom error page.
     */
    @RequestMapping(PATH)
    public String handleError() {
        // Handle error logic here, e.g., render a custom error page
        return "error"; // Render error.html template
    }

    /**
     * Retrieves the path for errors.
     *
     * @return The path for errors.
     */
    public String getErrorPath() {
        return PATH;
    }
}
