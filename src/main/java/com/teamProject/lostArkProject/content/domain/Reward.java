package com.teamProject.lostArkProject.content.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@Alias("reward")
@Schema(description = "컨텐츠 보상 Entity")
public class Reward {
    private int rewardId;
    private int contentNumber;
    private String rewardItemName;
    private int rewardItemLevel;
    private String rewardItemIconLink;
    private String rewardItemGrade;

    // 테스트 코드를 간결하게 작성하기 위한 생성자
    public Reward(int contentNumber, String rewardItemName, int rewardItemLevel, String rewardItemIconLink, String rewardItemGrade) {
        this.contentNumber = contentNumber;
        this.rewardItemName = rewardItemName;
        this.rewardItemLevel = rewardItemLevel;
        this.rewardItemIconLink = rewardItemIconLink;
        this.rewardItemGrade = rewardItemGrade;
    }

    // 데이터 매핑 시 빈 객체를 생성하기 위한 기본 생성자
    public Reward() {

    }
}
