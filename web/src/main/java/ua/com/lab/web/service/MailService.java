package ua.com.lab.web.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ua.com.lab.core.domain.Book;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter; // Додано
import java.util.HashMap;
import java.util.Map;

@Service
public class MailService {

    private final JavaMailSender mailSender;
    private final EmailTemplateProcessor templateProcessor;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public MailService(JavaMailSender mailSender, EmailTemplateProcessor templateProcessor) {
        this.mailSender = mailSender;
        this.templateProcessor = templateProcessor;
    }

    public void sendNewBookEmail(Book book) {
        // Форматуємо дату в Java, щоб уникнути помилок у шаблоні
        String formattedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        Map<String, Object> model = new HashMap<>();
        model.put("title", book.getTitle());
        model.put("author", book.getAuthor());
        model.put("year", book.getYear());
        model.put("added", formattedDate); // Передаємо вже готовий String

        try {
            String htmlContent = templateProcessor.process("new_book.ftl", model);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(senderEmail);
            helper.setSubject("Нова книга: " + book.getTitle());
            helper.setText(htmlContent, true);
            helper.setFrom(senderEmail);

            mailSender.send(message);
            System.out.println("Лист успішно відправлено на: " + senderEmail);
        } catch (Exception e) {
            System.err.println("Помилка відправки листа: " + e.getMessage());
            e.printStackTrace(); // Додаємо для детального логування в Render
        }
    }
}