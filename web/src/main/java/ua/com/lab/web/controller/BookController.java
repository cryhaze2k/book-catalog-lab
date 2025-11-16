package ua.com.lab.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.com.lab.core.domain.Book;
import ua.com.lab.core.domain.Page;
import ua.com.lab.core.domain.PageRequest;
import ua.com.lab.core.services.BookService;

@RestController // Каже Spring, що це REST-контролер (відповідає JSON)
@RequestMapping("/books") // Всі методи тут будуть починатися з /books
public class BookController {

    @Autowired
    private BookService bookService; // Ін'єкція через поле (як просило завдання)

    // GET /books
    @GetMapping
    public Page<Book> getBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String q
    ) {
        // Spring автоматично парсить параметри запиту
        PageRequest pageRequest = new PageRequest(page, size, "id", PageRequest.SortDirection.ASC);
        return bookService.getBooks(q, pageRequest);
    }

    // GET /books/{id}
    @GetMapping("/{id}")
    public Book getBookDetails(@PathVariable long id) {
        // Spring автоматично парсить {id} з URL
        return bookService.getBookDetails(id);
    }
}