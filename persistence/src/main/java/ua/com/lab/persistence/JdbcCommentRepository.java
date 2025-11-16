package ua.com.lab.persistence;

import org.springframework.stereotype.Repository;
import ua.com.lab.core.domain.Comment;
import ua.com.lab.core.ports.CommentRepositoryPort;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository // 1. Кажемо Spring, що це Бін
public class JdbcCommentRepository implements CommentRepositoryPort {

    private final DataSource dataSource; // 2. Ін'єктуємо DataSource

    public JdbcCommentRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public List<Comment> findByBookId(long bookId) {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT id, book_id, author, text, created_at " +
                "FROM comments WHERE book_id = ? ORDER BY created_at DESC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    comments.add(mapRowToComment(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch comments for book " + bookId, e);
        }
        return comments;
    }

    @Override
    public Comment save(Comment comment) {
        String sql = "INSERT INTO comments (book_id, author, text) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, comment.bookId());
            stmt.setString(2, comment.author());
            stmt.setString(3, comment.text());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    long id = keys.getLong(1);
                    return findById(id).orElseThrow();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save comment", e);
        }
        throw new RuntimeException("Failed to save comment, no ID obtained");
    }

    @Override
    public Optional<Comment> findById(long commentId) {
        String sql = "SELECT id, book_id, author, text, created_at FROM comments WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, commentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToComment(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch comment by id " + commentId, e);
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(long commentId) {
        String sql = "DELETE FROM comments WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, commentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete comment " + commentId, e);
        }
    }

    private Comment mapRowToComment(ResultSet rs) throws SQLException {
        return new Comment(
                rs.getLong("id"),
                rs.getLong("book_id"),
                rs.getString("author"),
                rs.getString("text"),
                rs.getTimestamp("created_at").toInstant()
        );
    }
}