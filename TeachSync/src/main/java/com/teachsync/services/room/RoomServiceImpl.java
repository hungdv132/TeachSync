package com.teachsync.services.room;

import com.teachsync.dtos.room.RoomReadDTO;
import com.teachsync.entities.BaseEntity;
import com.teachsync.entities.Room;
import com.teachsync.repositories.RoomRepository;
import com.teachsync.services.center.CenterService;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;



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
    public Map<Long, String> mapRoomIdRoomNameByIdIn(Collection<Long> idCollection) throws Exception {
        List<Room> roomList = getAllByIdIn(idCollection);

        if (roomList.isEmpty()) {
            return null; }

        return roomList.stream()
                .collect(Collectors.toMap(BaseEntity::getId, Room::getRoomName));
    }


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    @Override
    public RoomReadDTO wrapDTO(Room room, Collection<DtoOption> options) throws Exception {
        return null;
    }
    @Override
    public List<RoomReadDTO> wrapListDTO(Collection<Room> roomCollection, Collection<DtoOption> options) throws Exception {
        return null;
    }
    @Override
    public Page<RoomReadDTO> wrapPageDTO(Page<Room> roomPage, Collection<DtoOption> options) throws Exception {
        return null;
    }
}
