package com.teamProject.lostArkProject.member.dao;

import com.teamProject.lostArkProject.member.domain.Member;
import com.teamProject.lostArkProject.member.domain.MemberCharacter;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberDAO {
    void insertMember(Member member);
    void insertMemberCharacter(MemberCharacter memberCharacter);
    void updateRCN(String memberId, String RCN);
    void deleteMemberCharacter(String memberId);
    String getMemberPW(String memberId);
    String getRepresentativeCharacter(String memberId);
    String checkMemberId(String memberId);
    void updateMemberPW(String memberId, String memberPW);
    void updateCertification(String memberId);
    void insertAuthCode(String memberId, String authCode);
    String getAuthCode(String memberId);
    void deleteAuthCode(String memberId);
}
