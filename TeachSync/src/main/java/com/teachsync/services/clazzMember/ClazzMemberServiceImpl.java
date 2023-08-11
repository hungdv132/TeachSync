package com.teachsync.services.clazzMember;

import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.clazz.ClazzReadDTO;
import com.teachsync.dtos.clazzMember.ClazzMemberReadDTO;
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
    public List<ClazzMember> getAllByClazzIdIn(Collection<Long> clazzIdCollection) throws Exception {
        List<ClazzMember> clazzMemberList =
                clazzMemberRepository.findAllByClazzIdInAndStatusNot(clazzIdCollection, Status.DELETED);

        if (clazzMemberList.isEmpty()) {
            return null; }

        return clazzMemberList;
    }
    /* TODO: replace with dto */
    @Override
    public Map<Long, List<ClazzMember>> mapClazzIdClazzMemberListByClazzIdIn(Collection<Long> clazzIdCollection) throws Exception {
        List<ClazzMember> clazzMemberList = getAllByClazzIdIn(clazzIdCollection);

        if (clazzMemberList == null) {
            return new HashMap<>(); }

        Map<Long, List<ClazzMember>> clazzIdMemberListMap = new HashMap<>();
        long clazzId;
        List<ClazzMember> tmpList;
        for (ClazzMember member : clazzMemberList) {
            clazzId = member.getClazzId();

            tmpList = clazzIdMemberListMap.get(clazzId);
            if (tmpList == null) {
                clazzIdMemberListMap.put(clazzId, new ArrayList<>(List.of(member)));
            } else {
                tmpList.add(member);
                clazzIdMemberListMap.put(clazzId, tmpList);
            }
        }

        return clazzIdMemberListMap;
    }

    /* userId */
    @Override
    public List<ClazzMember> getAllByUserId(Long userId) throws Exception {
        return null;
    }

    @Override
    public List<ClazzMember> getAllByUserIdIn(Collection<Long> userIdCollection) throws Exception {
        return null;
    }

    /* clazzId & userId */
    @Override
    public ClazzMember getByClazzIdAndUserId(Long clazzId, Long userId) throws Exception {
        return clazzMemberRepository
                .findByClazzIdAndUserIdAndStatusNot(clazzId, userId, Status.DELETED)
                .orElse(null);
    }


    /* =================================================== UPDATE =================================================== */


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    @Override
    public ClazzMemberReadDTO wrapDTO(ClazzMember clazzMember, Collection<DtoOption> options) throws Exception {
        ClazzMemberReadDTO dto = mapper.map(clazzMember, ClazzMemberReadDTO.class);

        /* Add dependency */
        if (options != null && !options.isEmpty()) {
            if (options.contains(DtoOption.CLAZZ)) {
                ClazzReadDTO clazzDTO = clazzService.getDTOById(clazzMember.getClazzId(), options);
                dto.setClazz(clazzDTO);
            }
            if (options.contains(DtoOption.CLAZZ_NAME)) {
                Clazz clazz = clazzService.getById(clazzMember.getClazzId());
                dto.setClazzName(clazz.getClazzName());
            }

            if (options.contains(DtoOption.USER)) {
                UserReadDTO userDTO = userService.getDTOById(clazzMember.getUserId(), options);
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
                clazzIdClazzDTOMap = clazzService.mapIdDTOByIdIn(clazzIdSet, options);
            }
            if (options.contains(DtoOption.CLAZZ_NAME)) {
                clazzIdClazzNameMap = clazzService.mapClazzIdClazzNameByIdIn(clazzIdSet);
            }

            if (options.contains(DtoOption.USER)) {
                userIdUserDTOMap = userService.mapIdDTOByIdIn(clazzIdSet, options);
            }
            if (options.contains(DtoOption.USER_FULL_NAME)) {
                userIdUserFullNameMap = userService.mapIdFullNameByIdIn(clazzIdSet);
            }
        }

        for (ClazzMember clazzMember : clazzMemberCollection) {
            dto = mapper.map(clazzMember, ClazzMemberReadDTO.class);

            /* Add dependency */
            dto.setClazz(clazzIdClazzDTOMap.get(clazzMember.getClazzId()));
            dto.setClazzName(clazzIdClazzNameMap.get(clazzMember.getClazzId()));

            dto.setUser(userIdUserDTOMap.get(clazzMember.getClazzId()));
            dto.setUserFullName(userIdUserFullNameMap.get(clazzMember.getClazzId()));

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
