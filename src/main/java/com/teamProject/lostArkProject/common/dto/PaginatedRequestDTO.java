package com.teamProject.lostArkProject.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PaginatedRequestDTO {
    private int page;
    private int size;

    public PaginatedRequestDTO(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public int getOffset() {
        return (page - 1) * size;
    }
}
