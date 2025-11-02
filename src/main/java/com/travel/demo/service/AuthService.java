package com.travel.demo.service;

import com.travel.demo.dto.RequestLogin;
import org.springframework.stereotype.Service;


public interface AuthService {
    public String login(RequestLogin requestLogin);
}
