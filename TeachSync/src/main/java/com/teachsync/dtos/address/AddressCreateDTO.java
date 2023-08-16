package com.teachsync.dtos.address;

import com.teachsync.dtos.BaseCreateDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link com.teachsync.entities.Address}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressCreateDTO extends BaseCreateDTO {
    private String addressNo;
    private String street;
    private Long unitId;
}