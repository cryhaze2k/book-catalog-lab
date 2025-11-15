package ua.com.lab.core.domain;

import java.util.List;

public record Page<T>(
        List<T> content,
        int page,
        int size,
        long totalElements
) {
    public int getTotalPages() {
        if (size == 0) {
            return 0;
        }
        return (int) Math.ceil((double) totalElements / (double) size);
    }
}