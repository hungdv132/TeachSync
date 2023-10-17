package com.teachsync.services.request;

import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.clazz.ClazzReadDTO;
import com.teachsync.dtos.request.RequestCreateDTO;
import com.teachsync.dtos.request.RequestReadDTO;
import com.teachsync.dtos.request.RequestUpdateDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.entities.Request;
import com.teachsync.entities.User;
import com.teachsync.repositories.RequestRepository;
import com.teachsync.services.clazz.ClazzService;
import com.teachsync.services.user.UserService;
import com.teachsync.utils.MiscUtil;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.Status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.teachsync.utils.enums.Status.*;
import static com.teachsync.utils.enums.DtoOption.*;

@Service
public class RequestServiceImpl implements RequestService {
    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private ClazzService clazzService;
    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper mapper;
    @Autowired
    private MiscUtil miscUtil;


    /* =================================================== CREATE =================================================== */
    @Override
    public Request createRequest(Request request) throws Exception {
        /* Validate input */
        /* TODO:  */

        /* Check FK */
        /* TODO: clazzId, requesterId */

        /* Check duplicate */
        if (requestRepository.existsByRequesterIdAndClazzIdAndRequestTypeAndStatusNotIn(
                request.getRequesterId(),
                request.getClazzId(),
                request.getRequestType(),
                List.of(DELETED, DENIED))) {
            throw new IllegalArgumentException("Create error. You already have a request like this.");
        }

        /* Insert DB */
        return requestRepository.saveAndFlush(request);
    }

    @Override
    public RequestReadDTO createRequestByDTO(RequestCreateDTO createDTO) throws Exception {
        Request request = mapper.map(createDTO, Request.class);
        
        request = createRequest(request);
        
        return wrapDTO(request, null);
    }


    /* =================================================== READ ===================================================== */
    @Override
    public List<Request> getAll() throws Exception {
        List<Request> requestList =
                requestRepository.findAllByStatusNot(DELETED);

        if (requestList.isEmpty()) {
            return null;
        }

        return requestList;
    }
    @Override
    public List<RequestReadDTO> getAllDTO(Collection<DtoOption> options) throws Exception {
        List<Request> requestList = getAll();

        if (requestList == null) {
            return null;
        }

        return wrapListDTO(requestList, options);
    }

    @Override
    public Page<Request> getPageAll(Pageable pageable) throws Exception {
        if (pageable == null) {
            pageable = miscUtil.defaultPaging();
        }

        Page<Request> requestPage =
                requestRepository.findAllByStatusNot(DELETED, pageable);

        if (requestPage.isEmpty()) {
            return null;
        }

        return requestPage;
    }
    @Override
    public Page<RequestReadDTO> getPageAllDTO(Pageable pageable, Collection<DtoOption> options) throws Exception {
        Page<Request> requestPage = getPageAll(pageable);

        if (requestPage == null) {
            return null;
        }

        return wrapPageDTO(requestPage, options);
    }

    /* id */
    @Override
    public Request getById(Long id) throws Exception {
        return requestRepository
                .findByIdAndStatusNot(id, DELETED)
                .orElse(null);
    }
    @Override
    public RequestReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception {
        Request request = getById(id);

        if (request == null) {
            return null;
        }

        return wrapDTO(request, options);
    }

    @Override
    public List<Request> getAllByIdIn(Collection<Long> idCollection) throws Exception {
        List<Request> requestList =
                requestRepository.findAllByIdInAndStatusNot(idCollection, DELETED);

        if (requestList.isEmpty()) {
            return null;
        }

        return requestList;
    }
    @Override
    public List<RequestReadDTO> getAllDTOByIdIn(
            Collection<Long> idCollection, Collection<DtoOption> options) throws Exception {
        List<Request> requestList = getAllByIdIn(idCollection);

        if (requestList == null) {
            return null;
        }

        return wrapListDTO(requestList, options);
    }
    @Override
    public Map<Long, RequestReadDTO> mapIdDTOByIdIn(
            Collection<Long> idCollection, Collection<DtoOption> options) throws Exception {
        List<RequestReadDTO> requestDTOList = getAllDTOByIdIn(idCollection, options);

        if (requestDTOList == null) {
            return new HashMap<>();
        }

        return requestDTOList.stream()
                .collect(Collectors.toMap(BaseReadDTO::getId, Function.identity()));
    }

    /* requesterId (userId) */
    @Override
    public List<Request> getAllByRequesterId(Long requesterId) throws Exception {
        List<Request> requestList =
                requestRepository.findAllByRequesterIdAndStatusNot(requesterId, DELETED);

        if (requestList.isEmpty()) {
            return null;
        }

        return requestList;
    }
    @Override
    public List<RequestReadDTO> getAllDTOByRequesterId(
            Long requesterId, Collection<DtoOption> options) throws Exception {
        List<Request> requestList = getAllByRequesterId(requesterId);

        if (requestList == null) {
            return null;
        }

        return wrapListDTO(requestList, options);
    }

    @Override
    public Page<Request> getPageAllByRequesterId(Pageable pageable, Long requesterId) throws Exception {
        Page<Request> requestPage =
                requestRepository.findAllByRequesterIdAndStatusNot(requesterId, DELETED, pageable);

        if (requestPage.isEmpty()) {
            return null;
        }

        return requestPage;
    }
    @Override
    public Page<RequestReadDTO> getPageAllDTOByRequesterId(
            Pageable pageable, Long requesterId, Collection<DtoOption> options) throws Exception {
        Page<Request> requestPage = getPageAllByRequesterId(pageable, requesterId);

        if (requestPage == null) {
            return null;
        }

        return wrapPageDTO(requestPage, options);
    }

    @Override
    public List<Request> getAllByRequesterIdIn(Collection<Long> requesterIdCollection) throws Exception {
        List<Request> requestList =
                requestRepository.findAllByRequesterIdInAndStatusNot(requesterIdCollection, DELETED);

        if (requestList.isEmpty()) {
            return null;
        }

        return requestList;
    }
    @Override
    public List<RequestReadDTO> getAllDTOByRequesterIdIn(
            Collection<Long> requesterIdCollection, Collection<DtoOption> options) throws Exception {
        List<Request> requestList = getAllByRequesterIdIn(requesterIdCollection);

        if (requestList == null) {
            return null;
        }

        return wrapListDTO(requestList, options);
    }

    /* clazzId */
    @Override
    public List<Request> getAllByClazzId(Long clazzId) throws Exception {
        List<Request> requestList =
                requestRepository.findAllByClazzIdAndStatusNot(clazzId, DELETED);

        if (requestList.isEmpty()) {
            return null;
        }

        return requestList;
    }
    @Override
    public List<RequestReadDTO> getAllDTOByClazzId(Long clazzId, Collection<DtoOption> options) throws Exception {
        List<Request> requestList = getAllByClazzId(clazzId);

        if (requestList == null) {
            return null;
        }

        return wrapListDTO(requestList, options);
    }
    
    @Override
    public Page<Request> getPageAllByClazzId(Pageable pageable, Long clazzId) throws Exception {
        Page<Request> requestPage =
                requestRepository.findAllByClazzIdAndStatusNot(clazzId, DELETED, pageable);

        if (requestPage.isEmpty()) {
            return null;
        }

        return requestPage;
    }
    @Override
    public Page<RequestReadDTO> getPageAllDTOByClazzId(
            Pageable pageable, Long clazzId, Collection<DtoOption> options) throws Exception {
        Page<Request> requestPage = getPageAllByClazzId(pageable, clazzId);

        if (requestPage == null) {
            return null;
        }

        return wrapPageDTO(requestPage, options);
    }

    @Override
    public List<Request> getAllByClazzIdIn(Collection<Long> clazzIdCollection) throws Exception {
        List<Request> requestList =
                requestRepository.findAllByClazzIdInAndStatusNot(clazzIdCollection, DELETED);

        if (requestList.isEmpty()) {
            return null;
        }

        return requestList;
    }
    @Override
    public List<RequestReadDTO> getAllDTOByClazzIdIn(
            Collection<Long> clazzIdCollection, Collection<DtoOption> options) throws Exception {
        List<Request> requestList = getAllByClazzIdIn(clazzIdCollection);

        if (requestList == null) {
            return null;
        }

        return wrapListDTO(requestList, options);
    }


    /* =================================================== UPDATE =================================================== */
    @Override
    public Request updateRequest(Request request) throws Exception {
        /* Check exist */
        Request oldRequest = getById(request.getId());
        if (oldRequest == null) {
            throw new IllegalArgumentException("Update error. No request found with id: " + request.getId());
        }
        request.setCreatedBy(oldRequest.getCreatedBy());
        request.setCreatedAt(oldRequest.getCreatedAt());

        /* Validate input */
        /* TODO:  */

        /* Check FK */
        /* TODO: clazzId, requesterId, resolverId */

        /* Check duplicate */
        if (requestRepository.existsByIdNotAndRequesterIdAndClazzIdAndRequestTypeAndStatusNotIn(
                request.getId(),
                request.getRequesterId(),
                request.getClazzId(),
                request.getRequestType(),
                List.of(DELETED, DENIED))) {
            throw new IllegalArgumentException("Update error. You already have a request like this.");
        }

        /* Insert DB */
        return requestRepository.saveAndFlush(request);
    }

    @Override
    public RequestReadDTO updateRequestByDTO(RequestUpdateDTO updateDTO) throws Exception {
        Request request = mapper.map(updateDTO, Request.class);

        request = updateRequest(request);

        return wrapDTO(request, null);
    }


    /* =================================================== DELETE =================================================== */


    /* =================================================== WRAPPER ================================================== */
    @Override
    public RequestReadDTO wrapDTO(Request request, Collection<DtoOption> options) throws Exception {
        RequestReadDTO dto = mapper.map(request, RequestReadDTO.class);

        /* Add dependency */
        if (options != null && !options.isEmpty()) {
            if (options.contains(CLAZZ)) {
                ClazzReadDTO clazzDTO = clazzService.getDTOById(request.getClazzId(),
                        List.of(Status.DELETED),
                        false,options);
                dto.setClazz(clazzDTO);
            }

            if (options.contains(REQUESTER)) {
                UserReadDTO requesterDTO = userService.getDTOById(request.getRequesterId(), options);
                dto.setRequester(requesterDTO);
            }
            if (options.contains(REQUESTER_USERNAME)) {
                User requester = userService.getById(request.getRequesterId());
                dto.setRequesterUsername(requester.getUsername());
            }
            if (options.contains(REQUESTER_FULL_NAME)) {
                User requester = userService.getById(request.getRequesterId());
                dto.setRequesterFullName(requester.getFullName());
            }

            if (options.contains(RESOLVER)) {
                if (request.getResolverId() != null) {
                    UserReadDTO resolverDTO = userService.getDTOById(request.getResolverId(), options);
                    if (resolverDTO != null) {
                        dto.setResolver(resolverDTO);
                    }
                }
            }
            if (options.contains(RESOLVER_USERNAME)) {
                if (request.getResolverId() != null) {
                    User resolver = userService.getById(request.getResolverId());
                    if (resolver != null) {
                        dto.setResolverUsername(resolver.getUsername());
                    }
                }
            }
            if (options.contains(RESOLVER_FULL_NAME)) {
                if (request.getResolverId() != null) {
                    User resolver = userService.getById(request.getResolverId());
                    if (resolver != null) {
                        dto.setResolverFullName(resolver.getFullName());
                    }
                }
            }
        }

        return dto;
    }

    @Override
    public List<RequestReadDTO> wrapListDTO(
            Collection<Request> requestCollection, Collection<DtoOption> options) throws Exception {
        List<RequestReadDTO> dtoList = new ArrayList<>();
        RequestReadDTO dto;

        Long requesterId, resolverId;

        Map<Long, ClazzReadDTO> clazzIdClazzDTOMap = new HashMap<>();

        Map<Long, UserReadDTO> requesterIdRequesterDTOMap = new HashMap<>();
        Map<Long, String> requesterIdRequesterUsernameMap = new HashMap<>();
        Map<Long, String> requesterIdRequesterFullNameMap = new HashMap<>();

        Map<Long, UserReadDTO> resolverIdResolverDTOMap = new HashMap<>();
        Map<Long, String> resolverIdResolverUsernameMap = new HashMap<>();
        Map<Long, String> resolverIdResolverFullNameMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> clazzIdSet = new HashSet<>();
            Set<Long> requesterIdSet = new HashSet<>();
            Set<Long> resolverIdSet = new HashSet<>();

            for (Request request : requestCollection) {
                clazzIdSet.add(request.getClazzId());
                requesterIdSet.add(request.getRequesterId());
                resolverId = request.getResolverId();
                if (resolverId != null) {
                    resolverIdSet.add(resolverId);
                }
            }

            if (options.contains(CLAZZ)) {
                clazzIdClazzDTOMap = clazzService.mapIdDTOByIdIn(clazzIdSet,
                        List.of(Status.DELETED),
                        false, options);
            }

            if (options.contains(REQUESTER)) {
                requesterIdRequesterDTOMap = userService.mapIdDTOByIdIn(requesterIdSet, options);
            }
            if (options.contains(REQUESTER_USERNAME)) {
                requesterIdRequesterUsernameMap = userService.mapIdFullNameByIdIn(requesterIdSet);
            }
            if (options.contains(REQUESTER_FULL_NAME)) {
                requesterIdRequesterFullNameMap = userService.mapIdFullNameByIdIn(requesterIdSet);
            }

            if (options.contains(RESOLVER)) {
                if (!requesterIdSet.isEmpty()) {
                    resolverIdResolverDTOMap = userService.mapIdDTOByIdIn(resolverIdSet, options);
                }
            }
            if (options.contains(RESOLVER_USERNAME)) {
                if (!requesterIdSet.isEmpty()) {
                    resolverIdResolverUsernameMap = userService.mapIdFullNameByIdIn(resolverIdSet);
                }
            }
            if (options.contains(RESOLVER_FULL_NAME)) {
                if (!requesterIdSet.isEmpty()) {
                    resolverIdResolverFullNameMap = userService.mapIdFullNameByIdIn(resolverIdSet);
                }
            }
        }

        for (Request request : requestCollection) {
            dto = mapper.map(request, RequestReadDTO.class);

            /* Add dependency */
            dto.setClazz(clazzIdClazzDTOMap.get(request.getClazzId()));

            requesterId = request.getRequesterId();
            dto.setRequester(requesterIdRequesterDTOMap.get(requesterId));
            dto.setRequesterUsername(requesterIdRequesterUsernameMap.get(requesterId));
            dto.setRequesterFullName(requesterIdRequesterFullNameMap.get(requesterId));

            resolverId = request.getResolverId();
            if (resolverId != null) {
                dto.setResolver(resolverIdResolverDTOMap.get(resolverId));
                dto.setResolverUsername(resolverIdResolverUsernameMap.get(resolverId));
                dto.setResolverFullName(resolverIdResolverFullNameMap.get(resolverId));
            }

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public Page<RequestReadDTO> wrapPageDTO(
            Page<Request> requestPage, Collection<DtoOption> options) throws Exception {
        return new PageImpl<>(
                wrapListDTO(requestPage.getContent(), options),
                requestPage.getPageable(),
                requestPage.getTotalPages());
    }
}
