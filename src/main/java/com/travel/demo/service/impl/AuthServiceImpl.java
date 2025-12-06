package com.travel.demo.service.impl;

import com.travel.demo.dto.MeResponse;
import com.travel.demo.dto.RequestLogin;
import com.travel.demo.dto.ResponseLogin;
import com.travel.demo.entity.Accounts;
import com.travel.demo.entity.Employees;
import com.travel.demo.entity.Role;
import com.travel.demo.repository.AccountRepository;
import com.travel.demo.repository.EmployeesRepository;
import com.travel.demo.security.JwtUtil;
import com.travel.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    EmployeesRepository employeesRepository;
    @Override
    public ResponseLogin login(RequestLogin requestLogin) {
        Accounts account = accountRepository.findByEmail(requestLogin.getEmail());

        if (account == null) {
            throw new RuntimeException("Email không tồn tại");
        }

        // ⚠️ NOTE: sau này nên dùng PasswordEncoder
        if (!account.getPassword().equals(requestLogin.getPassword())) {
            throw new RuntimeException("Sai mật khẩu");
        }

        if (account.getRole() != Role.ADMIN) {
            throw new RuntimeException("Không có quyền admin");
        }

        String token = jwtUtil.generateToken(
                account.getEmail(),
                account.getRole()
        );
        return new ResponseLogin(
                account.getAccountId(),
                account.getEmail(),
                token
        );
    }

    @Override
    public MeResponse getMe() {

        // Lấy email từ token JWT đã được JwtFilter giải mã
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Accounts account = accountRepository.findByEmail(email);
        if (account == null) {
            throw new RuntimeException("Tài khoản không tồn tại");
        }

        Employees employee = employeesRepository.findByAccountEmail(email)
                .orElse(null);

        return new MeResponse(
                account.getAccountId(),
                account.getEmail(),
                account.getRole(),
                employee != null ? employee.getEmployeeId() : null,
                employee != null ? employee.getFullName() : null,
                employee != null ? employee.getPhoneNumber() : null
        );
    }
}

