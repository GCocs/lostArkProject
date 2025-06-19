package com.teamProject.lostArkProject.member.dto;

import com.teamProject.lostArkProject.member.dto.api.CharacterImageApiDTO;
import com.teamProject.lostArkProject.member.dto.api.EquipmentApiDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CharacterCertificationDTO {
    private CharacterImageApiDTO characterImage;
    private Map<String, EquipmentDTO> equipment;
}
