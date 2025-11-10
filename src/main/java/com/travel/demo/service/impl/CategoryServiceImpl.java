package com.travel.demo.service.impl;

import com.travel.demo.dto.CategoryDTO;
import com.travel.demo.entity.Categories;
import com.travel.demo.repository.CategoriesRepository;
import com.travel.demo.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoriesRepository categoriesReporsitory;

    public CategoryServiceImpl(CategoriesRepository categoriesReporsitory) {
        this.categoriesReporsitory = categoriesReporsitory;
    }


    private CategoryDTO toDTO(Categories categories) {

        return new CategoryDTO(
                categories.getCategoryId(),
                categories.getCategoryName(),
                categories.getDescription(),
                categories.getStatus(),
                categories.getImageUrl(),
                categories.getCreatedAt()
        );
    }

    @Override
    public Page<CategoryDTO> getAllCategories(int page, int size, String[] sort, String keyword) {
        // --- Tạo đối tượng Pageable ---
        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        // --- Query có điều kiện ---
        Page<Categories> categoryPage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            categoryPage = categoriesReporsitory.findByCategoryNameContainingIgnoreCaseAndDeletedAtIsNull(keyword, pageable);
        } else {
            categoryPage = categoriesReporsitory.findByDeletedAtIsNull(pageable);
        }

        // --- Convert entity -> DTO ---
        return categoryPage.map(cat -> new CategoryDTO(
                cat.getCategoryId(),
                cat.getCategoryName(),
                cat.getDescription(),
                cat.getStatus(),
                cat.getImageUrl(),
                cat.getCreatedAt()
        ));

    }

    @Override
    public CategoryDTO create(CategoryDTO categoryDTO) {
        Categories category = new Categories();
        category.setCategoryName(categoryDTO.getCategoryName());
        category.setDescription(categoryDTO.getDescription());
        category.setStatus(categoryDTO.getStatus() != null ? categoryDTO.getStatus() : true);
        category.setImageUrl(categoryDTO.getImageUrl());
        category.setDeletedAt(null); // nếu có trường deletedAt (xóa mềm)
        Categories saved = categoriesReporsitory.save(category);
        return toDTO(saved);
    }

    @Override
    public CategoryDTO update(Integer id, CategoryDTO categoryDTO) {
        Categories category = categoriesReporsitory.findByCategoryIdAndDeletedAtIsNull(id);
        if (category == null) {
            throw new RuntimeException("Không tìm thấy danh mục để cập nhật");
        }
        category.setCategoryName(categoryDTO.getCategoryName());
        category.setDescription(categoryDTO.getDescription());
        category.setStatus(categoryDTO.getStatus());
        category.setImageUrl(categoryDTO.getImageUrl());
        Categories saved = categoriesReporsitory.save(category);
        return toDTO(saved);
    }

    @Override
    @Transactional
    public void softDelete(Integer id) {
        Categories category = categoriesReporsitory.findByCategoryIdAndDeletedAtIsNull(id);
        if (category == null) {
            throw new RuntimeException("Không tìm thấy danh mục để xóa");
        }

        int itemCount = (category.getItems() != null) ? category.getItems().size() : 0;
        if (itemCount > 0) {
            throw new RuntimeException("Không thể xóa danh mục này vì vẫn còn " + itemCount + " tour đang liên kết.");
        }

        category.setDeletedAt(LocalDateTime.now());
        categoriesReporsitory.save(category);
    }

    @Override
    public CategoryDTO getById(Integer id) {
        Categories categories = categoriesReporsitory.findByCategoryIdAndDeletedAtIsNull(id);
        if (categories == null) {
            throw new RuntimeException("Không tìm thấy danh mục!");
        }
        return toDTO(categories);
    }

    @Override
    @Transactional
    public String deleteMultipe(List<Integer> ids) {
        List<Categories> categories = categoriesReporsitory.findAllById(ids);
        int deletedCount = 0;
        boolean hasLockedCategory = false;

        for (Categories c : categories) {
            int itemCount = (c.getItems() != null) ? c.getItems().size() : 0;

            if (itemCount > 0) {
                hasLockedCategory = true;
                continue; // bỏ qua không xóa
            }

            c.setDeletedAt(LocalDateTime.now());
            deletedCount++;
        }

        categoriesReporsitory.saveAll(categories);

        // Chỉ trả message đơn giản
        if (hasLockedCategory && deletedCount == 0) {
            return "Không thể xóa danh mục vì chứa tour đang liên kết.";
        } else if (hasLockedCategory) {
            return "Đã xóa các danh mục hợp lệ, một số danh mục không thể xóa vì đang chứa tour.";
        }
        return "Xóa danh mục thành công.";
    }

    @Override
    public List<CategoryDTO> getAllCategoriesList() {
        List<Categories> list = categoriesReporsitory.findByDeletedAtIsNullAndStatus(true);
        return list.stream()
                .map(c -> new CategoryDTO(
                        c.getCategoryId(),
                        c.getCategoryName(),
                        c.getDescription(),
                        c.getImageUrl(),
                        c.getCreatedAt(),
                        c.getUpdatedAt()
                ))
                .collect(Collectors.toList());    }
}



