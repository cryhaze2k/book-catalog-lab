package ua.com.lab.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.com.lab.core.domain.Comment;
import ua.com.lab.core.services.CommentService;

@RestController
public class CommentController {

    private final CommentService commentService;

    // Демонструємо ін'єкцію через конструктор (найкращий спосіб)
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // POST /comments
    @PostMapping("/comments")
    @ResponseStatus(HttpStatus.CREATED) // Відповідь 201 Created
    public Comment addComment(
            @RequestParam long bookId,
            @RequestParam String author,
            @RequestParam String text
    ) {
        // Spring автоматично парсить x-www-form-urlencoded (як у Лабі 2)
        return commentService.addComment(bookId, author, text);
    }

    // DELETE /comments/{id}
    @DeleteMapping("/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Відповідь 204 No Content
    public void deleteComment(@PathVariable long id) {
        commentService.deleteComment(id);
    }
}