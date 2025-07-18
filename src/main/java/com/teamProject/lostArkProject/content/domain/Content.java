package com.teamProject.lostArkProject.content.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.util.List;

@Getter
@Setter
@Alias("content")
@Schema(description = "컨텐츠 Entity")
public class Content {
    private int contentNumber;
    private String contentName;
    private String contentIconLink;
    private int minItemLevel;
    private String contentLocation;
    private String contentCategory;

    private List<StartTime> startTimes;
    private List<Reward> rewards;
}

