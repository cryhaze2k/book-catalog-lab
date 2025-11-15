package ua.com.lab.web.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.com.lab.core.domain.Comment;
import ua.com.lab.core.services.CommentService;
import ua.com.lab.web.e.GlobalExceptionHandler;

import java.io.IOException;

public class CommentServlet extends HttpServlet {

    private CommentService commentService;
    private ObjectMapper objectMapper;
    private GlobalExceptionHandler exceptionHandler;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.commentService = (CommentService) config.getServletContext().getAttribute("commentService");
        this.objectMapper = (ObjectMapper) config.getServletContext().getAttribute("objectMapper");
        this.exceptionHandler = (GlobalExceptionHandler) config.getServletContext().getAttribute("exceptionHandler");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String pathInfo = req.getPathInfo(); // /1/comments
            String[] parts = pathInfo.split("/");
            long bookId = Long.parseLong(parts[1]);

            String author = req.getParameter("author");
            String text = req.getParameter("text");

            Comment newComment = commentService.addComment(bookId, author, text);

            resp.setStatus(HttpServletResponse.SC_CREATED); // 201
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(resp.getWriter(), newComment);

        } catch (Exception e) {
            exceptionHandler.handleException(e, req, resp);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String pathInfo = req.getPathInfo(); // /1
            long commentId = Long.parseLong(pathInfo.substring(1));

            commentService.deleteComment(commentId);

            resp.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204

        } catch (Exception e) {
            exceptionHandler.handleException(e, req, resp);
        }
    }
}