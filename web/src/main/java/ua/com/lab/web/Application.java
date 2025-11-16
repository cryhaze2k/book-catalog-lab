package ua.com.lab.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
// Кажемо Spring шукати біни (@Service, @Repository, @Configuration)
// в усіх наших модулях
@ComponentScan(basePackages = "ua.com.lab")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}