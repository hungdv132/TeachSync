package com.teachsync.services.clazzSchedule;

import com.teachsync.dtos.clazzSchedule.ClazzScheduleReadDTO;
import com.teachsync.entities.Clazz;
import com.teachsync.entities.ClazzSchedule;
import com.teachsync.entities.Room;
import com.teachsync.repositories.ClazzScheduleRepository;
import com.teachsync.services.clazz.ClazzService;
import com.teachsync.services.room.RoomService;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.Status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ClazzScheduleServiceImpl implements ClazzScheduleService {
    @Autowired
    private ClazzScheduleRepository clazzScheduleRepository;

    @Lazy
    @Autowired
    private ClazzService clazzService;
    @Lazy
    @Autowired
    private RoomService roomService;

    @Autowired
    private ModelMapper mapper;


    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */
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


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    @Override
    public ClazzScheduleReadDTO wrapDTO(ClazzSchedule clazzSchedule, Collection<DtoOption> options) throws Exception {
        ClazzScheduleReadDTO dto = mapper.map(clazzSchedule, ClazzScheduleReadDTO.class);

        /* Add Dependency */
        if (options != null && !options.isEmpty()) {
            if (options.contains(DtoOption.CLAZZ_NAME)) {
                Clazz clazz = clazzService.getById(dto.getClazzId());
                dto.setClazzName(clazz.getClazzName());
            }

            if (options.contains(DtoOption.ROOM_NAME)) {
                Room room = roomService.getById(dto.getRoomId());
                dto.setRoomName(room.getRoomName());
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

        if (options != null && !options.isEmpty()) {
            Set<Long> clazzIdSet = new HashSet<>();
            Set<Long> roomIdSet = new HashSet<>();

            for (ClazzSchedule clazzSchedule : clazzScheduleCollection) {
                clazzIdSet.add(clazzSchedule.getClazzId());
                roomIdSet.add(clazzSchedule.getRoomId());
            }

            if (options.contains(DtoOption.CLAZZ_NAME)) {
                clazzIdClazzNameMap = clazzService.mapClazzIdClazzNameByIdIn(clazzIdSet);
            }

            if (options.contains(DtoOption.ROOM_NAME)) {
                roomIdRoomNameMap = roomService.mapRoomIdRoomNameByIdIn(roomIdSet);
            }
        }

        for (ClazzSchedule clazzSchedule : clazzScheduleCollection) {
            dto = mapper.map(clazzSchedule, ClazzScheduleReadDTO.class);

            /* Add Dependency */
            dto.setClazzName(clazzIdClazzNameMap.get(clazzSchedule.getClazzId()));

            dto.setRoomName(roomIdRoomNameMap.get(clazzSchedule.getRoomId()));

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
