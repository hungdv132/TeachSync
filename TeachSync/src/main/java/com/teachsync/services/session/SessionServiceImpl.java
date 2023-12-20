package com.teachsync.services.session;

import com.teachsync.dtos.clazzSchedule.ClazzScheduleReadDTO;
import com.teachsync.dtos.room.RoomReadDTO;
import com.teachsync.dtos.session.SessionCreateDTO;
import com.teachsync.dtos.session.SessionReadDTO;
import com.teachsync.dtos.session.SessionUpdateDTO;
import com.teachsync.dtos.staff.StaffReadDTO;
import com.teachsync.entities.BaseEntity;
import com.teachsync.entities.Room;
import com.teachsync.entities.Session;
import com.teachsync.repositories.SessionRepository;
import com.teachsync.services.clazzSchedule.ClazzScheduleService;
import com.teachsync.services.room.RoomService;
import com.teachsync.services.staff.StaffService;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.Status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SessionServiceImpl implements SessionService {
    @Autowired
    private SessionRepository sessionRepository;

    @Lazy
    @Autowired
    private ClazzScheduleService clazzScheduleService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private RoomService roomService;

    @Autowired
    private ModelMapper mapper;


    /* =================================================== CREATE =================================================== */
    @Override
    public Session createSession(Session session) throws Exception {
        /* Validate input */
        if (session.getSlot() < 1 || session.getSlot() > 9) {
            throw new IllegalArgumentException("Update error. Slot must be from 1 to 9.");
        }
        if (session.getSessionStart().isAfter(session.getSessionEnd())) {
            throw new IllegalArgumentException("Invalid time. Start cannot be after end.");
        }

        /* Check FK */
        if (!clazzScheduleService.existsById(session.getScheduleId())) {
            throw new IllegalArgumentException("Invalid FK. No ClazzSchedule found with id: " + session.getScheduleId());
        }
        if (!roomService.existsById(session.getRoomId())) {
            throw new IllegalArgumentException("Invalid FK. No Room found with id: " + session.getRoomId());
        }
        if (!staffService.existsById(session.getStaffId())) {
            throw new IllegalArgumentException("Invalid FK. No Staff found with id: " + session.getStaffId());
        }

        /* Check duplicate */
        if (sessionRepository
                .existsByRoomIdAndSlotAndSessionStartAndSessionEndAndStatusNot(
                        session.getRoomId(),
                        session.getSlot(),
                        session.getSessionStart(),
                        session.getSessionEnd(),
                        Status.DELETED)) {
            throw new IllegalArgumentException(
                    "Create error. Already exist Session plan for the same room at the same time.");
        }

        return sessionRepository.saveAndFlush(session);
    }
    @Override
    public SessionReadDTO createSessionByDTO(SessionCreateDTO createDTO) throws Exception {
        Session session = mapper.map(createDTO, Session.class);

        session = createSession(session);

        return wrapDTO(session, null);
    }

    @Override
    public List<Session> createBulkSession(Collection<Session> sessionCollection) throws Exception {
        Set<Long> scheduleIdSet = new HashSet<>();
        Set<Long> staffIdSet = new HashSet<>();
        Set<Long> roomIdSet = new HashSet<>();
        List<Object[]> criteriaList = new ArrayList<>();

        for (Session session : sessionCollection) {
            /* Validate input */
            if (session.getSlot() < 1 || session.getSlot() > 8) {
                throw new IllegalArgumentException("Update error. Slot must be from 1 to 9.");
            }
            if (session.getSessionStart().isAfter(session.getSessionEnd())) {
                throw new IllegalArgumentException("Invalid time. Start cannot be after end.");
            }

            scheduleIdSet.add(session.getScheduleId());
            staffIdSet.add(session.getStaffId());
            roomIdSet.add(session.getRoomId());

            criteriaList.add(new Object[]{
                    session.getRoomId(), session.getSlot(),
                    session.getSessionStart(), session.getSessionEnd()});
        }

        /* Check FK */
        if (!clazzScheduleService.existsAllByIdIn(scheduleIdSet)) {
            throw new IllegalArgumentException("Invalid FK. No ClazzSchedule found within id given.");
        }
        if (!roomService.existsAllByIdIn(staffIdSet)) {
            throw new IllegalArgumentException("Invalid FK. No Room found within id given.");
        }
        if (!staffService.existsAllByIdIn(roomIdSet)) {
            throw new IllegalArgumentException("Invalid FK. No Staff found within id given.");
        }

        /* Check duplicate */
//        if (sessionRepository.existsAllByRoomIdAndSlotAndSessionStartAndSessionEndAndStatusNot(criteriaList, Status.DELETED)) {
//            throw new IllegalArgumentException(
//                    "Create error. Already exist 1 or more Session plan for the same room at the same time.");
//        }

        return sessionRepository.saveAllAndFlush(sessionCollection);
    }
    @Override
    public List<SessionReadDTO> createBulkSessionByDTO(Collection<SessionCreateDTO> createDTOCollection) throws Exception {
        List<Session> sessionList =
                createDTOCollection.stream()
                        .map((createDTO) -> mapper.map(createDTO, Session.class))
                        .toList();

        sessionList = createBulkSession(sessionList);

        return wrapListDTO(sessionList, null);
    }


    /* =================================================== READ ===================================================== */
    /* id */
    @Override
    public Session getById(Long id) throws Exception {
        return sessionRepository
                .findByIdAndStatusNot(id, Status.DELETED)
                .orElse(null);
    }
    @Override
    public SessionReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception {
        Session session = getById(id);
        if (session == null) {
            return null;
        }
        return wrapDTO(session, options);
    }

    @Override
    public List<Session> getAllByIdIn(Collection<Long> idCollection) throws Exception {
        List<Session> sessionList = sessionRepository.findAllByIdInAndStatusNot(idCollection, Status.DELETED);

        if (sessionList.isEmpty()) {
            return null;
        }

        return sessionList;
    }
    @Override
    public Map<Long, Session> mapIdSessionByIdIn(Collection<Long> idCollection) throws Exception {
        List<Session> sessionList = getAllByIdIn(idCollection);

        if (sessionList == null) {
            return new HashMap<>();
        }

        return sessionList.stream()
                .collect(Collectors.toMap(BaseEntity::getId, Function.identity()));
    }
    @Override
    public List<SessionReadDTO> getAllDTOByIdIn(
            Collection<Long> idCollection, Collection<DtoOption> options) throws Exception {
        List<Session> sessionList = getAllByIdIn(idCollection);

        if (sessionList == null) {
            return null;
        }

        return wrapListDTO(sessionList, options);
    }

    /* scheduleId */
    @Override
    public List<Session> getAllByScheduleId(Long scheduleId) throws Exception {
        List<Session> sessionList = sessionRepository.findAllByScheduleIdAndStatusNot(scheduleId, Status.DELETED);

        if (sessionList.isEmpty()) {
            return null;
        }

        return sessionList;
    }
    @Override
    public List<SessionReadDTO> getAllDTOByScheduleId(Long scheduleId, Collection<DtoOption> options) throws Exception {
        List<Session> sessionList = getAllByScheduleId(scheduleId);

        if (sessionList == null) {
            return null;
        }

        return wrapListDTO(sessionList, options);
    }

    @Override
    public List<Session> getAllByScheduleIdAndInRange(Long scheduleId, LocalDateTime from, LocalDateTime to) throws Exception {
        List<Session> sessionList = sessionRepository
                .findAllByScheduleIdAndSessionStartAfterAndSessionEndBeforeAndStatusNot(
                        scheduleId, from, to, Status.DELETED);

        if (sessionList.isEmpty()) {
            return null;
        }

        return sessionList;
    }
    @Override
    public List<SessionReadDTO> getAllDTOByScheduleIdAndInRange(
            Long scheduleId, LocalDateTime from, LocalDateTime to, Collection<DtoOption> options) throws Exception {
        List<Session> sessionList = getAllByScheduleIdAndInRange(scheduleId, from, to);

        if (sessionList == null) {
            return null;
        }

        return wrapListDTO(sessionList, options);
    }

    @Override
    public List<Session> getAllByScheduleIdInAndInRange(
            Collection<Long> scheduleIds, LocalDateTime from, LocalDateTime to) throws Exception {
        List<Session> sessionList = sessionRepository
                .findAllByScheduleIdInAndSessionStartAfterAndSessionEndBeforeAndStatusNot(
                        scheduleIds, from, to, Status.DELETED);

        if (sessionList.isEmpty()) {
            return null;
        }

        return sessionList;
    }
    @Override
    public List<SessionReadDTO> getAllDTOByScheduleIdInAndInRange(
            Collection<Long> scheduleIds, LocalDateTime from, LocalDateTime to, Collection<DtoOption> options) throws Exception {
        List<Session> sessionList = getAllByScheduleIdInAndInRange(scheduleIds, from, to);

        if (sessionList == null) {
            return null;
        }

        return wrapListDTO(sessionList, options);
    }

    @Override
    public List<Session> getAllByScheduleIdAndAfter(Long scheduleId, LocalDateTime from) throws Exception {
        List<Session> sessionList = sessionRepository
                .findAllByScheduleIdAndSessionStartAfterAndStatusNot(
                        scheduleId, from, Status.DELETED);

        if (sessionList.isEmpty()) {
            return null;
        }

        return sessionList;
    }
    @Override
    public List<SessionReadDTO> getAllDTOByScheduleIdAndAfter(
            Long scheduleId, LocalDateTime from, Collection<DtoOption> options) throws Exception {
        List<Session> sessionList = getAllByScheduleIdAndAfter(scheduleId, from);

        if (sessionList == null) {
            return null;
        }

        return wrapListDTO(sessionList, options);
    }

    /* roomId */
    @Override
    public List<Session> getAllByRoomId(Long roomId) throws Exception {
        List<Session> sessionList = sessionRepository.findAllByRoomIdAndStatusNot(roomId, Status.DELETED);

        if (sessionList.isEmpty()) {
            return null;
        }

        return sessionList;
    }
    @Override
    public List<SessionReadDTO> getAllDTOByRoomId(Long roomId, Collection<DtoOption> options) throws Exception {
        List<Session> sessionList = getAllByRoomId(roomId);

        if (sessionList == null) {
            return null;
        }

        return wrapListDTO(sessionList, options);
    }
    @Override
    public List<Session> getAllByRoomIdAndInRange(Long roomId, LocalDateTime from, LocalDateTime to) throws Exception {
        List<Session> sessionList = sessionRepository
                .findAllByRoomIdAndSessionStartAfterAndSessionEndBeforeAndStatusNot(
                        roomId, from, to, Status.DELETED);

        if (sessionList.isEmpty()) {
            return null;
        }

        return sessionList;
    }
    @Override
    public List<SessionReadDTO> getAllDTOByRoomIdAndInRange(
            Long roomId, LocalDateTime from, LocalDateTime to, Collection<DtoOption> options) throws Exception {
        List<Session> sessionList = getAllByRoomIdAndInRange(roomId, from, to);

        if (sessionList == null) {
            return null;
        }

        return wrapListDTO(sessionList, options);
    }

    /* staffId */
    @Override
    public List<Session> getAllByStaffId(Long staffId) throws Exception {
        List<Session> sessionList = sessionRepository.findAllByStaffIdAndStatusNot(staffId, Status.DELETED);

        if (sessionList.isEmpty()) {
            return null;
        }

        return sessionList;
    }
    @Override
    public List<SessionReadDTO> getAllDTOByStaffId(Long staffId, Collection<DtoOption> options) throws Exception {
        List<Session> sessionList = getAllByStaffId(staffId);

        if (sessionList == null) {
            return null;
        }

        return wrapListDTO(sessionList, options);
    }

    @Override
    public List<Session> getAllByStaffIdAndInRange(Long staffId, LocalDateTime from, LocalDateTime to) throws Exception {
        List<Session> sessionList = sessionRepository
                .findAllByStaffIdAndSessionStartAfterAndSessionEndBeforeAndStatusNot(
                        staffId, from, to, Status.DELETED);

        if (sessionList.isEmpty()) {
            return null;
        }

        return sessionList;
    }
    @Override
    public List<SessionReadDTO> getAllDTOByStaffIdAndInRange(
            Long staffId, LocalDateTime from, LocalDateTime to, Collection<DtoOption> options) throws Exception {
        List<Session> sessionList = getAllByStaffIdAndInRange(staffId, from, to);

        if (sessionList == null) {
            return null;
        }

        return wrapListDTO(sessionList, options);
    }


    /* =================================================== UPDATE =================================================== */
    @Override
    public Session updateSession(Session session) throws Exception {
        /* Check exists */
        Session oldSession = getById(session.getId());
        if (oldSession == null) {
            throw new IllegalArgumentException("Update Error. No Session found with id: " + session.getId());
        }
        session.setCreatedAt(oldSession.getCreatedAt());
        session.setCreatedBy(oldSession.getCreatedBy());

        /* Validate input */
        if (session.getSlot() < 1 || session.getSlot() > 9) {
            throw new IllegalArgumentException("Update error. Slot must be from 1 to 9.");
        }
        if (session.getSessionStart().isAfter(session.getSessionEnd())) {
            throw new IllegalArgumentException("Invalid time. Start cannot be after end.");
        }
        if (!oldSession.getScheduleId().equals(session.getScheduleId())) {
            throw new IllegalArgumentException("Update error. Session does not allow change of scheduleId");
        }

        /* Check FK */
        if (!clazzScheduleService.existsById(session.getScheduleId())) {
            throw new IllegalArgumentException("Invalid FK. No ClazzSchedule found with id: " + session.getScheduleId());
        }
        if (!roomService.existsById(session.getRoomId())) {
            throw new IllegalArgumentException("Invalid FK. No Room found with id: " + session.getRoomId());
        }
        if (!staffService.existsById(session.getStaffId())) {
            throw new IllegalArgumentException("Invalid FK. No Staff found with id: " + session.getStaffId());
        }

        /* Check duplicate */
        if (sessionRepository
                .existsByIdNotAndRoomIdAndSlotAndSessionStartAndSessionEndAndStatusNot(
                        session.getId(),
                        session.getRoomId(),
                        session.getSlot(),
                        session.getSessionStart(),
                        session.getSessionEnd(),
                        Status.DELETED)) {
            throw new IllegalArgumentException(
                    "Update error. Already exist Session plan for the same room at the same time.");
        }

        return sessionRepository.saveAndFlush(session);
    }
    @Override
    public SessionReadDTO updateSessionByDTO(SessionUpdateDTO updateDTO) throws Exception {
        Session session = mapper.map(updateDTO, Session.class);

        session = updateSession(session);

        /* TODO: Cascade update attendant */

        return wrapDTO(session, null);
    }

    @Override
    public List<Session> updateBulkSession(Collection<Session> sessionCollection) throws Exception {
        Set<Long> scheduleIdSet = new HashSet<>();
        Set<Long> sessionIdSet = new HashSet<>();
        Set<Long> staffIdSet = new HashSet<>();
        Set<Long> roomIdSet = new HashSet<>();
        List<Object[]> criteriaList = new ArrayList<>();

        for (Session session : sessionCollection) {
            /* Validate input */
            if (session.getSlot() < 1 || session.getSlot() > 8) {
                throw new IllegalArgumentException("Update error. Slot must be from 1 to 9.");
            }
            if (session.getSessionStart().isAfter(session.getSessionEnd())) {
                throw new IllegalArgumentException("Invalid time. Start cannot be after end.");
            }

            sessionIdSet.add(session.getId());

            scheduleIdSet.add(session.getScheduleId());
            staffIdSet.add(session.getStaffId());
            roomIdSet.add(session.getRoomId());

            criteriaList.add(new Object[]{
                    session.getRoomId(), session.getSlot(),
                    session.getSessionStart(), session.getSessionEnd()});
        }

        /* Check exists */
        Map<Long, Session> idOldSessionMap = mapIdSessionByIdIn(sessionIdSet);
        if (idOldSessionMap.size() != sessionCollection.size()) {
            throw new IllegalArgumentException("Update error. 1 or more Session not found within id given.");
        } else {
            /* Validate input */
            for (Session session : sessionCollection) {
                if (!session.getScheduleId().equals(idOldSessionMap.get(session.getId()).getScheduleId())) {
                    throw new IllegalArgumentException("Update error. Session does not allow change of scheduleId");
                }
            }
        }

        /* Check FK */
        if (!clazzScheduleService.existsAllByIdIn(scheduleIdSet)) {
            throw new IllegalArgumentException("Invalid FK. No ClazzSchedule found within id given.");
        }
        if (!roomService.existsAllByIdIn(staffIdSet)) {
            throw new IllegalArgumentException("Invalid FK. No Room found within id given.");
        }
        if (!staffService.existsAllByIdIn(roomIdSet)) {
            throw new IllegalArgumentException("Invalid FK. No Staff found within id given.");
        }

        /* Check duplicate */
        List<Long> duplicateSessionIdList =
                sessionRepository.findAllIdByRoomIdAndSlotAndSessionStartAndSessionEndAndStatusNot(criteriaList, Status.DELETED);
        /* Remove the updated Session id */
        duplicateSessionIdList.removeAll(sessionIdSet);
        if (!duplicateSessionIdList.isEmpty()) {
            throw new IllegalArgumentException(
                    "Update error. Already exist 1 or more Session plan with the same room at the same time.");
        }

        return sessionRepository.saveAllAndFlush(sessionCollection);
    }
    @Override
    public List<SessionReadDTO> updateBulkSessionByDTO(Collection<SessionUpdateDTO> updateDTOCollection) throws Exception {
        List<Session> sessionList =
                updateDTOCollection.stream()
                        .map((updateDTO) -> mapper.map(updateDTO, Session.class))
                        .toList();

        sessionList = updateBulkSession(sessionList);

        return wrapListDTO(sessionList, null);
    }


    /* =================================================== DELETE =================================================== */
    @Override
    public Boolean deleteSession(Long id) throws Exception {
        /* Check exists */
        Session session = getById(id);
        if (session == null) {
            throw new IllegalArgumentException("Delete error. No Session found with id: " + id);
        }

        /* Delete */
        session.setStatus(Status.DELETED);
        sessionRepository.saveAndFlush(session);

        /* Cascade delete */
        /* TODO: cascade attendant
        *   if cascade fail, revert delete */

        return true;
    }
    @Override
    public Boolean deleteAllByIdIn(Collection<Long> idCollection) throws Exception {
        /* Check exists */
        List<Session> sessionList = getAllByIdIn(idCollection);
        if (sessionList == null) {
            throw new IllegalArgumentException("Delete error. No Session found within id given");
        }

        return deleteAll(sessionList);
    }

    @Override
    public Boolean deleteAllByScheduleId(Long scheduleId) throws Exception {
        /* Check exists */
        List<Session> sessionList = getAllByScheduleId(scheduleId);
        if (sessionList == null) {
            throw new IllegalArgumentException("Delete error. No Session found with scheduleId: " + scheduleId);
        }

        return deleteAll(sessionList);
    }
    @Override
    public Boolean deleteAllByScheduleIdAndAfter(Long scheduleId, LocalDateTime from) throws Exception {
        /* Check exists */
        List<Session> sessionList = getAllByScheduleIdAndAfter(scheduleId, from);
        if (sessionList == null) {
//            throw new IllegalArgumentException("Delete error. No Session found with scheduleId: " + scheduleId);
            /* Không quăng lỗi vì chỉ dùng xóa Session Schedule cũ để Update */
            return true;
        }

        return deleteAll(sessionList);
    }

    private Boolean deleteAll(Collection<Session> sessionCollection) throws Exception {
        /* Delete */
        List<Session> deletedSession =
                sessionCollection.stream()
                        .peek(session -> session.setStatus(Status.DELETED))
                        .toList();

        sessionRepository.saveAllAndFlush(deletedSession);

        /* Cascade delete */
        /* TODO: cascade attendant
         *   if cascade fail, revert delete */

        return true;
    }


    /* =================================================== WRAPPER ================================================== */
    @Override
    public SessionReadDTO wrapDTO(Session session, Collection<DtoOption> options) throws Exception {
        SessionReadDTO dto = mapper.map(session, SessionReadDTO.class);

        /* Add dependency */
        if (options != null && !options.isEmpty()) {
            if (options.contains(DtoOption.CLAZZ_SCHEDULE)) {
                ClazzScheduleReadDTO schedule = clazzScheduleService.getDTOById(session.getScheduleId(), options);
                dto.setSchedule(schedule);
            }

            if (options.contains(DtoOption.ROOM_NAME)) {
                Room room = roomService.getById(session.getRoomId());
                dto.setRoomName(room.getRoomName());
            }
            if (options.contains(DtoOption.ROOM)) {
                RoomReadDTO room = roomService.getDTOById(session.getRoomId(), options);
                dto.setRoom(room);
            }

            if (options.contains(DtoOption.STAFF)) {
                StaffReadDTO staff = staffService.getDTOById(session.getStaffId(), options);
                dto.setStaff(staff);
            }
        }

        return dto;
    }

    @Override
    public List<SessionReadDTO> wrapListDTO(Collection<Session> sessionCollection, Collection<DtoOption> options) throws Exception {
        List<SessionReadDTO> dtoList = new ArrayList<>();
        SessionReadDTO dto;

        Map<Long, ClazzScheduleReadDTO> scheduleIdScheduleDTOMap = new HashMap<>();
        Map<Long, String> roomIdRoomNameMap = new HashMap<>();
        Map<Long, RoomReadDTO> roomIdRoomDTOMap = new HashMap<>();
        Map<Long, StaffReadDTO> staffIdStaffDTOMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> roomIdSet = new HashSet<>();
            Set<Long> staffIdSet = new HashSet<>();
            Set<Long> scheduleIdSet = new HashSet<>();

            for (Session session : sessionCollection) {
                roomIdSet.add(session.getRoomId());
                staffIdSet.add(session.getStaffId());
                scheduleIdSet.add(session.getScheduleId());
            }

            if (options.contains(DtoOption.CLAZZ_SCHEDULE)) {
                scheduleIdScheduleDTOMap =
                        clazzScheduleService.mapIdDTOByIdIn(scheduleIdSet, options);
            }

            if (options.contains(DtoOption.ROOM_NAME)) {
                roomIdRoomNameMap = roomService.mapRoomIdRoomNameByIdIn(roomIdSet);
            }
            if (options.contains(DtoOption.ROOM)) {
                roomIdRoomDTOMap = roomService.mapIdDTOByIdIn(roomIdSet, options);
            }

            if (options.contains(DtoOption.STAFF)) {
                staffIdStaffDTOMap = staffService.mapIdDTOByIdIn(staffIdSet, options);
            }
        }

        for (Session session : sessionCollection) {
            dto = mapper.map(session, SessionReadDTO.class);

            /* Add dependency */
            dto.setSchedule(scheduleIdScheduleDTOMap.get(session.getScheduleId()));

            dto.setRoom(roomIdRoomDTOMap.get(session.getRoomId()));
            dto.setRoomName(roomIdRoomNameMap.get(session.getRoomId()));

            dto.setStaff(staffIdStaffDTOMap.get(session.getStaffId()));

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public Page<SessionReadDTO> wrapPageDTO(Page<Session> sessionPage, Collection<DtoOption> options) throws Exception {
        return new PageImpl<>(
                wrapListDTO(sessionPage.getContent(), options),
                sessionPage.getPageable(),
                sessionPage.getTotalPages());
    }
}
