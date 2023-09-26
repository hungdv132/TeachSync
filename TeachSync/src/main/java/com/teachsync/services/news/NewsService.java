package com.teachsync.services.news;

import com.teachsync.dtos.news.NewsReadDTO;
import com.teachsync.entities.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface NewsService {
    /* =================================================== READ ===================================================== */
    Page<News> getPageAll(Pageable paging) throws Exception;
    Page<NewsReadDTO> getPageDTOAll(Pageable paging) throws Exception;

    News getById(Long id) throws Exception;
    NewsReadDTO getDTOById(Long id) throws Exception;

    List<NewsReadDTO> getAllNewsByClazz(Long clazzId);



    /* =================================================== WRAPPER ================================================== */
    NewsReadDTO wrapDTO(News news) throws Exception;

    List<NewsReadDTO> wrapListDTO(Collection<News> newsCollection) throws Exception;

    Page<NewsReadDTO> wrapPageDTO(Page<News> newsPage) throws Exception;
}
