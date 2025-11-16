package ua.com.lab.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.com.lab.core.ports.BookRepositoryPort;
import ua.com.lab.core.ports.CommentRepositoryPort;
import ua.com.lab.core.services.CommentService;

@Configuration // Кажемо Spring, що це клас конфігурації
public class CoreConfiguration {

    // Створюємо Бін CommentService вручну
    // Spring автоматично знайде біни 'commentRepository' та 'bookRepository'
    // і передасть їх сюди як параметри.
    @Bean
    public CommentService commentService(
            CommentRepositoryPort commentRepository,
            BookRepositoryPort bookRepository
    ) {
        // Тут ми демонструємо Ін'єкцію через конструктор
        return new CommentService(commentRepository, bookRepository);
    }
}