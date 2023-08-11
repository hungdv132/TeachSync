package com.teachsync.services.session;

import com.teachsync.dtos.session.SessionReadDTO;
import com.teachsync.entities.Session;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

public interface SessionService {
    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    SessionReadDTO wrapDTO(Session session, Collection<DtoOption> options) throws Exception;
    List<SessionReadDTO> wrapListDTO(Collection<Session> sessionCollection, Collection<DtoOption> options) throws Exception;
    Page<SessionReadDTO> wrapPageDTO(Page<Session> sessionPage, Collection<DtoOption> options) throws Exception;
}
