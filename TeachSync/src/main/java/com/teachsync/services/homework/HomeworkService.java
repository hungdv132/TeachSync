package com.teachsync.services.homework;

import com.teachsync.dtos.homework.HomeworkReadDTO;
import com.teachsync.dtos.user.UserReadDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HomeworkService {

    Page<HomeworkReadDTO> getPageAll(Pageable paging,UserReadDTO userDTO) throws Exception;

    HomeworkReadDTO findById(Long id) throws Exception;

    HomeworkReadDTO findById(Long id,UserReadDTO userDTO) throws Exception;

    void addHomework(HomeworkReadDTO homeworkReadDTO, UserReadDTO userDTO) throws Exception;

    void editHomework(HomeworkReadDTO homeworkReadDTO ,UserReadDTO userDTO) throws Exception;

    void deleteHomework(Long Id ,UserReadDTO userDTO) throws Exception;
}
