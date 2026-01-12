package ua.com.lab.persistence;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.com.lab.core.domain.Book;
import ua.com.lab.core.domain.Page;
import ua.com.lab.core.domain.PageRequest;
import ua.com.lab.core.ports.BookRepositoryPort;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcBookRepository implements BookRepositoryPort {

    private final JdbcTemplate jdbcTemplate;

    public JdbcBookRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Book book) {
        if (book.getId() == 0) {
            // ВИПРАВЛЕНО: назва колонки змінена на publication_year
            String sql = "INSERT INTO books (title, author, publication_year, description) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sql, book.getTitle(), book.getAuthor(), book.getYear(), book.getDescription());
        } else {
            // ВИПРАВЛЕНО: назва колонки змінена на publication_year
            String sql = "UPDATE books SET title = ?, author = ?, publication_year = ?, description = ? WHERE id = ?";
            jdbcTemplate.update(sql, book.getTitle(), book.getAuthor(), book.getYear(), book.getDescription(), book.getId());
        }
    }

    @Override
    public Page<Book> findAll(String query, PageRequest pageRequest) {
        String sql = "SELECT * FROM books";
        List<Book> books = jdbcTemplate.query(sql, new BookRowMapper());
        return new Page<>(books, pageRequest.page(), pageRequest.size(), books.size());
    }

    @Override
    public Optional<Book> findById(long id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        List<Book> result = jdbcTemplate.query(sql, new BookRowMapper(), id);
        return result.stream().findFirst();
    }

    private static class BookRowMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Book(
                    rs.getLong("id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getInt("publication_year"), // Тут уже було правильно
                    rs.getString("description")
            );
        }
    }
}