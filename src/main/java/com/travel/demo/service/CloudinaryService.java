package com.travel.demo.service;

import org.apache.commons.lang3.mutable.Mutable;
import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    String uploadImage(MultipartFile file);
}
