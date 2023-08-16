package com.teachsync.dtos.user;

import com.teachsync.dtos.BaseUpdateDTO;
import com.teachsync.utils.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for {@link com.teachsync.entities.User}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO extends BaseUpdateDTO {
    @NotBlank
    @Size(min = 5, max = 45)
    private String username;

    @NotBlank
    @Size(min = 5, max = 45)
    private String password;

    @NotNull
    @Positive
    private Long roleId;

    private String userAvatar;

    private String about;

    @NotBlank
    @Size(min = 1, max = 255)
    private String fullName;

    private Gender gender = Gender.OTHER;

    @Email
    @Size(min = 5, max = 255)
    private String email;

    @Pattern(regexp = "\\b\\d{10}\\b")
    @Size(min = 10, max = 10)
    private String phone;

    @Positive
    private Long addressId;

    @Positive
    private Long parentId;

    @Size(min = 1)
    private List<Long> childList;
}
