package com.travel.demo.dto;

import com.travel.demo.entity.Role;

import java.util.List;

public class MeResponse {
    private Integer accountId;
    private String email;

    private Integer roleId;
    private String roleName;

    private List<Integer> permissionIds;

    private Integer employeeId;
    private String fullName;
    private String phoneNumber;

    public MeResponse(Integer accountId, String email, Integer roleId, String roleName, List<Integer> permissionIds, Integer employeeId, String fullName, String phoneNumber) {
        this.accountId = accountId;
        this.email = email;
        this.roleId = roleId;
        this.roleName = roleName;
        this.permissionIds = permissionIds;
        this.employeeId = employeeId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<Integer> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(List<Integer> permissionIds) {
        this.permissionIds = permissionIds;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
