package com.teachsync.services.session;

import com.teachsync.dtos.session.SessionCreateDTO;
import com.teachsync.dtos.session.SessionReadDTO;
import com.teachsync.dtos.session.SessionUpdateDTO;
import com.teachsync.entities.Session;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface SessionService {
    /* =================================================== CREATE =================================================== */
    Session createSession(Session session) throws Exception;
    SessionReadDTO createSessionByDTO(SessionCreateDTO createDTO) throws Exception;

    List<Session> createBulkSession(Collection<Session> sessionCollection) throws Exception;
    List<SessionReadDTO> createBulkSessionByDTO(Collection<SessionCreateDTO> createDTOCollection) throws Exception;

    /* =================================================== READ ===================================================== */
    /* id */
    Session getById(Long id) throws Exception;
    SessionReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception;

    List<Session> getAllByIdIn(Collection<Long> idCollection) throws Exception;
    Map<Long, Session> mapIdSessionByIdIn(Collection<Long> idCollection) throws Exception;
    List<SessionReadDTO> getAllDTOByIdIn(Collection<Long> idCollection, Collection<DtoOption> options) throws Exception;

    /* scheduleId */
    List<Session> getAllByScheduleId(Long scheduleId) throws Exception;
    List<SessionReadDTO> getAllDTOByScheduleId(Long scheduleId, Collection<DtoOption> options) throws Exception;

    List<Session> getAllByScheduleIdAndInRange(Long scheduleId, LocalDateTime from, LocalDateTime to) throws Exception;
    List<SessionReadDTO> getAllDTOByScheduleIdAndInRange(
            Long scheduleId, LocalDateTime from, LocalDateTime to, Collection<DtoOption> options) throws Exception;

    List<Session> getAllByScheduleIdInAndInRange(
            Collection<Long> scheduleIds, LocalDateTime from, LocalDateTime to) throws Exception;
    List<SessionReadDTO> getAllDTOByScheduleIdInAndInRange(
            Collection<Long> scheduleIds, LocalDateTime from, LocalDateTime to, Collection<DtoOption> options) throws Exception;

    List<Session> getAllByScheduleIdAndAfter(Long scheduleId, LocalDateTime from) throws Exception;
    List<SessionReadDTO> getAllDTOByScheduleIdAndAfter(
            Long scheduleId, LocalDateTime from, Collection<DtoOption> options) throws Exception;

    /* roomId */
    List<Session> getAllByRoomId(Long roomId) throws Exception;
    List<SessionReadDTO> getAllDTOByRoomId(Long roomId, Collection<DtoOption> options) throws Exception;

    List<Session> getAllByRoomIdAndInRange(Long roomId, LocalDateTime from, LocalDateTime to) throws Exception;
    List<SessionReadDTO> getAllDTOByRoomIdAndInRange(
            Long roomId, LocalDateTime from, LocalDateTime to, Collection<DtoOption> options) throws Exception;

    /* staffId */
    List<Session> getAllByStaffId(Long staffId) throws Exception;
    List<SessionReadDTO> getAllDTOByStaffId(Long staffId, Collection<DtoOption> options) throws Exception;

    List<Session> getAllByStaffIdAndInRange(Long staffId, LocalDateTime from, LocalDateTime to) throws Exception;
    List<SessionReadDTO> getAllDTOByStaffIdAndInRange(
            Long staffId, LocalDateTime from, LocalDateTime to, Collection<DtoOption> options) throws Exception;


    /* =================================================== UPDATE =================================================== */
    Session updateSession(Session session) throws Exception;
    SessionReadDTO updateSessionByDTO(SessionUpdateDTO updateDTO) throws Exception;

    List<Session> updateBulkSession(Collection<Session> sessionCollection) throws Exception;
    List<SessionReadDTO> updateBulkSessionByDTO(Collection<SessionUpdateDTO> updateDTOCollection) throws Exception;


    /* =================================================== DELETE =================================================== */
    Boolean deleteSession(Long id) throws Exception;
    Boolean deleteAllByIdIn(Collection<Long> idCollection) throws Exception;

    Boolean deleteAllByScheduleId(Long scheduleId) throws Exception;
    Boolean deleteAllByScheduleIdAndAfter(Long scheduleId, LocalDateTime from) throws Exception;


    /* =================================================== WRAPPER ================================================== */
    SessionReadDTO wrapDTO(Session session, Collection<DtoOption> options) throws Exception;
    List<SessionReadDTO> wrapListDTO(Collection<Session> sessionCollection, Collection<DtoOption> options) throws Exception;
    Page<SessionReadDTO> wrapPageDTO(Page<Session> sessionPage, Collection<DtoOption> options) throws Exception;
}
