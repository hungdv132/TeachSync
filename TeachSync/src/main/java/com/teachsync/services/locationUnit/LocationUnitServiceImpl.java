package com.teachsync.services.locationUnit;

import com.teachsync.dtos.locationUnit.LocationUnitReadDTO;
import com.teachsync.entities.LocationUnit;
import com.teachsync.repositories.LocationUnitRepository;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.Status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class LocationUnitServiceImpl implements LocationUnitService {
    @Autowired
    private LocationUnitRepository locationUnitRepository;

    @Autowired
    private ModelMapper mapper;


    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */
    /* id */
    @Override
    public LocationUnit getById(Long id) throws Exception {
        return locationUnitRepository.findByIdAndStatusNot(id, Status.DELETED).orElse(null);
    }

    /* parentId (id) */
    @Override
    public List<LocationUnit> getAllByParentId(Long parentId) throws Exception {
        List<LocationUnit> listLocationUnit = locationUnitRepository.findAllByParentIdAndStatusNot(parentId,Status.DELETED);
        if(listLocationUnit.isEmpty()){
            return null;
        }
        return listLocationUnit;
    }


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    @Override
    public LocationUnitReadDTO wrapDTO(LocationUnit locationUnit, Collection<DtoOption> options) throws Exception {
        LocationUnitReadDTO dto = mapper.map(locationUnit, LocationUnitReadDTO.class);

        /* Add dependency */
        /* TODO:
        if (options != null && !options.isEmpty()) {
            if (options.contains(DtoOption.FK)) {
                FkReadDTO fkDTO = fkService.getDTOById(locationUnit.getFkId());
                dto.setFk(fkDTO);
            }

            if (options.contains(DtoOption.FK_LIST)) {
                List<FkReadDTO> fkDTOList = fkService.getAllDTOById(locationUnit.getFkId());
                dto.setFkList(fkDTOList);
            }
        }
        */

        return dto;
    }
    @Override
    public List<LocationUnitReadDTO> wrapListDTO(Collection<LocationUnit> locationUnitCollection, Collection<DtoOption> options) throws Exception {
        List<LocationUnitReadDTO> dtoList = new ArrayList<>();
        LocationUnitReadDTO dto;

        /* TODO:
        Map<Long, FkReadDTO> fkIdFkDTOMap = new HashMap<>();
        Map<Long, List<FkReadDTO>> fkIdFkDTOListMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> fkIdSet = new HashSet<>();

            for (LocationUnit locationUnit : locationUnitCollection) {
                fkIdSet.add(locationUnit.getFkId());
            }

            if (options.contains(DtoOption.FK)) {
                fkIdFkDTOMap = fkService.mapIdDTOByIdIn(fkIdSet);
            }

            if (options.contains(DtoOption.FK_LIST)) {
                fkIdFkDTOListMap = fkService.mapIdListDTOByIdIn(fkIdSet);
            }
        }
        */

        for (LocationUnit locationUnit : locationUnitCollection) {
            dto = mapper.map(locationUnit, LocationUnitReadDTO.class);

            /* Add dependency */
            /*
            dto.setFk(fkIdFkDTOMap.get(locationUnit.getFkId()));

            dto.setFkList(fkIdFkDTOListMap.get(locationUnit.getFkId()));
            */

            dtoList.add(dto);
        }

        return dtoList;
    }
    @Override
    public Page<LocationUnitReadDTO> wrapPageDTO(Page<LocationUnit> locationUnitPage, Collection<DtoOption> options) throws Exception {
        return new PageImpl<>(
                wrapListDTO(locationUnitPage.getContent(), options),
                locationUnitPage.getPageable(),
                locationUnitPage.getTotalPages());
    }
}
