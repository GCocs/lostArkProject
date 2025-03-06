package com.teamProject.lostArkProject.academy.service;

import com.teamProject.lostArkProject.academy.dao.AcademyDAO;
import com.teamProject.lostArkProject.academy.domain.AcademyBoard;
import com.teamProject.lostArkProject.academy.dto.AcademyRequestDTO;
import com.teamProject.lostArkProject.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AcademyService {
    private final AcademyDAO academyDAO;

    public void createAcademyPost(AcademyRequestDTO academyRequestDTO, Member member) {
        AcademyBoard academyBoard = AcademyBoard.builder()
                .writer(member.getRepresentativeCharacterNickname())
                .title(academyRequestDTO.getTitle())
                .content(academyRequestDTO.getContent())
                .raid(academyRequestDTO.getSelectedRaid())
                .image(academyRequestDTO.getImage())
                .build();

        academyDAO.createAcademyPost(academyBoard);
    }
}
