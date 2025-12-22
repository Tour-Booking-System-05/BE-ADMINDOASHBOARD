package com.travel.demo.controller;

import com.travel.demo.annotation.ActivityAudit;
import com.travel.demo.dto.UserDTO;
import com.travel.demo.service.UserService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*") // Cho phép gọi từ file HTML
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "userId,desc") String[] sort,
            @RequestParam(required = false) String keyword
    ){
        Page<UserDTO> result = userService.getAllUsers(page, size,sort, keyword);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}/reset-password")
    public ResponseEntity<?> resetPassword(@PathVariable Integer id) {
        String newPass = userService.resetPassword(id);
        return ResponseEntity.ok("Cập nhập mật khẩu thành công"
        );
    }

    @DeleteMapping("/{id}")
    @ActivityAudit(
            type = "USER",
            entityType = "USER",
            entityIdParam = "id",
            title = "Xóa khách hàng #{id}",
            description = "Admin {actor} xóa khách hàng #{id}"
    )
    public ResponseEntity<UserDTO> deleteUser(@PathVariable Integer id){
        return ResponseEntity.ok(userService.delelete(id));
    }
    @DeleteMapping("/bulk-delete")
    @ActivityAudit(
            type = "USER",
            entityType = "USER",
            entityIdParam = "id",
            title = "Xóa khách hàng #{id}",
            description = "Admin {actor} xóa khách hàng #{id}"
    )
    public ResponseEntity<?> deleteMultipe(@RequestBody List<Integer> ids) {
        try {
            String message = userService.deleteMultipe(ids);
            return ResponseEntity.ok(Map.of("message", message));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Lỗi khi xóa content: " + e.getMessage()));
        }
    }
    @PutMapping("/{id}")
    @ActivityAudit(
            type = "USER",
            entityType = "USER",
            entityIdParam = "id",
            title = "Cập nhật khách hàng #{id}",
            description = "Admin {actor} cập nhật khách hàng  #{id}"
    )
    public  ResponseEntity<UserDTO> updateUser(@PathVariable Integer id, @Valid @RequestBody UserDTO userDTO){
        return ResponseEntity.ok(userService.updateUser(id, userDTO));
    }
}
