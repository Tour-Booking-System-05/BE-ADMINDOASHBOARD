package com.travel.demo.service.impl;

import com.travel.demo.dto.SettingsDTO;
import com.travel.demo.entity.Settings;
import com.travel.demo.repository.SettingsRepository;
import com.travel.demo.service.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettingsServiceImpl implements SettingsService {

    private final SettingsRepository settingsRepo;

    @Override
    public SettingsDTO get() {
        // singleton: lấy row đầu tiên, nếu chưa có thì auto-create 1 row rỗng
        Settings s = settingsRepo.findAll().stream()
                .findFirst()
                .orElseGet(() -> settingsRepo.save(new Settings()));

        return toDto(s);
    }

    @Override
    public SettingsDTO save(SettingsDTO dto) {
        // singleton: update row đầu tiên; nếu dto có id thì update theo id
        Settings s;

        if (dto.getSettingId() != null) {
            s = settingsRepo.findById(dto.getSettingId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy settings id=" + dto.getSettingId()));
        } else {
            s = settingsRepo.findAll().stream().findFirst().orElse(new Settings());
        }

        s.setCompanyName(dto.getCompanyName());
        s.setCompanyTax(dto.getCompanyTax());
        s.setCompanyAddress(dto.getCompanyAddress());
        s.setPhone(dto.getPhone());
        s.setCompanyEmail(dto.getCompanyEmail());
        s.setUrlLogo(dto.getUrlLogo()); // ✅ url từ Cloudinary

        return toDto(settingsRepo.save(s));
    }

    private SettingsDTO toDto(Settings s) {
        return new SettingsDTO(
                s.getSettingId(),
                s.getCompanyName(),
                s.getCompanyTax(),
                s.getCompanyAddress(),
                s.getPhone(),
                s.getCompanyEmail(),
                s.getUrlLogo()
        );
    }
}
