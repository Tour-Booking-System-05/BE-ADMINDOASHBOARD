package com.travel.demo.service;

import com.travel.demo.dto.CategoryDTO;
import com.travel.demo.entity.Categories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    //lay all category
    Page<CategoryDTO> getAllCategories(int page, int size, String[] sort, String keyword);
    //create category
    CategoryDTO create(CategoryDTO categoryDTO);
    //update category
    CategoryDTO update(Integer id,CategoryDTO categoryDTO);
    //xoa meem
    void softDelete(Integer id);
    CategoryDTO getById(Integer id);
    String deleteMultipe(List<Integer> ids);
    List<CategoryDTO> getAllCategoriesList();

}
