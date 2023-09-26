package com.teachsync.services.room;

import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.center.CenterReadDTO;
import com.teachsync.dtos.room.RoomReadDTO;
import com.teachsync.entities.BaseEntity;
import com.teachsync.entities.Room;
import com.teachsync.repositories.RoomRepository;
import com.teachsync.services.center.CenterService;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.Status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Lazy
    @Autowired
    private CenterService centerService;

    @Autowired
    private ModelMapper mapper;



    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */
    /* id */
    @Override
    public Boolean existsById(Long id) throws Exception {
        return roomRepository
                .existsByIdAndStatusNot(id, Status.DELETED);
    }
    @Override
    public Room getById(Long id) throws Exception {
        return roomRepository
                .findByIdAndStatusNot(id, Status.DELETED)
                .orElse(null);
    }
    @Override
    public RoomReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception {
        Room room = getById(id);

        if (room == null) {
            return null; }

        return wrapDTO(room, options);
    }

    @Override
    public Boolean existsAllByIdIn(Collection<Long> idCollection) throws Exception {
        return roomRepository
                .existsAllByIdInAndStatusNot(idCollection, Status.DELETED);
    }
    @Override
    public List<Room> getAllByIdIn(Collection<Long> idCollection) throws Exception {
        List<Room> roomList =
                roomRepository.findAllByIdInAndStatusNot(idCollection, Status.DELETED);

        if (roomList.isEmpty()) {
            return null; }

        return roomList;
    }
    @Override
    public Map<Long, String> mapRoomIdRoomNameByIdIn(Collection<Long> idCollection) throws Exception {
        List<Room> roomList = getAllByIdIn(idCollection);

        if (roomList == null) {
            return new HashMap<>(); }

        return roomList.stream()
                .collect(Collectors.toMap(BaseEntity::getId, Room::getRoomName));
    }
    @Override
    public List<RoomReadDTO> getAllDTOByIdIn(Collection<Long> idCollection, Collection<DtoOption> options) throws Exception {
        List<Room> roomList = getAllByIdIn(idCollection);

        if (roomList == null) {
            return null; }

        return wrapListDTO(roomList, options);
    }
    @Override
    public Map<Long, RoomReadDTO> mapIdDTOByIdIn(Collection<Long> idCollection, Collection<DtoOption> options) throws Exception {
        List<RoomReadDTO> roomList = getAllDTOByIdIn(idCollection, options);

        if (roomList == null) {
            return new HashMap<>(); }

        return roomList.stream()
                .collect(Collectors.toMap(BaseReadDTO::getId, Function.identity()));
    }

    /* centerId */
    @Override
    public List<Room> getAllByCenterId(Long centerId) throws Exception {
        List<Room> roomList = roomRepository.getAllByCenterIdAndStatusNot(centerId, Status.DELETED);
        if(roomList.isEmpty()){
            return null;
        }
        return roomList;
    }
    @Override
    public List<RoomReadDTO> getAllDTOByCenterId(Long centerId, Collection<DtoOption> options) throws Exception {
        List<Room> roomList = getAllByCenterId(centerId);
        if (roomList == null){
            return null;
        }
        return wrapListDTO(roomList,options);
    }

    @Override
    public List<Room> getAllByCenterIdIn(Collection<Long> centerIdCollection) throws Exception {
        List<Room> roomList = roomRepository.getAllByCenterIdInAndStatusNot(centerIdCollection, Status.DELETED);
        if(roomList.isEmpty()){
            return null;
        }
        return roomList;
    }
    @Override
    public List<RoomReadDTO> getAllDTOByCenterIdIn(
            Collection<Long> centerIdCollection, Collection<DtoOption> options) throws Exception {
        List<Room> roomList = getAllByCenterIdIn(centerIdCollection);
        if (roomList == null){
            return null;
        }
        return wrapListDTO(roomList,options);
    }


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    @Override
    public RoomReadDTO wrapDTO(Room room, Collection<DtoOption> options) throws Exception {
        RoomReadDTO dto = mapper.map(room, RoomReadDTO.class);

        /* Add dependency */
        if (!ObjectUtils.isEmpty(options)) {
            if (options.contains(DtoOption.CENTER)) {
                CenterReadDTO center = centerService.getDTOById(room.getCenterId(), options);
                dto.setCenter(center);
            }
        }

        return dto;
    }
    @Override
    public List<RoomReadDTO> wrapListDTO(Collection<Room> roomCollection, Collection<DtoOption> options) throws Exception {
        List<RoomReadDTO> dtoList = new ArrayList<>();
        RoomReadDTO dto;

        Map<Long, CenterReadDTO> centerIdCenterDTOMap = new HashMap<>();

        if (!ObjectUtils.isEmpty(options)) {
            Set<Long> centerIdSet = new HashSet<>();

            for (Room room: roomCollection){
                centerIdSet.add(room.getCenterId());
            }

            if (options.contains(DtoOption.CENTER)) {
                centerIdCenterDTOMap = centerService.mapIdDTOByIdIn(centerIdSet, options);
            }
        }

        for (Room room : roomCollection){
            dto = mapper.map(room, RoomReadDTO.class);
            dto.setCenter(centerIdCenterDTOMap.get(room.getCenterId()));
            dtoList.add(dto);
        }

        return dtoList;
    }
    @Override
    public Page<RoomReadDTO> wrapPageDTO(Page<Room> roomPage, Collection<DtoOption> options) throws Exception {
        return new PageImpl<>(
                wrapListDTO(roomPage.getContent(), options),
                roomPage.getPageable(),
                roomPage.getTotalPages());
    }
}
