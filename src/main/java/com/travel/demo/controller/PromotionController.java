package com.travel.demo.controller;

import com.travel.demo.annotation.ActivityAudit;
import com.travel.demo.dto.PromotionDTO;
import com.travel.demo.service.PromotionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/promotions")
@CrossOrigin(origins = "*")
public class PromotionController {
    @Autowired
    private PromotionService promotionService;
    @GetMapping
    public ResponseEntity<Page<PromotionDTO>> getAllPromotion(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "promotionId, desc") String[] sort,
            @RequestParam(required = false) String keyword
    ){
        Page<PromotionDTO> result = promotionService.getAllPromotions(page, size, sort, keyword);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/{id}")
    public ResponseEntity<PromotionDTO> getPromotionById(@PathVariable Integer id){
        return ResponseEntity.ok(promotionService.getPromotionById(id));
    }

    @DeleteMapping("/{id}")
    @ActivityAudit(
            type = "PROMOTION",
            entityType = "PROMOTION",
            entityIdParam = "id",
            title = "Xóa mã khuyến mãi #{id}",
            description = "Admin {actor} xóa mã khuyến mãi#{id}"
    )
    public ResponseEntity<String> deletePromotionById(@PathVariable Integer id){
        promotionService.deletePromotionById(id);
        return ResponseEntity.ok( "Đã xóa thành công " + id);
    }
    @PostMapping
    @ActivityAudit(
            type = "PROMOTION",
            entityType = "PROMOTION",
            title = "Tạo mã khuyến mãi #{id}",
            description = "Admin {actor} tạo mã khuyến mãi#{id}"
    )
    public ResponseEntity<PromotionDTO> createPromotion(@Valid @RequestBody PromotionDTO promotionDTO){
        return ResponseEntity.ok(promotionService.createPromotion(promotionDTO));
    }
    @PutMapping("/{id}")
    @ActivityAudit(
            type = "PROMOTION",
            entityType = "PROMOTION",
            entityIdParam = "id",
            title = "Chỉnh sửa mã khuyến mãi #{id}",
            description = "Admin {actor} cập nhật thông tin khuyến mãi#{id}"
    )
    public ResponseEntity<PromotionDTO> updatePromotion(@PathVariable Integer id,@Valid @RequestBody PromotionDTO promotionDTO){
        return ResponseEntity.ok(promotionService.updatePromotion(id,promotionDTO));
    }
    @DeleteMapping("/bulk-delete")
    @ActivityAudit(
            type = "PROMOTION",
            entityType = "PROMOTION",
            entityIdParam = "id",
            title = "Xóa mã khuyến mãi #{id}",
            description = "Admin {actor} xóa mã khuyến mãi#{id}"
    )
    public ResponseEntity<Void> deleteMultiple(@RequestBody List<Integer> ids) {
        promotionService.deleteMultipe(ids);
        return ResponseEntity.noContent().build();
    }
}
