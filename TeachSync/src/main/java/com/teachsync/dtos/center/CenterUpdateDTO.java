package com.teachsync.dtos.center;

import com.teachsync.dtos.BaseUpdateDTO;
import com.teachsync.dtos.address.AddressCreateDTO;
import com.teachsync.utils.enums.CenterType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CenterUpdateDTO extends BaseUpdateDTO {
    private Long addressId;
    private AddressCreateDTO address;

    private String centerImg;

    private String centerName;

    private CenterType centerType;

    private String centerDesc;

    private Integer centerSize;
}
