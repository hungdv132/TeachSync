package com.teachsync.services.locationUnit;

import com.teachsync.dtos.locationUnit.LocationUnitReadDTO;
import com.teachsync.entities.LocationUnit;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface LocationUnitService {
    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */
    /* id */
    LocationUnit getById(Long id) throws Exception;
    Map<Integer, Long> mapLevelUnitIdByBottomChildId(
            Long bottomChildId, Map<Integer, Long> levelUnitIdMap) throws Exception;

    List<LocationUnit> getAllByLevel(Integer level) throws Exception;

    /* parentId (id) */
    List<LocationUnit> getAllByParentId(Long parentId) throws Exception;


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    LocationUnitReadDTO wrapDTO(LocationUnit locationUnit, Collection<DtoOption> options) throws Exception;
    List<LocationUnitReadDTO> wrapListDTO(Collection<LocationUnit> locationUnitCollection, Collection<DtoOption> options) throws Exception;
    Page<LocationUnitReadDTO> wrapPageDTO(Page<LocationUnit> locationUnitPage, Collection<DtoOption> options) throws Exception;
}
