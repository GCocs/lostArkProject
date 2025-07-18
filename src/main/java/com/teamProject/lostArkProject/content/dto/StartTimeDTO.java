package com.teamProject.lostArkProject.content.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

@Getter
@Setter
@Alias("startTimeDTO")
@Schema(description = "컨텐츠 시작시간 Dto")
public class StartTimeDTO {

    @Schema(description = "컨텐츠 인덱스", example = "1")
    @NotBlank(message = "컨텐츠 인덱스는 필수 입력값입니다.")
    private int contentNumber;

    @Schema(description = "컨텐츠 시작시간", example = "2025-07-16T14:00:00.00")
    private LocalDateTime contentStartTime;
}
