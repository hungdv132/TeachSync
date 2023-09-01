package com.teachsync.services.room;

import com.teachsync.dtos.center.CenterReadDTO;
import com.teachsync.dtos.room.RoomReadDTO;
import com.teachsync.dtos.room.RoomUpdateDTO;
import com.teachsync.entities.BaseEntity;
import com.teachsync.entities.Center;
import com.teachsync.entities.Room;
import com.teachsync.repositories.RoomRepository;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.Status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ModelMapper mapper;



    /* =================================================== CREATE =================================================== */



    /* =================================================== READ ===================================================== */
    /* id */
    @Override
    public Room getById(Long id) throws Exception {
        return roomRepository
                .findByIdAndStatusNot(id, Status.DELETED)
                .orElse(null);
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
    public List<Room> getAllByCenterId(Long id) throws Exception {
        List<Room> roomList = roomRepository.getAllByCenterIdAndStatusNot(id, Status.DELETED);
        if(roomList.isEmpty()){
            return null;
        }
        return roomList;
    }

    @Override
    public List<RoomReadDTO> getAllDTOByCenterId(Long id, Collection<DtoOption> options) throws Exception {
        List<Room> roomList = getAllByCenterId(id);
        if (roomList.isEmpty()){
            return null;
        }
        return wrapListDTO(roomList,options);
    }


    @Override
    public Map<Long, String> mapRoomIdRoomNameByIdIn(Collection<Long> idCollection) throws Exception {
        List<Room> roomList = getAllByIdIn(idCollection);

        if (roomList.isEmpty()) {
            return null; }

        return roomList.stream()
                .collect(Collectors.toMap(BaseEntity::getId, Room::getRoomName));
    }

    @Override
    public List<RoomReadDTO> getAllDTOByCenterId(Collection<Long> idCollection, Collection<DtoOption> options) {
        return null;
    }




    /* =================================================== UPDATE =================================================== */
    @Override
    public Room updateRoom(Room room) throws Exception {
        Room oldRoom = getById(room.getId());
        if(oldRoom == null){
            throw new IllegalArgumentException("Loi update - khong tim thay center id");
        }
        room.setCreatedAt(oldRoom.getCreatedAt());
        room.setCreatedBy(oldRoom.getCreatedBy());

        /* Validate input */

        /* Check FK */
        /* No FK */

        /* Update to DB */
        room = roomRepository.saveAndFlush(room);

        return room;
    }

    @Override
    public RoomReadDTO updateRoomByDTO(RoomUpdateDTO updateDTO) throws Exception {
        Room room = mapper.map(updateDTO,Room.class);

        room = updateRoom(room);

        /* create dependencies */

        /* update dependencies */

        return wrapDTO(room,null);

    }

    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    @Override
    public RoomReadDTO wrapDTO(Room room, Collection<DtoOption> options) throws Exception {
        RoomReadDTO dto = mapper.map(room, RoomReadDTO.class);
        return dto;
    }
    @Override
    public List<RoomReadDTO> wrapListDTO(Collection<Room> roomCollection, Collection<DtoOption> options) throws Exception {
        List<RoomReadDTO> dtoList = new ArrayList<>();
        RoomReadDTO dto;
        Map<Long, CenterReadDTO> centerIdCenterDTOMap = new HashMap<>();
        if (options != null && !options.isEmpty()){
            Set<Long> centerIdSet = new HashSet<>();

            for (Room room: roomCollection){
                centerIdSet.add(room.getCenterId());
            }

            if (options.contains(DtoOption.CENTER)) {
                /* TODO: */
            }
        }

        for (Room room: roomCollection){
            dto = mapper.map(room, RoomReadDTO.class);
            dto.setCenter(centerIdCenterDTOMap.get(room.getCenterId()));
            dtoList.add(dto);
        }
        return dtoList;
    }
    @Override
    public Page<RoomReadDTO> wrapPageDTO(Page<Room> roomPage, Collection<DtoOption> options) throws Exception {
        return null;
    }
}
