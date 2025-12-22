package com.travel.demo.controller;

import com.travel.demo.annotation.ActivityAudit;
import com.travel.demo.dto.EmployeeCreateRequest;
import com.travel.demo.dto.EmployeeDTO;
import com.travel.demo.dto.EmployeeUpdateRequest;
import com.travel.demo.repository.EmployeesRepository;
import com.travel.demo.service.EmployeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@CrossOrigin(origins = "*") // Cho phép gọi từ file HTML
public class EmployeeController {
    @Autowired
    private EmployeesService employeesService;
    @GetMapping("/guider")
    public ResponseEntity<List<EmployeeDTO>> getGuider(){
        List<EmployeeDTO> result = employeesService.getAdminsWithRoleId2();
        return ResponseEntity.ok(result);
    }


    @PostMapping
    @ActivityAudit(
            type = "EMPLOYEE",
            entityType = "EMPLOYEE",
            title = "Tạo nhân viên mới",
            description = "Admin {actor} đã tạo nhân viên mới"
    )
    @PreAuthorize("@permissionChecker.hasPermission(authentication,'EMPLOYEE_MANAGE')")
    public ResponseEntity<EmployeeDTO> create(@RequestBody EmployeeCreateRequest req) {
        return ResponseEntity.ok(employeesService.createEmployee(req));
    }

    // READ ALL - PAGEABLE
    @GetMapping
    @PreAuthorize("@permissionChecker.hasPermission(authentication,'EMPLOYEE_MANAGE')")
    public ResponseEntity<Page<EmployeeDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "employeeId") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(
                employeesService.getAllEmployees(page, size, sortBy, direction, keyword)
        );
    }


    // READ BY ID
    @GetMapping("/{id}")
    @PreAuthorize("@permissionChecker.hasPermission(authentication,'EMPLOYEE_MANAGE')")
    public ResponseEntity<EmployeeDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(employeesService.getEmployeeById(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    @ActivityAudit(
            type = "EMPLOYEE",
            entityType = "EMPLOYEE",
            entityIdParam = "id",
            title = "Cập nhật thông tin nhân viên #{id}",
            description = "Admin {actor} cập nhật thông tin nhân viên #{id}"
    )
    @PreAuthorize("@permissionChecker.hasPermission(authentication,'EMPLOYEE_MANAGE')")
    public ResponseEntity<EmployeeDTO> update(@PathVariable Integer id,
                                              @RequestBody EmployeeUpdateRequest req) {
        return ResponseEntity.ok(employeesService.updateEmployee(id, req));
    }

    // DELETE (soft delete account)
    @DeleteMapping("/{id}")
    @ActivityAudit(
            type = "EMPLOYEE",
            entityType = "EMPLOYEE",
            entityIdParam = "id",
            title = "Xóa nhân viên #{id}",
            description = "Admin {actor} xóa nhân viên #{id}"
    )
    @PreAuthorize("@permissionChecker.hasPermission(authentication,'EMPLOYEE_MANAGE')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        employeesService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}/reset-password")
    @ActivityAudit(
            type = "EMPLOYEE",
            entityType = "EMPLOYEE",
            entityIdParam = "id",
            title = "Cập nhật thông tin nhân viên #{id}",
            description = "Admin {actor} cập nhật thông tin nhân viên #{id}"
    )
    public ResponseEntity<?> resetPassword(@PathVariable Integer id) {
        String newPass = employeesService.resetPassword(id);
        return ResponseEntity.ok("Cập nhập mật khẩu thành công"
        );
    }
}
