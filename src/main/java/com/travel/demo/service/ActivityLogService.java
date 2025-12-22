package com.travel.demo.service;

import com.travel.demo.dto.ActivityDTO;
import org.springframework.data.domain.Page;

public interface ActivityLogService {
    Page<ActivityDTO> getRecentActivities(int page, int size);
}
