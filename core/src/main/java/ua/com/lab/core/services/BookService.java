package ua.com.lab.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.lab.core.domain.Book;
import ua.com.lab.core.domain.Page;
import ua.com.lab.core.domain.PageRequest;
import ua.com.lab.core.e.NotFoundException;
import ua.com.lab.core.ports.BookRepositoryPort;

@Service // 1. Кажемо Spring, що це Бін типу Сервіс
public class BookService {

    // 2. Ін'єкція залежностей через поле (Field Injection)
    @Autowired
    private BookRepositoryPort bookRepository;

    // 3. Конструктор більше не потрібен

    public Page<Book> getBooks(String query, PageRequest pageRequest) {
        // Тут може бути валідація PageRequest
        return bookRepository.findAll(query, pageRequest);
    }

    public Book getBookDetails(long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id " + id + " not found"));
    }
}