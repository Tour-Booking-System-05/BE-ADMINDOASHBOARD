package com.travel.demo.service.impl;

import com.travel.demo.dto.EmployeeCreateRequest;
import com.travel.demo.dto.EmployeeDTO;
import com.travel.demo.dto.EmployeeUpdateRequest;
import com.travel.demo.entity.*;
import com.travel.demo.repository.AccountRepository;
import com.travel.demo.repository.EmployeesRepository;
import com.travel.demo.repository.RolesRepository;
import com.travel.demo.service.EmployeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeesServiceImpl implements EmployeesService {

    @Autowired
    private EmployeesRepository employeesRepository;
    @Autowired
    private AccountRepository accountsRepository;
    @Autowired
    private RolesRepository rolesRepository;

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
        Employees e = employeesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
        if (e.getAccount() != null && e.getAccount().getDeleteAt() != null) {
            throw new RuntimeException("Tài khoản đã bị khóa / xóa");
        }
        return toDTO(e);
    }

    //  (Sau này) Lấy nhân viên đang đăng nhập
    @Override
    public EmployeeDTO getCurrentEmployee() {
        // Lấy email từ JWT token đã được Spring Security giải mã
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Employees e = employeesRepository.findByAccountEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên đang đăng nhập!"));

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
    // ====== MAPPER ======
    private EmployeeDTO toDTO(Employees e) {
        return new EmployeeDTO(
                e.getEmployeeId(),
                e.getFullName(),
                e.getPhoneNumber(),
                e.getGender() != null ? e.getGender().name() : null,
                e.getAccount() != null && e.getAccount().getRoleEntity() != null
                        ? e.getAccount().getRoleEntity().getName()
                        : null,
                e.getAccount() != null && e.getAccount().getRoleEntity() != null
                        ? e.getAccount().getRoleEntity().getRoleId()
                        : null
        );
    }
    @Override
    public EmployeeDTO createEmployee(EmployeeCreateRequest request) {
        if (accountsRepository.findByEmail(request.getEmail()) != null) {
            throw new RuntimeException("Email đã tồn tại!");
        }

        Roles role = rolesRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role không tồn tại"));
        Accounts acc = new Accounts();
        acc.setEmail(request.getEmail());
        acc.setPassword(request.getPassword());
        acc.setRole(Role.ADMIN);
        acc.setRoleEntity(role);
        accountsRepository.save(acc);

        Employees emp = new Employees();
        emp.setFullName(request.getFullName());
        emp.setPhoneNumber(request.getPhoneNumber());
        emp.setDateOfBirth(request.getDateOfBirth());
        emp.setGender(Gender.valueOf(request.getGender()));
        emp.setDescription(request.getDescription());
        emp.setAccount(acc);

        employeesRepository.save(emp);

        return toDTO(emp);
    }

    @Override
    public Page<EmployeeDTO> getAllEmployees(int page, int size, String sortBy, String direction, String keyword) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        // --- Query có điều kiện ---
        Page<Employees> employeesPage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            employeesPage = employeesRepository
                    .findByAccount_DeleteAtIsNullAndFullNameContainingIgnoreCase(keyword, pageable);
        } else {
            employeesPage = employeesRepository
                    .findByAccount_DeleteAtIsNull(pageable);
        }

        // --- Convert entity -> DTO ---
        return employeesPage.map(this::toDTO);
    }


    @Override
    public EmployeeDTO updateEmployee(Integer id, EmployeeUpdateRequest request) {
        Employees e = employeesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));

        e.setFullName(request.getFullName());
        e.setPhoneNumber(request.getPhoneNumber());
        e.setDateOfBirth(request.getDateOfBirth());
        e.setGender(request.getGender());
        e.setDescription(request.getDescription());

        if (request.getRoleId() != null) {
            Roles role = rolesRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role không tồn tại"));
            e.getAccount().setRoleEntity(role);
        }

        employeesRepository.save(e);
        return toDTO(e);
    }

    @Override
    public void deleteEmployee(Integer id) {
        Employees e = employeesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));

        if (e.getAccount() != null) {
            e.getAccount().setDeleteAt(LocalDateTime.now());
            accountsRepository.save(e.getAccount());

        }
    }
}
