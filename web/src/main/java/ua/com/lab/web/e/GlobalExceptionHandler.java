package ua.com.lab.web.e;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.lab.core.e.DeleteNotAllowedException;
import ua.com.lab.core.e.NotFoundException;
import ua.com.lab.core.e.ValidationException;
import ua.com.lab.web.dto.ErrorResponse;

import java.io.IOException;
import java.time.Instant;

public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void handleException(Exception e, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        int status;
        String error;

        if (e instanceof ValidationException) {
            status = HttpServletResponse.SC_BAD_REQUEST; // 400
            error = "Bad Request";
            log.warn("Validation error: {} (Path: {})", e.getMessage(), req.getRequestURI()); // Лог 4xx - WARN
        } else if (e instanceof NotFoundException) {
            status = HttpServletResponse.SC_NOT_FOUND; // 404
            error = "Not Found";
            log.warn("Resource not found: {} (Path: {})", e.getMessage(), req.getRequestURI()); // Лог 4xx - WARN
        } else if (e instanceof DeleteNotAllowedException) {
            status = HttpServletResponse.SC_CONFLICT; // 409
            error = "Conflict";
            log.warn("Business rule conflict: {} (Path: {})", e.getMessage(), req.getRequestURI()); // Лог 4xx - WARN
        } else {
            status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR; // 500
            error = "Internal Server Error";
            log.error("Unhandled exception: " + e.getMessage(), e); // Лог 5xx - ERROR
        }

        resp.setStatus(status);
        ErrorResponse errorResponse = new ErrorResponse(
                Instant.now(),
                status,
                error,
                e.getMessage(),
                req.getRequestURI()
        );
        objectMapper.writeValue(resp.getWriter(), errorResponse);
    }
}