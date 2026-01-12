package ua.com.lab.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

@Configuration
public class EmailFreemarkerConfig {

    @Bean
    public FreeMarkerConfigurationFactoryBean freemarkerEmailConfig() {
        FreeMarkerConfigurationFactoryBean factoryBean = new FreeMarkerConfigurationFactoryBean();
        // Вказуємо папку в resources, де лежать .ftl файли
        factoryBean.setTemplateLoaderPath("classpath:/mail-templates/");
        factoryBean.setDefaultEncoding("UTF-8");
        return factoryBean;
    }
}