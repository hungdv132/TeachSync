package com.teachsync.services.room;

import com.teachsync.dtos.room.RoomReadDTO;
import com.teachsync.entities.Room;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface RoomService {
    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */
    /* id */
    Room getById(Long id) throws Exception;

    List<Room> getAllByIdIn(Collection<Long> idCollection) throws Exception;

    List<Room> getAllByCenterId(Long id) throws Exception;
    List<RoomReadDTO> getAllDTOByCenterId(Long id, Collection<DtoOption> options) throws Exception;

    Map<Long, String> mapRoomIdRoomNameByIdIn(Collection<Long> idCollection) throws Exception;

    List<RoomReadDTO> getAllDTOByCenterId(Collection<Long> idCollection, Collection<DtoOption> options);

    
    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */
    
    
    /* =================================================== WRAPPER ================================================== */
    RoomReadDTO wrapDTO(Room room, Collection<DtoOption> options) throws Exception;
    List<RoomReadDTO> wrapListDTO(Collection<Room> roomCollection, Collection<DtoOption> options) throws Exception;
    Page<RoomReadDTO> wrapPageDTO(Page<Room> roomPage, Collection<DtoOption> options) throws Exception;
}
