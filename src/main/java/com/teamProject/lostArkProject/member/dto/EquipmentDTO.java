package com.teamProject.lostArkProject.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EquipmentDTO {
    private String type;
    private String name;
    private String icon;
    private String grade;
    private boolean unequippedRequired;
}
