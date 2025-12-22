package com.travel.demo.service.impl;

import com.travel.demo.dto.EmployeeCreateRequest;
import com.travel.demo.dto.EmployeeDTO;
import com.travel.demo.dto.EmployeeUpdateRequest;
import com.travel.demo.entity.*;
import com.travel.demo.repository.AccountRepository;
import com.travel.demo.repository.EmployeesRepository;
import com.travel.demo.repository.RolesRepository;
import com.travel.demo.service.EmailService;
import com.travel.demo.service.EmployeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeesServiceImpl implements EmployeesService {

    @Autowired
    private EmployeesRepository employeesRepository;
    @Autowired
    private AccountRepository accountsRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private EmailService emailService;
    // 🔹 Lấy danh sách nhân viên có role_id = 2 (ví dụ hướng dẫn viên)
    @Override
    public List<EmployeeDTO> getAdminsWithRoleId2() {
        List<Employees> employees = employeesRepository.findByDeletedAtIsNull();

        return employees.stream()
                .filter(e ->
                        e.getAccount() != null &&
                                e.getAccount().getRoleEntity() != null &&
                                e.getAccount().getDeleteAt() == null &&
                                e.getAccount().getRoleEntity().getRoleId() == 5 &&
                                e.getAccount().getRole() == Role.ADMIN
                )
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO getEmployeeById(Integer id) {
        Employees e = employeesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
        if (e.getAccount() != null && e.getAccount().getDeleteAt() != null) {
            throw new RuntimeException("Tài khoản đã bị khóa / xóa");
        }
        return toDTO(e);
    }

    //  (Sau này) Lấy nhân viên đang đăng nhập
    @Override
    public EmployeeDTO getCurrentEmployee() {
        Accounts current = currentAccount();
        Employees e = employeesRepository.findByAccount_AccountId(current.getAccountId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
        return toDTO(e);
    }
    // ====== MAPPER ======
    private EmployeeDTO toDTO(Employees e) {
        String email = (e.getAccount() != null) ? e.getAccount().getEmail() : null;
        String roleName = (e.getAccount() != null && e.getAccount().getRoleEntity() != null)
                ? e.getAccount().getRoleEntity().getName()
                : null;
        Integer roleId = (e.getAccount() != null && e.getAccount().getRoleEntity() != null)
                ? e.getAccount().getRoleEntity().getRoleId()
                : null;
        Accounts acc = e.getAccount();
        String accountStatus = (acc != null && acc.getStatus() != null) ? acc.getStatus().name() : null;

        return new EmployeeDTO(
                e.getEmployeeId(),
                e.getFullName(),
                e.getPhoneNumber(),
                e.getGender() != null ? e.getGender().name() : null,
                roleName,
                roleId,
                email,
                e.getDateOfBirth(),
                accountStatus
        );
    }
    @Override
    public EmployeeDTO createEmployee(EmployeeCreateRequest request) {

        // ======================
        //  CHECK CURRENT USER
        // ======================
        Accounts current = currentAccount();
        int myRoleId = current.getRoleEntity().getRoleId();

        // ======================
        //  CHECK EMAIL
        // ======================
        if (accountsRepository.findByEmail(request.getEmail()) != null) {
            throw new RuntimeException("Email đã tồn tại!");
        }

        // ======================
        //  CHECK ROLE CREATE
        // ======================
        Roles role = rolesRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role không tồn tại"));

        int targetRoleId = role.getRoleId();

        //  Không được tạo role cao hơn mình
        if (targetRoleId < myRoleId) {
            throw new RuntimeException("Không đủ quyền tạo nhân viên với role này");
        }


        // ======================
        // 🔐 CREATE ACCOUNT
        // ======================
        String rawPassword = generateStrongPassword();

        Accounts acc = new Accounts();
        acc.setEmail(request.getEmail());
        acc.setPassword(rawPassword);
        acc.setRole(Role.ADMIN);
        acc.setRoleEntity(role);
        acc.setStatus(
                request.getStatus() != null
                        ? AccountStatus.valueOf(request.getStatus())
                        : AccountStatus.ACTIVE
        );

        accountsRepository.save(acc);

        // ======================
        // 👤 CREATE EMPLOYEE
        // ======================
        Employees emp = new Employees();
        emp.setFullName(request.getFullName());
        emp.setPhoneNumber(request.getPhoneNumber());
        emp.setDateOfBirth(request.getDateOfBirth());
        emp.setGender(
                request.getGender() != null
                        ? Gender.valueOf(request.getGender())
                        : null
        );
        emp.setAccount(acc);

        employeesRepository.save(emp);

        // ======================
        // 📧 SEND EMAIL
        // ======================
        emailService.sendCreateAccount(acc.getEmail(), rawPassword);

        return toDTO(emp);
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
    public Page<EmployeeDTO> getAllEmployees(int page, int size, String sortBy, String direction, String keyword) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        // --- Query có điều kiện ---
        Page<Employees> employeesPage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            employeesPage = employeesRepository
                    .findByAccount_DeleteAtIsNullAndFullNameContainingIgnoreCase(keyword, pageable);
        } else {
            employeesPage = employeesRepository
                    .findByAccount_DeleteAtIsNull(pageable);
        }

        Accounts current = currentAccount();
        int myRoleId = current.getRoleEntity().getRoleId();

        List<EmployeeDTO> filtered = employeesPage.getContent().stream()
                .map(this::toDTO)
                .filter(dto ->
                        dto.getRoleId() != null &&
                                dto.getRoleId() >= myRoleId
                )
                .toList();

        // ⚠️ totalElements:
        // - dùng employeesPage.getTotalElements(): giữ nguyên tổng DB
        // - dùng filtered.size(): tổng đúng theo quyền
        return new PageImpl<>(
                filtered,
                pageable,
                filtered.size() // 👈 khuyên dùng cái này
        );
    }


    @Override
    public EmployeeDTO updateEmployee(Integer id, EmployeeUpdateRequest request) {
        // ✅ chặn thao tác bản thân
        forbidSelfAction(id);

        Employees e = employeesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));

        // ✅ chặn thao tác người có role cao hơn (role_id nhỏ hơn)
        checkRoleHierarchy(e);

        e.setFullName(request.getFullName());
        e.setPhoneNumber(request.getPhoneNumber());
        e.setDateOfBirth(request.getDateOfBirth());
        e.setGender(request.getGender());
        e.setDescription(request.getDescription());

        // ✅ chặn nâng role vượt quyền
        if (request.getRoleId() != null) {
            Accounts current = currentAccount();
            int myRoleId = current.getRoleEntity().getRoleId();

            if (request.getRoleId() < myRoleId) {
                throw new RuntimeException("Không thể gán role cao hơn quyền của bạn");
            }

            Roles role = rolesRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role không tồn tại"));
            e.getAccount().setRoleEntity(role);
        }

        employeesRepository.save(e);
        return toDTO(e);
    }

    @Override
    public void deleteEmployee(Integer id) {

        // ✅ chặn thao tác bản thân
        forbidSelfAction(id);

        Employees e = employeesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));

        // ✅ chặn thao tác người có role cao hơn
        checkRoleHierarchy(e);

        if (e.getAccount() != null) {
            e.getAccount().setDeleteAt(LocalDateTime.now());
            accountsRepository.save(e.getAccount());
        }
    }

    @Override
    public String resetPassword(Integer employeeId) {

        Employees emp = employeesRepository
                .findByEmployeeIdAndAccount_DeleteAtIsNull(employeeId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));

        Accounts acc = emp.getAccount();

        String newPass = generateStrongPassword();
        acc.setPassword(newPass);
        accountsRepository.save(acc);

        emailService.sendResetPasswordEmail(acc.getEmail(), newPass);

        return newPass;
    }
    private Accounts currentAccount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Accounts acc) {
            return acc;
        }
        throw new RuntimeException("Unauthenticated");
    }
    private void forbidSelfAction(Integer employeeId) {
        Accounts current = currentAccount();

        employeesRepository.findByAccount_AccountId(current.getAccountId())
                .ifPresent(self -> {
                    if (self.getEmployeeId().equals(employeeId)) {
                        throw new RuntimeException("Không thể thao tác với chính mình");
                    }
                });
    }
    private void checkRoleHierarchy(Employees target) {
        Accounts current = currentAccount();

        int myRole = current.getRoleEntity().getRoleId();
        int targetRole = target.getAccount().getRoleEntity().getRoleId();

        if (targetRole < myRole) {
            throw new RuntimeException("Không đủ quyền thao tác");
        }
    }
    @Override
    public EmployeeDTO CurrentEmployee() {
        return null;
    }


}
