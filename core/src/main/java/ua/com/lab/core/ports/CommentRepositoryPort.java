package ua.com.lab.core.ports;

import ua.com.lab.core.domain.Comment;
import java.util.List;
import java.util.Optional;

public interface CommentRepositoryPort {
    List<Comment> findByBookId(long bookId);
    Comment save(Comment comment);
    Optional<Comment> findById(long commentId);
    void deleteById(long commentId);
}