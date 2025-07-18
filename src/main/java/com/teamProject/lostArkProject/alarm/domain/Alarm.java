package com.teamProject.lostArkProject.alarm.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("alarm")
@Schema(description = "알림 Entity")
public class Alarm {
    @Schema(description = "사용자 아이디", example = "test1")
    @NotBlank(message = "아이디는 필수 입력값입니다.")
    private String memberId;

    @Schema(description = "컨텐츠 인덱스", example = "1")
    private int contentNumber;

    @Schema(description = "컨텐츠명", example = "그릇된 욕망의 섬")
    @NotBlank(message = "컨텐츠명은 필수 입력값입니다.")
    private String contentName;
}
