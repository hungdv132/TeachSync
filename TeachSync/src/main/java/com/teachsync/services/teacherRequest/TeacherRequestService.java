package com.teachsync.services.teacherRequest;

import com.teachsync.dtos.request.RequestCreateDTO;
import com.teachsync.dtos.request.RequestReadDTO;
import com.teachsync.dtos.request.RequestUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TeacherRequestService {
    Page<RequestReadDTO> getPageAll(Pageable paging) throws Exception;

    String addRequest(RequestCreateDTO createDTO);

    String editRequest(RequestUpdateDTO updateDTO);

    String deleteRequest(Long Id);

    RequestReadDTO findById(Long Id) throws Exception;

}
