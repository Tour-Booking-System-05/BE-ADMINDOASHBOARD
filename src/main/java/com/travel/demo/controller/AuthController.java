package com.travel.demo.controller;

import com.travel.demo.dto.ApiResponse;
import com.travel.demo.dto.RequestLogin;
import com.travel.demo.dto.ResponseLogin;
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
    public ApiResponse login(@Valid @RequestBody RequestLogin requestLogin) {

        ApiResponse api = new ApiResponse();

        try {
            ResponseLogin responseLogin = authService.login(requestLogin);

            api.setMessage("Đăng nhập thành công");
            api.setData(responseLogin);

        } catch (Exception e) {

            api.setMessage(e.getMessage());
            api.setData(null);
        }

        return api;
    }
    @GetMapping("/me")
    public ApiResponse getMe() {
        ApiResponse api = new ApiResponse();
        api.setMessage("Lấy thông tin thành công");
        api.setData(authService.getMe());
        return api;
    }
    @PostMapping("/logout")
    public ApiResponse logout() {
        ApiResponse api = new ApiResponse();
        api.setMessage("Đăng xuất thành công");
        api.setData(null);
        return api;
    }

}
