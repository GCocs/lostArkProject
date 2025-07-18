package com.teamProject.lostArkProject.content.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "서버시간 포함된 컨텐츠 Dto")
public class CalendarWithServerTimeDTO {

    @Schema(description = "카테고리명", example = "모험 섬")
    private String categoryName;

    @Schema(description = "컨텐츠명", example = "몬테 섬")
    private String contentsName;

    @Schema(description = "필터링된 컨텐츠명", example = "몬테_섬")
    private String sanitizedContentsName;

    @Schema(description = "컨텐츠 아이콘", example = "")
    private String contentsIcon;

    @Schema(description = "컨텐츠 최소레벨", example = "1580")
    private int minItemLevel;

    @Schema(description = "컨텐츠 시작시간", example = "1265275107687")
    private List<Long> startTimes;

    @Schema(description = "서버 시간", example = "1232255636163")
    private long serverTime;
}