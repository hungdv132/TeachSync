package com.teachsync.services.request;

import com.teachsync.dtos.request.RequestCreateDTO;
import com.teachsync.dtos.request.RequestReadDTO;
import com.teachsync.dtos.request.RequestUpdateDTO;
import com.teachsync.entities.Request;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.RequestType;
import com.teachsync.utils.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface RequestService {
    /* =================================================== CREATE =================================================== */
    Request createRequest(
            Request request) throws Exception;
    RequestReadDTO createRequestByDTO(
            RequestCreateDTO createDTO) throws Exception;


    /* =================================================== READ ===================================================== */
    List<Request> getAll(
            Collection<Status> statuses, boolean isStatusIn) throws Exception;
    List<RequestReadDTO> getAllDTO(
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception;

    Page<Request> getPageAll(
            Pageable pageable,
            Collection<Status> statuses, boolean isStatusIn) throws Exception;
    Page<RequestReadDTO> getPageAllDTO(
            Pageable pageable,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception;

    /* id */
    Request getById(
            Long id,
            Collection<Status> statuses, boolean isStatusIn) throws Exception;
    RequestReadDTO getDTOById(
            Long id,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception;

    List<Request> getAllByIdIn(
            Collection<Long> ids,
            Collection<Status> statuses, boolean isStatusIn) throws Exception;
    List<RequestReadDTO> getAllDTOByIdIn(
            Collection<Long> ids,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception;
    Map<Long, RequestReadDTO> mapIdDTOByIdIn(
            Collection<Long> ids,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception;

    /* requesterId (User.id) */
    List<Request> getAllByRequesterId(
            Long requesterId,
            Collection<Status> statuses, boolean isStatusIn) throws Exception;
    List<RequestReadDTO> getAllDTOByRequesterId(
            Long requesterId,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception;

    Page<Request> getPageAllByRequesterId(
            Pageable pageable, Long requesterId,
            Collection<Status> statuses, boolean isStatusIn) throws Exception;
    Page<RequestReadDTO> getPageAllDTOByRequesterId(
            Pageable pageable, Long requesterId,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception;

    List<Request> getAllByRequesterIdIn(
            Collection<Long> requesterIds,
            Collection<Status> statuses, boolean isStatusIn) throws Exception;
    List<RequestReadDTO> getAllDTOByRequesterIdIn(
            Collection<Long> requesterIds,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception;

    Page<Request> getPageAllByRequesterIdIn(
            Pageable pageable, Collection<Long> requesterIds,
            Collection<Status> statuses, boolean isStatusIn) throws Exception;
    Page<RequestReadDTO> getPageAllDTOByRequesterIdIn(
            Pageable pageable, Collection<Long> requesterIds,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception;

    /* id & requesterId */
    Request getByIdAndRequesterId(
            Long id, Long requesterId,
            Collection<Status> statuses, boolean isStatusIn) throws Exception;
    RequestReadDTO getDTOByIdAndRequesterId(
            Long id, Long requesterId,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception;
    
    /* clazzId */
    List<Request> getAllByClazzId(
            Long clazzId,
            Collection<Status> statuses, boolean isStatusIn) throws Exception;
    List<RequestReadDTO> getAllDTOByClazzId(
            Long clazzId,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception;

    Page<Request> getPageAllByClazzId(
            Pageable pageable, Long clazzId,
            Collection<Status> statuses, boolean isStatusIn) throws Exception;
    Page<RequestReadDTO> getPageAllDTOByClazzId(
            Pageable pageable, Long clazzId,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception;

    List<Request> getAllByClazzIdAndRequestType(
            Long clazzId, RequestType requestType,
            Collection<Status> statuses, boolean isStatusIn) throws Exception;
    List<RequestReadDTO> getAllDTOByClazzIdAndRequestType(
            Long clazzId, RequestType requestType,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception;

    Page<Request> getPageAllByClazzIdAndRequestType(
            Pageable pageable, Long clazzId, RequestType requestType,
            Collection<Status> statuses, boolean isStatusIn) throws Exception;
    Page<RequestReadDTO> getPageAllDTOByClazzIdAndRequestType(
            Pageable pageable, Long clazzId, RequestType requestType,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception;

    List<Request> getAllByClazzIdIn(
            Collection<Long> clazzIds,
            Collection<Status> statuses, boolean isStatusIn) throws Exception;
    List<RequestReadDTO> getAllDTOByClazzIdIn(
            Collection<Long> clazzIds,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception;

    Page<Request> getPageAllByClazzIdIn(
            Pageable pageable, Collection<Long> clazzIds,
            Collection<Status> statuses, boolean isStatusIn) throws Exception;
    Page<RequestReadDTO> getPageAllDTOByClazzIdIn(
            Pageable pageable, Collection<Long> clazzIds,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception;

    /* requesterId & clazzId & requestType */
    List<Request> getAllByRequesterIdAndClazzIdAndRequestType(
            Long requesterId, Long clazzId, RequestType requestType,
            Collection<Status> statuses, boolean isStatusIn) throws Exception;
    List<RequestReadDTO> getAllDTOByRequesterIdAndClazzIdAndRequestType(
            Long requesterId, Long clazzId, RequestType requestType,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception;

    Page<Request> getPageAllByRequesterIdAndClazzIdAndRequestType(
            Pageable pageable, Long requesterId, Long clazzId, RequestType requestType,
            Collection<Status> statuses, boolean isStatusIn) throws Exception;
    Page<RequestReadDTO> getPageAllDTOByRequesterIdAndClazzIdAndRequestType(
            Pageable pageable, Long requesterId, Long clazzId, RequestType requestType,
            Collection<Status> statuses, boolean isStatusIn, Collection<DtoOption> options) throws Exception;
    

    /* =================================================== UPDATE =================================================== */
    Request updateRequest(
            Request request) throws Exception;
    RequestReadDTO updateRequestByDTO(
            RequestUpdateDTO updateDTO) throws Exception;


    /* =================================================== DELETE =================================================== */
    /** Only Requester (User) can delete THEIR Request */
    Boolean deleteRequest(
            Long id, Long requesterId) throws Exception;
    

    /* =================================================== WRAPPER ================================================== */
    RequestReadDTO wrapDTO(
            Request request, Collection<DtoOption> options) throws Exception;
    List<RequestReadDTO> wrapListDTO(
            Collection<Request> requestCollection, Collection<DtoOption> options) throws Exception;
    Page<RequestReadDTO> wrapPageDTO(
            Page<Request> requestPage, Collection<DtoOption> options) throws Exception;
}
