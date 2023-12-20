package com.teachsync.services.request;

import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.clazz.ClazzReadDTO;
import com.teachsync.dtos.request.RequestCreateDTO;
import com.teachsync.dtos.request.RequestReadDTO;
import com.teachsync.dtos.request.RequestUpdateDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.entities.Clazz;
import com.teachsync.entities.Request;
import com.teachsync.entities.User;
import com.teachsync.repositories.RequestRepository;
import com.teachsync.services.clazz.ClazzService;
import com.teachsync.services.user.UserService;
import com.teachsync.utils.MiscUtil;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.RequestType;
import com.teachsync.utils.enums.Status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.teachsync.utils.enums.Status.*;
import static com.teachsync.utils.enums.DtoOption.*;

@Service
public class RequestServiceImpl implements RequestService {
    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private ClazzService clazzService;
    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper mapper;
    @Autowired
    private MiscUtil miscUtil;


    /* =================================================== CREATE =================================================== */
    @Override
    public Request createRequest(
            Request request) throws Exception {

        StringBuilder errorMsg = new StringBuilder();

        /* Validate input */
        /* TODO:  */

        /* Check FK */
        if (!clazzService.existsById(request.getClazzId())) {
            errorMsg.append("Không tìm thấy Lớp Học nào với id: ")
                    .append(request.getClazzId()).append("\n");
        }
        if (!userService.existsById(request.getRequesterId())) {
            errorMsg.append("Không tìm thấy Tài Khoản lập đơn. Không tìm thấy Tài Khoản nào với id: ")
                    .append(request.getRequesterId()).append("\n");
        }

        /* Check duplicate */
        if (requestRepository.existsByRequesterIdAndClazzIdAndRequestTypeAndStatusNotIn(
                request.getRequesterId(),
                request.getClazzId(),
                request.getRequestType(),
                List.of(DELETED, DENIED))) {
            errorMsg.append("Bạn đã nộp đơn loại: ").append(request.getRequestType().getStringValueVie())
                    .append(" cho lớp: ").append(request.getClazzId())
                    .append(" đang chờ xét duyệt hoặc đã duyệt thành công.\n");
        }

        /* Is error */
        if (!errorMsg.isEmpty()) {
            throw new IllegalArgumentException(
                    "Lỗi tạo Đơn: \n" + errorMsg.toString());
        }

        /* Insert DB */
        return requestRepository.saveAndFlush(request);
    }

    @Override
    public RequestReadDTO createRequestByDTO(
            RequestCreateDTO createDTO) throws Exception {
        Request request = mapper.map(createDTO, Request.class);
        
        request = createRequest(request);
        
        return wrapDTO(request, null);
    }


    /* =================================================== READ ===================================================== */
    @Override
    public List<Request> getAll(
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        List<Request> requestList;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }
            
            requestList =
                    requestRepository.findAllByStatusIn(
                            statuses);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            requestList =
                    requestRepository.findAllByStatusNotIn(
                            statuses);
        }

        if (requestList.isEmpty()) { return null; }

        return requestList;
    }
    @Override
    public List<RequestReadDTO> getAllDTO(
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {
        
        List<Request> requestList = 
                getAll(statuses, isStatusIn);

        return wrapListDTO(requestList, options);
    }

    @Override
    public Page<Request> getPageAll(
            Pageable pageable,
            Collection<Status> statuses, boolean isStatusIn) throws Exception {
        
        if (pageable == null) { pageable = miscUtil.defaultPaging(); }

        Page<Request> requestPage;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            requestPage =
                    requestRepository.findAllByStatusIn(
                            statuses,
                            pageable);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            requestPage =
                    requestRepository.findAllByStatusNotIn(
                            statuses,
                            pageable);
        }

        if (requestPage.isEmpty()) { return null; }

        return requestPage;
    }
    @Override
    public Page<RequestReadDTO> getPageAllDTO(
            Pageable pageable,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        Page<Request> requestPage = 
                getPageAll(pageable, statuses, isStatusIn);

        return wrapPageDTO(requestPage, options);
    }

    /* id */
    @Override
    public Request getById(
            Long id,
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            return requestRepository
                    .findByIdAndStatusIn(id, statuses)
                    .orElse(null);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            return requestRepository
                    .findByIdAndStatusNotIn(id, statuses)
                    .orElse(null);
        }
    }
    @Override
    public RequestReadDTO getDTOById(
            Long id,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {
        
        Request request = 
                getById(id, statuses, isStatusIn);

        return wrapDTO(request, options);
    }

    @Override
    public List<Request> getAllByIdIn(
            Collection<Long> ids,
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        List<Request> requestList;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            requestList =
                    requestRepository.findAllByIdInAndStatusIn(
                            ids,
                            statuses);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            requestList =
                    requestRepository.findAllByIdInAndStatusNotIn(
                            ids,
                            statuses);
        }

        if (requestList.isEmpty()) { return null; }

        return requestList;
    }
    @Override
    public List<RequestReadDTO> getAllDTOByIdIn(
            Collection<Long> ids,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {
        
        List<Request> requestList = 
                getAllByIdIn(ids, statuses, isStatusIn);
        
        return wrapListDTO(requestList, options);
    }
    @Override
    public Map<Long, RequestReadDTO> mapIdDTOByIdIn(
            Collection<Long> ids,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        List<RequestReadDTO> requestDTOList = 
                getAllDTOByIdIn(ids, statuses, isStatusIn, options);

        if (requestDTOList == null) { return new HashMap<>(); }

        return requestDTOList.stream()
                .collect(Collectors.toMap(BaseReadDTO::getId, Function.identity()));
    }

    /* requesterId (User.id) */
    @Override
    public List<Request> getAllByRequesterId(
            Long requesterId,
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        List<Request> requestList;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            requestList =
                    requestRepository.findAllByRequesterIdAndStatusIn(
                            requesterId,
                            statuses);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            requestList =
                    requestRepository.findAllByRequesterIdAndStatusNotIn(
                            requesterId,
                            statuses);
        }

        if (requestList.isEmpty()) { return null; }

        return requestList;
    }
    @Override
    public List<RequestReadDTO> getAllDTOByRequesterId(
            Long requesterId,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        List<Request> requestList = 
                getAllByRequesterId(requesterId, statuses, isStatusIn);

        return wrapListDTO(requestList, options);
    }

    @Override
    public Page<Request> getPageAllByRequesterId(
            Pageable pageable, Long requesterId,
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        if (pageable == null) { pageable = miscUtil.defaultPaging(); }

        Page<Request> requestPage;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            requestPage =
                    requestRepository.findAllByRequesterIdAndStatusIn(
                            requesterId,
                            statuses,
                            pageable);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            requestPage =
                    requestRepository.findAllByRequesterIdAndStatusNotIn(
                            requesterId,
                            statuses,
                            pageable);
        }

        if (requestPage.isEmpty()) { return null; }

        return requestPage;
    }
    @Override
    public Page<RequestReadDTO> getPageAllDTOByRequesterId(
            Pageable pageable, Long requesterId,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        Page<Request> requestPage = 
                getPageAllByRequesterId(pageable, requesterId, statuses, isStatusIn);

        return wrapPageDTO(requestPage, options);
    }

    @Override
    public List<Request> getAllByRequesterIdIn(
            Collection<Long> requesterIds,
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        List<Request> requestList;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            requestList =
                    requestRepository.findAllByRequesterIdInAndStatusIn(
                            requesterIds,
                            statuses);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            requestList =
                    requestRepository.findAllByRequesterIdInAndStatusNotIn(
                            requesterIds,
                            statuses);
        }

        if (requestList.isEmpty()) { return null; }

        return requestList;
    }
    @Override
    public List<RequestReadDTO> getAllDTOByRequesterIdIn(
            Collection<Long> requesterIds,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        List<Request> requestList = 
                getAllByRequesterIdIn(requesterIds, statuses, isStatusIn);

        return wrapListDTO(requestList, options);
    }

    @Override
    public Page<Request> getPageAllByRequesterIdIn(
            Pageable pageable, Collection<Long> requesterIds,
            Collection<Status> statuses, boolean isStatusIn) throws Exception {
        
        if (pageable == null) { pageable = miscUtil.defaultPaging(); }

        Page<Request> requestPage;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            requestPage =
                    requestRepository.findAllByRequesterIdInAndStatusIn(
                            requesterIds,
                            statuses,
                            pageable);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            requestPage =
                    requestRepository.findAllByRequesterIdInAndStatusNotIn(
                            requesterIds,
                            statuses,
                            pageable);
        }

        if (requestPage.isEmpty()) { return null; }

        return requestPage;
    }
    @Override
    public Page<RequestReadDTO> getPageAllDTOByRequesterIdIn(
            Pageable pageable, Collection<Long> requesterIds,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        Page<Request> requestPage = 
                getPageAllByRequesterIdIn(pageable, requesterIds, statuses, isStatusIn);

        return wrapPageDTO(requestPage, options);
    }

    /* id & requesterId */
    @Override
    public Request getByIdAndRequesterId(
            Long id, Long requesterId,
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            return requestRepository
                    .findByIdAndRequesterIdAndStatusIn(id, requesterId, statuses)
                    .orElse(null);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            return requestRepository
                    .findByIdAndRequesterIdAndStatusNotIn(id, requesterId, statuses)
                    .orElse(null);
        }
    }
    @Override
    public RequestReadDTO getDTOByIdAndRequesterId(
            Long id, Long requesterId,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        Request request = 
                getByIdAndRequesterId(id, requesterId, statuses, isStatusIn);

        return wrapDTO(request, options);
    }

    /* clazzId */
    @Override
    public List<Request> getAllByClazzId(
            Long clazzId,
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        List<Request> requestList;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            requestList =
                    requestRepository.findAllByClazzIdAndStatusIn(
                            clazzId,
                            statuses);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            requestList =
                    requestRepository.findAllByClazzIdAndStatusNotIn(
                            clazzId,
                            statuses);
        }

        if (requestList.isEmpty()) { return null; }

        return requestList;
    }
    @Override
    public List<RequestReadDTO> getAllDTOByClazzId(
            Long clazzId,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        List<Request> requestList =
                getAllByClazzId(clazzId, statuses, isStatusIn);

        return wrapListDTO(requestList, options);
    }

    @Override
    public Page<Request> getPageAllByClazzId(
            Pageable pageable, Long clazzId,
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        if (pageable == null) { pageable = miscUtil.defaultPaging(); }

        Page<Request> requestPage;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            requestPage =
                    requestRepository.findAllByClazzIdAndStatusIn(
                            clazzId,
                            statuses,
                            pageable);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            requestPage =
                    requestRepository.findAllByClazzIdAndStatusNotIn(
                            clazzId,
                            statuses,
                            pageable);
        }

        if (requestPage.isEmpty()) { return null; }

        return requestPage;
    }
    @Override
    public Page<RequestReadDTO> getPageAllDTOByClazzId(
            Pageable pageable, Long clazzId,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {
        Page<Request> requestPage =
                getPageAllByClazzId(pageable, clazzId, statuses, isStatusIn);

        return wrapPageDTO(requestPage, options);
    }

    @Override
    public List<Request> getAllByClazzIdAndRequestType(
            Long clazzId, RequestType requestType,
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        List<Request> requestList;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            requestList =
                    requestRepository.findAllByClazzIdAndRequestTypeAndStatusIn(
                            clazzId,
                            requestType,
                            statuses);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            requestList =
                    requestRepository.findAllByClazzIdAndRequestTypeAndStatusNotIn(
                            clazzId,
                            requestType,
                            statuses);
        }

        if (requestList.isEmpty()) { return null; }

        return requestList;
    }
    @Override
    public List<RequestReadDTO> getAllDTOByClazzIdAndRequestType(
            Long clazzId, RequestType requestType,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        List<Request> requestList =
                getAllByClazzIdAndRequestType(clazzId, requestType, statuses, isStatusIn);

        return wrapListDTO(requestList, options);
    }

    @Override
    public Page<Request> getPageAllByClazzIdAndRequestType(
            Pageable pageable, Long clazzId, RequestType requestType,
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        if (pageable == null) { pageable = miscUtil.defaultPaging(); }

        Page<Request> requestPage;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            requestPage =
                    requestRepository.findAllByClazzIdAndRequestTypeAndStatusIn(
                            clazzId,
                            requestType,
                            statuses,
                            pageable);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            requestPage =
                    requestRepository.findAllByClazzIdAndRequestTypeAndStatusNotIn(
                            clazzId,
                            requestType,
                            statuses,
                            pageable);
        }

        if (requestPage.isEmpty()) { return null; }

        return requestPage;
    }
    @Override
    public Page<RequestReadDTO> getPageAllDTOByClazzIdAndRequestType(
            Pageable pageable, Long clazzId, RequestType requestType,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {
        Page<Request> requestPage =
                getPageAllByClazzIdAndRequestType(pageable, clazzId, requestType, statuses, isStatusIn);

        return wrapPageDTO(requestPage, options);
    }

    @Override
    public List<Request> getAllByClazzIdIn(
            Collection<Long> clazzIds,
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        List<Request> requestList;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            requestList =
                    requestRepository.findAllByClazzIdInAndStatusIn(
                            clazzIds,
                            statuses);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            requestList =
                    requestRepository.findAllByClazzIdInAndStatusNotIn(
                            clazzIds,
                            statuses);
        }

        if (requestList.isEmpty()) { return null; }

        return requestList;
    }
    @Override
    public List<RequestReadDTO> getAllDTOByClazzIdIn(
            Collection<Long> clazzIds,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        List<Request> requestList = 
                getAllByClazzIdIn(clazzIds, statuses, isStatusIn);

        return wrapListDTO(requestList, options);
    }

    @Override
    public Page<Request> getPageAllByClazzIdIn(
            Pageable pageable, Collection<Long> clazzIds,
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        if (pageable == null) { pageable = miscUtil.defaultPaging(); }

        Page<Request> requestPage;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            requestPage =
                    requestRepository.findAllByClazzIdInAndStatusIn(
                            clazzIds,
                            statuses,
                            pageable);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            requestPage =
                    requestRepository.findAllByClazzIdInAndStatusNotIn(
                            clazzIds,
                            statuses,
                            pageable);
        }

        if (requestPage.isEmpty()) { return null; }

        return requestPage;
    }
    @Override
    public Page<RequestReadDTO> getPageAllDTOByClazzIdIn(
            Pageable pageable, Collection<Long> clazzIds,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        Page<Request> requestPage = 
                getPageAllByClazzIdIn(pageable, clazzIds, statuses, isStatusIn);

        return wrapPageDTO(requestPage, options);
    }
    
    /* requesterId & clazzId & requestType */
    @Override
    public List<Request> getAllByRequesterIdAndClazzIdAndRequestType(
            Long requesterId, Long clazzId, RequestType requestType,
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        List<Request> requestList;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            requestList =
                    requestRepository.findAllByRequesterIdAndClazzIdAndRequestTypeAndStatusIn(
                            requesterId,
                            clazzId,
                            requestType,
                            statuses);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            requestList =
                    requestRepository.findAllByRequesterIdAndClazzIdAndRequestTypeAndStatusNotIn(
                            requesterId,
                            clazzId,
                            requestType,
                            statuses);
        }

        if (requestList.isEmpty()) { return null; }

        return requestList;
    }
    @Override
    public List<RequestReadDTO> getAllDTOByRequesterIdAndClazzIdAndRequestType(
            Long requesterId, Long clazzId, RequestType requestType,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        List<Request> requestList =
                getAllByRequesterIdAndClazzIdAndRequestType(requesterId, clazzId, requestType, statuses, isStatusIn);

        return wrapListDTO(requestList, options);
    }

    @Override
    public Page<Request> getPageAllByRequesterIdAndClazzIdAndRequestType(
            Pageable pageable, Long requesterId, Long clazzId, RequestType requestType,
            Collection<Status> statuses, boolean isStatusIn) throws Exception {

        if (pageable == null) { pageable = miscUtil.defaultPaging(); }

        Page<Request> requestPage;

        if (isStatusIn) {
            if (ObjectUtils.isEmpty(statuses)) { return null; }

            requestPage =
                    requestRepository.findAllByRequesterIdAndClazzIdAndRequestTypeAndStatusIn(
                            requesterId,
                            clazzId,
                            requestType,
                            statuses,
                            pageable);
        } else {
            if (ObjectUtils.isEmpty(statuses)) { statuses = List.of(DELETED); }

            requestPage =
                    requestRepository.findAllByRequesterIdAndClazzIdAndRequestTypeAndStatusNotIn(
                            requesterId,
                            clazzId,
                            requestType,
                            statuses,
                            pageable);
        }

        if (requestPage.isEmpty()) { return null; }

        return requestPage;
    }
    @Override
    public Page<RequestReadDTO> getPageAllDTOByRequesterIdAndClazzIdAndRequestType(
            Pageable pageable, Long requesterId, Long clazzId, RequestType requestType,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception {

        Page<Request> requestPage = 
                getPageAllByRequesterIdAndClazzIdAndRequestType(pageable, requesterId, clazzId, requestType, statuses, isStatusIn);

        return wrapPageDTO(requestPage, options);
    }

    /* =================================================== UPDATE =================================================== */
    @Override
    public Request updateRequest(
            Request request) throws Exception {

        /* Check exist */
        Request oldRequest = 
                getById(request.getId(), List.of(DELETED), false);
        if (oldRequest == null) {
            throw new IllegalArgumentException("Lỗi sửa Đơn. Không tìm thấy Đơn nào với id: " + request.getId());
        }
        request.setCreatedBy(oldRequest.getCreatedBy());
        request.setCreatedAt(oldRequest.getCreatedAt());

        StringBuilder errorMsg = new StringBuilder();

        /* Validate input */
        /* TODO:  */

        /* Check FK */
        if (!clazzService.existsById(request.getClazzId())) {
            errorMsg.append("Không tìm thấy Lớp Học nào với id: ")
                    .append(request.getClazzId()).append("\n");
        }
        if (!userService.existsById(request.getRequesterId())) {
            errorMsg.append("Không tìm thấy Tài Khoản lập đơn. Không tìm thấy Tài Khoản nào với id: ")
                    .append(request.getRequesterId()).append("\n");
        }
        if (request.getResolverId() != null) {
            if (!userService.existsById(request.getResolverId())) {
                errorMsg.append("Không tìm thấy Tài Khoản duyệt đơn. Không tìm thấy Tài Khoản nào với id: ")
                        .append(request.getResolverId()).append("\n");
            }
        }

        /* Check duplicate */
        if (requestRepository.existsByIdNotAndRequesterIdAndClazzIdAndRequestTypeAndStatusNotIn(
                request.getId(),
                request.getRequesterId(),
                request.getClazzId(),
                request.getRequestType(),
                List.of(DELETED, DENIED))) {
            errorMsg.append("Bạn đã nộp đơn loại: ").append(request.getRequestType().getStringValueVie())
                    .append(" cho lớp: ").append(request.getClazzId())
                    .append(" đang chờ xét duyệt hoặc đã duyệt thành công.\n");
        }

        /* Is error */
        if (!errorMsg.isEmpty()) {
            throw new IllegalArgumentException(
                    "Lỗi sửa Đơn: \n" + errorMsg.toString());
        }

        /* Insert DB */
        return requestRepository.saveAndFlush(request);
    }

    @Override
    public RequestReadDTO updateRequestByDTO(
            RequestUpdateDTO updateDTO) throws Exception {
        Request request = mapper.map(updateDTO, Request.class);

        request = updateRequest(request);

        return wrapDTO(request, null);
    }


    /* =================================================== DELETE =================================================== */
    @Override
    public Boolean deleteRequest(Long id, Long requesterId) throws Exception {
        Request request = getByIdAndRequesterId(id, requesterId, List.of(DELETED), false);

        if (request == null) {
            throw new IllegalArgumentException(
                    "Lỗi xóa Đơn. Không tìm thấy Đơn nào của bạn với id: " + id);
        }

        if (!request.getStatus().equals(Status.PENDING_PAYMENT)) {
            throw new IllegalArgumentException(
                    "Lỗi xóa Đơn. Chỉ có những Đơn với trạng thái 'Đang chờ thanh toán' mới được phép xóa.");
        }

//        Status oldStatus = request.getStatus();

        /* Delete */
        request.setStatus(DELETED);

        /* Save to DB */
        requestRepository.saveAndFlush(request);

        return true;
    }


    /* =================================================== WRAPPER ================================================== */
    @Override
    public RequestReadDTO wrapDTO(
            Request request, Collection<DtoOption> options) throws Exception {
        if (request == null) { return null; }

        RequestReadDTO dto = mapper.map(request, RequestReadDTO.class);

        /* Add dependency */
        if (options != null && !options.isEmpty()) {
            if (options.contains(CLAZZ)) {
                ClazzReadDTO clazzDTO = clazzService.getDTOById(
                        request.getClazzId(),
                        List.of(Status.DELETED),
                        false,
                        options);
                dto.setClazz(clazzDTO);
            }
            if (options.contains(CLAZZ_NAME)) {
                Clazz clazz = clazzService.getById(
                        request.getClazzId(),
                        List.of(Status.DELETED),
                        false);
                dto.setClazzName(clazz.getClazzName());
            }
            if (options.contains(CLAZZ_ALIAS)) {
                Clazz clazz = clazzService.getById(
                        request.getClazzId(),
                        List.of(Status.DELETED),
                        false);
                dto.setClazzAlias(clazz.getClazzAlias());
            }

            if (options.contains(REQUESTER)) {
                UserReadDTO requesterDTO =
                        userService.getDTOById(request.getRequesterId(), options);
                dto.setRequester(requesterDTO);
            }
            if (options.contains(REQUESTER_USERNAME)) {
                User requester =
                        userService.getById(request.getRequesterId());
                dto.setRequesterUsername(requester.getUsername());
            }
            if (options.contains(REQUESTER_FULL_NAME)) {
                User requester =
                        userService.getById(request.getRequesterId());
                dto.setRequesterFullName(requester.getFullName());
            }

            /* if no resolverId then no bother */
            if (request.getResolverId() != null) {
                if (options.contains(RESOLVER)) {
                    UserReadDTO resolverDTO = userService.getDTOById(request.getResolverId(), options);
                    if (resolverDTO != null) {
                        dto.setResolver(resolverDTO);
                    }
                }
                if (options.contains(RESOLVER_USERNAME)) {
                    User resolver = userService.getById(request.getResolverId());
                    if (resolver != null) {
                        dto.setResolverUsername(resolver.getUsername());
                    }
                }
                if (options.contains(RESOLVER_FULL_NAME)) {
                    User resolver = userService.getById(request.getResolverId());
                    if (resolver != null) {
                        dto.setResolverFullName(resolver.getFullName());
                    }
                }
            }
        }

        return dto;
    }

    @Override
    public List<RequestReadDTO> wrapListDTO(
            Collection<Request> requestCollection, Collection<DtoOption> options) throws Exception {
        if (requestCollection == null) { return null; }

        List<RequestReadDTO> dtoList = new ArrayList<>();
        RequestReadDTO dto;

        Long clazzId;
        Long requesterId;
        Long resolverId;

        Map<Long, ClazzReadDTO> clazzIdClazzDTOMap = new HashMap<>();
        Map<Long, String> clazzIdClazzNameMap = new HashMap<>();
        Map<Long, String> clazzIdClazzAliasMap = new HashMap<>();

        Map<Long, UserReadDTO> requesterIdRequesterDTOMap = new HashMap<>();
        Map<Long, String> requesterIdRequesterUsernameMap = new HashMap<>();
        Map<Long, String> requesterIdRequesterFullNameMap = new HashMap<>();

        Map<Long, UserReadDTO> resolverIdResolverDTOMap = new HashMap<>();
        Map<Long, String> resolverIdResolverUsernameMap = new HashMap<>();
        Map<Long, String> resolverIdResolverFullNameMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> clazzIdSet = new HashSet<>();
            Set<Long> requesterIdSet = new HashSet<>();
            Set<Long> resolverIdSet = new HashSet<>();

            for (Request request : requestCollection) {
                clazzIdSet.add(request.getClazzId());

                requesterIdSet.add(request.getRequesterId());

                resolverId = request.getResolverId();
                if (resolverId != null) {
                    resolverIdSet.add(resolverId);
                }
            }

            if (options.contains(CLAZZ)) {
                clazzIdClazzDTOMap = clazzService.mapIdDTOByIdIn(
                        clazzIdSet,
                        List.of(Status.DELETED),
                        false,
                        options);
            }
            if (options.contains(CLAZZ_NAME)) {
                clazzIdClazzNameMap = clazzService.mapIdClazzNameByIdIn(
                        clazzIdSet,
                        List.of(Status.DELETED),
                        false);
            }
            if (options.contains(CLAZZ_ALIAS)) {
                clazzIdClazzAliasMap = clazzService.mapIdClazzNameByIdIn(
                        clazzIdSet,
                        List.of(Status.DELETED),
                        false);
            }

            if (options.contains(REQUESTER)) {
                requesterIdRequesterDTOMap = userService.mapIdDTOByIdIn(requesterIdSet, options);
            }
            if (options.contains(REQUESTER_USERNAME)) {
                requesterIdRequesterUsernameMap = userService.mapIdFullNameByIdIn(requesterIdSet);
            }
            if (options.contains(REQUESTER_FULL_NAME)) {
                requesterIdRequesterFullNameMap = userService.mapIdFullNameByIdIn(requesterIdSet);
            }

            /* if no resolverId then no bother */
            if (!requesterIdSet.isEmpty()) {
                if (options.contains(RESOLVER)) {
                    resolverIdResolverDTOMap = userService.mapIdDTOByIdIn(resolverIdSet, options);
                }
                if (options.contains(RESOLVER_USERNAME)) {
                    resolverIdResolverUsernameMap = userService.mapIdFullNameByIdIn(resolverIdSet);

                }
                if (options.contains(RESOLVER_FULL_NAME)) {
                    resolverIdResolverFullNameMap = userService.mapIdFullNameByIdIn(resolverIdSet);

                }
            }
        }

        for (Request request : requestCollection) {
            dto = mapper.map(request, RequestReadDTO.class);

            /* Add dependency */
            clazzId = request.getClazzId();
            dto.setClazz(clazzIdClazzDTOMap.get(clazzId));
            dto.setClazzName(clazzIdClazzNameMap.get(clazzId));
            dto.setClazzAlias(clazzIdClazzAliasMap.get(clazzId));

            requesterId = request.getRequesterId();
            dto.setRequester(requesterIdRequesterDTOMap.get(requesterId));
            dto.setRequesterUsername(requesterIdRequesterUsernameMap.get(requesterId));
            dto.setRequesterFullName(requesterIdRequesterFullNameMap.get(requesterId));

            resolverId = request.getResolverId();
            if (resolverId != null) {
                dto.setResolver(resolverIdResolverDTOMap.get(resolverId));
                dto.setResolverUsername(resolverIdResolverUsernameMap.get(resolverId));
                dto.setResolverFullName(resolverIdResolverFullNameMap.get(resolverId));
            }

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public Page<RequestReadDTO> wrapPageDTO(
            Page<Request> requestPage, Collection<DtoOption> options) throws Exception {
        if (requestPage == null) { return null; }

        return new PageImpl<>(
                wrapListDTO(requestPage.getContent(), options),
                requestPage.getPageable(),
                requestPage.getTotalPages());
    }
}
