package com.travel.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerVipDTO {
    private long bronze;
    private long silver;
    private long gold;
    private long diamond;
}
