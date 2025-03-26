package com.teamProject.lostArkProject.common.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PaginatedResponseDTO<T> {
    private final List<T> data;
    private final Pagination pagination;

    public PaginatedResponseDTO(List<T> data, Pagination pagination) {
        this.data = data;
        this.pagination = pagination;
    }
}
