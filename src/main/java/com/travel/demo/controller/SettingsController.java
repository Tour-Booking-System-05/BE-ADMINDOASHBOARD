package com.travel.demo.controller;

import com.travel.demo.dto.SettingsDTO;
import com.travel.demo.service.CloudinaryService;
import com.travel.demo.service.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/settings")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SettingsController {
    @Autowired
    private  SettingsService settingsService;
    @Autowired
    private CloudinaryService cloudinaryService;
    // READ
    @GetMapping
    public ResponseEntity<SettingsDTO> get() {
        return ResponseEntity.ok(settingsService.get());
    }

    // CREATE/UPDATE (singleton)
    @PutMapping
    public ResponseEntity<SettingsDTO> save(@RequestBody SettingsDTO dto) {
        return ResponseEntity.ok(settingsService.save(dto));
    }
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        String url = cloudinaryService.uploadImage(file);
        return ResponseEntity.ok(Map.of("url", url));
    }

}
