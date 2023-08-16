package com.teachsync.dtos.center;

import com.teachsync.dtos.BaseUpdateDTO;
import com.teachsync.dtos.address.AddressCreateDTO;
import com.teachsync.dtos.room.RoomReadDTO;
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
public class CenterCreateDTO extends BaseUpdateDTO {
    private Long addressId;
    private AddressCreateDTO address;

    private String centerImg;

    private String centerName;

    private CenterType centerType;

    private String centerDesc;

    private Integer centerSize;

    private List<RoomReadDTO> roomList;
}
