package com.travel.demo.service.impl;

import com.travel.demo.dto.MeResponse;
import com.travel.demo.dto.RequestLogin;
import com.travel.demo.dto.ResponseLogin;
import com.travel.demo.entity.*;
import com.travel.demo.repository.AccountRepository;
import com.travel.demo.repository.EmployeesRepository;
import com.travel.demo.security.JwtUtil;
import com.travel.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional(readOnly = true)
    @Override
    public MeResponse getMe() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String email = null;
        if (auth != null && auth.getPrincipal() instanceof Accounts acc) {
            email = acc.getEmail();
        }

        Accounts account = accountRepository.findByEmailWithPermissions(email);
        if (account == null) throw new RuntimeException("Tài khoản không tồn tại");

        Employees employee = employeesRepository.findByAccountEmail(email).orElse(null);

        Roles role = account.getRoleEntity();

        List<Integer> permissionIds = List.of();
        if (role != null && role.getAdminRolePermissions() != null) {
            permissionIds = role.getAdminRolePermissions().stream()
                    .map(AdminRolePermissions::getAdminPermission)
                    .filter(p -> p != null && p.getId() != null)
                    .map(AdminPermissions::getId)
                    .distinct()
                    .toList();
        }

        return new MeResponse(
                account.getAccountId(),
                account.getEmail(),
                role != null ? role.getRoleId() : null,
                role != null ? role.getName() : null,
                permissionIds,
                employee != null ? employee.getEmployeeId() : null,
                employee != null ? employee.getFullName() : null,
                employee != null ? employee.getPhoneNumber() : null
        );
    }
}

