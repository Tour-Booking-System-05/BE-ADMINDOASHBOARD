package com.travel.demo.service;

import com.travel.demo.dto.SettingsDTO;

public interface SettingsService {
    SettingsDTO get();
    SettingsDTO save(SettingsDTO dto);
}
