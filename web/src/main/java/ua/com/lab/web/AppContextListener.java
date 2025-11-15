package ua.com.lab.web;

// Імпорти з Jakarta EE
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

// Імпорти з наших модулів
import ua.com.lab.core.services.BookService;
import ua.com.lab.core.services.CommentService;
import ua.com.lab.persistence.H2Database;
import ua.com.lab.persistence.JdbcBookRepository;
import ua.com.lab.persistence.JdbcCommentRepository;

// Імпорти для ObjectMapper (JSON) та обробника помилок
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ua.com.lab.web.e.GlobalExceptionHandler;

// Цей клас - наш "Dependency Injector". Він запускається один раз при старті.
@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 1. Створюємо шар Persistence
        H2Database database = new H2Database();
        database.initialize(); // Створюємо таблиці та дані

        JdbcBookRepository bookRepository = new JdbcBookRepository(database);
        JdbcCommentRepository commentRepository = new JdbcCommentRepository(database);

        // 2. Створюємо шар Core (Сервіси) та ін'єктуємо в них репозиторії
        BookService bookService = new BookService(bookRepository);
        CommentService commentService = new CommentService(commentRepository, bookRepository);

        // 3. Створюємо утиліти для Web-шару
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Для дат (Instant)

        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler(objectMapper);

        // 4. Зберігаємо все у ServletContext, щоб сервлети мали до них доступ
        ServletContext context = sce.getServletContext();
        context.setAttribute("bookService", bookService);
        context.setAttribute("commentService", commentService);
        context.setAttribute("objectMapper", objectMapper);
        context.setAttribute("exceptionHandler", exceptionHandler);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // ... можна додати логіку закриття з'єднань з БД ...
    }
}