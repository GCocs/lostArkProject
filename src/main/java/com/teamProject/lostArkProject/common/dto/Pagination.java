package com.teamProject.lostArkProject.common.dto;

import lombok.Getter;

@Getter
public class Pagination {
    private int currentPage;
    private int size;
    private int totalCount;
    private int totalPages;
    private int startPage;
    private int endPage;
    private boolean hasPreviousPage;
    private boolean hasNextPage;
    private boolean isFirstPage;
    private boolean isLastPage;

    public Pagination() {}

    public Pagination(int currentPage, int size, int totalCount) {
        this.currentPage = currentPage;
        this.size = size;
        this.totalCount = totalCount;
        this.totalPages = (int) Math.ceil((double) totalCount / size);
        this.startPage = (int) Math.floor((double) currentPage / size) * size + 1;
        this.endPage = Math.min(currentPage * size, totalPages);
        this.hasPreviousPage = currentPage > 1;
        this.hasNextPage = currentPage < totalPages;
        this.isFirstPage = currentPage == 1;
        this.isLastPage = currentPage == totalPages;
    }
}
