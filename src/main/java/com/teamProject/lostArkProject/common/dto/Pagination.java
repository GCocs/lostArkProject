package com.teamProject.lostArkProject.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@Schema(description = "페이지네이션 유틸리티 객체")
public class Pagination {

    @Schema(description = "현재 페이지", example = "2")
    @NotBlank(message = "현재 페이지는 필수 입력값입니다.")
    private int currentPage;

    @Schema(description = "크기", example = "5")
    @NotBlank(message = "크기는 필수 입력값입니다.")
    private int size;

    @Schema(description = "총 개수", example = "150")
    @NotBlank(message = "총 개수는 필수 입력값입니다.")
    private int totalCount;

    @Schema(description = "총 페이지수", example = "30")
    private int totalPages;

    @Schema(description = "시작 페이지", example = "1")
    private int startPage;

    @Schema(description = "마지막 페이지", example = "5")
    private int endPage;

    @Schema(description = "이전 페이지 존재여부", example = "false")
    private boolean hasPreviousPage;

    @Schema(description = "다음 페이지 존재여부", example = "true")
    private boolean hasNextPage;

    @Schema(description = "첫 페이지 여부", example = "false")
    private boolean isFirstPage;

    @Schema(description = "마지막 페이지 여부", example = "false")
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
