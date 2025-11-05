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
    public void softDelete(Integer id) {
        Categories category = categoriesReporsitory.findByCategoryIdAndDeletedAtIsNull(id);
        if (category == null) {
            throw new RuntimeException("Không tìm thấy danh mục để xóa");

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
    public void deleteMultipe(List<Integer> ids) {
        List<Categories> categories = categoriesReporsitory.findAllById(ids);
        for (Categories c : categories) {
            c.setDeletedAt(LocalDateTime.now());
        }
        categoriesReporsitory.saveAll(categories);
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



