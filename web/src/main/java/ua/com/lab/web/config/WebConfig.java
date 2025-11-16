package ua.com.lab.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc; // Ключовий імпорт
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc // 1. Активує анотаційний режим MVC
public class WebConfig implements WebMvcConfigurer {

}