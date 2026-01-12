package ua.com.lab.web.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.Map;

@Component
public class EmailTemplateProcessor {

    private final Configuration configuration;

    // Важливо: інжектимо бін, який ми створили в EmailFreemarkerConfig
    public EmailTemplateProcessor(@Qualifier("freemarkerEmailConfig") Configuration configuration) {
        this.configuration = configuration;
    }

    public String process(String templateName, Map<String, Object> model) {
        try {
            Template template = configuration.getTemplate(templateName);
            StringWriter writer = new StringWriter();
            template.process(model, writer);
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException("Помилка обробки шаблону листа: " + templateName, e);
        }
    }
}