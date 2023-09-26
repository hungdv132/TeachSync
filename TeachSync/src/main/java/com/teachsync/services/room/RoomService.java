package com.teachsync.services.room;

import com.teachsync.dtos.room.RoomReadDTO;
import com.teachsync.dtos.room.RoomUpdateDTO;
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
    Boolean existsById(Long id) throws Exception;
    Room getById(Long id) throws Exception;
    RoomReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception;

    Boolean existsAllByIdIn(Collection<Long> idCollection) throws Exception;
    List<Room> getAllByIdIn(Collection<Long> idCollection) throws Exception;
    Map<Long, String> mapRoomIdRoomNameByIdIn(Collection<Long> idCollection) throws Exception;
    List<RoomReadDTO> getAllDTOByIdIn(Collection<Long> idCollection, Collection<DtoOption> options) throws Exception;
    Map<Long, RoomReadDTO> mapIdDTOByIdIn(Collection<Long> idCollection, Collection<DtoOption> options) throws Exception;

    List<Room> getAllByCenterId(Long centerId) throws Exception;
    List<RoomReadDTO> getAllDTOByCenterId(Long centerId, Collection<DtoOption> options) throws Exception;


    List<Room> getAllByCenterIdIn(Collection<Long> centerIdCollection) throws Exception;
    List<RoomReadDTO> getAllDTOByCenterIdIn(
            Collection<Long> centerIdCollection, Collection<DtoOption> options) throws Exception;

    
    /* =================================================== UPDATE =================================================== */
    Room updateRoom(Room room) throws Exception;
    RoomReadDTO updateRoomByDTO(RoomUpdateDTO updateDTO) throws Exception;

    /* =================================================== DELETE =================================================== */
    
    
    /* =================================================== WRAPPER ================================================== */
    RoomReadDTO wrapDTO(Room room, Collection<DtoOption> options) throws Exception;
    List<RoomReadDTO> wrapListDTO(Collection<Room> roomCollection, Collection<DtoOption> options) throws Exception;
    Page<RoomReadDTO> wrapPageDTO(Page<Room> roomPage, Collection<DtoOption> options) throws Exception;
}
