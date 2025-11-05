package com.travel.demo.controller;

import com.travel.demo.dto.EmployeeDTO;
import com.travel.demo.repository.EmployeesRepository;
import com.travel.demo.service.EmployeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
