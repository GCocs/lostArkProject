package com.teamProject.lostArkProject.content.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@Alias("rewardDTO")
@Schema(description = "컨텐츠 보상 Dto")
public class RewardDTO {

    @Schema(description = "보상 인덱스", example = "2")
    private int rewardId;

    @Schema(description = "컨텐츠 인덱스", example = "1")
    @NotBlank(message = "컨텐츠 인덱스는 필수 입력값입니다.")
    private int contentNumber;

    @Schema(description = "보상명", example = "몬테 섬")
    private String rewardItemName;

    @Schema(description = "보상 레벨", example = "1580")
    private int rewardItemLevel;

    @Schema(description = "보상 아이콘 경로", example = "")
    private String rewardItemIconLink;

    @Schema(description = "보상 등급", example = "영웅")
    private String rewardItemGrade;
}
