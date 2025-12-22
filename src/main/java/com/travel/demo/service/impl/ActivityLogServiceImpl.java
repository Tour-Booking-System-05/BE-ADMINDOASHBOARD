package com.travel.demo.service.impl;

import com.travel.demo.dto.ActivityDTO;
import com.travel.demo.entity.ActivityLog;
import com.travel.demo.repository.ActivityLogRepository;
import com.travel.demo.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ActivityLogServiceImpl implements ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    @Override
    public Page<ActivityDTO> getRecentActivities(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return activityLogRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(this::toDTO);
    }

    private ActivityDTO toDTO(ActivityLog log) {
        ActivityDTO dto = new ActivityDTO();
        BeanUtils.copyProperties(log, dto);

        dto.setDisplayTime(timeAgo(log.getCreatedAt()));
        dto.setColor(resolveColor(log.getType(), log.getAction()));

        return dto;
    }

    private String resolveColor(String type, String action) {
        if ("ORDER".equalsIgnoreCase(type)) {
            if ("CANCELED".equalsIgnoreCase(action)) return "yellow";
            return "red";
        }
        if ("TOUR".equalsIgnoreCase(type)) return "blue";
        if ("USER".equalsIgnoreCase(type)) return "green";
        return "gray";
    }

    private String timeAgo(LocalDateTime createdAt) {
        Duration d = Duration.between(createdAt, LocalDateTime.now());
        if (d.toMinutes() < 60) return d.toMinutes() + " phút";
        if (d.toHours() < 24) return d.toHours() + " giờ";
        return d.toDays() + " ngày";
    }
}
