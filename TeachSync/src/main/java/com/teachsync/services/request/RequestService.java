package com.teachsync.services.request;

import com.teachsync.dtos.request.RequestReadDTO;
import com.teachsync.entities.Request;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

public interface RequestService {
    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    RequestReadDTO wrapDTO(Request request, Collection<DtoOption> options) throws Exception;
    List<RequestReadDTO> wrapListDTO(Collection<Request> requestCollection, Collection<DtoOption> options) throws Exception;
    Page<RequestReadDTO> wrapPageDTO(Page<Request> requestPage, Collection<DtoOption> options) throws Exception;
}
