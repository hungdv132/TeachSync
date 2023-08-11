package com.teachsync.services.answer;

import com.teachsync.dtos.answer.AnswerCreateDTO;
import com.teachsync.dtos.answer.AnswerReadDTO;
import com.teachsync.dtos.answer.AnswerUpdateDTO;
import com.teachsync.entities.Answer;
import com.teachsync.entities.BaseEntity;
import com.teachsync.entities.LocationUnit;
import com.teachsync.repositories.AnswerRepository;
import com.teachsync.utils.enums.DtoOption;
import com.teachsync.utils.enums.Status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AnswerServiceImpl implements AnswerService {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private ModelMapper mapper;


    /* =================================================== CREATE =================================================== */
    @Override
    public Answer createAnswer(Answer answer) throws Exception {
        //TODO: Kiểm tra khóa ngoại, chính tả,..

        return answerRepository.saveAndFlush(answer);
    }
    @Override
    public AnswerReadDTO createAnswerByDTO(AnswerCreateDTO createDTO) throws Exception {
        Answer answer = mapper.map(createDTO, Answer.class);

        answer = createAnswer(answer);

        return mapper.map(answer, AnswerReadDTO.class);
    }

    @Override
    public List<Answer> createBulkAnswer(Collection<Answer> answerCollection) throws Exception {
        //TODO: Kiểm tra khóa ngoại, chính tả,..


        return answerRepository.saveAllAndFlush(answerCollection);
    }

    @Override
    public List<AnswerReadDTO> createBulkAnswerByDTO(Collection<AnswerCreateDTO> createDTOCollection) throws Exception {
        List<Answer> answerList =
                createDTOCollection.stream()
                        .map(dto -> mapper.map(dto, Answer.class))
                        .collect(Collectors.toList());

        answerList = createBulkAnswer(answerList);

        return wrapListDTO(answerList, null);
    }


    /* =================================================== READ ===================================================== */
    /* id */
    @Override
    public Answer getById(Long id) throws Exception {
        return answerRepository
                .findByIdAndStatusNot(id, Status.DELETED)
                .orElse(null);
    }
    @Override
    public AnswerReadDTO getDTOById(Long id, Collection<DtoOption> options) throws Exception {
        Answer answer = getById(id);

        if (answer == null) {
            return null;
        }

        return wrapDTO(answer, options);
    }

    @Override
    public List<Answer> getAllByIdIn(Collection<Long> idCollection) throws Exception {
        List<Answer> answerList =
                answerRepository.findAllByIdInAndStatusNot(idCollection, Status.DELETED);

        if(answerList.isEmpty()){
            return null;
        }

        return answerList;
    }
    @Override
    public List<AnswerReadDTO> getAllDTOByIdIn(
            Collection<Long> idCollection, Collection<DtoOption> options) throws Exception {
        List<Answer> answerList =
                answerRepository.findAllByIdInAndStatusNot(idCollection, Status.DELETED);

        if(answerList == null){
            return null;
        }

        return wrapListDTO(answerList, options);
    }

    /* questionId */
    @Override
    public List<Answer> getAllByQuestionId(Long questionId) throws Exception {
        List<Answer> answerList =
                answerRepository.findAllByQuestionIdAndStatusNot(questionId, Status.DELETED);

        if(answerList.isEmpty()){
            return null;
        }

        return answerList;
    }
    @Override
    public List<AnswerReadDTO> getAllDTOByQuestionId(Long questionId, Collection<DtoOption> options) throws Exception {
        List<Answer> answerList = getAllByQuestionId(questionId);

        if(answerList == null){
            return null;
        }

        return wrapListDTO(answerList, options);
    }

    @Override
    public List<Answer> getAllByQuestionIdIn(Collection<Long> questionIdCollection) throws Exception {
        List<Answer> answerList =
                answerRepository.findAllByQuestionIdInAndStatusNot(questionIdCollection, Status.DELETED);

        if(answerList.isEmpty()){
            return null;
        }

        return answerList;
    }
    @Override
    public List<AnswerReadDTO> getAllDTOByQuestionIdIn(
            Collection<Long> questionIdCollection, Collection<DtoOption> options) throws Exception {
        List<Answer> answerList = getAllByQuestionIdIn(questionIdCollection);

        if(answerList == null){
            return null;
        }

        return wrapListDTO(answerList, options);
    }
    @Override
    public Map<Long, List<AnswerReadDTO>> mapQuestionIdListDTOByQuestionIdIn(
            Collection<Long> questionIdCollection, Collection<DtoOption> options) throws Exception {
        List<AnswerReadDTO> answerDTOList = getAllDTOByQuestionIdIn(questionIdCollection, options);

        if (answerDTOList == null) {
            return new HashMap<>();
        }
        Map<Long, List<AnswerReadDTO>> questionIdDTOListMap = new HashMap<>();
        Long tmpQuestionId;
        List<AnswerReadDTO> tmpList;
        for (AnswerReadDTO dto : answerDTOList) {
            tmpQuestionId = dto.getQuestionId();
            tmpList = questionIdDTOListMap.get(tmpQuestionId);

            if (tmpList == null) {
                questionIdDTOListMap.put(tmpQuestionId, new ArrayList<>(List.of(dto)));
            } else {
                tmpList.add(dto);
                questionIdDTOListMap.put(tmpQuestionId, tmpList);
            }
        }

        return questionIdDTOListMap;
    }


    /* =================================================== UPDATE =================================================== */
    @Override
    public Answer updateAnswer(Answer answer) throws Exception {
        /* Check exist */
        Answer oldAnswer = getById(answer.getId());
        if (oldAnswer == null) {
            throw new IllegalArgumentException("Update Error. No Answer found with id: " + answer.getId());
        }
        answer.setCreatedAt(oldAnswer.getCreatedAt());
        answer.setCreatedBy(oldAnswer.getCreatedBy());

        /* Validate input */

        /* Check fk */

        return answerRepository.saveAndFlush(answer);
    }
    @Override
    public AnswerReadDTO updateAnswerByDTO(AnswerUpdateDTO updateDTO) throws Exception {
        Answer answer = mapper.map(updateDTO, Answer.class);

        answer = updateAnswer(answer);

        return mapper.map(answer, AnswerReadDTO.class);
    }


    @Override
    public List<Answer> updateBulkAnswer(Collection<Answer> answerCollection) throws Exception {
        Map<Long, Answer> idAnswerMap =
                answerCollection.stream()
                        .collect(Collectors.toMap(BaseEntity::getId, Function.identity()));

        /* Check exist */
        List<Answer> oldAnswerList = getAllByIdIn(idAnswerMap.keySet());
        if (oldAnswerList == null) {
            throw new IllegalArgumentException("Update Error. No Answer found within id given");
        } else if (oldAnswerList.size() != idAnswerMap.size()) {
            throw new IllegalArgumentException("Update Error. Some Answer found within id given");
        }
        Map<Long, Answer> idOldAnswerMap = oldAnswerList.stream()
                .collect(Collectors.toMap(BaseEntity::getId, Function.identity()));

        Answer tmpAnswer;
        Answer tmpOldAnswer;
        for (Long id : idAnswerMap.keySet()) {
            tmpAnswer = idAnswerMap.get(id);
            tmpOldAnswer = idOldAnswerMap.get(id);

            tmpAnswer.setCreatedAt(tmpOldAnswer.getCreatedAt());
            tmpAnswer.setCreatedBy(tmpOldAnswer.getCreatedBy());

            idAnswerMap.put(id, tmpAnswer);
        }

        /* Validate input */

        /* Check fk */

        return answerRepository.saveAllAndFlush(idAnswerMap.values());
    }

    @Override
    public List<AnswerReadDTO> updateBulkAnswerByDTO(Collection<AnswerUpdateDTO> updateDTOCollection) throws Exception {
        List<Answer> answerList =
                updateDTOCollection.stream()
                        .map(dto -> mapper.map(dto, Answer.class))
                        .collect(Collectors.toList());

        answerList = createBulkAnswer(answerList);

        return wrapListDTO(answerList, null);
    }

    /* =================================================== DELETE =================================================== */
    /* id */
    @Override
    public Boolean deleteById(Long id) throws Exception {
        Answer answer = getById(id);

        if (answer == null) {
            throw new IllegalArgumentException("Delete error. No answer found with id: " + id);
        }

        answer.setUpdatedAt(LocalDateTime.now());
        answer.setStatus(Status.DELETED);

        answerRepository.saveAndFlush(answer);

        return true;
    }
    @Override
    public Boolean deleteAllByIdIn(Collection<Long> idCollection) throws Exception {
        List<Answer> answerList = getAllByIdIn(idCollection);

        if (answerList == null) {
            throw new IllegalArgumentException("Delete error. No answer found within id given");
        }

        LocalDateTime deleteTime = LocalDateTime.now();

        answerList =
                answerList.stream()
                        .peek(answer -> {
                            answer.setUpdatedAt(deleteTime);
                            answer.setStatus(Status.DELETED);})
                        .collect(Collectors.toList());

        answerRepository.saveAllAndFlush(answerList);

        return true;
    }

    /* questionId */
    @Override
    public Boolean deleteAllByQuestionId(Long questionId) throws Exception {
        List<Answer> answerList = getAllByQuestionId(questionId);

        if (answerList == null) {
            throw new IllegalArgumentException("Delete error. No answer found within questionId: " + questionId);
        }

        LocalDateTime deleteTime = LocalDateTime.now();

        answerList =
                answerList.stream()
                        .peek(answer -> {
                            answer.setUpdatedAt(deleteTime);
                            answer.setStatus(Status.DELETED);})
                        .collect(Collectors.toList());

        answerRepository.saveAllAndFlush(answerList);

        return true;
    }
    @Override
    public Boolean deleteAllByQuestionIdIn(Collection<Long> questionIdCollection) throws Exception {
        List<Answer> answerList = getAllByQuestionIdIn(questionIdCollection);

        if (answerList == null) {
            throw new IllegalArgumentException("Delete error. No answer found within questionId given");
        }

        LocalDateTime deleteTime = LocalDateTime.now();

        answerList =
                answerList.stream()
                        .peek(answer -> {
                            answer.setUpdatedAt(deleteTime);
                            answer.setStatus(Status.DELETED);})
                        .collect(Collectors.toList());

        answerRepository.saveAllAndFlush(answerList);

        return true;
    }


    /* =================================================== WRAPPER ================================================== */
    @Override
    public AnswerReadDTO wrapDTO(Answer answer, Collection<DtoOption> options) throws Exception {
        AnswerReadDTO dto = mapper.map(answer, AnswerReadDTO.class);

        /* Add dependency */
        /* TODO:
        if (options != null && !options.isEmpty()) {
            if (options.contains(DtoOption.FK)) {
                FkReadDTO fkDTO = fkService.getDTOById(answer.getFkId());
                dto.setFk(fkDTO);
            }

            if (options.contains(DtoOption.FK_LIST)) {
                List<FkReadDTO> fkDTOList = fkService.getAllDTOById(answer.getFkId());
                dto.setFkList(fkDTOList);
            }
        }
        */

        return dto;
    }

    @Override
    public List<AnswerReadDTO> wrapListDTO(Collection<Answer> answerCollection, Collection<DtoOption> options) throws Exception {
        List<AnswerReadDTO> dtoList = new ArrayList<>();
        AnswerReadDTO dto;

        /* TODO:
        Map<Long, FkReadDTO> fkIdFkDTOMap = new HashMap<>();
        Map<Long, List<FkReadDTO>> fkIdFkDTOListMap = new HashMap<>();

        if (options != null && !options.isEmpty()) {
            Set<Long> fkIdSet = new HashSet<>();

            for (Answer answer : answerCollection) {
                fkIdSet.add(answer.getFkId());
            }

            if (options.contains(DtoOption.FK)) {
                fkIdFkDTOMap = fkService.mapIdDTOByIdIn(fkIdSet);
            }

            if (options.contains(DtoOption.FK_LIST)) {
                fkIdFkDTOListMap = fkService.mapIdListDTOByIdIn(fkIdSet);
            }
        }
        */

        for (Answer answer : answerCollection) {
            dto = mapper.map(answer, AnswerReadDTO.class);

            /* Add dependency */
            /*
            dto.setFk(fkIdFkDTOMap.get(answer.getFkId()));

            dto.setFkList(fkIdFkDTOListMap.get(answer.getFkId()));
            */

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public Page<AnswerReadDTO> wrapPageDTO(Page<Answer> answerPage, Collection<DtoOption> options) throws Exception {
        return new PageImpl<>(
                wrapListDTO(answerPage.getContent(), options),
                answerPage.getPageable(),
                answerPage.getTotalPages());
    }
}
