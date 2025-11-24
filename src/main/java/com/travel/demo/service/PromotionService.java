package com.travel.demo.service;

import com.travel.demo.dto.PromotionDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PromotionService {
    Page<PromotionDTO> getAllPromotions(int page, int size, String[] sort, String keyword);

    PromotionDTO getPromotionById(Integer id);

    void deletePromotionById(Integer id);

   PromotionDTO createPromotion(@Valid PromotionDTO promotionDTO);

    PromotionDTO updatePromotion(Integer id, @Valid PromotionDTO promotionDTO);

    void deleteMultipe(List<Integer> ids);
}
