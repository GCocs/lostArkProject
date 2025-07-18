package com.teamProject.lostArkProject.member.domain;

import lombok.*;
import org.apache.ibatis.type.Alias;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("member_character")
public class MemberCharacter {
    private String characterNickname;
    private String serverName;
    private String characterClass;
    private String itemLevel;
    private String rosterLevel;
    private String memberId;
}
