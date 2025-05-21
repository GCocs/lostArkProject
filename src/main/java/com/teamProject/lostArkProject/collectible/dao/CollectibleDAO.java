package com.teamProject.lostArkProject.collectible.dao;

import com.teamProject.lostArkProject.collectible.domain.CollectiblePoint;
import com.teamProject.lostArkProject.collectible.domain.RecommendCollectible;
import com.teamProject.lostArkProject.collectible.dto.CollectiblePointSummaryDTO;
import com.teamProject.lostArkProject.collectible.dto.RecommendCollectibleDetailDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CollectibleDAO {
    void insertCollectiblePoint(CollectiblePoint collectiblePoint);
    void insertClearCollectible(String memberId, int clearCollectibleId);
    List<RecommendCollectible> getRecommendCollectible(String memberId);
    List<CollectiblePointSummaryDTO> getCollectiblePointSummary(String memberId);
    void deleteCollectible(String memberId);
}
