package com.teachsync.services.locationUnit;

import com.teachsync.entities.LocationUnit;

import java.util.List;
import java.util.Map;

public interface LocationUnitService {

    LocationUnit getById(Long id) throws Exception;
    Map<Integer, Long> mapLevelUnitIdByBottomChildId(
            Long bottomChildId, Map<Integer, Long> levelUnitIdMap) throws Exception;

    List<LocationUnit> getAllByLevel(Integer level) throws Exception;

    List<LocationUnit> getAllByParentId(Long parentId) throws Exception;

}
