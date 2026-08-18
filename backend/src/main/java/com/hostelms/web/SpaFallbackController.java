package com.hostelms.web;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The React frontend (built into src/main/resources/static) handles its
 * own routing in the browser via React Router. If someone directly types
 * a URL like "/students" or refreshes that page, Spring Boot would
 * normally show an error since no such server-side route exists. This
 * controller forwards that error to index.html instead, so React Router
 * gets a chance to render the right page from the URL.
 */
@Controller
public class SpaFallbackController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        return "forward:/index.html";
    }
}
