package com.teachsync.services.user;

import com.teachsync.dtos.user.UserCreateDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.dtos.user.UserUpdateDTO;
import com.teachsync.entities.User;
import com.teachsync.utils.enums.DtoOption;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface UserService {
    /* =================================================== CREATE =================================================== */
    User createUser(User user) throws Exception;
    UserReadDTO signupDTO(UserCreateDTO dto) throws Exception;



    /* =================================================== READ ===================================================== */
    User login(String username) throws Exception;
    UserReadDTO loginDTO(String username,String password) throws Exception;

    /* id */
    User getById(Long id) throws Exception;
    UserReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception;

    List<User> getAllByIdIn(Collection<Long> idCollection) throws Exception;
    Map<Long, String> mapIdFullNameByIdIn(Collection<Long> idCollection) throws Exception;
    List<UserReadDTO> getAllDTOByIdIn(Collection<Long> idCollection, Collection<DtoOption> options) throws Exception;
    Map<Long, UserReadDTO> mapIdDTOByIdIn(Collection<Long> idCollection, Collection<DtoOption> options) throws Exception;

    /* =================================================== UPDATE =================================================== */

    User updateUser(User user) throws Exception;
    UserReadDTO updateDTOUser(UserUpdateDTO dto) throws Exception;

    UserReadDTO activateTeacherAccount(Long unactivatedTeacherAccId) throws Exception;

    /* =================================================== DELETE =================================================== */

    List<User> getListUserByType(Long type);
    List<User> getListUserByUserName(String username);



    /* =================================================== WRAPPER ================================================== */
    @Deprecated
    UserReadDTO wrapDTO(User user) throws Exception;
    @Deprecated
    List<UserReadDTO> wrapListDTO(Collection<User> userCollection) throws Exception;
    @Deprecated
    Page<UserReadDTO> wrapPageDTO(Page<User> userPage) throws Exception;

    UserReadDTO wrapDTO(User user, Collection<DtoOption> options) throws Exception;
    List<UserReadDTO> wrapListDTO(Collection<User> userCollection, Collection<DtoOption> options) throws Exception;
    Page<UserReadDTO> wrapPageDTO(Page<User> userPage, Collection<DtoOption> options) throws Exception;



    /* =================================================== Forgot Password ========================================== */
    User getByResetPasswordToken(String token) throws Exception;
    void updatePassword(User user, String password) throws Exception;
    void updateResetPasswordToken(String token, String email) throws Exception;
}