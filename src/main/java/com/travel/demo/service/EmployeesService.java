package com.travel.demo.service;

import com.travel.demo.dto.EmployeeDTO;

import java.util.List;

public interface EmployeesService {
    List<EmployeeDTO> getAdminsWithRoleId2();

    EmployeeDTO getEmployeeById(Integer id);
    // 🔹 (Sau này) Lấy nhân viên đang đăng nhập
    EmployeeDTO getCurrentEmployee();

}
