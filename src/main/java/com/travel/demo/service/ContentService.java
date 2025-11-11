package com.travel.demo.service;

import com.travel.demo.dto.ContentDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ContentService {
    Page<ContentDTO> getAllContent(int page, int size, String[] sort, String keyword);

    ContentDTO getContentById(Integer id);

    ContentDTO createContent(ContentDTO contentDTO);

    ContentDTO updateContent(Integer id, ContentDTO contentDTO);

    void  softDelete(Integer id);

    String deleteMultipe(List<Integer> ids);
}
