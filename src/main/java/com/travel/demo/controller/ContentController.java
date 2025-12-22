package com.travel.demo.controller;

import com.travel.demo.annotation.ActivityAudit;
import com.travel.demo.dto.ContentDTO;
import com.travel.demo.service.CloudinaryService;
import com.travel.demo.service.ContentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/contents")
@CrossOrigin(origins = "*")
public class ContentController {
    @Autowired
    private ContentService contentService;
    @Autowired
    private CloudinaryService cloudinaryService;
    @GetMapping
    public ResponseEntity<Page<ContentDTO>> getAllContent(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "contentId, desc") String[] sort,
            @RequestParam(required = false) String keyword
    ){
        Page<ContentDTO> result = contentService.getAllContent(page, size, sort, keyword);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ContentDTO> getContentById(@PathVariable Integer id){
        return ResponseEntity.ok(contentService.getContentById(id));
    }
    @PostMapping
    @ActivityAudit(
            type = "CONTENT",
            entityType = "CONTENT",
            title = "Tạo tin tức  mới",
            description = "Admin {actor} đã tạo tin tức  mới"
    )
    public ResponseEntity<ContentDTO> createContent(@Valid  @RequestBody ContentDTO contentDTO){
        return ResponseEntity.ok(contentService.createContent(contentDTO));
    }
    @PutMapping("/{id}")
    @ActivityAudit(
            type = "CONTENT",
            entityType = "CONTENT",
            entityIdParam = "id",
            title = "Cập nhật tin tức #{id}",
            description = "Admin {actor} cập nhật tin tức #{id}"
    )
    public ResponseEntity<ContentDTO> updateContent(@PathVariable Integer id, @Valid @RequestBody ContentDTO contentDTO){
        return  ResponseEntity.ok(contentService.updateContent(id, contentDTO));
    }
    @DeleteMapping("/{id}")
    @ActivityAudit(
            type = "CONTENT",
            entityType = "CONTENT",
            entityIdParam = "id",
            title = "Xóa tin tức #{id}",
            description = "Admin {actor} xóa tin tức  #{id}"
    )
    public  ResponseEntity<?> deleteContent(@PathVariable Integer id){
        try {
            contentService.softDelete(id);
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
    @DeleteMapping("/bulk-delete")
    @ActivityAudit(
            type = "CONTENT",
            entityType = "CONTENT",
            entityIdParam = "id",
            title = "Xóa tin tức #{id}",
            description = "Admin {actor} xóa tin tức  #{id}"
    )
    public ResponseEntity<?> deleteMultipe(@RequestBody List<Integer> ids) {
        try {
            String message = contentService.deleteMultipe(ids);
            return ResponseEntity.ok(Map.of("message", message));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Lỗi khi xóa content: " + e.getMessage()));
        }
    }
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        String url = cloudinaryService.uploadImage(file);
        return ResponseEntity.ok(Map.of("url", url));
    }
}
