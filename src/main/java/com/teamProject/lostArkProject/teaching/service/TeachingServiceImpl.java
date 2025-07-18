package com.teamProject.lostArkProject.teaching.service;

import com.teamProject.lostArkProject.teaching.dao.TeachingDAO;
import com.teamProject.lostArkProject.teaching.dto.MenteeApplyDTO;
import com.teamProject.lostArkProject.teaching.dto.MenteeDTO;
import com.teamProject.lostArkProject.teaching.dto.MentorDTO;
import com.teamProject.lostArkProject.teaching.dto.MentorListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.sql.Timestamp;

@Service
public class TeachingServiceImpl implements TeachingService {

    @Autowired
    private TeachingDAO teachingDAO;

    @Override
    public void newMentor(MentorDTO mentorDTO) {
        // 1. MENTOR 테이블에 멘토 기본 정보 저장
        teachingDAO.newMentor(mentorDTO);

        // 2. MENTOR_CONTENT 테이블에 콘텐츠 정보 저장
        if (mentorDTO.getMentorContentId() != null && !mentorDTO.getMentorContentId().isEmpty()) {
            // 쉼표로 구분된 문자열을 배열로 변환
            String[] contentIds = mentorDTO.getMentorContentId().split(", ");
            for (String contentId : contentIds) {
                teachingDAO.insertMentorContent(mentorDTO.getMentorMemberId(), contentId.trim());
            }
        }
    }


    @Override
    public void newMentee(MenteeDTO menteeDTO) {
        teachingDAO.newMentee(menteeDTO);
    }

    //@Override
    //public void newMenteeApply(MenteeApplyDTO menteeApplyDTO){

   // }
   @Override
   public List<MentorListDTO> getMentorList() {
       List<Map<String, Object>> mentorList = Optional.ofNullable(teachingDAO.getMentorList()).orElse(Collections.emptyList());
       List<Map<String, Object>> mentorContentList = Optional.ofNullable(teachingDAO.getMentorContent()).orElse(Collections.emptyList());
       List<Map<String, Object>> memberCharacterList = Optional.ofNullable(teachingDAO.getMemberCharacter()).orElse(Collections.emptyList());

       List<MentorListDTO> resultList = new ArrayList<>();

       for (Map<String, Object> mentor : mentorList) {
           MentorListDTO dto = new MentorListDTO();
           dto.setMentorMemberId(String.valueOf(mentor.get("mentorMemberId")));
           dto.setMentorWantToSay(String.valueOf(mentor.get("mentorWantToSay")));

           // mentorContentIds 처리
           String mentorMemberId = String.valueOf(mentor.get("mentorMemberId"));
           mentorContentList.stream()
                   .filter(content -> String.valueOf(content.get("mentorMemberId")).equals(mentorMemberId))
                   .findFirst()
                   .ifPresent(content -> {
                       String contentIdsString = String.valueOf(content.get("mentorContentNames"));
                       List<String> contentIds = Arrays.asList(contentIdsString.split(","));
                       dto.setMentorContentIds(contentIds);
                   });

           // memberCharacterList 처리
           memberCharacterList.stream()
                   .filter(character -> String.valueOf(character.get("mentorMemberId")).equals(mentorMemberId))
                   .findFirst()
                   .ifPresent(character -> {
                       dto.setCharacterNickname(String.valueOf(character.get("characterNickname")));
                       dto.setItemLevel(String.valueOf(character.get("itemLevel")));
                       dto.setServerName(String.valueOf(character.get("serverName")));
                   });

           resultList.add(dto);
       }

       return resultList;
   }
    @Override
    public List<MentorListDTO> getMentorDetail(String mentorMemberId) {
        Map<String, Map<String, Object>> mentorMap = teachingDAO.getMentorList().stream()
                .collect(Collectors.toMap(m -> String.valueOf(m.get("mentorMemberId")), m -> m));
        Map<String, Map<String, Object>> contentMap = teachingDAO.getMentorContent().stream()
                .collect(Collectors.toMap(c -> String.valueOf(c.get("mentorMemberId")), c -> c));
        Map<String, Map<String, Object>> characterMap = teachingDAO.getMemberCharacter().stream()
                .collect(Collectors.toMap(c -> String.valueOf(c.get("mentorMemberId")), c -> c));

        List<MentorListDTO> resultList = new ArrayList<>();
        Map<String, Object> mentor = mentorMap.get(mentorMemberId);
        if (mentor != null) {
            MentorListDTO dto = new MentorListDTO();
            dto.setMentorMemberId(mentorMemberId);
            dto.setMentorWantToSay(String.valueOf(mentor.get("mentorWantToSay")));

            Map<String, Object> content = contentMap.get(mentorMemberId);
            if (content != null) {
                String contentIdsString = String.valueOf(content.get("mentorContentNames"));
                dto.setMentorContentIds(Arrays.asList(contentIdsString.split(",")));
            }

            Map<String, Object> character = characterMap.get(mentorMemberId);
            if (character != null) {
                dto.setCharacterNickname(String.valueOf(character.get("characterNickname")));
                dto.setItemLevel(String.valueOf(character.get("itemLevel")));
                dto.setServerName(String.valueOf(character.get("serverName")));
            }

            resultList.add(dto);
        }

        return resultList;
    }

    @Override
    public String getMentorDiscordId(String mentorMemberId) {
        return teachingDAO.findDiscordIdByMentorId(mentorMemberId);
    }


    @Override
    public List<Map<String, Object>> getApplyStatusByMentee(String menteeMemberId) {
        return teachingDAO.getApplyStatusByMentee(menteeMemberId);
    }
    @Override
    public void insertMenteeApply(MenteeApplyDTO menteeApplyDTO) {
        teachingDAO.insertMenteeApply(menteeApplyDTO);
    }
    @Override
    public boolean isDuplicateMenteeApply(String mentorId, String menteeId) {
        MenteeApplyDTO dto = new MenteeApplyDTO();
        dto.setMentorMemberId(mentorId);
        dto.setMenteeMemberId(menteeId);
        int count = teachingDAO.isDuplicateMenteeApply(dto);
        return count > 0;
    }

    @Override
    public List<Map<String, Object>> getRequestedAppliesByMentor(String mentorMemberId) {
        System.out.println("Checking requests for mentor: " + mentorMemberId);
        List<Map<String, Object>> result = teachingDAO.getRequestedAppliesByMentor(mentorMemberId);
        System.out.println("Found requests: " + result);
        return result;
    }

    @Override
    public Set<String> getAppliedMentorIdsByMentee(String menteeId) {
        return teachingDAO.getAppliedMentorIdsByMentee(menteeId);
    }

    @Override
    public Map<String, String> getAppliedMentorStatusByMentee(String menteeId) {
        List<Map<String, Object>> results = teachingDAO.getAppliedMentorStatusByMentee(menteeId);
        Map<String, String> statusMap = new HashMap<>();
        
        for (Map<String, Object> result : results) {
            String mentorId = String.valueOf(result.get("mentor_member_id"));
            String status = String.valueOf(result.get("apply_status"));
            statusMap.put(mentorId, status);
        }
        
        return statusMap;
    }

    @Override
    public boolean canReapplyToMentor(String mentorMemberId, String menteeMemberId) {

        if (teachingDAO.isBlockedMentee(mentorMemberId, menteeMemberId)) {
            return false; // 차단된 경우 재신청 불가
        }
        

        Map<String, Object> applyInfo = teachingDAO.getMenteeApplyInfo(mentorMemberId, menteeMemberId);
        if (applyInfo == null || !"REJECTED".equals(applyInfo.get("apply_status"))) {
            return true;
        }
        

        Object updatedAtObj = applyInfo.get("updated_at");
        if (updatedAtObj == null) {
            return true;
        }
        
        try {
            LocalDateTime rejectedTime;
            if (updatedAtObj instanceof Timestamp) {
                rejectedTime = ((Timestamp) updatedAtObj).toLocalDateTime();
            } else {
                return true; // 시간 정보가 없으면 신청 가능
            }
            
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime reapplyAllowedTime = rejectedTime.plusDays(7); // 7일 후 재신청 가능
            
            return now.isAfter(reapplyAllowedTime);
        } catch (Exception e) {
            System.err.println("Error parsing date: " + e.getMessage());
            return true;
        }
    }

    @Override
    public void cleanupExpiredRejectedApplies() {
        try {
           
            List<Map<String, Object>> expiredApplies = teachingDAO.getExpiredRejectedApplies();
            
            for (Map<String, Object> apply : expiredApplies) {
                String mentorMemberId = String.valueOf(apply.get("mentor_member_id"));
                String menteeMemberId = String.valueOf(apply.get("mentee_member_id"));
                
                
                boolean isBlocked = teachingDAO.isBlockedMentee(mentorMemberId, menteeMemberId);
                
                if (!isBlocked) {
                    teachingDAO.deleteRejectedApply(mentorMemberId, menteeMemberId);
                    System.out.println("Deleted expired rejected apply: mentor=" + mentorMemberId + ", mentee=" + menteeMemberId);
                }
            }
        } catch (Exception e) {
            System.err.println("Error cleaning up expired rejected applies: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getMentorInfoById(String mentorMemberId) {
        return teachingDAO.getMentorInfoById(mentorMemberId);
    }

    @Override
    public List<String> getMentorContentIdsById(String mentorMemberId) {
        return teachingDAO.getMentorContentIdsById(mentorMemberId);
    }

    @Override
    public boolean isMentorExists(String mentorMemberId) {
        return teachingDAO.isMentorExists(mentorMemberId) > 0;
    }

    @Override
    public void updateMentor(MentorDTO mentorDTO) {

        teachingDAO.updateMentor(mentorDTO);
        teachingDAO.deleteMentorContent(mentorDTO.getMentorMemberId());
        if (mentorDTO.getMentorContentId() != null && !mentorDTO.getMentorContentId().isEmpty()) {
            String[] contentIds = mentorDTO.getMentorContentId().split(", ");
            for (String contentId : contentIds) {
                teachingDAO.insertMentorContent(mentorDTO.getMentorMemberId(), contentId.trim());
            }
        }
    }
}
