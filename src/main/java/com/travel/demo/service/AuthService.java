package com.travel.demo.service;

import com.travel.demo.dto.MeResponse;
import com.travel.demo.dto.RequestLogin;
import com.travel.demo.dto.ResponseLogin;
import org.springframework.stereotype.Service;


public interface AuthService {
    ResponseLogin login(RequestLogin requestLogin);
    MeResponse getMe();

}
