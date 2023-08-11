package com.teachsync.services.teacherRequest;

import com.teachsync.dtos.request.RequestCreateDTO;
import com.teachsync.dtos.request.RequestReadDTO;
import com.teachsync.dtos.request.RequestUpdateDTO;
import com.teachsync.entities.Request;
import com.teachsync.entities.User;
import com.teachsync.repositories.RequestRepository;
import com.teachsync.repositories.UserRepository;
import com.teachsync.utils.Constants;
import com.teachsync.utils.MiscUtil;
import com.teachsync.utils.enums.RequestType;
import com.teachsync.utils.enums.Status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeacherRequestServiceImpl implements TeacherRequestService {
    @Autowired
    RequestRepository requestRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private MiscUtil miscUtil;

    @Autowired
    UserRepository userRepository;

    @Override
    public Page<RequestReadDTO> getPageAll(Pageable paging) throws Exception {
        if (paging == null) {
            paging = miscUtil.defaultPaging();
        }
        Page<Request> teacherRequests = requestRepository.findAllByStatusNot(Status.DELETED, paging);
        List<RequestReadDTO> teacherRequestDtoList = new ArrayList<>();
        for(Request request : teacherRequests){
            RequestReadDTO requestReadDTO = mapper.map(request, RequestReadDTO.class);
            //get user
            User user = userRepository.findById(request.getRequesterId()).orElseThrow(() -> new Exception("không tìm thấy tài khoản"));
            requestReadDTO.setUsername(user.getUsername());
            requestReadDTO.setFullName(user.getFullName());
            teacherRequestDtoList.add(requestReadDTO);
        }

        Page<RequestReadDTO> requestReadList = new PageImpl<>(teacherRequestDtoList, paging, teacherRequests.getTotalElements());

        if (requestReadList.isEmpty()) {
            return null;
        }

        return requestReadList;
    }

    @Transactional
    @Override
    public String addRequest(RequestCreateDTO createDTO) {
        Request teacherRequest = new Request();
        teacherRequest.setRequesterId(createDTO.getRequesterId());
        teacherRequest.setRequestName("Request apply Teacher " + MiscUtil.generateRandomName());
        teacherRequest.setRequestType(RequestType.APPLICATION);
        teacherRequest.setRequestDesc(createDTO.getRequestDesc());
        teacherRequest.setRequestContent(createDTO.getRequestContent());
        teacherRequest.setContentLink(createDTO.getContentLink());
        teacherRequest.setStatus(Status.CREATED);

        Request teacherDb = requestRepository.saveAndFlush(teacherRequest);

        if (ObjectUtils.isEmpty(teacherDb)) {
            return "error";
        }

        return "success";
    }

    @Override
    public String editRequest(RequestUpdateDTO updateDTO) {
        return null;
    }

    @Override
    public String deleteRequest(Long Id) {
        return null;
    }

    @Override
    public RequestReadDTO findById(Long Id) throws Exception {
        return null;
    }



}
