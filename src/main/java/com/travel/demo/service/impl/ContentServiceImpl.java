package com.travel.demo.service.impl;

import com.travel.demo.dto.ContentDTO;
import com.travel.demo.entity.Contents;
import com.travel.demo.entity.Employees;
import com.travel.demo.repository.ContentRepository;
import com.travel.demo.repository.EmployeesRepository;
import com.travel.demo.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private EmployeesRepository employeesRepository;

    @Override
    public Page<ContentDTO> getAllContent(int page, int size, String[] sort, String keyword) {
        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        Page<Contents> contents;

        if (keyword != null && !keyword.trim().isEmpty()) {
            contents = contentRepository.findByTitleContainingIgnoreCaseAndDeletedAtIsNull(keyword, pageable);
        } else {
            contents = contentRepository.findByDeletedAtIsNull(pageable);
        }

        return contents.map(c -> new ContentDTO(
                c.getContentId(),
                c.getTitle(),
                c.getContent(),
                c.getEmployee() != null ? c.getEmployee().getEmployeeId() : null,
                c.getCreateAt(),
                c.getPublishedAt(),
                c.getStatus(),
                c.getImageUrl()
        ));
    }

    @Override
    public ContentDTO getContentById(Integer id) {
        Contents contents = contentRepository.findByContentIdAndDeletedAtIsNull(id);
        if (contents == null) {
            throw new RuntimeException("Không tìm thấy nội dung!");
        }

        ContentDTO dto = new ContentDTO();
        dto.setContentId(contents.getContentId());
        dto.setTitle(contents.getTitle());
        dto.setContent(contents.getContent());
        dto.setEmployeeId(contents.getEmployee() != null ? contents.getEmployee().getEmployeeId() : null);
        dto.setCreateAt(contents.getCreateAt());
        dto.setPublishedAt(contents.getPublishedAt());
        dto.setStatus(contents.getStatus());
        dto.setImageUrl(contents.getImageUrl());

        return dto;
    }

    @Override
    public ContentDTO createContent(ContentDTO contentDTO) {
        Contents contents = new Contents();

        contents.setTitle(contentDTO.getTitle());
        contents.setContent(contentDTO.getContent());
        contents.setImageUrl(contentDTO.getImageUrl());
        contents.setStatus(contentDTO.getStatus());
        contents.setCreateAt(LocalDateTime.now());
        contents.setPublishedAt(contentDTO.getPublishedAt());


        /*
        SAU NÀY KHI CÓ LOGIN:
        - Sử dụng Spring Security để lấy user đang đăng nhập
        - Ví dụ:

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Employees employee = employeesRepository.findByAccountUsername(username);
        contents.setEmployee(employee);
        */
        // 🔥 LẤY EMAIL ĐANG ĐĂNG NHẬP TỪ JWT
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Employees employee = employeesRepository.findByAccountEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên đang đăng nhập!"));

        contents.setEmployee(employee);
        Contents saved = contentRepository.save(contents);

        ContentDTO result = new ContentDTO();
        result.setContentId(saved.getContentId());
        result.setTitle(saved.getTitle());
        result.setContent(saved.getContent());
        result.setEmployeeId(saved.getEmployee().getEmployeeId());
        result.setCreateAt(saved.getCreateAt());
        result.setPublishedAt(saved.getPublishedAt());
        result.setStatus(saved.getStatus());
        result.setImageUrl(saved.getImageUrl());

        return result;
    }


    @Override
    public ContentDTO updateContent(Integer id, ContentDTO contentDTO) {
        Optional<Contents> optional = contentRepository.findById(id);
        if (optional.isEmpty()) {
            throw new RuntimeException("Không tìm thấy nội dung cần cập nhật!");
        }

        Contents contents = optional.get();

        contents.setTitle(contentDTO.getTitle());
        contents.setContent(contentDTO.getContent());
        contents.setImageUrl(contentDTO.getImageUrl());
        contents.setStatus(contentDTO.getStatus());
        contents.setPublishedAt(contentDTO.getPublishedAt());


        Contents updated = contentRepository.save(contents);

        ContentDTO result = new ContentDTO();
        result.setContentId(updated.getContentId());
        result.setTitle(updated.getTitle());
        result.setContent(updated.getContent());
        result.setEmployeeId(
                updated.getEmployee() != null ? updated.getEmployee().getEmployeeId() : null
        );
        result.setCreateAt(updated.getCreateAt());
        result.setPublishedAt(updated.getPublishedAt());
        result.setStatus(updated.getStatus());
        result.setImageUrl(updated.getImageUrl());

        return result;
    }

    @Override
    public void softDelete(Integer id) {
        Optional<Contents> optional = contentRepository.findById(id);
        if (optional.isEmpty()) {
            throw new RuntimeException("Không tìm thấy nội dung để xóa!");
        }

        Contents content = optional.get();
        content.setDeletedAt(LocalDateTime.now());
        contentRepository.save(content);
    }

    @Override
    public String deleteMultipe(List<Integer> ids) {
        for (Integer id : ids) {
            Optional<Contents> optional = contentRepository.findById(id);
            optional.ifPresent(c -> {
                c.setDeletedAt(LocalDateTime.now());
                contentRepository.save(c);
            });
        }
        return "Đã xóa " + ids.size() + " nội dung thành công!";
    }
}
