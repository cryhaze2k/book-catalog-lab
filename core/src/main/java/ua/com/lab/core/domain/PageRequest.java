package ua.com.lab.core.domain;

public record PageRequest(
        int page, // 0-based
        int size,
        String sortColumn,
        SortDirection sortDirection
) {
    public enum SortDirection { ASC, DESC }

    public int getOffset() {
        return page * size;
    }
}