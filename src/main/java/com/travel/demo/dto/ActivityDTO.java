package com.travel.demo.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ActivityDTO {
    private Long id;
    private String type;
    private String action;
    private String title;
    private String description;
    private String entityType;
    private Long entityId;
    private String actorEmail;
    private String actorRole;
    private boolean isAdmin;
    private String method;
    private String uri;
    private String ipAddress;
    private LocalDateTime createdAt;
    private String displayTime;   // "32 phút", "2 giờ", "1 ngày"
    private String color;         // green / red / blue / yellow

}
