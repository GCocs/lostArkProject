package com.teamProject.lostArkProject.notice.dao;

import com.teamProject.lostArkProject.common.dto.PaginatedRequestDTO;
import com.teamProject.lostArkProject.notice.domain.Notice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeDAO {
    List<Notice> getNoticeList(PaginatedRequestDTO req);
}
