package com.teachsync.controllers;

import com.teachsync.dtos.BaseReadDTO;
import com.teachsync.dtos.center.CenterReadDTO;
import com.teachsync.dtos.course.CourseReadDTO;
import com.teachsync.dtos.semester.SemesterReadDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.services.center.CenterService;
import com.teachsync.services.course.CourseService;
import com.teachsync.services.courseSemester.CourseSemesterService;
import com.teachsync.services.semester.SemesterService;
import com.teachsync.utils.Constants;
import com.teachsync.utils.MiscUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.teachsync.utils.enums.DtoOption.CURRENT_PRICE;

@Controller
public class SemesterController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseSemesterService courseSemesterService;
    @Autowired
    private SemesterService semesterService;
    @Autowired
    private CenterService centerService;
    
    @Autowired
    private MiscUtil miscUtil;
    
    

    @GetMapping("/semester")
    public String getSemester(
            HttpSession session,
            Model model,
            @ModelAttribute("mess") String mess,
            @RequestParam(value = "pageNo", required = false) Integer pageNo,
            @SessionAttribute(name = "user", required = false) UserReadDTO userDTO) {
        model.addAttribute("mess", mess);
        
        if (userDTO == null || !userDTO.getRoleId().equals(Constants.ROLE_ADMIN)) {
            /* Không phải admin, đá ra ngoài */
            /* TODO: notification */
            return "redirect:/index";
        }
        
        try {
            Map<Long, CourseReadDTO> courseIdCourseDTOMap = courseService.mapIdDTO(null);
            model.addAttribute("courseIdCourseDTOMap", courseIdCourseDTOMap);

            Map<Long, CenterReadDTO> centerIdCenterDTOMap = centerService.mapIdDTO(null);
            model.addAttribute("centerIdCenterDTOMap", centerIdCenterDTOMap);

            if (pageNo == null) { pageNo = 0; }
            Pageable paging = miscUtil.makePaging(pageNo, 10, "startDate", false);

            Page<SemesterReadDTO> semesterDTOPage = semesterService.getPageAllDTO(paging, null);

            if (semesterDTOPage == null) {
                return "semester/list-semester";
            }

            Map<Long, SemesterReadDTO> semesterIdSemesterDTOMap =
                    semesterDTOPage.getContent().stream()
                            .collect(Collectors.toMap(BaseReadDTO::getId, Function.identity()));

            Map<Long, Map<Long, Set<Long>>> semesterIdCenterIdCourseIdSetMapMap =
                    courseSemesterService.mapSemesterIdMapCenterIdListCourseIdBySemesterIdIn(semesterIdSemesterDTOMap.keySet());

            model.addAttribute("semesterIdSemesterDTOMap", semesterIdSemesterDTOMap);

            model.addAttribute("pageNo", semesterDTOPage.getPageable().getPageNumber());
            model.addAttribute("pageTotal", semesterDTOPage.getTotalPages());

            model.addAttribute("distributionMap", semesterIdCenterIdCourseIdSetMapMap);
            
        } catch (Exception e) {
            e.printStackTrace();
            
        }
        
        return "semester/list-semester";
    }

    @GetMapping("/api/semester-detail")
    @ResponseBody
    public Map<String, Object> getSemesterDetail(
            @RequestParam Long semesterId) {
        Map<String, Object> response = new HashMap<>();
        try {
            SemesterReadDTO semesterDTO = semesterService.getDTOById(semesterId, null);
            response.put("semester", semesterDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

}
