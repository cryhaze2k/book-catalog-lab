package ua.com.lab.web.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.com.lab.core.domain.Book;
import ua.com.lab.core.domain.Page;
import ua.com.lab.core.domain.PageRequest;
import ua.com.lab.core.services.BookService;
import ua.com.lab.web.e.GlobalExceptionHandler;

import java.io.IOException;

public class BookServlet extends HttpServlet {

    private BookService bookService;
    private ObjectMapper objectMapper;
    private GlobalExceptionHandler exceptionHandler;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.bookService = (BookService) config.getServletContext().getAttribute("bookService");
        this.objectMapper = (ObjectMapper) config.getServletContext().getAttribute("objectMapper");
        this.exceptionHandler = (GlobalExceptionHandler) config.getServletContext().getAttribute("exceptionHandler");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            String pathInfo = req.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                int page = req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : 0;
                int size = req.getParameter("size") != null ? Integer.parseInt(req.getParameter("size")) : 5;
                String query = req.getParameter("q");

                PageRequest pageRequest = new PageRequest(page, size, "id", PageRequest.SortDirection.ASC);

                Page<Book> bookPage = bookService.getBooks(query, pageRequest);
                objectMapper.writeValue(resp.getWriter(), bookPage);

            } else {
                long id = Long.parseLong(pathInfo.substring(1));
                Book book = bookService.getBookDetails(id);
                objectMapper.writeValue(resp.getWriter(), book);
            }

        } catch (Exception e) {
            exceptionHandler.handleException(e, req, resp);
        }
    }
}