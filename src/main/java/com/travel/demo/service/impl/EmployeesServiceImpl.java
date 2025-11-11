package com.travel.demo.service.impl;

import com.travel.demo.dto.EmployeeDTO;
import com.travel.demo.entity.Employees;
import com.travel.demo.entity.Role;
import com.travel.demo.repository.EmployeesRepository;
import com.travel.demo.service.EmployeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeesServiceImpl implements EmployeesService {

    @Autowired
    private EmployeesRepository employeesRepository;

    // 🔹 Lấy danh sách nhân viên có role_id = 2 (ví dụ hướng dẫn viên)
    @Override
    public List<EmployeeDTO> getAdminsWithRoleId2() {
        List<Employees> employees = employeesRepository.findByDeletedAtIsNull();

        return employees.stream()
                .filter(e ->
                        e.getAccount() != null &&
                                e.getAccount().getRoleEntity() != null &&
                                e.getAccount().getDeleteAt() == null &&
                                e.getAccount().getRoleEntity().getRoleId() == 2 &&
                                e.getAccount().getRole() == Role.ADMIN
                )
                .map(e -> new EmployeeDTO(
                        e.getEmployeeId(),
                        e.getFullName(),
                        e.getPhoneNumber(),
                        e.getGender() != null ? e.getGender().name() : null,
                        e.getAccount().getRoleEntity().getName()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO getEmployeeById(Integer id) {
        Optional<Employees> optional = employeesRepository.findById(id);
        if (optional.isEmpty()) {
            throw new RuntimeException("Không tìm thấy nhân viên!");
        }

        Employees e = optional.get();
        return new EmployeeDTO(
                e.getEmployeeId(),
                e.getFullName(),
                e.getPhoneNumber(),
                e.getGender() != null ? e.getGender().name() : null,
                e.getAccount() != null && e.getAccount().getRoleEntity() != null
                        ? e.getAccount().getRoleEntity().getName()
                        : null
        );
    }

    //  (Sau này) Lấy nhân viên đang đăng nhập
    @Override
    public EmployeeDTO getCurrentEmployee() {
        /*
         String username = SecurityContextHolder.getContext().getAuthentication().getName();
         Employees e = employeesRepository.findByAccountUsername(username);
         return new EmployeeDTO(
             e.getEmployeeId(),
             e.getFullName(),
             e.getPhoneNumber(),
             e.getGender() != null ? e.getGender().name() : null,
             e.getAccount().getRoleEntity().getName()
         );
        */

        // 🔹 Tạm thời: trả về nhân viên mặc định ID = 1
        return getEmployeeById(1);
    }
}
