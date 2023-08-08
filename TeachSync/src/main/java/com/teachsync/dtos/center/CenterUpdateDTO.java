package com.teachsync.dtos.center;

import com.teachsync.dtos.BaseUpdateDTO;
import com.teachsync.dtos.room.RoomReadDTO;
import com.teachsync.entities.Address;
import com.teachsync.utils.enums.CenterType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CenterUpdateDTO extends BaseUpdateDTO {
    private Long addressId;

    private Address address;

    private String centerName;

    private CenterType centerType;

    private String centerDesc;

    private Integer centerSize;

    private List<RoomReadDTO> roomList;
}
