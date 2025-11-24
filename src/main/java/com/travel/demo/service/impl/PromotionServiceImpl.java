package com.travel.demo.service.impl;

import com.travel.demo.dto.PromotionDTO;
import com.travel.demo.entity.Categories;
import com.travel.demo.entity.Promotions;
import com.travel.demo.repository.CategoriesRepository;
import com.travel.demo.repository.PromotionRepository;
import com.travel.demo.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PromotionServiceImpl implements PromotionService {
    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private CategoriesRepository categoriesRepository;
    @Override
    public Page<PromotionDTO> getAllPromotions(int page, int size, String[] sort, String keyword) {
        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        Page<Promotions> promotions;
        if(keyword != null && !keyword.trim().isEmpty()) {
            promotions = promotionRepository.findByDeletedAtIsNullAndTitleContainingIgnoreCaseOrDeletedAtIsNullAndCodeContainingIgnoreCase(
                    keyword.trim(),
                    keyword.trim(),
                    pageable
            );
        }
        else {
            promotions = promotionRepository.findByDeletedAtIsNull(pageable);
        }
        return promotions.map(promotions1 -> new PromotionDTO(
                promotions1.getPromotionId(),
                promotions1.getTitle(),
                promotions1.getCode(),
                promotions1.getPercentDecrease(),
                promotions1.getCategory() != null ? promotions1.getCategory().getCategoryId() : null,
                promotions1.getStartDate(),
                promotions1.getEndDate(),
                promotions1.getDescription()
        ));
    }

    @Override
    public PromotionDTO getPromotionById(Integer id) {
        Promotions promotions = promotionRepository.findByPromotionIdAndDeletedAtIsNull(id);
        if(promotions == null) {
            throw new RuntimeException("Không tìm mã khuyến mãi");
        }
        return new PromotionDTO(
                promotions.getPromotionId(),
                promotions.getTitle(),
                promotions.getCode(),
                promotions.getPercentDecrease(),
                promotions.getCategory() != null ? promotions.getCategory().getCategoryId() : null,
                promotions.getStartDate(),
                promotions.getEndDate(),
                promotions.getDescription()
        );
    }

    @Override
    public void deletePromotionById(Integer id) {
        Promotions promotions = promotionRepository.findByPromotionIdAndDeletedAtIsNull(id);
        if(promotions == null) {
            throw new RuntimeException("Không tìm mã khuyến mãi");
        }
        promotions.setDeletedAt(LocalDate.now());
        promotionRepository.save(promotions);
    }

    @Override
    public PromotionDTO createPromotion(PromotionDTO promotionDTO) {
        if (promotionRepository.existsByCodeAndDeletedAtIsNull(promotionDTO.getCode())) {
            throw new RuntimeException("Mã khuyến mãi đã tồn tại!");
        }

        Promotions promotions = new Promotions();
        if (promotionDTO.getCategoryId() != null) {
            Categories category = categoriesRepository
                    .findByCategoryIdAndStatusAndDeletedAtIsNull(
                            promotionDTO.getCategoryId(), true
                    )
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy category"));

            promotions.setCategory(category);
        } else {
            promotions.setCategory(null); // KHÔNG CHỌN CATEGORY
        }
        if (promotionDTO.getPercentDecrease() < 1 || promotionDTO.getPercentDecrease() > 99)
            throw new RuntimeException("Mức giảm phải trong khoảng 1% - 99%!");
        // ===== VALIDATE NGÀY =====
        if (promotionDTO.getStartDate().isBefore(LocalDate.now().plusDays(1)))
            throw new RuntimeException("Ngày bắt đầu phải từ ngày mai!");

        if (promotionDTO.getEndDate().isBefore(promotionDTO.getStartDate()))
            throw new RuntimeException("Ngày kết thúc phải lớn hơn ngày bắt đầu!");
        promotions.setTitle(promotionDTO.getTitle());
        promotions.setCode(promotionDTO.getCode());
        promotions.setPercentDecrease(promotionDTO.getPercentDecrease());
        promotions.setStartDate(promotionDTO.getStartDate());
        promotions.setEndDate(promotionDTO.getEndDate());
        promotions.setDescription(promotionDTO.getDescription());
        promotions.setDeletedAt(null);

        promotionRepository.save(promotions);
        promotionDTO.setPromotionId(promotions.getPromotionId());
        return promotionDTO;
    }

    @Override
    public PromotionDTO updatePromotion(Integer id, PromotionDTO promotionDTO) {
        Promotions promotions = promotionRepository.findByPromotionIdAndDeletedAtIsNull(id);
        if (promotionDTO.getCategoryId() != null) {
            Categories category = categoriesRepository
                    .findByCategoryIdAndStatusAndDeletedAtIsNull(
                            promotionDTO.getCategoryId(), true
                    )
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy category"));

            promotions.setCategory(category);
        } else {
            promotions.setCategory(null); // KHÔNG CHỌN CATEGORY
        }
        // CHECK CODE TRÙNG VỚI RECORD KHÁC
        if (!promotions.getCode().equals(promotionDTO.getCode()) &&
                promotionRepository.existsByCodeAndDeletedAtIsNull(promotionDTO.getCode())) {

            throw new RuntimeException("Mã khuyến mãi đã tồn tại!");
        }
        if (promotionDTO.getPercentDecrease() < 1 || promotionDTO.getPercentDecrease() > 100)
            throw new RuntimeException("Mức giảm phải trong khoảng 1% - 100%!");
        // ===== VALIDATE NGÀY =====
        if (promotionDTO.getStartDate().isBefore(LocalDate.now().plusDays(1)))
            throw new RuntimeException("Ngày bắt đầu phải từ ngày mai!");

        if (promotionDTO.getEndDate().isBefore(promotionDTO.getStartDate()))
            throw new RuntimeException("Ngày kết thúc phải lớn hơn ngày bắt đầu!");
        promotions.setTitle(promotionDTO.getTitle());
        promotions.setCode(promotionDTO.getCode());
        promotions.setPercentDecrease(promotionDTO.getPercentDecrease());
        promotions.setStartDate(promotionDTO.getStartDate());
        promotions.setEndDate(promotionDTO.getEndDate());
        promotions.setDescription(promotionDTO.getDescription());
        promotionRepository.save(promotions);
        promotionDTO.setPromotionId(promotions.getPromotionId());

        return promotionDTO;
    }
    @Override
    public void deleteMultipe(List<Integer> ids) {

        // Lấy tất cả promotions theo list id
        List<Promotions> promotionsList = promotionRepository.findByPromotionIdIn(ids);

        if (promotionsList.isEmpty()) {
            throw new RuntimeException("Không tìm thấy khuyến mãi nào để xóa!");
        }

        for (Promotions p : promotionsList) {

            // Nếu đã bị xóa rồi thì bỏ qua
            if (p.getDeletedAt() != null) {
                continue;
            }

            // Không cho xóa nếu còn hiệu lực
            if (p.getCategory() != null && LocalDate.now().isBefore(p.getEndDate())) {
                throw new RuntimeException(
                        "Khuyến mãi ID " + p.getPromotionId() + " vẫn còn hiệu lực, không thể xóa!"
                );
            }

            // Thực hiện xóa mềm
            p.setDeletedAt(LocalDate.now());
        }

        // Lưu tất cả lại
        promotionRepository.saveAll(promotionsList);
    }

    private PromotionDTO convertToDTO(Promotions p) {
        PromotionDTO dto = new PromotionDTO();

        dto.setPromotionId(p.getPromotionId());
        dto.setTitle(p.getTitle());
        dto.setCode(p.getCode());
        dto.setPercentDecrease(p.getPercentDecrease());
        dto.setCategoryId(
                p.getCategory() != null ? p.getCategory().getCategoryId() : null
        );
        dto.setStartDate(p.getStartDate());
        dto.setEndDate(p.getEndDate());
        dto.setDescription(p.getDescription());

        return dto;
    }

}
