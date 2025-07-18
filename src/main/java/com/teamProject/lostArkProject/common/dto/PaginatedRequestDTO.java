package com.teamProject.lostArkProject.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "페이지네이션 요청 Dto")
public class PaginatedRequestDTO {

    @Schema(description = "페이지", example = "1")
    private int page;

    @Schema(description = "크기", example = "5")
    private int size;

    public PaginatedRequestDTO(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public int getOffset() {
        return (page - 1) * size;
    }
}
