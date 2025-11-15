package ua.com.lab.core.domain;

public record Book(
        long id,
        String title,
        String author,
        String description
) {}