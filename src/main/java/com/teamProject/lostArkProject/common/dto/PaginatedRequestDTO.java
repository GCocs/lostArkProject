package com.teamProject.lostArkProject.common.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PaginatedRequestDTO {
    private final int page;
    private final int size;
    private final int offset;

    public PaginatedRequestDTO(int page, int size) {
        this.page = page;
        this.size = size;
        this.offset = (page - 1) * size;
    }
}
