package com.travel.demo.controller;

import com.travel.demo.dto.TourDTO;
import com.travel.demo.service.CloudinaryService;
import com.travel.demo.service.ItemService;
import com.travel.demo.service.impl.ItemServiceImpl;
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
@RequestMapping("/api/v1/tours")
@CrossOrigin(origins = "*") // Cho phép gọi từ file HTML

public class TourController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private CloudinaryService cloudinaryService;
    @GetMapping
    public ResponseEntity<Page<TourDTO>> getAllTour(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "itemId,desc") String[] sort,
            @RequestParam(required = false) String keyword
    ){
        Page<TourDTO> result = itemService.getAllTour(page, size, sort, keyword);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        String url = cloudinaryService.uploadImage(file);
        return ResponseEntity.ok(Map.of("url", url));
    }
    @PostMapping
    public ResponseEntity<TourDTO> create (@Valid @RequestBody TourDTO tourDTO){
//request body -> lấy body từ request gửi lên
        return ResponseEntity.ok(itemService.create(tourDTO));
    }
    @GetMapping("/{id}")
    public ResponseEntity<TourDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(itemService.getById(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<TourDTO> update(@PathVariable Integer id, @RequestBody TourDTO tourDTO){
        return ResponseEntity.ok(itemService.update(id, tourDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id){
        itemService.softDelete(id);
        return ResponseEntity.ok(("Done" + id));
    }
    @DeleteMapping("/bulk-delete")
    public ResponseEntity<Void> deleteMultiple(@RequestBody List<Integer> ids) {
        itemService.deleteMultipe(ids);
        return ResponseEntity.noContent().build();
    }

    // API clone tour
    @PostMapping("/{id}/clone")
    public ResponseEntity<TourDTO> cloneTour(@PathVariable Integer id) {
        TourDTO cloned = itemService.cloneTour(id);
        return ResponseEntity.ok(cloned);
    }
}
