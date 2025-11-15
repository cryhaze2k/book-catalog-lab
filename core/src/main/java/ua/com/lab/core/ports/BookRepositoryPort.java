package ua.com.lab.core.ports;

import ua.com.lab.core.domain.Book;
import ua.com.lab.core.domain.Page;
import ua.com.lab.core.domain.PageRequest;
import java.util.Optional;

public interface BookRepositoryPort {
    Page<Book> findAll(String query, PageRequest pageRequest);
    Optional<Book> findById(long id);
}