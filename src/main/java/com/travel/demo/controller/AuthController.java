package com.travel.demo.controller;

import com.travel.demo.dto.ApiResponse;
import com.travel.demo.dto.RequestLogin;
import com.travel.demo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")

public class AuthController {
    @Autowired
    private AuthService authService;
    @PostMapping("/login")
    public ApiResponse login(@Valid @RequestBody RequestLogin requestLogin){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(authService.login(requestLogin));
//        apiResponse.setData(data);
        return apiResponse;
    }

}
