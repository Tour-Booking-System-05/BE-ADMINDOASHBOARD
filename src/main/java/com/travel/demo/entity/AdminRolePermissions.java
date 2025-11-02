package com.travel.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "admin_role_permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminRolePermissions {

    @EmbeddedId
    private AdminRolePermissionsId id;

    // Liên kết với Roles
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    private Roles role;

    // Liên kết với AdminPermissions
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("adminPermissionId")
    @JoinColumn(name = "admin_permission_id")
    private AdminPermissions adminPermission;
}
