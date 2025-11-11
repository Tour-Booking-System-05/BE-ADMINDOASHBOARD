package com.travel.demo.controller;

import com.travel.demo.dto.EmployeeDTO;
import com.travel.demo.repository.EmployeesRepository;
import com.travel.demo.service.EmployeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Integer id) {
        EmployeeDTO result = employeesService.getEmployeeById(id);
        return ResponseEntity.ok(result);
    }

//    // 🔹 Sau này: lấy nhân viên đang đăng nhập (khi có JWT)
//    @GetMapping("/me")
//    public ResponseEntity<EmployeeDTO> getCurrentEmployee() {
//        EmployeeDTO result = employeesService.getCurrentEmployee();
//        return ResponseEntity.ok(result);
//    }
}
