package com.teachsync.services.locationUnit;

import com.teachsync.entities.LocationUnit;
import com.teachsync.repositories.LocationUnitRepository;
import com.teachsync.utils.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LocationUnitServiceImpl implements LocationUnitService {
    @Autowired
    private LocationUnitRepository locationUnitRepository;
    @Override
    public LocationUnit getById(Long id) throws Exception {
        return locationUnitRepository.findByIdAndStatusNot(id, Status.DELETED).orElse(null);
    }

    @Override
    public Map<Integer, Long> mapLevelUnitIdByBottomChildId(
            Long bottomChildId, Map<Integer, Long> levelUnitIdMap) throws Exception{
        if (levelUnitIdMap == null) {
            levelUnitIdMap = new HashMap<>();
        }
        LocationUnit unit = getById(bottomChildId);

        levelUnitIdMap.put(unit.getLevel(), unit.getId());

        if (unit.getLevel() > 0) {
            levelUnitIdMap = mapLevelUnitIdByBottomChildId(unit.getParentId(), levelUnitIdMap);
        }

        return levelUnitIdMap;
    }

    @Override
    public List<LocationUnit> getAllByLevel(Integer level) throws Exception {
        List<LocationUnit> listLocationUnit =
                locationUnitRepository.findAllByLevelAndStatusNot(level, Status.DELETED);
        if(listLocationUnit.isEmpty()){
            return null;
        }
        return listLocationUnit;
    }


    @Override
    public List<LocationUnit> getAllByParentId(Long parentId) throws Exception {
        List<LocationUnit> listLocationUnit =
                locationUnitRepository.findAllByParentIdAndStatusNot(parentId, Status.DELETED);
        if(listLocationUnit.isEmpty()){
            return null;
        }
        return listLocationUnit;
    }

}
