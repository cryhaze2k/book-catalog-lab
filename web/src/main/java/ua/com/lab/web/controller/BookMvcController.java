package ua.com.lab.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

// Імпорти твоїх класів
import ua.com.lab.core.domain.Book;
import ua.com.lab.core.domain.PageRequest;
import ua.com.lab.core.domain.PageRequest.SortDirection;
import ua.com.lab.core.ports.BookRepositoryPort;
// Імпорт сервісу розсилки
import ua.com.lab.web.service.MailService;

@Controller
public class BookMvcController {

    private final BookRepositoryPort bookRepository;
    private final MailService mailService; // <--- 1. Додали сервіс

    // 2. Оновили конструктор (Spring сам знайде і підставить MailService)
    public BookMvcController(BookRepositoryPort bookRepository, MailService mailService) {
        this.bookRepository = bookRepository;
        this.mailService = mailService;
    }

    // 1. Сторінка списку книг (GET /books)
    @GetMapping("/books")
    public String listBooks(Model model) {
        // Створюємо запит: сторінка 0, 100 елементів, сортування по id
        PageRequest pageRequest = new PageRequest(0, 100, "id", SortDirection.ASC);

        // Викликаємо метод репозиторію
        var page = bookRepository.findAll("", pageRequest);

        // Передаємо список книг у модель.
        model.addAttribute("books", page.content());

        return "books"; // Повертає шаблон books.html
    }

    // 2. Сторінка форми додавання (GET /books/new)
    @GetMapping("/books/new")
    public String showAddForm(Model model) {
        // Створюємо порожній об'єкт книги для форми
        model.addAttribute("book", new Book());
        return "book-form"; // Повертає шаблон book-form.html
    }

    // 3. Обробка збереження (POST /books)
    @PostMapping("/books")
    public String saveBook(@ModelAttribute("book") Book book) {
        // Зберігаємо книгу через порт
        bookRepository.save(book);

        // 3. Відправляємо email (Лабораторна 7)
        mailService.sendNewBookEmail(book);

        // Перенаправляємо користувача назад на список книг
        return "redirect:/books";
    }
}