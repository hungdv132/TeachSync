package com.teachsync.services.user;

import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.address.AddressReadDTO;
import com.teachsync.dtos.role.RoleReadDTO;
import com.teachsync.dtos.user.UserCreateDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.dtos.user.UserUpdateDTO;
import com.teachsync.entities.BaseEntity;
import com.teachsync.entities.Role;
import com.teachsync.entities.User;
import com.teachsync.repositories.UserRepository;
import com.teachsync.services.role.RoleService;
import com.teachsync.utils.Constants;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.Status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ModelMapper mapper;


    /* =================================================== CREATE =================================================== */
    @Override
    public User createUser(User user) throws Exception {
        /* Check duplicate */
        boolean isExists =
                userRepository.existsByUsernameAndStatusNot(user.getUsername(), Status.DELETED);
        if (isExists) {
            throw new IllegalArgumentException("Đã tồn tại tài khoản : " + user.getUsername());
        }
        boolean isExistsEmail =
                userRepository.existsByEmailAndStatusNot(user.getEmail(), Status.DELETED);
        if (isExistsEmail) {
            throw new IllegalArgumentException("email đã có người đăng ký ");
        }

        /* Check FK */
        Role role = roleService.getById(user.getRoleId());
        if (role == null) {
            throw new Exception("Không có role hợp lệ : " + user.getRoleId());
        }
//        user.setRole(role);

        user = userRepository.saveAndFlush(user);

        return user;
    }

    @Override
    public UserReadDTO signupDTO(UserCreateDTO dto) throws Exception {
        dto.setRoleId(Constants.ROLE_STUDENT);

        User user = mapper.map(dto, User.class);

        if (dto.getParentEmail() != null) {
//            TODO: Create parent account
//            User parent = createUser(new User());
//            user.setParent(parent);
        }
        //process password BCryptPasswordEncoder
        String password = user.getPassword();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        user.setPassword(hashedPassword);

        user = createUser(user);

        return wrapDTO(user);
    }


    /* =================================================== READ ===================================================== */
    @Override
    public User login(String username) throws Exception {
        Optional<User> user = userRepository.findByUsernameAndStatusNot(username, Status.DELETED);

        return user.orElse(null);
    }

    @Override
    public UserReadDTO loginDTO(String username, String password) throws Exception {
        User user = login(username);

        if (ObjectUtils.isEmpty(user)) {
            throw new UsernameNotFoundException("Không tìm thấy tài khoản với username: " + username);
        }

        //check password
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean isMatch = passwordEncoder.matches(password, user.getPassword());
        if (!isMatch) {
            /* Wrong password */
            throw new BadCredentialsException("Sai username hoặc password");
        }

        return wrapDTO(user);
    }

    @Override
    public List<User> getListUserByType(Long type) {
        System.out.println("type = " + type);
        List<User> x = userRepository.findAllByRoleId(type);
        System.out.println(x);
        return x;
    }

    @Override
    public List<User> getListUserByUserName(String username) {
        return userRepository.findAllByUsernameContaining(username);
    }

    /* id */
    @Override
    public User getById(Long id) throws Exception {
        return userRepository
                .findByIdAndStatusNot(id, Status.DELETED)
                .orElse(null);
    }
    @Override
    public UserReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception {
        User user = getById(id);

        if (user == null) {
            return null;
        }

        return wrapDTO(user, options);
    }

    @Override
    public List<User> getAllByIdIn(Collection<Long> idCollection) throws Exception {
        List<User> userList = userRepository.findAllByIdInAndStatusNot(idCollection, Status.DELETED);

        if (userList.isEmpty()) {
            return null;
        }

        return userList;
    }
    @Override
    public Map<Long, String> mapIdFullNameByIdIn(Collection<Long> idCollection) throws Exception {
        List<User> userList = getAllByIdIn(idCollection);

        if (userList == null) {
            return new HashMap<>();
        }

        return userList.stream()
                .collect(Collectors.toMap(BaseEntity::getId, User::getFullName));
    }
    @Override
    public List<UserReadDTO> getAllDTOByIdIn(Collection<Long> idCollection, Collection<DtoOption> options) throws Exception {
        List<User> userList = getAllByIdIn(idCollection);

        if (userList == null) {
            return null;
        }

        return wrapListDTO(userList, options);
    }
    @Override
    public Map<Long, UserReadDTO> mapIdDTOByIdIn(Collection<Long> idCollection, Collection<DtoOption> options) throws Exception {
        List<UserReadDTO> userDTOList = getAllDTOByIdIn(idCollection, options);

        if (userDTOList == null) {
            return new HashMap<>();
        }

        return userDTOList.stream()
                .collect(Collectors.toMap(BaseReadDTO::getId, Function.identity()));
    }

    @Override
    public User updateUser(User user) throws Exception {
        User oldUser = getById(user.getId());

        if (oldUser == null) {
            throw new IllegalArgumentException("No User found with Id: " + user.getId());
        }

        //Check valid input (Vd: email, phone)

        //Check FK
        Role role = roleService.getById(user.getRoleId());
        if (role == null) {
            throw new IllegalArgumentException("No role found with roleId: " + user.getRoleId());
        }
//        user.setRole(role);

        user.setUsername(oldUser.getUsername());
        user.setPassword(oldUser.getPassword());

        return userRepository.saveAndFlush(user);
    }

    @Override
    public UserReadDTO updateDTOUser(UserUpdateDTO dto) throws Exception {
        User user = mapper.map(dto, User.class);

        user = updateUser(user);

        return wrapDTO(user);
    }

    @Override
    public UserReadDTO activateTeacherAccount(Long unactivatedTeacherAccId) throws Exception {
        Optional<User> teacherOptional = userRepository.findByIdAndStatus(unactivatedTeacherAccId, Status.DELETED); /* TODO: different status */

        if (teacherOptional.isEmpty()) {
            throw new IllegalArgumentException(
                    "No unactivated teacher account found with id: " + unactivatedTeacherAccId);
        }

        User teacher = teacherOptional.get();
        teacher.setStatus(Status.UPDATED); /* TODO: different status */

        teacher = updateUser(teacher);

        return wrapDTO(teacher);
    }

    /* =================================================== UPDATE =================================================== */



    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    @Override
    public UserReadDTO wrapDTO(User user) throws Exception {
        UserReadDTO dto = mapper.map(user, UserReadDTO.class);

        /* Add Dependency */
//        dto.setRequestMadeList();
//        if (user.getRoleId() == parentRoleId) {
//            dto.setChildList();
//        }

        return dto;
    }
    @Override
    public List<UserReadDTO> wrapListDTO(Collection<User> userCollection) throws Exception {
        return userCollection.stream()
                .map(user -> mapper.map(user, UserReadDTO.class))
                .collect(Collectors.toList());
    }
    @Override
    public Page<UserReadDTO> wrapPageDTO(Page<User> userPage) throws Exception {
        return new PageImpl<>(
                wrapListDTO(userPage.getContent()),
                userPage.getPageable(),
                userPage.getTotalPages());
    }

    @Override
    public UserReadDTO wrapDTO(User user, Collection<DtoOption> options) throws Exception {
        UserReadDTO dto = mapper.map(user, UserReadDTO.class);

        /* Add Dependency */
        if (options != null && !options.isEmpty()) {
            if (options.contains(DtoOption.ROLE)) {
                RoleReadDTO roleDTO = roleService.getDTOById(user.getRoleId(), options);
                dto.setRole(roleDTO);
            }

            if (options.contains(DtoOption.ADDRESS)) {
                /* TODO: */
            }
        }

        return dto;
    }
    @Override
    public List<UserReadDTO> wrapListDTO(Collection<User> userCollection, Collection<DtoOption> options) throws Exception {
        List<UserReadDTO> dtoList = new ArrayList<>();
        UserReadDTO dto;

        Map<Long, RoleReadDTO> roleIdRoleDTOMap = new HashMap<>();
        Map<Long, AddressReadDTO> addressIdAddressDTOMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> roleId = new HashSet<>();
            Set<Long> addressId = new HashSet<>();

            for (User user : userCollection) {
                roleId.add(user.getRoleId());
                addressId.add(user.getAddressId());
            }

            if (options.contains(DtoOption.ROLE)) {
                /* TODO: */
            }

            if (options.contains(DtoOption.ADDRESS)) {
                /* TODO: */
            }
        }

        for (User user : userCollection) {
            dto = mapper.map(user, UserReadDTO.class);

            dto.setRole(roleIdRoleDTOMap.get(user.getRoleId()));
            dto.setAddress(addressIdAddressDTOMap.get(user.getAddressId()));

            dtoList.add(dto);
        }


        return dtoList;
    }
    @Override
    public Page<UserReadDTO> wrapPageDTO(Page<User> userPage, Collection<DtoOption> options) throws Exception {
        return new PageImpl<>(
                wrapListDTO(userPage.getContent(), options),
                userPage.getPageable(),
                userPage.getTotalPages());
    }

    /* =================================================== Forgot Password ========================================== */
    @Override
    public User getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }

    @Override
    public void updatePassword(User user, String newPassword) {
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String encodedPassword = passwordEncoder.encode(newPassword);
//        user.setPassword(encodedPassword);
        user.setPassword(newPassword);

        user.setResetPasswordToken(null);
        userRepository.saveAndFlush(user);
    }

    @Override
    public void updateResetPasswordToken(String token, String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setResetPasswordToken(token);

            /* Check data */
            userRepository.saveAndFlush(user);
        }
    }
}