package com.travel.demo.service;

import com.travel.demo.dto.TourDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ItemService {
    Page<TourDTO> getAllTour(int page, int size, String[] sort, String keyword);
    TourDTO create(TourDTO tourDTO);
    TourDTO update(Integer id,TourDTO tourDTO);
    void softDelete(Integer id);
    TourDTO getById(Integer id);
    void deleteMultipe(List<Integer> ids);

    TourDTO cloneTour(Integer id);
}
