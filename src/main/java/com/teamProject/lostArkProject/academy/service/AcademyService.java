package com.teamProject.lostArkProject.academy.service;

import com.teamProject.lostArkProject.academy.dao.AcademyDAO;
import com.teamProject.lostArkProject.academy.domain.AcademyBoard;
import com.teamProject.lostArkProject.academy.dto.AcademyRequestDTO;
import com.teamProject.lostArkProject.common.dto.PaginatedResponseDTO;
import com.teamProject.lostArkProject.common.dto.Pagination;
import com.teamProject.lostArkProject.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public PaginatedResponseDTO<AcademyBoard> getAcademyList(int page) {
        if (page < 1) {
            page = 1;
        }

        int size = 10;
        int offset = (page - 1) * size;
        int totalCount = academyDAO.getTotalCount();

        Pagination pagination = new Pagination(page, size, totalCount);
        List<AcademyBoard> academyList = academyDAO.getAcademyList(size, offset);

        return new PaginatedResponseDTO<>(academyList, pagination);
    }
}
