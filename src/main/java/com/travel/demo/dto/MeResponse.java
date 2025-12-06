package com.travel.demo.dto;

import com.travel.demo.entity.Role;

public class MeResponse {
    private Integer accountId;
    private String email;
    private Role role;
    private Integer employeeId;
    private String fullName;
    private String phoneNumber;

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    public MeResponse(Integer accountId, String email, Role role, Integer employeeId, String fullName, String phoneNumber) {
        this.accountId = accountId;
        this.email = email;
        this.role = role;
        this.employeeId = employeeId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }
}
