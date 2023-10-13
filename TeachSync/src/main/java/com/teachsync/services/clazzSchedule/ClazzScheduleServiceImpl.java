package com.teachsync.services.clazzSchedule;

import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.clazzSchedule.ClazzScheduleCreateDTO;
import com.teachsync.dtos.clazzSchedule.ClazzScheduleReadDTO;
import com.teachsync.dtos.clazzSchedule.ClazzScheduleUpdateDTO;
import com.teachsync.dtos.scheduleCategory.ScheduleCaReadDTO;
import com.teachsync.entities.*;
import com.teachsync.repositories.ClazzRepository;
import com.teachsync.repositories.ClazzScheduleRepository;
import com.teachsync.repositories.RoomRepository;
import com.teachsync.services.clazz.ClazzService;
import com.teachsync.services.room.RoomService;
import com.teachsync.services.scheduleCategory.ScheduleCateService;
import com.teachsync.services.session.SessionService;
import com.teachsync.utils.MiscUtil;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.Status;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.teachsync.utils.enums.Status.*;

@Service
public class ClazzScheduleServiceImpl implements ClazzScheduleService {
    @Autowired
    private ClazzScheduleRepository clazzScheduleRepository;

    @Lazy
    @Autowired
    private ClazzService clazzService;
    @Autowired
    private SessionService sessionService;
    @Lazy
    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ClazzRepository clazzRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private MiscUtil miscUtil;

    @Autowired
    private ScheduleCateService scheduleCateService;

    private Logger logger = LoggerFactory.getLogger(ClazzScheduleServiceImpl.class);


    /* =================================================== CREATE =================================================== */
    @Override
    @Transactional
    public String addClazzSchedule(ClazzScheduleCreateDTO createDTO) {
        try {
            ClazzSchedule clazzSchedule = mapper.map(createDTO, ClazzSchedule.class);

            Optional<Clazz> clazz =
                    clazzRepository.findByIdAndStatusNotIn(createDTO.getClazzId(),
                            List.of(Status.DELETED));
            Clazz clazz1 = clazz.orElse(null);
            clazzSchedule.setClazzId(clazz1.getId());

            Optional<Room> room =
                    roomRepository.findByIdAndStatusNot(createDTO.getRoomId(), Status.DELETED);
            Room room1 = room.orElse(null);

            clazzSchedule.setRoomId(room1.getId());

            clazzScheduleRepository.saveAndFlush(clazzSchedule);
            return "Success";

        }catch (Exception e){
            logger.error("Error when addClazzSchedule  : " + e.getMessage());
            return "error";
        }
    }

    @Override
    public ClazzSchedule createClazzSchedule(ClazzSchedule clazzSchedule) throws Exception {
        /* Validate input */
        if (clazzSchedule.getSlot() < 1 || clazzSchedule.getSlot() > 9) {
            throw new IllegalArgumentException("Update error. Slot must be from 1 to 9.");
        }
        if (clazzSchedule.getSessionStart().isAfter(clazzSchedule.getSessionEnd())) {
            throw new IllegalArgumentException("Update error. Start cannot be after end.");
        }

        /* Check FK */
        /* TODO: */

        /* Check duplicate */
        if (clazzScheduleRepository.existsByClazzIdAndStatusNot(
                clazzSchedule.getClazzId(),
                Status.DELETED)) {
            throw new IllegalArgumentException(
                    "Create error. Already exists a schedule for Clazz with id: " + clazzSchedule.getClazzId());
        }

        /* Create */
        clazzSchedule = clazzScheduleRepository.saveAndFlush(clazzSchedule);

        return clazzSchedule;
    }

    @Override
    public ClazzScheduleReadDTO createClazzScheduleByDTO(ClazzScheduleCreateDTO createDTO) throws Exception {
        ClazzSchedule clazzSchedule = mapper.map(createDTO, ClazzSchedule.class);

        clazzSchedule = createClazzSchedule(clazzSchedule);

        return wrapDTO(clazzSchedule, null);
    }


    /* =================================================== READ ===================================================== */
    @Override
    public Page<ClazzSchedule> getPageAll(Pageable paging) throws Exception {
        if (paging == null) {
            paging = miscUtil.defaultPaging();
        }

        Page<ClazzSchedule> clazzSchedulePage =
                clazzScheduleRepository.findAllByStatusNot(Status.DELETED, paging);

        if (clazzSchedulePage.isEmpty()) {
            return null;
        }

        return clazzSchedulePage;
    }
    @Override
    public Page<ClazzScheduleReadDTO> getPageAllDTO(Pageable paging, Collection<DtoOption> options) throws Exception {
        Page<ClazzSchedule> clazzSchedulePage = getPageAll(paging);

        if (clazzSchedulePage == null) {
            return null;
        }

        return wrapPageDTO(clazzSchedulePage, options);
    }

    /* id */
    @Override
    public Boolean existsById(Long id) throws Exception {
        return clazzScheduleRepository
                .existsByIdAndStatusNot(id, Status.DELETED);
    }
    @Override
    public ClazzSchedule getById(Long id) throws Exception {
        return clazzScheduleRepository
                .findByIdAndStatusNot(id, Status.DELETED)
                .orElse(null);
    }
    @Override
    public ClazzScheduleReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception {
        ClazzSchedule clazzSchedule = getById(id);

        if (clazzSchedule == null) {
            return null;
        }

        return wrapDTO(clazzSchedule, options);
    }

    @Override
    public Boolean existsAllByIdIn(Collection<Long> idCollection) throws Exception {
        return clazzScheduleRepository
                .existsByIdInAndStatusNot(idCollection, Status.DELETED);
    }
    @Override
    public List<ClazzSchedule> getAllByIdIn(Collection<Long> idCollection) throws Exception {
        List<ClazzSchedule> scheduleList =
                clazzScheduleRepository.findAllByIdInAndStatusNot(idCollection, Status.DELETED);

        if (scheduleList.isEmpty()) {
            return null;
        }

        return scheduleList;
    }
    @Override
    public List<ClazzScheduleReadDTO> getAllDTOByIdIn(
            Collection<Long> idCollection, Collection<DtoOption> options) throws Exception {
        List<ClazzSchedule> scheduleList = getAllByIdIn(idCollection);

        if (scheduleList == null) {
            return null;
        }

        return wrapListDTO(scheduleList, options);
    }
    @Override
    public Map<Long, ClazzScheduleReadDTO> mapIdDTOByIdIn(
            Collection<Long> idCollection, Collection<DtoOption> options) throws Exception {
        List<ClazzScheduleReadDTO> scheduleList = getAllDTOByIdIn(idCollection, options);

        if (scheduleList == null) {
            return new HashMap<>();
        }

        return scheduleList.stream()
                .collect(Collectors.toMap(BaseReadDTO::getId, Function.identity()));
    }

    /* clazzId */
    @Override
    public ClazzSchedule getByClazzId(Long clazzId) throws Exception {
        return clazzScheduleRepository
                .findByClazzIdAndStatusNot(clazzId, Status.DELETED)
                .orElse(null);
    }

    @Override
    public ClazzScheduleReadDTO getDTOByClazzId(Long clazzId, Collection<DtoOption> options) throws Exception {
        ClazzSchedule clazzSchedule = getByClazzId(clazzId);

        if (clazzSchedule == null) {
            return null; }

        return wrapDTO(clazzSchedule, options);
    }

    @Override
    public List<ClazzSchedule> getAllByClazzIdIn(Collection<Long> clazzIdCollection) throws Exception {
        List<ClazzSchedule> clazzScheduleList =
                clazzScheduleRepository.findAllByClazzIdInAndStatusNot(clazzIdCollection, Status.DELETED);

        if (clazzScheduleList.isEmpty()) {
            return null; }

        return clazzScheduleList;
    }
    @Override
    public List<ClazzScheduleReadDTO> getAllDTOByClazzIdIn(
            Collection<Long> clazzIdCollection, Collection<DtoOption> options) throws Exception {
        List<ClazzSchedule> clazzScheduleList = getAllByClazzIdIn(clazzIdCollection);

        if (clazzScheduleList == null) {
            return null; }

        return wrapListDTO(clazzScheduleList, options);
    }
    @Override
    public Map<Long, ClazzScheduleReadDTO> mapClazzIdDTOByClazzIdIn(
            Collection<Long> clazzIdCollection, Collection<DtoOption> options) throws Exception {
        List<ClazzScheduleReadDTO> clazzScheduleDTOList = getAllDTOByClazzIdIn(clazzIdCollection, options);

        if (clazzScheduleDTOList == null) {
            return new HashMap<>(); }

        return clazzScheduleDTOList.stream()
                .collect(Collectors.toMap(ClazzScheduleReadDTO::getClazzId, Function.identity()));
    }

    /* roomId & slot & startDate & endDate */
    @Override
    public List<ClazzSchedule> getAllByRoomIdAndScheduleCaIdAndSlotAndInRange(
            Long roomId, Long scheduleCaId, Integer slot, LocalDate from, LocalDate to) throws Exception {
        List<ClazzSchedule> clazzScheduleList =
                clazzScheduleRepository.findAllByRoomIdAndScheduleCaIdAndSlotAndInRange(
                        roomId, scheduleCaId, slot, from, to);
        if (clazzScheduleList.isEmpty()) {
            return null;
        }
        return clazzScheduleList;
    }

    /* =================================================== UPDATE =================================================== */
    @Override
    @Transactional
    public String editClazzSchedule(ClazzScheduleUpdateDTO updateDTO) {
        try {
            ClazzSchedule clazzSchedule = clazzScheduleRepository.findById(updateDTO.getId()).orElse(null);
            if (ObjectUtils.isEmpty(clazzSchedule)){
                throw new Exception();
            }
            clazzSchedule = mapper.map(updateDTO, ClazzSchedule.class);

            Optional<Clazz> clazz =
                    clazzRepository.findByIdAndStatusNotIn(updateDTO.getClazzId(),
                            List.of(Status.DELETED));
            Clazz clazz1 = clazz.orElse(null);
            clazzSchedule.setClazzId(clazz1.getId());

            Optional<Room> room =
                    roomRepository.findByIdAndStatusNot(updateDTO.getRoomId(), Status.DELETED);
            Room room1 = room.orElse(null);

            clazzSchedule.setRoomId(room1.getId());

            clazzScheduleRepository.saveAndFlush(clazzSchedule);

            return "success";
        }catch (Exception e){
            logger.error("Error when EditClazzRoom  : " + e.getMessage());
            return "error";
        }
    }

    @Override
    public ClazzSchedule updateClazzSchedule(ClazzSchedule clazzSchedule) throws Exception {
        /* Check exists */
        ClazzSchedule oldSchedule = getById(clazzSchedule.getId());
        if (oldSchedule == null) {
            throw new IllegalArgumentException("Update error. No Schedule found with id: " + clazzSchedule.getId());
        }
        clazzSchedule.setCreatedAt(oldSchedule.getCreatedAt());
        clazzSchedule.setCreatedBy(oldSchedule.getCreatedBy());

        /* Validate input */
        if (!oldSchedule.getClazzId().equals(clazzSchedule.getClazzId())) {
            throw new IllegalArgumentException("Update error. Schedule does not allow change of clazzId");
        }
        if (clazzSchedule.getSlot() < 1 || clazzSchedule.getSlot() > 9) {
            throw new IllegalArgumentException("Update error. Slot must be from 1 to 9.");
        }
        if (clazzSchedule.getSessionStart().isAfter(clazzSchedule.getSessionEnd())) {
            throw new IllegalArgumentException("Update error. Start cannot be after end.");
        }

        /* Check FK */
        /* TODO: */

        /* Check duplicate */
        ClazzSchedule duplicateSchedule = getByClazzId(clazzSchedule.getClazzId());
        if (!duplicateSchedule.getId().equals(clazzSchedule.getId())) {
            throw new IllegalArgumentException(
                    "Update error. Already exists a schedule for Clazz with id: " + clazzSchedule.getClazzId());
        }

        /* Create */
        clazzSchedule = clazzScheduleRepository.saveAndFlush(clazzSchedule);

        return clazzSchedule;
    }

    @Override
    public ClazzScheduleReadDTO updateClazzScheduleByDTO(ClazzScheduleUpdateDTO updateDTO) throws Exception {
        ClazzSchedule clazzSchedule = mapper.map(updateDTO, ClazzSchedule.class);

        clazzSchedule = updateClazzSchedule(clazzSchedule);

        /* TODO: update cascade clazzSchedule */
        return wrapDTO(clazzSchedule, null);
    }


    /* =================================================== DELETE =================================================== */
    @Override
    public Boolean deleteClazzSchedule(Long id) throws Exception {
        ClazzSchedule schedule = getById(id);

        if (schedule == null) {
            throw new IllegalArgumentException(
                    "Lỗi xóa Lịch Học. Không tìm thấy Lịch Học nào với id: " + id);
        }
        Status oldStatus = schedule.getStatus();

        /* Delete */
        schedule.setStatus(DELETED);

        /* Save to DB */
        clazzScheduleRepository.saveAndFlush(schedule);

        try {
            sessionService.deleteAllByScheduleId(id);
        } catch (IllegalArgumentException iAE) {
            /* Revert delete */
            schedule.setStatus(oldStatus);

            clazzScheduleRepository.saveAndFlush(schedule);

            throw new IllegalArgumentException(
                    "Lỗi xóa Lịch Học. Bắt nguồn từ: \n" + iAE.getMessage());
        }

        return true;
    }

    @Override
    public Boolean deleteByClazzId(Long clazzId) throws Exception {
        ClazzSchedule schedule = getByClazzId(clazzId);

        if (schedule == null) {
            throw new IllegalArgumentException(
                    "Lỗi xóa Lịch Học. Không tìm thấy Lớp Học nào với id: " + clazzId);
        }
        Status oldStatus = schedule.getStatus();

        /* Delete */
        schedule.setStatus(DELETED);

        /* Save to DB */
        clazzScheduleRepository.saveAndFlush(schedule);

        try {
            sessionService.deleteAllByScheduleId(schedule.getId());
        } catch (IllegalArgumentException iAE) {
            /* Revert delete */
            schedule.setStatus(oldStatus);

            clazzScheduleRepository.saveAndFlush(schedule);

            throw new IllegalArgumentException(
                    "Lỗi xóa Lịch Học. Bắt nguồn từ: \n" + iAE.getMessage());
        }

        return true;
    }


    /* =================================================== WRAPPER ================================================== */
    @Override
    public ClazzScheduleReadDTO wrapDTO(ClazzSchedule clazzSchedule, Collection<DtoOption> options) throws Exception {
        ClazzScheduleReadDTO dto = mapper.map(clazzSchedule, ClazzScheduleReadDTO.class);

        /* Add Dependency */
        if (!ObjectUtils.isEmpty(options)) {
            if (options.contains(DtoOption.CLAZZ_NAME)) {
                Clazz clazz = clazzService.getById(
                        dto.getClazzId(),
                        List.of(Status.DELETED),
                        false);
                dto.setClazzName(clazz.getClazzName());
            }

            if (options.contains(DtoOption.ROOM_NAME)) {
                Room room = roomService.getById(dto.getRoomId());
                dto.setRoomName(room.getRoomName());
            }

            if (options.contains(DtoOption.SCHEDULE_CAT)) {
                ScheduleCaReadDTO scheduleCategory = scheduleCateService.getDTOById(dto.getSchedulecaId());
                dto.setScheduleCategory(scheduleCategory);
            }
        }

        return dto;
    }

    @Override
    public List<ClazzScheduleReadDTO> wrapListDTO(
            Collection<ClazzSchedule> clazzScheduleCollection, Collection<DtoOption> options) throws Exception {
        List<ClazzScheduleReadDTO> dtoList = new ArrayList<>();

        ClazzScheduleReadDTO dto;

        Map<Long, String> clazzIdClazzNameMap = new HashMap<>();
        Map<Long, String> roomIdRoomNameMap = new HashMap<>();
        Map<Long, ScheduleCaReadDTO> scheduleIdScheduleDescMap = new HashMap<>();


        if (!ObjectUtils.isEmpty(options)) {
            Set<Long> clazzIdSet = new HashSet<>();
            Set<Long> roomIdSet = new HashSet<>();
            Set<Long> scheduleCategoryIdSet = new HashSet<>();

            for (ClazzSchedule clazzSchedule : clazzScheduleCollection) {
                clazzIdSet.add(clazzSchedule.getClazzId());
                roomIdSet.add(clazzSchedule.getRoomId());
                scheduleCategoryIdSet.add(clazzSchedule.getSchedulecaId());
            }

            if (options.contains(DtoOption.CLAZZ_NAME)) {
                clazzIdClazzNameMap = clazzService.mapIdClazzNameByIdIn(
                        clazzIdSet,
                        List.of(Status.DELETED),
                        false);
            }

            if (options.contains(DtoOption.ROOM_NAME)) {
                roomIdRoomNameMap = roomService.mapRoomIdRoomNameByIdIn(roomIdSet);
            }

            if (options.contains(DtoOption.SCHEDULE_CAT)) {
                scheduleIdScheduleDescMap = scheduleCateService.mapScheduleIdScheduleDescByIdIn(scheduleCategoryIdSet);
            }
        }

        for (ClazzSchedule clazzSchedule : clazzScheduleCollection) {
            dto = mapper.map(clazzSchedule, ClazzScheduleReadDTO.class);

            /* Add Dependency */
            dto.setClazzName(clazzIdClazzNameMap.get(clazzSchedule.getClazzId()));

            dto.setRoomName(roomIdRoomNameMap.get(clazzSchedule.getRoomId()));

            dto.setScheduleCategory(scheduleIdScheduleDescMap.get(clazzSchedule.getSchedulecaId()));

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public Page<ClazzScheduleReadDTO> wrapPageDTO(
            Page<ClazzSchedule> clazzSchedulePage, Collection<DtoOption> options) throws Exception {
        return new PageImpl<>(
                wrapListDTO(clazzSchedulePage.getContent(), options),
                clazzSchedulePage.getPageable(),
                clazzSchedulePage.getTotalPages());
    }
}
