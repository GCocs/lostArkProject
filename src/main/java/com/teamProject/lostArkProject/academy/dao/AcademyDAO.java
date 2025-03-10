package com.teamProject.lostArkProject.academy.dao;

import com.teamProject.lostArkProject.academy.domain.AcademyBoard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AcademyDAO {
    void createAcademyPost(AcademyBoard academyBoard);

    List<AcademyBoard> getAcademyList(@Param("size") int size, @Param("offset") int offset);
    AcademyBoard getAcademy(@Param("academyId") int academyId);

    int getTotalCount();
}
