package com.teachsync.services.clazzMember;

import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.clazz.ClazzReadDTO;
import com.teachsync.dtos.clazzMember.ClazzMemberCreateDTO;
import com.teachsync.dtos.clazzMember.ClazzMemberReadDTO;
import com.teachsync.dtos.clazzMember.ClazzMemberUpdateDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.entities.Clazz;
import com.teachsync.entities.ClazzMember;
import com.teachsync.entities.User;
import com.teachsync.repositories.ClazzMemberRepository;
import com.teachsync.services.clazz.ClazzService;
import com.teachsync.services.user.UserService;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.Status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ClazzMemberServiceImpl implements ClazzMemberService {
    @Autowired
    private ClazzMemberRepository clazzMemberRepository;

    @Lazy
    @Autowired
    private ClazzService clazzService;
    @Lazy
    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper mapper;


    /* =================================================== CREATE =================================================== */
    @Override
    public ClazzMember createClazzMember(ClazzMember member) throws Exception {
        /* Validate input */
        /* TODO: Check slot open for clazz, reject if full */

        /* Check FK */
        /* TODO: Check exist clazz, user */

        /* Insert DB */
        return clazzMemberRepository.saveAndFlush(member);
    }
    @Override
    public ClazzMemberReadDTO createClazzMemberByDTO(ClazzMemberCreateDTO createDTO) throws Exception {
        ClazzMember member = mapper.map(createDTO, ClazzMember.class);

        member = createClazzMember(member);

        return wrapDTO(member, null);
    }


    /* =================================================== READ ===================================================== */
    /* id */
    @Override
    public ClazzMember getById(Long id) throws Exception {
        return clazzMemberRepository
                .findByIdAndStatusNot(id, Status.DELETED)
                .orElse(null);
    }
    @Override
    public ClazzMemberReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception {
        ClazzMember member = getById(id);

        if (member == null) {
            return null;
        }

        return wrapDTO(member, options);
    }

    @Override
    public List<ClazzMember> getAllByIdIn(Collection<Long> idCollection) throws Exception {
        List<ClazzMember> memberList = clazzMemberRepository.findAllByIdInAndStatusNot(idCollection, Status.DELETED);

        if (memberList.isEmpty()) {
            return null;
        }

        return memberList;
    }
    @Override
    public List<ClazzMemberReadDTO> getAllDTOByIdIn(Collection<Long> idCollection, Collection<DtoOption> options) throws Exception {
        List<ClazzMember> memberList = getAllByIdIn(idCollection);

        if (memberList == null) {
            return null;
        }

        return wrapListDTO(memberList, options);
    }
    @Override
    public Map<Long, ClazzMemberReadDTO> mapIdDTOByIdIn(Collection<Long> idCollection, Collection<DtoOption> options) throws Exception {
        List<ClazzMemberReadDTO> memberDTOList = getAllDTOByIdIn(idCollection, options);

        if (memberDTOList == null) {
            return new HashMap<>();
        }

        return memberDTOList.stream()
                .collect(Collectors.toMap(BaseReadDTO::getId, Function.identity()));
    }

    /* clazzId */
    @Override
    public List<ClazzMember> getAllByClazzId(Long clazzId) throws Exception {
        List<ClazzMember> clazzMemberList =
                clazzMemberRepository.findAllByClazzIdAndStatusNot(clazzId, Status.DELETED);

        if (clazzMemberList.isEmpty()) {
            return null; }

        return clazzMemberList;
    }
    @Override
    public List<ClazzMemberReadDTO> getAllDTOByClazzId(Long clazzId, Collection<DtoOption> options) throws Exception {
        List<ClazzMember> clazzMemberList = getAllByClazzId(clazzId);

        if (clazzMemberList == null) {
            return null; }

        return wrapListDTO(clazzMemberList, options);
    }

    @Override
    public List<ClazzMember> getAllByClazzIdIn(Collection<Long> clazzIdCollection) throws Exception {
        List<ClazzMember> clazzMemberList =
                clazzMemberRepository.findAllByClazzIdInAndStatusNot(clazzIdCollection, Status.DELETED);

        if (clazzMemberList.isEmpty()) {
            return null; }

        return clazzMemberList;
    }
    @Override
    public List<ClazzMemberReadDTO> getAllDTOByClazzIdIn(
            Collection<Long> clazzIdCollection, Collection<DtoOption> options) throws Exception {
        List<ClazzMember> clazzMemberList = getAllByClazzIdIn(clazzIdCollection);

        if (clazzMemberList == null) {
            return null; }

        return wrapListDTO(clazzMemberList, options);
    }
    @Override
    public Map<Long, List<ClazzMemberReadDTO>> mapClazzIdListDTOByClazzIdIn(
            Collection<Long> clazzIdCollection, Collection<DtoOption> options) throws Exception {
        List<ClazzMemberReadDTO> clazzMemberDTOList = getAllDTOByClazzIdIn(clazzIdCollection, options);

        if (clazzMemberDTOList == null) {
            return new HashMap<>(); }

        Map<Long, List<ClazzMemberReadDTO>> clazzIdDTOListMap = new HashMap<>();
        long clazzId;
        List<ClazzMemberReadDTO> tmpList;
        for (ClazzMemberReadDTO memberDTO : clazzMemberDTOList) {
            clazzId = memberDTO.getClazzId();

            tmpList = clazzIdDTOListMap.get(clazzId);
            if (tmpList == null) {
                clazzIdDTOListMap.put(clazzId, new ArrayList<>(List.of(memberDTO)));
            } else {
                tmpList.add(memberDTO);
                clazzIdDTOListMap.put(clazzId, tmpList);
            }
        }

        return clazzIdDTOListMap;
    }

    /* userId */
    @Override
    public List<ClazzMember> getAllByUserId(Long userId) throws Exception {
        List<ClazzMember> clazzMemberList =
                clazzMemberRepository.findAllByUserIdAndStatusNot(userId, Status.DELETED);

        if (clazzMemberList.isEmpty()) {
            return null; }

        return clazzMemberList;
    }
    @Override
    public List<ClazzMemberReadDTO> getAllDTOByUserId(Long userId, Collection<DtoOption> options) throws Exception {
        List<ClazzMember> clazzMemberList = getAllByUserId(userId);

        if (clazzMemberList == null) {
            return null; }

        return wrapListDTO(clazzMemberList, options);
    }

    @Override
    public List<ClazzMember> getAllByUserIdIn(Collection<Long> userIdCollection) throws Exception {
        List<ClazzMember> clazzMemberList =
                clazzMemberRepository.findAllByUserIdInAndStatusNot(userIdCollection, Status.DELETED);

        if (clazzMemberList.isEmpty()) {
            return null; }

        return clazzMemberList;
    }
    @Override
    public List<ClazzMemberReadDTO> getAllDTOByUserIdIn(
            Collection<Long> userIdCollection, Collection<DtoOption> options) throws Exception {
        List<ClazzMember> clazzMemberList = getAllByUserIdIn(userIdCollection);

        if (clazzMemberList == null) {
            return null; }

        return wrapListDTO(clazzMemberList, options);
    }


    /* clazzId & userId */
    @Override
    public ClazzMember getByClazzIdAndUserId(Long clazzId, Long userId) throws Exception {
        return clazzMemberRepository
                .findByClazzIdAndUserIdAndStatusNot(clazzId, userId, Status.DELETED)
                .orElse(null);
    }
    @Override
    public ClazzMemberReadDTO getDTOByClazzIdAndUserId(Long clazzId, Long userId, Collection<DtoOption> options) throws Exception {
        return null;
    }


    /* =================================================== UPDATE =================================================== */
    @Override
    public ClazzMember updateClazzMember(ClazzMember member) throws Exception {
        /* Check exist */
        ClazzMember oldMemberRecord = getById(member.getId());
        if (oldMemberRecord == null) {
            throw new IllegalArgumentException("Update error. No ClazzMember found with id: " + member.getId());
        }
        member.setCreatedAt(oldMemberRecord.getCreatedAt());
        member.setCreatedBy(oldMemberRecord.getCreatedBy());

        /* Validate input */
        /* TODO: Check slot open for clazz, reject if full */

        /* Check FK */
        /* TODO: Check exist clazz, user */

        /* Insert DB */
        return clazzMemberRepository.saveAndFlush(member);
    }
    @Override
    public ClazzMemberReadDTO updateClazzMemberByDTO(ClazzMemberUpdateDTO updateDTO) throws Exception {
        ClazzMember member = mapper.map(updateDTO, ClazzMember.class);

        member = updateClazzMember(member);

        return wrapDTO(member, null);
    }


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    @Override
    public ClazzMemberReadDTO wrapDTO(ClazzMember clazzMember, Collection<DtoOption> options) throws Exception {
        ClazzMemberReadDTO dto = mapper.map(clazzMember, ClazzMemberReadDTO.class);

        /* Add dependency */
        if (options != null && !options.isEmpty()) {
            if (options.contains(DtoOption.CLAZZ)) {
                ClazzReadDTO clazzDTO = clazzService.getDTOById(clazzMember.getClazzId(),
                        List.of(Status.DELETED),
                        false,options);
                dto.setClazz(clazzDTO);
            }
            if (options.contains(DtoOption.CLAZZ_NAME)) {
                Clazz clazz = clazzService.getById(
                        clazzMember.getClazzId(),
                        List.of(Status.DELETED),
                        false);
                dto.setClazzName(clazz.getClazzName());
            }

            if (options.contains(DtoOption.USER)) {
                UserReadDTO userDTO = userService.getDTOById(
                        clazzMember.getUserId(), options);
                dto.setUser(userDTO);
            }
            if (options.contains(DtoOption.USER_FULL_NAME)) {
                User user = userService.getById(clazzMember.getUserId());
                dto.setUserFullName(user.getFullName());
            }
        }

        return dto;
    }

    @Override
    public List<ClazzMemberReadDTO> wrapListDTO(
            Collection<ClazzMember> clazzMemberCollection, Collection<DtoOption> options) throws Exception {
        List<ClazzMemberReadDTO> dtoList = new ArrayList<>();
        ClazzMemberReadDTO dto;

        Map<Long, ClazzReadDTO> clazzIdClazzDTOMap = new HashMap<>();
        Map<Long, String> clazzIdClazzNameMap = new HashMap<>();
        Map<Long, UserReadDTO> userIdUserDTOMap = new HashMap<>();
        Map<Long, String> userIdUserFullNameMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> clazzIdSet = new HashSet<>();
            Set<Long> userIdSet = new HashSet<>();

            for (ClazzMember clazzMember : clazzMemberCollection) {
                clazzIdSet.add(clazzMember.getClazzId());
                userIdSet.add(clazzMember.getUserId());
            }

            if (options.contains(DtoOption.CLAZZ)) {
                clazzIdClazzDTOMap = clazzService.mapIdDTOByIdIn(
                        clazzIdSet,
                        List.of(Status.DELETED),
                        false,
                        options);
            }
            if (options.contains(DtoOption.CLAZZ_NAME)) {
                clazzIdClazzNameMap = clazzService.mapIdClazzNameByIdIn(
                        clazzIdSet,
                        List.of(Status.DELETED),
                        false);
            }

            if (options.contains(DtoOption.USER)) {
                userIdUserDTOMap = userService.mapIdDTOByIdIn(userIdSet, options);
            }
            if (options.contains(DtoOption.USER_FULL_NAME)) {
                userIdUserFullNameMap = userService.mapIdFullNameByIdIn(userIdSet);
            }
        }

        for (ClazzMember clazzMember : clazzMemberCollection) {
            dto = mapper.map(clazzMember, ClazzMemberReadDTO.class);

            /* Add dependency */
            dto.setClazz(clazzIdClazzDTOMap.get(clazzMember.getClazzId()));
            dto.setClazzName(clazzIdClazzNameMap.get(clazzMember.getClazzId()));

            dto.setUser(userIdUserDTOMap.get(clazzMember.getUserId()));
            dto.setUserFullName(userIdUserFullNameMap.get(clazzMember.getUserId()));

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public Page<ClazzMemberReadDTO> wrapPageDTO(
            Page<ClazzMember> clazzMemberPage, Collection<DtoOption> options) throws Exception {
        return new PageImpl<>(
                wrapListDTO(clazzMemberPage.getContent(), options),
                clazzMemberPage.getPageable(),
                clazzMemberPage.getTotalPages());
    }
}
