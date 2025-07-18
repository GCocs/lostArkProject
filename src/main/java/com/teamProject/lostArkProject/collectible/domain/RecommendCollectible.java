package com.teamProject.lostArkProject.collectible.domain;

import lombok.*;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("recommend_collectible")
public class RecommendCollectible {
    private String memberId;
    private int recommendCollectibleID;
    private String recommendCollectibleName;
    private String recommendCollectibleURL;
}
