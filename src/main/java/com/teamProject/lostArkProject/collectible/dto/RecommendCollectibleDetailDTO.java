package com.teamProject.lostArkProject.collectible.dto;

import lombok.*;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("recommend_collectible_detail")
public class RecommendCollectibleDetailDTO {
    private String recommendCollectibleName;
    private String recommendCollectibleURL;
}