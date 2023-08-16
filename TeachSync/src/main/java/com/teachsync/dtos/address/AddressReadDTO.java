package com.teachsync.dtos.address;

import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.entities.LocationUnit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO for {@link com.teachsync.entities.Address}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressReadDTO extends BaseReadDTO {
    private String addressNo;
    private String street;
    private Long unitId;
    private Map<Integer, Long> levelUnitIdMap;
    private Map<Integer, LocationUnit> levelUnitMap;

    private String addressString;
}