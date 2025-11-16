package ua.com.lab.core.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.lab.core.domain.Comment;
import ua.com.lab.core.e.DeleteNotAllowedException;
import ua.com.lab.core.e.NotFoundException;
import ua.com.lab.core.e.ValidationException;
import ua.com.lab.core.ports.BookRepositoryPort;
import ua.com.lab.core.ports.CommentRepositoryPort;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

// БЕЗ @Service!
public class CommentService {

    private static final Logger log = LoggerFactory.getLogger(CommentService.class);

    private final CommentRepositoryPort commentRepository;
    private final BookRepositoryPort bookRepository;

    // Ін'єкція через конструктор (Constructor Injection)
    public CommentService(CommentRepositoryPort commentRepository, BookRepositoryPort bookRepository) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
    }

    public List<Comment> getCommentsForBook(long bookId) {
        return commentRepository.findByBookId(bookId);
    }

    public Comment addComment(long bookId, String author, String text) {
        // Валідація
        if (author == null || author.isBlank() || author.length() > 64) {
            throw new ValidationException("Author is required and must be <= 64 chars");
        }
        if (text == null || text.isBlank() || text.length() > 1000) {
            throw new ValidationException("Text is required and must be <= 1000 chars");
        }

        // Перевірка, чи існує книга
        bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Cannot add comment to non-existent book " + bookId));

        Comment comment = new Comment(0, bookId, author, text, Instant.now());
        Comment savedComment = commentRepository.save(comment);

        log.info("New comment added! id={}, bookId={}, author='{}'",
                savedComment.id(), savedComment.bookId(), savedComment.author());

        return savedComment;
    }

    public void deleteComment(long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id " + commentId + " not found"));

        // *** БІЗНЕС-ПРАВИЛО: Видалення лише протягом 24 годин ***
        Instant now = Instant.now();
        Instant createdAt = comment.createdAt();

        if (createdAt.plus(24, ChronoUnit.HOURS).isBefore(now)) {
            log.warn("Delete attempt failed for comment id={}: 24-hour rule violation", commentId);
            throw new DeleteNotAllowedException("Comment can only be deleted within 24 hours of creation");
        }

        commentRepository.deleteById(commentId);
        log.info("Comment deleted. id={}", commentId);
    }
}