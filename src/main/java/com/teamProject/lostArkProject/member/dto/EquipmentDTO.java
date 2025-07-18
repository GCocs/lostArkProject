package com.teamProject.lostArkProject.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "장비 Dto")
public class EquipmentDTO {

    @Schema(description = "타입", example = "무기")
    private String type;

    @Schema(description = "장비명", example = "+14 운명의 결단 데스사이드")
    private String name;

    @Schema(description = "아이콘 경로", example = "")
    private String icon;

    @Schema(description = "등급", example = "영웅")
    private String grade;

    @Schema(description = "해제 필요 여부", example = "true")
    private boolean unequippedRequired;
}
