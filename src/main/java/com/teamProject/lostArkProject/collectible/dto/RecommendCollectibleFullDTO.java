package com.teamProject.lostArkProject.collectible.domain;

import lombok.*;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("recommend_full_collectible")
public class RecommendCollectibleFullDTO {
    private int recommendCollectibleID;
    private String recommendCollectibleName;
    private String recommendCollectibleURL;
    private boolean isCleared;
}
