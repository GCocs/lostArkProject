package com.teamProject.lostArkProject.academy.service;

import com.teamProject.lostArkProject.academy.dao.AcademyDAO;
import com.teamProject.lostArkProject.academy.domain.AcademyBoard;
import com.teamProject.lostArkProject.academy.dto.AcademyRequestDTO;
import com.teamProject.lostArkProject.common.dto.PaginatedResponseDTO;
import com.teamProject.lostArkProject.common.dto.Pagination;
import com.teamProject.lostArkProject.common.exception.UnauthorizedException;
import com.teamProject.lostArkProject.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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

    public AcademyBoard getAcademy(int academyBoardNumber) {
        AcademyBoard academy = academyDAO.getAcademy(academyBoardNumber);
        return academy;
    }
    public AcademyBoard getAcademyWithPermissionCheck(int academyBoardNumber, Member member) {
        AcademyBoard academy = academyDAO.getAcademy(academyBoardNumber);

        if (!academy.getWriter().equals(member.getRepresentativeCharacterNickname())) {
            throw new UnauthorizedException("게시글 권한이 없습니다.");
        }

        return academy;
    }

    public void editAcademyPost(int academyBoardNumber, AcademyRequestDTO academyRequestDTO, Member member) {
        AcademyBoard academy = academyDAO.getAcademy(academyBoardNumber);
        if (academy == null) {
            throw new NoSuchElementException("해당 학원팟 게시글이 존재하지 않습니다.");
        }
        if (!academy.getRaid().equals(academyRequestDTO.getSelectedRaid())) {
            throw new IllegalArgumentException("레이드는 수정할 수 없습니다.");
        }
        if (!academy.getWriter().equals(member.getRepresentativeCharacterNickname())) {
            throw new UnauthorizedException("게시글 수정 권한이 없습니다.");
        }

        AcademyBoard academyBoard = AcademyBoard.builder()
                .academyBoardNumber(academyBoardNumber)
                .writer(member.getRepresentativeCharacterNickname())
                .title(academyRequestDTO.getTitle())
                .content(academyRequestDTO.getContent())
                .build();

        academyDAO.editAcademyPost(academyBoard);
    }

    public void deleteAcademyPost(int academyBoardNumber, Member member) {
        AcademyBoard academy = academyDAO.getAcademy(academyBoardNumber);
        if (academy == null) {
            throw new NoSuchElementException("해당 학원팟 게시글이 존재하지 않습니다.");
        }
        if (!academy.getWriter().equals(member.getRepresentativeCharacterNickname())) {
            throw new UnauthorizedException("게시글 삭제 권한이 없습니다.");
        }

        academyDAO.deleteAcademyPost(academyBoardNumber);
    }
}
