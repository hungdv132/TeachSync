package com.teachsync.dtos.user;

import com.teachsync.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateAccountDTO implements Serializable {
    private String username;

    private String password;

    private String oldPassword;
}
