package ua.com.lab.core.services;

import ua.com.lab.core.domain.Book;
import ua.com.lab.core.domain.Page;
import ua.com.lab.core.domain.PageRequest;
import ua.com.lab.core.e.NotFoundException;
import ua.com.lab.core.ports.BookRepositoryPort;

public class BookService {

    private final BookRepositoryPort bookRepository;

    public BookService(BookRepositoryPort bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Page<Book> getBooks(String query, PageRequest pageRequest) {
        return bookRepository.findAll(query, pageRequest);
    }

    public Book getBookDetails(long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id " + id + " not found"));
    }
}