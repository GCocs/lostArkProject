package com.teamProject.lostArkProject.content.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.util.List;

@Getter
@Setter
@Alias("contentDTO")
@Schema(description = "컨텐츠 Dto")
public class ContentDTO {

    @Schema(description = "컨텐츠 인덱스", example = "1")
    private int contentNumber;

    @Schema(description = "컨텐츠명", example = "몬테 섬")
    @NotBlank(message = "컨텐츠명은 필수 입력값입니다.")
    private String contentName;

    @Schema(description = "컨텐츠 아이콘 경로", example = "")
    @NotBlank(message = "컨텐츠명은 필수 입력값입니다.")
    private String contentIconLink;

    @Schema(description = "컨텐츠 최소레벨", example = "1580")
    private int minItemLevel;

    @Schema(description = "컨텐츠 장소", example = "")
    private String contentLocation;

    @Schema(description = "카테고리", example = "모험 섬")
    private String contentCategory;

    @Schema(description = "컨텐츠 시작시간 객체 배열", example = "")
    private List<StartTimeDTO> startTimes;

    @Schema(description = "컨텐츠 보상 객체 배열", example = "")
    private List<RewardDTO> rewards;
}
