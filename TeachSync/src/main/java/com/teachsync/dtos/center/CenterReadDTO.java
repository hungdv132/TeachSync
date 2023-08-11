package com.teachsync.dtos.center;

import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.room.RoomReadDTO;
import com.teachsync.entities.Address;
import com.teachsync.entities.Center;
import com.teachsync.utils.enums.CenterType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for {@link Center}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CenterReadDTO extends BaseReadDTO {
    private Long addressId;

    private Address address;

    private String centerName;

    private CenterType centerType;

    private String centerDesc;

    private Integer centerSize;

    private List<RoomReadDTO> roomList;
//    private List<CenterStaffReadDTO> staffList;


}