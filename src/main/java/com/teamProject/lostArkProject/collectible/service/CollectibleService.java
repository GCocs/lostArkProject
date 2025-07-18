package com.teamProject.lostArkProject.collectible.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamProject.lostArkProject.collectible.dao.CollectibleDAO;
import com.teamProject.lostArkProject.collectible.domain.CollectiblePoint;
import com.teamProject.lostArkProject.collectible.domain.RecommendCollectible;
import com.teamProject.lostArkProject.collectible.dto.CollectiblePointDTO;
import com.teamProject.lostArkProject.collectible.dto.CollectiblePointSummaryDTO;
import com.teamProject.lostArkProject.collectible.dto.RecommendCollectibleDetailDTO;
import com.teamProject.lostArkProject.collectible.dto.RecommendCollectibleFullDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
public class CollectibleService {
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final CollectibleDAO collectibleDAO;
    private volatile boolean running = false; // 플래그 변수

    public CollectibleService(WebClient webClient, ObjectMapper objectMapper, CollectibleDAO collectibleDAO) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
        this.collectibleDAO = collectibleDAO;
    }

    // 내실 내용 가져오는 메소드
    public void saveCollectiblePoint(String characterName, String memberId) {
        webClient.get()
                .uri("/armories/characters/" + characterName + "/collectibles") // 실제 API의 경로로 변경
                .retrieve()
                .bodyToMono(String.class) // CollectibleItem으로 매핑
                .flatMap(response -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        // API 응답을 DTO 리스트로 변환
                        List<CollectiblePointDTO> allItems = objectMapper.readValue(response, new TypeReference<List<CollectiblePointDTO>>() {
                        });

                        // DTO 리스트를 도메인 리스트로 변환
                        List<CollectiblePoint> filterItem = allItems.stream()
                                .flatMap(item -> item.getCollectiblePoints().stream()
                                        .map(detail -> {
                                            CollectiblePoint collectible = new CollectiblePoint();
                                            collectible.setMemberId(memberId);
                                            collectible.setCollectibleTypeName(item.getCollectibleTypeName());
                                            collectible.setCollectibleIconLink(item.getCollectibleIconLink());
                                            collectible.setCollectiblePointName(detail.getCollectiblePointName());
                                            collectible.setCollectedPoint(detail.getCollectedPoint());
                                            collectible.setCollectibleMaxPoint(detail.getCollectibleMaxPoint());
                                            return collectible;
                                        })
                                )
                                .toList();

                        // 데이터베이스에 저장
                        filterItem.forEach(collectibleDAO::insertCollectiblePoint);

                        // 저장 완료 후 빈 Mono 반환
                        return Mono.empty();
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                }).subscribe();
    }

    public List<CollectiblePointSummaryDTO> getCollectiblePointSummary(String memberId) {
        return collectibleDAO.getCollectiblePointSummary(memberId);
    }

    public List<RecommendCollectible> getRecommendCollectible(String memberId){
        return collectibleDAO.getRecommendCollectible(memberId);
    }

    public void clearCollectible(String memberId, int clearCollectibleId) {
        collectibleDAO.insertClearCollectible(memberId, clearCollectibleId);
    }

    public void updateCollectible(String memberId, String currentRCN) {
        collectibleDAO.deleteCollectible(memberId);
        saveCollectiblePoint(currentRCN, memberId);
    }
    public List<RecommendCollectibleFullDTO> getRecommendFullCollectible(String memberId) {
        return collectibleDAO.getRecommendFullCollectible(memberId);
    }

    public boolean updateCleared(int recommendCollectibleID, boolean cleared, String memberId) {
        try {
            if (cleared) {
                collectibleDAO.insertClearCollectible(memberId, recommendCollectibleID);
            } else {
                collectibleDAO.deleteClearCollectible(memberId, recommendCollectibleID);
            }
            return true;
        } catch (DataAccessException ex) {
            // 로그 남기고 실패 리턴
            log.error("내실 해결 상태 업데이트 실패: memberId={}, recommendCollectibleID={}, cleared={}",
                    memberId, recommendCollectibleID, cleared, ex);
            return false;
        }
    }

    // 추천내실 데이터 초기화 작업
    @Transactional
    @Scheduled(cron = "0 0 4 ? * WED") // 매주 수요일 오전 4시에 로직 실행
    public void saveRecommendCollectible() {
        // 이미 실행 중이라면 메서드 호출 방지
        if(running) {
            log.warn("saveRecommendCollectible 작업이 이미 실행 중입니다.");
            return;
        }
        running = true;

        // db 저장 파이프라인
        Mono.fromRunnable(() -> {
            collectibleDAO.deleteRecommendCollectible();
            log.info("기존 추천내실 데이터 삭제 완료");
        })
        .thenMany(Flux.just(
                new RecommendCollectibleDetailDTO ("내실 익스프레스 (스포)", "https://doyulv.tistory.com/46"),
                new RecommendCollectibleDetailDTO("지혜의 섬 보조 사서 (스포)", "https://www.inven.co.kr/board/lostark/4821/95622"),
                new RecommendCollectibleDetailDTO("거심 12개 (스포)", "https://www.inven.co.kr/board/lostark/4821/95622"),
                new RecommendCollectibleDetailDTO("파푸니카 80% (스포)", "https://www.inven.co.kr/board/lostark/4821/95622"),
                new RecommendCollectibleDetailDTO("이그네아의 징표 8개 (스포)", "https://www.inven.co.kr/board/lostark/4821/95622"),
                new RecommendCollectibleDetailDTO("타워 오브 데스티니 15층 (스포)", "https://blog.naver.com/bledel90/222525736538"),
                new RecommendCollectibleDetailDTO("타워 오브 데스티니 50층 (스포)", "https://blog.naver.com/bledel90/222525736538"),
                new RecommendCollectibleDetailDTO("세베크 아툰 (상깨물)", "https://www.inven.co.kr/webzine/news/?news=297450&site=lostark"),
                new RecommendCollectibleDetailDTO("평판: 끝나지 않은 싸움 (깨물)", "https://vortexgaming.io/postdetail/369504"),
                new RecommendCollectibleDetailDTO("쿠르잔 북부 70% (깨물)", "https://gam3.tistory.com/58"),
                new RecommendCollectibleDetailDTO("크림스네일의 해도 2개 (깨물)", "https://gopenguin.tistory.com/382"),
                new RecommendCollectibleDetailDTO("이그네아의 징표 9개 (비프로스트)", "https://www.inven.co.kr/board/lostark/4821/95622"),
                new RecommendCollectibleDetailDTO("모험물 34개 (영웅 풍요 룬)", "https://www.inven.co.kr/board/lostark/4821/100483"),
                new RecommendCollectibleDetailDTO("이그네아의 징표 15개 (전설 정화 룬)", "https://arca.live/b/lostark/83541704"),
                new RecommendCollectibleDetailDTO("전설 단죄 구매 (영지)", "https://www.inven.co.kr/board/lostark/4821/73762"),
                new RecommendCollectibleDetailDTO("전설 심판 구매 (영지)", "https://www.inven.co.kr/board/lostark/4821/73762"),
                new RecommendCollectibleDetailDTO("미술품 36개 (웨이 카드)", "https://canfactory.tistory.com/1241"),
                new RecommendCollectibleDetailDTO("거인의 심장 13개 (영웅 집중)", "https://inty.kr/entry/lostark-hearts"),
                new RecommendCollectibleDetailDTO("항해 모험물 42개 (전설 집중)", "https://oksk.tistory.com/24"),
                new RecommendCollectibleDetailDTO("미술품 44개 (전설 심판)", "https://canfactory.tistory.com/1241"),
                new RecommendCollectibleDetailDTO("오르페우스의 별 7개 (전설 수호)", "https://m.blog.naver.com/aaccq123/223169503483"),
                new RecommendCollectibleDetailDTO("미술품 58개 (전설 철벽)", "https://canfactory.tistory.com/1241"),
                new RecommendCollectibleDetailDTO("기억의 오르골 10개 (전설 카드팩 2개)", "https://www.inven.co.kr/board/lostark/4821/88649"),
                new RecommendCollectibleDetailDTO("기억의 오르골 14개 (도약의 전설 카드 선택팩)", "https://www.inven.co.kr/board/lostark/4821/94727"),
                new RecommendCollectibleDetailDTO("아스트레이 7렙 (쾌속항해)", "https://blog.naver.com/jej0572/222646100193")
        ))
        .map(this::toDomain)
        .doOnNext(collectibleDAO::insertRecommendCollectible)
        .doFinally(signalType -> running = false)
        .subscribe(
                null, // onNext (성공 데이터 처리)
                error -> log.error("추천내실 데이터 저장 중 에러 발생: \n{}", error.getMessage()), // onError (에러 처리)
                () -> log.info("추천내실 데이터 저장 완료") // onComplete (완료 처리)
        );
    }

    private RecommendCollectible toDomain(RecommendCollectibleDetailDTO collectibleDetailDTO) {
        RecommendCollectible recommendCollectible = new RecommendCollectible();

        recommendCollectible.setRecommendCollectibleName(collectibleDetailDTO.getRecommendCollectibleName());
        recommendCollectible.setRecommendCollectibleURL(collectibleDetailDTO.getRecommendCollectibleURL());

        return recommendCollectible;
    }
}