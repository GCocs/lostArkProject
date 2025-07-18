package com.teamProject.lostArkProject.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "페이지네이션 응답 Dto")
public class PaginatedResponseDTO<T> {

    @Schema(description = "데이터 리스트", example = "")
    private final List<T> data;

    @Schema(description = "페이지네이션 객체", example = "")
    private final Pagination pagination;

    public PaginatedResponseDTO(List<T> data, Pagination pagination) {
        this.data = data;
        this.pagination = pagination;
    }
}
