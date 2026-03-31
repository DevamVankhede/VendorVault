package com.orion.vendorvault.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class PaginationUtil {

    public static PageRequest buildPageRequest(int page, int size, String sortBy, String sortDir) {
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return PageRequest.of(Math.max(0, page - 1), size, Sort.by(direction, sortBy));
    }

    public static List<Integer> getPageNumbers(Page<?> page) {
        int totalPages = page.getTotalPages();
        List<Integer> pageNumbers = new ArrayList<>();
        if (totalPages > 0) {
            for (int i = 1; i <= totalPages; i++) {
                pageNumbers.add(i);
            }
        }
        return pageNumbers;
    }
}
