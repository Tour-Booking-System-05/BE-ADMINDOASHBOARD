package com.travel.demo.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class SettingsDTO {
    private Integer settingId;
    private String companyName;
    private String companyTax;
    private String companyAddress;
    private String phone;
    private String companyEmail;
    private String urlLogo;
}
