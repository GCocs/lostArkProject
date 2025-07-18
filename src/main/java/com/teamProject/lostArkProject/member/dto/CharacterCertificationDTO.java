package com.teamProject.lostArkProject.member.dto;

import com.teamProject.lostArkProject.member.dto.api.CharacterImageApiDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "캐릭터 인증 Dto")
public class CharacterCertificationDTO {

    @Schema(description = "캐릭터 이미지 Dto", example = "")
    private CharacterImageApiDTO characterImage;

    @Schema(description = "캐릭터 장비", example = "")
    private Map<String, EquipmentDTO> equipment;
}
