package com.teamProject.lostArkProject.member.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CharacterImageApiDTO {
    @JsonProperty("CharacterImage")
    private String characterImage;
}
