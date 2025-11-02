package com.travel.demo.service.impl;

import com.travel.demo.dto.RequestLogin;
import com.travel.demo.entity.Accounts;
import com.travel.demo.entity.Role;
import com.travel.demo.repository.AccountRepository;
import com.travel.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public String login(RequestLogin requestLogin) {
        String email = requestLogin.getEmail();
        String password = requestLogin.getPassword();
        Accounts account = accountRepository.findByEmail(email);
        if (account == null) {
            return "Sai thông tin đăng nhập";
        } else if (!account.getPassword().equals(password)) {
            return "Sai mật khẩu!";
        }else if(account.getRole() != Role.ADMIN){
            return "Vui lòng đăng ký tài khoản admin";
        }
        else {
            return "Đăng nhập thành công!";
        }
    }
}
