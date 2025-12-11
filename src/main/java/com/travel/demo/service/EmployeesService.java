package com.travel.demo.service;

import com.travel.demo.dto.EmployeeCreateRequest;
import com.travel.demo.dto.EmployeeDTO;
import com.travel.demo.dto.EmployeeUpdateRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeesService {
    List<EmployeeDTO> getAdminsWithRoleId2();

    EmployeeDTO getEmployeeById(Integer id);
    EmployeeDTO getCurrentEmployee();
    EmployeeDTO createEmployee(EmployeeCreateRequest request);

    Page<EmployeeDTO> getAllEmployees(int page, int size, String sortBy, String direction, String keyword);

    EmployeeDTO updateEmployee(Integer id, EmployeeUpdateRequest request);

    void deleteEmployee(Integer id);
}
