package ua.com.lab.core.ports;

import ua.com.lab.core.domain.Book;
import ua.com.lab.core.domain.Page;
import ua.com.lab.core.domain.PageRequest;

import java.util.Optional; // <--- Не забудь цей імпорт

public interface BookRepositoryPort {
    void save(Book book);

    Page<Book> findAll(String query, PageRequest pageRequest);

    // --- ДОДАЙ ЦЕЙ МЕТОД ---
    Optional<Book> findById(long id);
}