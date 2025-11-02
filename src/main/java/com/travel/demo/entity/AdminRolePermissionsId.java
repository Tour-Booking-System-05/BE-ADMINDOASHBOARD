package com.travel.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AdminRolePermissionsId implements Serializable {

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "admin_permission_id")
    private Integer adminPermissionId;
}
