package com.teamProject.lostArkProject.academy.dao;

import com.teamProject.lostArkProject.academy.domain.AcademyBoard;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AcademyDAO {
    void createAcademyPost(AcademyBoard academyBoard);
}
