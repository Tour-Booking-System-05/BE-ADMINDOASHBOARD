package com.travel.demo.dto;

import com.travel.demo.entity.AccountStatus;

import java.time.LocalDate;

public class EmployeeDTO {
    private Integer employeeId;
    private String fullName;
    private String phoneNumber;
    private String gender;
    private String roleName;
    private Integer roleId;
    private String email;
    private LocalDate dateOfBirth;
    private String  accountStatus;

    public EmployeeDTO(Integer employeeId, String fullName, String phoneNumber, String gender, String roleName, Integer roleId, String email, LocalDate dateOfBirth, String accountStatus) {
        this.employeeId = employeeId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.roleName = roleName;
        this.roleId = roleId;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.accountStatus = accountStatus;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public EmployeeDTO(Integer employeeId, String fullName, String phoneNumber, String gender, String roleName, Integer roleId) {
        this.employeeId = employeeId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.roleName = roleName;
        this.roleId = roleId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

}