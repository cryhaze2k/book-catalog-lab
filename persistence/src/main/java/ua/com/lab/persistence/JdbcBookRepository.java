package ua.com.lab.persistence;

import ua.com.lab.core.domain.Book;
import ua.com.lab.core.domain.Page;
import ua.com.lab.core.domain.PageRequest;
import ua.com.lab.core.ports.BookRepositoryPort;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcBookRepository implements BookRepositoryPort {

    private final H2Database db;

    public JdbcBookRepository(H2Database db) {
        this.db = db;
    }

    @Override
    public Page<Book> findAll(String query, PageRequest pageRequest) {

        List<Book> books = new ArrayList<>();
        String sql = "SELECT id, title, author, description FROM books " +
                "LIMIT ? OFFSET ?";

        String countSql = "SELECT COUNT(*) FROM books";

        try (Connection conn = db.getConnection();
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
        try (Connection conn = db.getConnection();
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