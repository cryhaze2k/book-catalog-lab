package ua.com.lab.persistence;

import org.springframework.stereotype.Repository;
import ua.com.lab.core.domain.Book;
import ua.com.lab.core.domain.Page;
import ua.com.lab.core.domain.PageRequest;
import ua.com.lab.core.ports.BookRepositoryPort;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository // 1. Кажемо Spring, що це Бін типу Репозиторій
public class JdbcBookRepository implements BookRepositoryPort {

    private final DataSource dataSource; // 2. Замінюємо H2Database на DataSource

    // 3. Ін'єкція DataSource через конструктор
    public JdbcBookRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // 4. Допоміжний метод для отримання Connection
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public Page<Book> findAll(String query, PageRequest pageRequest) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT id, title, author, description FROM books " +
                "LIMIT ? OFFSET ?";

        String countSql = "SELECT COUNT(*) FROM books";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             PreparedStatement countStmt = conn.prepareStatement(countSql)) {

            stmt.setInt(1, pageRequest.size());
            stmt.setInt(2, pageRequest.getOffset());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(mapRowToBook(rs));
                }
            }

            long totalElements = 0;
            try (ResultSet rs = countStmt.executeQuery()) {
                if (rs.next()) {
                    totalElements = rs.getLong(1);
                }
            }

            return new Page<>(books, pageRequest.page(), pageRequest.size(), totalElements);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch books", e);
        }
    }

    @Override
    public Optional<Book> findById(long id) {
        String sql = "SELECT id, title, author, description FROM books WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToBook(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch book by id", e);
        }
        return Optional.empty();
    }

    private Book mapRowToBook(ResultSet rs) throws SQLException {
        return new Book(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("description")
        );
    }
}