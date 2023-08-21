package com.teachsync.dtos.request;

import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.clazz.ClazzReadDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.utils.enums.RequestType;
import com.teachsync.utils.enums.Status;
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
public class RequestReadDTO extends BaseReadDTO {
    private Long requesterId;
    private String requesterUsername;
    private String requesterFullName;
    private UserReadDTO requester;

    private String requestName;

    private String requestDesc;

    private RequestType requestType;

    private Long clazzId;
    private String clazzName;
    private ClazzReadDTO clazz;

    private byte[] requestContent;
    private String contentLink;

    private Long resolverId;
    private String resolverUsername;
    private String resolverFullName;
    private UserReadDTO resolver;

}