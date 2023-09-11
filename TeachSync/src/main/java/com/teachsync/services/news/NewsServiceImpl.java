package com.teachsync.services.news;

import com.teachsync.dtos.news.NewsReadDTO;
import com.teachsync.entities.News;
import com.teachsync.repositories.NewsRepository;
import com.teachsync.utils.MiscUtil;
import com.teachsync.utils.enums.Status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class NewsServiceImpl implements NewsService{

    @Autowired
    private NewsRepository newsRepository;


    @Autowired
    private MiscUtil miscUtil;
    @Autowired
    private ModelMapper mapper;
    @Override
    public Page<News> getPageAll(Pageable paging) throws Exception {
        if (paging == null) {
            paging = miscUtil.defaultPaging(); }

        Page<News> newsPage =
                newsRepository.findAllByStatusNot(Status.DELETED, paging);

        if (newsPage.isEmpty()) {
            return null; }

        return newsPage;
    }

    @Override
    public Page<NewsReadDTO> getPageDTOAll(Pageable paging) throws Exception {
        Page<News> newsPage = getPageAll(paging);

        if (newsPage == null) {
            return null; }

        return wrapPageDTO(newsPage);
    }

    @Override
    public News getById(Long id) throws Exception {
        Optional<News> news =
                newsRepository.findByIdAndStatusNot(id, Status.DELETED);

        return news.orElse(null);
    }

    @Override
    public NewsReadDTO getDTOById(Long id) throws Exception {
        News news = getById(id);

        if (news == null) {
            return null; }

        return wrapDTO(news);
    }

    @Override
    public List<NewsReadDTO> getAllNewsByClazz(Long clazzId) {
        List<News> list = newsRepository.findAllByClazzIdAndStatusNot(clazzId, Status.DELETED);
        List<NewsReadDTO> dtoList = new ArrayList<>();
        for (News news : list) {
            NewsReadDTO dto = mapper.map(news, NewsReadDTO.class);
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public NewsReadDTO wrapDTO(News news) throws Exception {
        NewsReadDTO dto = mapper.map(news, NewsReadDTO.class);

        return dto;
    }

    @Override
    public List<NewsReadDTO> wrapListDTO(Collection<News> newsCollection) throws Exception {
        List<NewsReadDTO> dtoList = new ArrayList<>();

        NewsReadDTO dto;

        for (News news : newsCollection) {
            dto = mapper.map(news, NewsReadDTO.class);


            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public Page<NewsReadDTO> wrapPageDTO(Page<News> newsPage) throws Exception {
        return new PageImpl<>(
                wrapListDTO(newsPage.getContent()),
                newsPage.getPageable(),
                newsPage.getTotalPages());
    }
}
