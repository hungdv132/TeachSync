package com.teachsync.services.locationUnit;

import com.teachsync.entities.LocationUnit;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface LocationUnitService {
    /* =================================================== CREATE =================================================== */


    /* =================================================== READ ===================================================== */
    /* id */
    LocationUnit getById(Long id) throws Exception;
    List<LocationUnit> getAllByIdIn(Collection<Long> idCollection) throws Exception;
    Map<Integer, Long> mapLevelUnitIdByBottomChildId(
            Long bottomChildId, Map<Integer, Long> levelUnitIdMap) throws Exception;
    Map<Integer, LocationUnit> mapLevelUnitByBottomChildId(
            Long bottomChildId, Map<Integer, LocationUnit> levelUnitMap) throws Exception;

    /* parentId */
    List<LocationUnit> getAllByParentId(Long parentId) throws Exception;

    /* level */
    List<LocationUnit> getAllByLevel(Integer level) throws Exception;


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
}
