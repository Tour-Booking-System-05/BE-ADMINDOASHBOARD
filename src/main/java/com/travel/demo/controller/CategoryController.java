package com.travel.demo.controller;

import com.travel.demo.annotation.ActivityAudit;
import com.travel.demo.dto.CategoryDTO;
import com.travel.demo.service.CategoryService;
import com.travel.demo.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    //  Lấy danh sách tất cả danh mục
    @GetMapping
//    Là đối tượng đại diện cho phản hồi HTTP (HTTP response) mà Spring sẽ trả về cho client (bao gồm status code, body, headers, ...).
    public ResponseEntity<Page<CategoryDTO>> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "categoryId,desc") String[] sort,
            @RequestParam(required = false) String keyword
    ) {
        Page<CategoryDTO> result = categoryService.getAllCategories(page, size, sort, keyword);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @ActivityAudit(
            type = "CATEGORY",
            entityType = "CATEGORY",
            title = "Tạo danh mục mới",
            description = "Admin {actor} đã tạo danh mục mới"
    )
    public ResponseEntity<CategoryDTO> create(@RequestBody CategoryDTO categoryDTO) {
//request body -> lấy body từ request gửi lên
        return ResponseEntity.ok(categoryService.create(categoryDTO));



    }
    @ActivityAudit(
            type = "CATEGORY",
            entityType = "CATEGORY",
            entityIdParam = "id",
            title = "Cập nhật danh mục #{id}",
            description = "Admin {actor} cập nhật danh mục #{id}"
    )
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable Integer id, @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.update(id, categoryDTO));
    }

    //    Lấy giá trị từ URL path (đường dẫn) mà người dùng gọi API.
    @DeleteMapping("/{id}")
    @ActivityAudit(
            type = "CATEGORY",
            entityType = "CATEGORY",
            entityIdParam = "id",
            title = "Xoá danh mục #{id}",
            description = "Admin {actor} đã xoá danh mục #{id}"
    )
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id) {
        try {
            categoryService.softDelete(id);
            return ResponseEntity.ok(Map.of("message", "Xóa danh mục thành công"));
        } catch (RuntimeException e) {
            //  Nếu lỗi do chứa tour thì trả về 400 Bad Request với message rõ ràng
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // Lỗi khác thì vẫn báo chung
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Đã xảy ra lỗi không mong muốn"));
        }
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

    // Xóa nhiều
    @DeleteMapping("/bulk-delete")
    @ActivityAudit(
            type = "CATEGORY",
            entityType = "CATEGORY",
            entityIdParam = "id",
            title = "Xoá danh mục #{id}",
            description = "Admin {actor} đã xoá danh mục #{id}"
    )
    public ResponseEntity<?> deleteMultipe(@RequestBody List<Integer> ids) {
        try {
            String message = categoryService.deleteMultipe(ids);
            return ResponseEntity.ok(Map.of("message", message));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Lỗi khi xóa danh mục: " + e.getMessage()));
        }
    }


    @GetMapping("/all")
    public ResponseEntity<List<CategoryDTO>> getAllCategoriesList() {
        List<CategoryDTO> categories = categoryService.getAllCategoriesList();
        return ResponseEntity.ok(categories);
    }

}

