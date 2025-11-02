package com.travel.demo.controller;

import com.travel.demo.dto.CategoryDTO;
import com.travel.demo.entity.Categories;
import com.travel.demo.service.CategoryService;
import com.travel.demo.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/categories")
@CrossOrigin(origins = "*") // Cho phép gọi từ file HTML

public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CloudinaryService cloudinaryService;

    //    public CategoryController(CategoryService categoryService, CloudinaryService cloudinaryService) {
//        this.categoryService = categoryService;
//        this.cloudinaryService = cloudinaryService;
//    }
    // ✅ Lấy danh sách tất cả danh mục
    @GetMapping
//    Là đối tượng đại diện cho phản hồi HTTP (HTTP response) mà Spring sẽ trả về cho client (bao gồm status code, body, headers, ...).
    public ResponseEntity<Page<CategoryDTO>> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "categoryId,asc") String[] sort,
            @RequestParam(required = false) String keyword
    ) {
        Page<CategoryDTO> result = categoryService.getAllCategories(page, size, sort, keyword);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> create(@RequestBody CategoryDTO categoryDTO) {
//request body -> lấy body từ request gửi lên
        return ResponseEntity.ok(categoryService.create(categoryDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable Integer id, @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.update(id, categoryDTO));
    }

    //    👉 Lấy giá trị từ URL path (đường dẫn) mà người dùng gọi API.
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        categoryService.softDelete(id);
        return ResponseEntity.ok(("Done" + id));
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        String url = cloudinaryService.uploadImage(file);
        return ResponseEntity.ok(Map.of("url", url));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    // xóa nhiều
    @DeleteMapping("/bulk-delete")
//Còn <?> nghĩa là generic type chưa xác định
    public ResponseEntity<?> deleteMultipe(@RequestBody List<Integer> ids) {
        try {
            categoryService.deleteMultipe(ids);
            return ResponseEntity.ok().body("Đã xóa " + ids.size() + " danh mục");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi xóa danh mục: " + e.getMessage());
        }
    }
}

