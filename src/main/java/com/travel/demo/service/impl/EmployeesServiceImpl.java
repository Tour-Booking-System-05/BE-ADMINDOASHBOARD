package com.travel.demo.service.impl;

import com.travel.demo.dto.EmployeeDTO;
import com.travel.demo.entity.Employees;
import com.travel.demo.entity.Role;
import com.travel.demo.repository.EmployeesRepository;
import com.travel.demo.service.EmployeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeesServiceImpl implements EmployeesService {
    @Autowired
    private EmployeesRepository employeesRepository;
    @Override
    public List<EmployeeDTO> getAdminsWithRoleId2() {
        List<Employees> employees = employeesRepository.findByDeletedAtIsNull();
        return employees.stream()
                .filter(e -> e.getAccount() != null
                        && e.getAccount().getRoleEntity() != null
                        && e.getAccount().getDeleteAt() == null
                        && e.getAccount().getRoleEntity().getRoleId() == 2
                        && e.getAccount().getRole() == Role.ADMIN)
                .map(e -> new EmployeeDTO(
                        e.getEmployeeId(),
                        e.getFullName(),
                        e.getPhoneNumber(),
                        e.getGender() != null ? e.getGender().name() : null,
                        e.getAccount().getRoleEntity().getName()
                ))
                .collect(Collectors.toList());
    }
}
