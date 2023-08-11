package com.teachsync.controllers;


import com.teachsync.dtos.news.NewsReadDTO;
import com.teachsync.dtos.user.UserReadDTO;
import com.teachsync.entities.News;
import com.teachsync.entities.User;
import com.teachsync.repositories.NewsRepository;
import com.teachsync.repositories.UserRepository;
import com.teachsync.services.news.NewsService;
import com.teachsync.utils.MiscUtil;
import com.teachsync.utils.enums.Status;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Date;

@Controller
public class NewsController {
    @Autowired
    NewsRepository newsRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    NewsService newsService;

    @Autowired
    private MiscUtil miscUtil;

    @GetMapping("/create-news")
    public String createNews(Model model, HttpSession session) {
        UserReadDTO user = (UserReadDTO) session.getAttribute("user");
        if (user == null || user.getRoleId() != 4) {
            return "redirect:/";
        } else return "news/create-news";
    }

    @PostMapping("/submitcreatenews")
    public String submitCreateNews(Model model, HttpSession session,
                                   @RequestParam String title,
                                   @RequestParam String description,
                                   @RequestParam String content) {

        UserReadDTO user = (UserReadDTO) session.getAttribute("user");
        if (user == null || user.getRoleId() != 4) {
            return "redirect:/";
        }
        System.out.println("user id = " + user.getId());

        User user1 = userRepository.findById(user.getId()).orElse(null);

        News news = new News(user1.getId(), title, null, content, description);
        LocalDateTime date = LocalDateTime.now();
        news.setStatus(Status.CREATED);
        news.setCreatedAt(date);
        news.setCreatedBy(user.getId());


        newsRepository.save(news);
        return "redirect:/";
    }


    @GetMapping("/editnews")
    public String editNews(Model model, HttpSession session,
                           @RequestParam String id) {

        UserReadDTO user = (UserReadDTO) session.getAttribute("user");
        if (user == null || user.getRoleId() != 4) {
            return "redirect:/";
        }
        System.out.println("user id = " + user.getId());


        News news = newsRepository.findAllById(Long.parseLong(id));
        model.addAttribute("news", news);
        return "news/edit-news";
    }

    @PostMapping("/submiteditnews")
    public String submitEditNews(Model model, HttpSession session,
                                 @RequestParam String idNews,
                                 @RequestParam String title,
                                 @RequestParam String description,
                                 @RequestParam String content) {

        UserReadDTO user = (UserReadDTO) session.getAttribute("user");
        if (user == null || user.getRoleId() != 4) {
            return "redirect:/";
        }
        System.out.println("user id = " + user.getId());

        User user1 = userRepository.findById(user.getId()).orElse(null);

        News news = new News(user1.getId(), title, null, content, description);
        news.setId(Long.parseLong(idNews));
        news.setStatus(Status.UPDATED);
        news.setUpdatedBy(user.getId());

        newsRepository.save(news);
        return "redirect:/";
    }


    @GetMapping("/news")
    public String news(Model model, @ModelAttribute("mess") String mess) {

        try {
            Page<NewsReadDTO> dtoPage = newsService.getPageDTOAll(null);

            if (dtoPage != null) {
                model.addAttribute("newsList", dtoPage.getContent());
                model.addAttribute("pageNo", dtoPage.getPageable().getPageNumber());
                model.addAttribute("pageTotal", dtoPage.getTotalPages());

            }

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMsg", "Server error, please try again later");
        }
        model.addAttribute("mess", mess);

        return "news/list-news";
    }

    @GetMapping("/news-detail")
    public String getDetailById(
            @RequestParam(name = "id") Long authorId,
            Model model) {
        try {
            NewsReadDTO news = newsService.getDTOById(authorId);

            if (news == null) {
                /* Not found by Id */
                return "redirect:/news";
            }

            model.addAttribute("news", news);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMsg", "Server error, please try again later");
        }

        return "news/news-detail";
    }

}
