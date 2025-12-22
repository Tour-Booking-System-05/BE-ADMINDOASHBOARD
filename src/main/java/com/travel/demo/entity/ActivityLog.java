package com.travel.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "activity_logs")
@Getter @Setter
@NoArgsConstructor
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ADMIN_API / ORDER_CREATED / USER_REGISTERED ...
    @Column(nullable = false, length = 50)
    private String type;

    // CREATE/UPDATE/DELETE/READ/LOGIN/LOGOUT...
    @Column(length = 30)
    private String action;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "entity_type", length = 30)
    private String entityType;

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "actor_email", length = 120)
    private String actorEmail;

    @Column(name = "actor_role", length = 30)
    private String actorRole;

    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin;

    @Column(length = 10)
    private String method;

    @Column(length = 255)
    private String uri;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
