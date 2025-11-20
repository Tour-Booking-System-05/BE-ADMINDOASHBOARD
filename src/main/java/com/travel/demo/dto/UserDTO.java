package com.travel.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;


public class UserDTO {

    private Integer userId; // Mã khách hàng (KHxxx)

    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(max = 45, message = "Tên đăng nhập không vượt quá 45 ký tự")
    private String username;

    @NotBlank(message = "Tên khách hàng không được để trống")
    @Size(max = 255, message = "Tên khách hàng vượt quá 255 ký tự")
    private String fullname;

    private LocalDate dateOfBirth;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Size(max = 20, message = "Số điện thoại không vượt quá 20 ký tự")
    private String phoneNumber;

    // Email nằm trong Accounts
    @NotBlank(message = "Email không được để trống")
    private String email;

    // Hạng vip (BRONZE, SILVER, GOLD, DIAMOND)
    private String userRank;

    // Trạng thái (ACTIVE, INACTIVE)
    private String status;

    // Nếu cần lấy accountId cho BE xử lý
    private Integer accountId;

    public UserDTO(Integer userId, String username, String fullname, LocalDate dateOfBirth, String phoneNumber, String email, String userRank, String status, Integer accountId) {
        this.userId = userId;
        this.username = username;
        this.fullname = fullname;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.userRank = userRank;
        this.status = status;
        this.accountId = accountId;
    }

    public UserDTO() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserRank() {
        return userRank;
    }

    public void setUserRank(String userRank) {
        this.userRank = userRank;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }
}
