package ua.com.lab.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType; // Додай цей імпорт
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "ua.com.lab")
public class WebApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(WebApplication.class);

        app.setWebApplicationType(WebApplicationType.SERVLET);

        app.run(args);
    }
}