package com.travel.demo.service.impl;

import com.travel.demo.dto.UserDTO;
import com.travel.demo.entity.AccountStatus;
import com.travel.demo.entity.Accounts;
import com.travel.demo.entity.Role;
import com.travel.demo.entity.Users;
import com.travel.demo.repository.AccountRepository;
import com.travel.demo.repository.UserRepository;
import com.travel.demo.service.EmailService;
import com.travel.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private EmailService emailService;
    @Override
    public Page<UserDTO> getAllUsers(int page, int size, String[] sort, String keyword) {
        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        Page<Users> users;
        if (keyword != null && !keyword.trim().isEmpty()) {
            users =
                    userRepository.findByFullnameContainingIgnoreCaseAndAccount_DeleteAtIsNullAndAccount_RoleAndAccount_IsAdminRoot(
                            keyword.trim(),
                            Role.USER,
                            false,
                            pageable
                    );
        } else {
            users =
                    userRepository.findByAccount_DeleteAtIsNullAndAccount_RoleAndAccount_IsAdminRoot(
                            Role.USER,
                            false,
                            pageable
                    );
        }

        return users.map(user -> new UserDTO(
                user.getUserId(),
                user.getUsername(),
                user.getFullname(),
                user.getDateOfBirth(),
                user.getPhoneNumber(),
                user.getAccount().getEmail(),
                user.getUserRank().name(),
                user.getAccount().getStatus().name(),
                user.getAccount().getAccountId()
        ));
    }
    @Override
    public UserDTO getUserById(Integer id) {
        Users user = userRepository
                .findByUserIdAndAccount_DeleteAtIsNullAndAccount_RoleAndAccount_IsAdminRoot(
                        id,
                        Role.USER,
                        false
                );

        if (user == null) {
            throw new RuntimeException("Không tìm người dùng");
        }

        return new UserDTO(
                user.getUserId(),
                user.getUsername(),
                user.getFullname(),
                user.getDateOfBirth(),
                user.getPhoneNumber(),
                user.getAccount().getEmail(),
                user.getUserRank().name(),
                user.getAccount().getStatus().name(),
                user.getAccount().getAccountId()
        );
    }


    @Override
    public UserDTO delelete(Integer id) {

        Users user = userRepository
                .findByUserIdAndAccount_DeleteAtIsNullAndAccount_RoleAndAccount_IsAdminRoot(
                        id,
                        Role.USER,
                        false
                );

        if (user == null) {
            throw new RuntimeException("Không tìm thấy user!");
        }

        // XÓA MỀM: set deleted_at vào account
        Accounts acc = user.getAccount();
        acc.setDeleteAt(LocalDateTime.now());
        accountRepository.save(acc);


        return new UserDTO(
                user.getUserId(),
                user.getUsername(),
                user.getFullname(),
                user.getDateOfBirth(),
                user.getPhoneNumber(),
                user.getAccount().getEmail(),
                user.getUserRank().name(),
                user.getAccount().getStatus().name(),
                user.getAccount().getAccountId()
        );
    }

    @Override
    public String deleteMultipe(List<Integer> ids) {
        ids.forEach(this::delelete);
        return "Xóa thành công " + ids.size() + " mục.";

    }

    private String generateStrongPassword() {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String special = "!@#$%^&*()_+";

        SecureRandom random = new SecureRandom();

        List<Character> chars = new ArrayList<>();
        chars.add(upper.charAt(random.nextInt(upper.length())));
        chars.add(lower.charAt(random.nextInt(lower.length())));
        chars.add(digits.charAt(random.nextInt(digits.length())));
        chars.add(special.charAt(random.nextInt(special.length())));

        // tổng 6 ký tự
        String all = upper + lower + digits + special;
        for (int i = 0; i < 2; i++) {
            chars.add(all.charAt(random.nextInt(all.length())));
        }

        Collections.shuffle(chars);

        return chars.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    @Override
    public String resetPassword(Integer id) {

        Users user = userRepository
                .findByUserIdAndAccount_DeleteAtIsNullAndAccount_RoleAndAccount_IsAdminRoot(
                        id,
                        Role.USER,
                        false
                );
        if (user == null) {
            throw new RuntimeException("Không tìm thấy user!");
        }

        Accounts acc = user.getAccount();

        // Tạo mật khẩu mạnh
        String newPass = generateStrongPassword();

        // Lưu mật khẩu (có thể encode sau này)
        acc.setPassword(newPass);
        accountRepository.save(acc);

        // 🔥 Gửi email thông báo mật khẩu mới
            emailService.sendResetPasswordEmail(acc.getEmail(), newPass);
        return newPass;
    }

    @Override
    public UserDTO updateUser(Integer id, UserDTO userDTO) {

        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user cần cập nhật!"));

        Accounts account = user.getAccount();
        if (account == null) {
            throw new RuntimeException("User không có account gắn kèm!");
        }

        // Lấy trạng thái FE gửi lên (String)
        String newStatus = userDTO.getStatus();   // ACTIVE / INACTIVE

        // Kiểm tra hợp lệ
        if (!"ACTIVE".equalsIgnoreCase(newStatus) &&
                !"INACTIVE".equalsIgnoreCase(newStatus)) {
            throw new RuntimeException("Trạng thái không hợp lệ! Chỉ nhận ACTIVE hoặc INACTIVE");
        }

        // Cập nhật trạng thái
        if ("ACTIVE".equalsIgnoreCase(newStatus)) {
            account.setStatus(AccountStatus.ACTIVE);
        } else {
            account.setStatus(AccountStatus.INACTIVE);
        }

        accountRepository.save(account);

        // Tạo DTO trả về
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setFullname(user.getFullname());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setEmail(account.getEmail());

        dto.setUserRank(
                user.getUserRank() != null
                        ? user.getUserRank().name()
                        : null
        );

        dto.setStatus(
                account.getStatus() != null
                        ? account.getStatus().name()
                        : null
        );

        dto.setAccountId(account.getAccountId());

        return dto;
    }

}
