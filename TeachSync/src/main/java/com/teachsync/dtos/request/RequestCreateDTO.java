package com.teachsync.dtos.request;

import com.teachsync.dtos.BaseCreateDTO;
import com.teachsync.utils.enums.RequestType;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link com.teachsync.entities.Request}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestCreateDTO extends BaseCreateDTO {
    private Long requesterId;

    private String requestName;

    @Lob
    private String requestDesc;

    private RequestType requestType;

    private Long clazzId;

    private byte[] requestContent;

    @Lob
    private String contentLink;
}