package com.teachsync.dtos.news;

import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.entities.User;
import com.teachsync.utils.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link com.teachsync.entities.News}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsReadDTO extends BaseReadDTO {
    private Long id;

    private Long clazzId;

    private String newsTitle;

    private String newsDesc;

    private String newsLink;

    private User author;

    private Long authorId;

    private Status status;
}
