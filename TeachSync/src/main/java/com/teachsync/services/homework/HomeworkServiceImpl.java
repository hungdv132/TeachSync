package com.teachsync.services.homework;

import com.teachsync.dtos.homework.HomeworkReadDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.entities.Clazz;
import com.teachsync.entities.Homework;
import com.teachsync.repositories.ClazzRepository;
import com.teachsync.repositories.HomeworkRepository;
import com.teachsync.utils.Constants;
import com.teachsync.utils.MiscUtil;
import com.teachsync.utils.enums.Status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class HomeworkServiceImpl implements HomeworkService {

    @Autowired
    private MiscUtil miscUtil;

    @Autowired
    private HomeworkRepository homeworkRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    ClazzRepository clazzRepository;

    @Override
    public Page<HomeworkReadDTO> getPageAll(Pageable paging, UserReadDTO userDTO) throws Exception {
        if (paging == null) {
            paging = miscUtil.defaultPaging();
        }
        Page<Homework> homeworkPage = homeworkRepository.findAllByStatusNot(Status.DELETED, paging);

        List<HomeworkReadDTO> homeworkDtoList = new ArrayList<>();
        for (Homework homework : homeworkPage) {
            HomeworkReadDTO homeworkReadDTO = mapper.map(homework, HomeworkReadDTO.class);
            //get class
            Clazz clazz = clazzRepository.findById(homeworkReadDTO.getClazzId()).orElseThrow(() -> new Exception("không tìm lớp học"));
            homeworkReadDTO.setClazzName(clazz.getClazzName());

            //check student to show
            if (!userDTO.getRoleId().equals(Constants.ROLE_ADMIN) && !userDTO.getRoleId().equals(Constants.ROLE_TEACHER)) {
                if (!ObjectUtils.isEmpty(homeworkReadDTO.getOpenAt()) && homeworkReadDTO.getOpenAt().isBefore(LocalDateTime.now())) {
                    homeworkDtoList.add(homeworkReadDTO);
                }
            } else {
                homeworkDtoList.add(homeworkReadDTO);
            }
        }

        Page<HomeworkReadDTO> homeworkReadDTOS = new PageImpl<>(homeworkDtoList, paging, homeworkPage.getTotalElements());

        if (homeworkReadDTOS.isEmpty()) {
            return new PageImpl<>(Collections.emptyList());
        }

        return homeworkReadDTOS;
    }

    @Override
    public HomeworkReadDTO findById(Long id) throws Exception {
        Homework homework = homeworkRepository.findById(id).orElseThrow(() -> new Exception("không tìm bài tập về nhà"));
        HomeworkReadDTO homeworkReadDTO = mapper.map(homework, HomeworkReadDTO.class);
        Clazz clazz = clazzRepository.findById(homeworkReadDTO.getClazzId()).orElseThrow(() -> new Exception("không tìm lớp học"));
        homeworkReadDTO.setClazzName(clazz.getClazzName());
        return homeworkReadDTO;
    }

    @Override
    @Transactional
    public void addHomework(HomeworkReadDTO homeworkReadDTO, UserReadDTO userDTO) throws Exception {
        Homework homework = new Homework();

        homework.setHomeworkName(homeworkReadDTO.getHomeworkName());
        homework.setClazzId(homeworkReadDTO.getClazzId());
        homework.setHomeworkDesc(homeworkReadDTO.getHomeworkDesc());
        homework.setHomeworkDoc(homeworkReadDTO.getHomeworkDoc());
        homework.setHomeworkContent(null);//TODO : upload file
        homework.setHomeworkDoc(null);//TODO : upload file
        //homework.setHomeworkDocLink(homeworkReadDTO.getHomeworkDocLink());
        homework.setDeadline(homeworkReadDTO.getDeadline());
        homework.setOpenAt(homeworkReadDTO.getOpenAt());
        homework.setCreatedBy(userDTO.getId());
        homework.setUpdatedBy(userDTO.getId());
        homework.setStatus(Status.CREATED);

        Homework homeworkDb = homeworkRepository.save(homework);
        if (ObjectUtils.isEmpty(homeworkDb)) {
            throw new Exception("Lỗi khi tạo bài tập về nhà");
        }
    }

    @Override
    @Transactional
    public void editHomework(HomeworkReadDTO homeworkReadDTO, UserReadDTO userDTO) throws Exception {
        Homework homework = homeworkRepository.findById(homeworkReadDTO.getId()).orElseThrow(() -> new Exception("không tìm bài tập về nhà"));

        homework.setHomeworkName(homeworkReadDTO.getHomeworkName());
        homework.setClazzId(homeworkReadDTO.getClazzId());
        homework.setHomeworkDesc(homeworkReadDTO.getHomeworkDesc());
        homework.setHomeworkDoc(homeworkReadDTO.getHomeworkDoc());
        homework.setHomeworkContent(null);//TODO : upload file
        homework.setHomeworkDoc(null);//TODO : upload file
        //homework.setHomeworkDocLink(homeworkReadDTO.getHomeworkDocLink());
        homework.setDeadline(homeworkReadDTO.getDeadline());
        homework.setOpenAt(homeworkReadDTO.getOpenAt());
        homework.setUpdatedBy(userDTO.getId());
        homework.setStatus(Status.UPDATED);

        Homework homeworkDb = homeworkRepository.save(homework);
        if (ObjectUtils.isEmpty(homeworkDb)) {
            throw new Exception("Lỗi khi sửa bài tập về nhà");
        }
    }

    @Override
    public void deleteHomework(Long Id, UserReadDTO userDTO) throws Exception {
        Homework homework = homeworkRepository.findById(Id).orElseThrow(() -> new Exception("không tìm bài tập về nhà"));
        homework.setUpdatedBy(userDTO.getId());
        homework.setStatus(Status.DELETED);
        Homework homeworkDb = homeworkRepository.save(homework);
        if (ObjectUtils.isEmpty(homeworkDb)) {
            throw new Exception("Lỗi khi xóa bài tập về nhà");
        }
    }
}
