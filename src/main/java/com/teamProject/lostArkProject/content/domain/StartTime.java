package com.teamProject.lostArkProject.content.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

@Getter
@Setter
@Alias("startTime")
@Schema(description = "컨텐츠 시작시간 Entity")
public class StartTime {
    private int contentNumber;
    private LocalDateTime contentStartTime;
}
