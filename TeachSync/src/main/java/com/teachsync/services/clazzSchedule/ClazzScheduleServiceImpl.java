package com.teachsync.services.clazzSchedule;

import com.teachsync.dtos.clazz.ClazzReadDTO;
import com.teachsync.dtos.clazzSchedule.ClazzScheduleCreateDTO;
import com.teachsync.dtos.clazzSchedule.ClazzScheduleReadDTO;
import com.teachsync.dtos.clazzSchedule.ClazzScheduleUpdateDTO;
import com.teachsync.entities.Clazz;
import com.teachsync.entities.ClazzSchedule;
import com.teachsync.entities.Room;
import com.teachsync.repositories.ClazzRepository;
import com.teachsync.repositories.ClazzScheduleRepository;
import com.teachsync.repositories.RoomRepository;
import com.teachsync.services.clazz.ClazzService;
import com.teachsync.services.room.RoomService;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.Status;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

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
    private RoomRepository roomRepository;

    @Autowired
    private ClazzRepository clazzRepository;

    @Autowired
    private ModelMapper mapper;

    private Logger logger = LoggerFactory.getLogger(ClazzScheduleServiceImpl.class);


    /* =================================================== CREATE =================================================== */
    @Override
    @Transactional
    public String addClazzSchedule(ClazzScheduleCreateDTO createDTO) {
        try {
            ClazzSchedule clazzSchedule = mapper.map(createDTO, ClazzSchedule.class);

            Optional<Clazz> clazz =
                    clazzRepository.findByIdAndStatusNot(createDTO.getClazzId(), Status.DELETED);
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
        /* TODO: */

        /* Check FK */
        /* TODO: */

        /* Check duplicate */
        /* TODO: */

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
    /*id*/
    @Override
    public ClazzSchedule getById(Long id) throws Exception {
        return clazzScheduleRepository
                .findByIdAndStatusNot(id, Status.DELETED)
                .orElse(null);
    }

    @Override
    public ClazzScheduleReadDTO getDTOById(Long id) throws Exception {
        ClazzSchedule clazzSchedule = getById(id);

        if (clazzSchedule == null) {
            return null;
        }

        return wrapDTO(clazzSchedule, null);
    }

    @Override
    public ClazzScheduleReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception {
        ClazzSchedule clazzSchedule = getById(id);

        if (clazzSchedule == null) {
            return null;
        }

        return wrapDTO(clazzSchedule, options);
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
                    clazzRepository.findByIdAndStatusNot(updateDTO.getClazzId(), Status.DELETED);
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
        /* Validate input */
        /* TODO: */

        /* Check FK */
        /* TODO: */

        /* Check duplicate */
        /* TODO: */

        /* Create */
        clazzSchedule = clazzScheduleRepository.saveAndFlush(clazzSchedule);

        return clazzSchedule;
    }

    @Override
    public ClazzScheduleReadDTO updateClazzScheduleByDTO(ClazzScheduleUpdateDTO updateDTO) throws Exception {
        ClazzSchedule oldClazzSchedule = getById(updateDTO.getId());
        if(oldClazzSchedule == null){
            throw new IllegalArgumentException("No Clazz Schedule Found With Id: " + updateDTO.getId());
        }

        ClazzSchedule clazzSchedule = mapper.map(updateDTO, ClazzSchedule.class);

        clazzSchedule = updateClazzSchedule(clazzSchedule);

        /* TODO: update clazzSchedule */
        return wrapDTO(clazzSchedule, null);
    }



    /* =================================================== DELETE =================================================== */
    @Override
    public String deleteClazzSchedule(Long Id) {
        try{
            ClazzSchedule clazzSchedule = clazzScheduleRepository.findById(Id).orElseThrow(() -> new Exception("Không tìm thấy lớp học"));
            clazzSchedule.setStatus(Status.DELETED);
            clazzScheduleRepository.saveAndFlush(clazzSchedule);
            return "success";
        }catch (Exception e){
            logger.error("Error when deleteClazz  : " + e.getMessage());
            return "error";
        }
    }

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
                dto.setClazzName(room.getRoomName());
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
