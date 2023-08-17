package com.teachsync.services.request;

import com.teachsync.dtos.request.RequestCreateDTO;
import com.teachsync.dtos.request.RequestReadDTO;
import com.teachsync.dtos.request.RequestUpdateDTO;
import com.teachsync.entities.Request;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface RequestService {
    /* =================================================== CREATE =================================================== */
    Request createRequest(Request request) throws Exception;
    RequestReadDTO createRequestByDTO(RequestCreateDTO createDTO) throws Exception;


    /* =================================================== READ ===================================================== */
    List<Request> getAll() throws Exception;
    List<RequestReadDTO> getAllDTO(Collection<DtoOption> options) throws Exception;

    Page<Request> getPageAll(Pageable pageable) throws Exception;
    Page<RequestReadDTO> getPageAllDTO(Pageable pageable, Collection<DtoOption> options) throws Exception;

    /* id */
    Request getById(Long id) throws Exception;
    RequestReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception;

    List<Request> getAllByIdIn(Collection<Long> idCollection) throws Exception;
    List<RequestReadDTO> getAllDTOByIdIn(Collection<Long> idCollection, Collection<DtoOption> options) throws Exception;

    /* requesterId (userId) */
    List<Request> getAllByRequesterId(Long requesterId) throws Exception;
    List<RequestReadDTO> getAllDTOByRequesterId(Long requesterId, Collection<DtoOption> options) throws Exception;

    Page<Request> getPageAllByRequesterId(Pageable pageable, Long requesterId) throws Exception;
    Page<RequestReadDTO> getPageAllDTOByRequesterId(
            Pageable pageable, Long requesterId, Collection<DtoOption> options) throws Exception;

    List<Request> getAllByRequesterIdIn(Collection<Long> requesterIdCollection) throws Exception;
    List<RequestReadDTO> getAllDTOByRequesterIdIn(
            Collection<Long> requesterIdCollection, Collection<DtoOption> options) throws Exception;

    /* clazzId */
    List<Request> getAllByClazzId(Long clazzId) throws Exception;
    List<RequestReadDTO> getAllDTOByClazzId(Long clazzId, Collection<DtoOption> options) throws Exception;

    Page<Request> getPageAllByClazzId(Pageable pageable, Long clazzId) throws Exception;
    Page<RequestReadDTO> getPageAllDTOByClazzId(
            Pageable pageable, Long clazzId, Collection<DtoOption> options) throws Exception;
    
    List<Request> getAllByClazzIdIn(Collection<Long> clazzIdCollection) throws Exception;
    List<RequestReadDTO> getAllDTOByClazzIdIn(
            Collection<Long> clazzIdCollection, Collection<DtoOption> options) throws Exception;


    /* =================================================== UPDATE =================================================== */
    Request updateRequest(Request request) throws Exception;
    RequestReadDTO updateRequestByDTO(RequestUpdateDTO updateDTO) throws Exception;


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    RequestReadDTO wrapDTO(Request request, Collection<DtoOption> options) throws Exception;
    List<RequestReadDTO> wrapListDTO(Collection<Request> requestCollection, Collection<DtoOption> options) throws Exception;
    Page<RequestReadDTO> wrapPageDTO(Page<Request> requestPage, Collection<DtoOption> options) throws Exception;
}
