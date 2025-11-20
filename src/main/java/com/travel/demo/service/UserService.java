package com.travel.demo.service;

import com.travel.demo.dto.UserDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    Page<UserDTO> getAllUsers(int page, int size, String[] sort, String keyword);

    UserDTO getUserById(Integer id);

    UserDTO delelete(Integer id);

    String  deleteMultipe(List<Integer> ids);

    String resetPassword(Integer id);

    UserDTO updateUser(Integer id, @Valid UserDTO userDTO);
}
