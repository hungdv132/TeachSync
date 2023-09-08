package com.teachsync.services.center;

import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.address.AddressReadDTO;
import com.teachsync.dtos.center.CenterCreateDTO;
import com.teachsync.dtos.center.CenterReadDTO;
import com.teachsync.dtos.center.CenterUpdateDTO;
import com.teachsync.dtos.room.RoomReadDTO;
import com.teachsync.entities.BaseEntity;
import com.teachsync.entities.Center;
import com.teachsync.repositories.CenterRepository;
import com.teachsync.services.address.AddressService;
import com.teachsync.services.room.RoomService;
import com.teachsync.services.staff.StaffService;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.Status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CenterServiceImpl implements CenterService {
    @Autowired
    private CenterRepository centerRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private AddressService addressService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private StaffService staffService;


    /* =================================================== CREATE =================================================== */
    @Override
    public Center createCenter(Center center) throws Exception {
        /* Validate input */

        /* Check FK */
        /* No FK */

        /* Insert to DB */
        center = centerRepository.saveAndFlush(center);

        return center;
    }
    @Override
    public CenterReadDTO createCenterByDTO(CenterCreateDTO createDTO) throws Exception {
        Center center = mapper.map(createDTO,Center.class);

        center = createCenter(center);

        /* create dependencies */


        return wrapDTO(center,List.of(DtoOption.ROOM_LIST));
    }


    /* =================================================== READ ===================================================== */
    @Override
    public List<Center> getAll() throws Exception {
        List<Center> centerList =
                centerRepository.findAllByStatusNot(Status.DELETED);

        if (centerList.isEmpty()) {
            return null; }

        return centerList;
    }
    @Override
    public List<CenterReadDTO> getAllDTO(Collection<DtoOption> options) throws Exception {
        List<Center> centerList = getAll();

        if (centerList == null) {
            return null; }

        return wrapListDTO(centerList, options);
    }
    @Override
    public Map<Long, CenterReadDTO> mapIdDTO(Collection<DtoOption> options) throws Exception {
        List<CenterReadDTO> centerDTOList = getAllDTO(options);

        if (centerDTOList == null) {
            return new HashMap<>(); }

        return centerDTOList.stream()
                .collect(Collectors.toMap(BaseReadDTO::getId, Function.identity()));
    }

    /* id */
    @Override
    public Center getById(Long id) throws Exception {
        return centerRepository
                .findByIdAndStatusNot(id, Status.DELETED)
                .orElse(null);
    }
    @Override
    public CenterReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception {
        Center center = getById(id);

        if (center == null) {
            return null; }

        return wrapDTO(center, options);
    }

    @Override
    public List<Center> getAllByIdIn(Collection<Long> idCollection) throws Exception {
        List<Center> centerList =
                centerRepository.findAllByIdInAndStatusNot(idCollection, Status.DELETED);

        if (centerList.isEmpty()) {
            return null; }

        return centerList;
    }
    @Override
    public Map<Long, String> mapIdCenterNameByIdIn(Collection<Long> idCollection) throws Exception {
        List<Center> centerList = getAllByIdIn(idCollection);

        if (centerList == null) {
            return new HashMap<>(); }

        return centerList.stream()
                .collect(Collectors.toMap(BaseEntity::getId, Center::getCenterName));
    }
    @Override
    public List<CenterReadDTO> getAllDTOByIdIn(
            Collection<Long> idCollection, Collection<DtoOption> options) throws Exception {
        List<Center> centerList = getAllByIdIn(idCollection);

        if (centerList == null) {
            return null; }

        return wrapListDTO(centerList, options);
    }
    @Override
    public Map<Long, CenterReadDTO> mapIdDTOByIdIn(
            Collection<Long> idCollection, Collection<DtoOption> options) throws Exception {
        List<CenterReadDTO> centerList = getAllDTOByIdIn(idCollection, options);

        if (centerList == null) {
            return new HashMap<>(); }

        return centerList.stream()
                .collect(Collectors.toMap(BaseReadDTO::getId, Function.identity()));
    }


    /* =================================================== UPDATE =================================================== */
    @Override
    public Center updateCenter(Center center) throws Exception {
        Center oldCenter = getById(center.getId());
        if(oldCenter == null){
            throw new IllegalArgumentException("Loi update - khong tim thay center id");
        }
        center.setCreatedAt(oldCenter.getCreatedAt());
        center.setCreatedBy(oldCenter.getCreatedBy());

        /* Validate input */

        /* Check FK */
        /* No FK */

        /* Update to DB */
        center = centerRepository.saveAndFlush(center);

        return center;
    }

    @Override
    public CenterReadDTO updateCenterByDTO(CenterUpdateDTO updateDTO) throws Exception {
        Center center = mapper.map(updateDTO,Center.class);

        center = updateCenter(center);

        /* create dependencies */

        /* update dependencies */

        return wrapDTO(center,List.of(DtoOption.ROOM_LIST));
    }

    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    @Override
    public CenterReadDTO wrapDTO(Center center, Collection<DtoOption> options) throws Exception {
        CenterReadDTO dto = mapper.map(center, CenterReadDTO.class);

        if (options != null && !options.isEmpty()) {
            if (options.contains(DtoOption.ROOM_LIST)) {
                List<RoomReadDTO> roomList = roomService.getAllDTOByCenterId(center.getId(), options);
                dto.setRoomList(roomList);
            }

            if (options.contains(DtoOption.STAFF_LIST)) {
                /* TODO: */
            }

            if (options.contains(DtoOption.ADDRESS)) {
                AddressReadDTO addressReadDTO = addressService.getDTOById(center.getAddressId(), options);
                dto.setAddress(addressReadDTO);
            }
        }

        return dto;
    }
    @Override
    public List<CenterReadDTO> wrapListDTO(Collection<Center> centerCollection, Collection<DtoOption> options) throws Exception {
        List<CenterReadDTO> dtoList = new ArrayList<>();

        CenterReadDTO dto;

        Map<Long, List<RoomReadDTO>> centerIdRoomListMap = new HashMap<>();
//        Map<Long, List<CenterStaffReadDTO>> centerIdStaffListMap = new HashMap<>();
        Map<Long, AddressReadDTO> addressIdAddressDTOMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> centerIdSet = new HashSet<>();
            Set<Long> addressIdSet = new HashSet<>();

            for (Center center : centerCollection) {
                centerIdSet.add(center.getId());
                addressIdSet.add(center.getAddressId());
            }

            if (options.contains(DtoOption.ROOM_LIST)) {
//                centerIdRoomListMap = roomService.mapCenterIdListRoomDTOByCenterIdIn(centerIdSet, options);
            }

            if (options.contains(DtoOption.STAFF_LIST)) {
                /* TODO: */
            }

            if (options.contains(DtoOption.ADDRESS)) {
                addressIdAddressDTOMap = addressService.mapIdDTOByIdIn(addressIdSet, options);
            }
        }

        for (Center center : centerCollection) {
            dto = mapper.map(center, CenterReadDTO.class);

            /* Add Dependency */
//            dto.setRoomList(centerIdRoomListMap.get(center.getId()));

//            dto.setStaffList(centerIdStaffListMap.get(center.getId()));

            dto.setAddress(addressIdAddressDTOMap.get(center.getAddressId()));
        
            dtoList.add(dto);
        }
    
        return dtoList;
    }
    @Override
    public Page<CenterReadDTO> wrapPageDTO(Page<Center> centerPage, Collection<DtoOption> options) throws Exception {
        return new PageImpl<>(
                wrapListDTO(centerPage.getContent(), options),
                centerPage.getPageable(),
                centerPage.getTotalPages());
    }
}
