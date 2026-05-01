package com.grocery.oopproject.service;

import com.grocery.oopproject.model.Admin;
import com.grocery.oopproject.repository.AdminRepository;
import com.grocery.oopproject.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository adminRepository,
                        UserRepository userRepository,
                        PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Admin> findAll() {
        return adminRepository.findAll();
    }

    public Admin find(String id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found: " + id));
    }

    @Transactional
    public Admin register(String name, String email, String rawPassword, String roleName) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered: " + email);
        }
        String userId = "U-A-" + UUID.randomUUID().toString().substring(0, 8);
        Admin a = new Admin(userId, name, email, passwordEncoder.encode(rawPassword),
                roleName == null || roleName.isBlank() ? "ADMIN" : roleName);
        return adminRepository.save(a);
    }

    @Transactional
    public Admin update(String id, String name, String email, String roleName) {
        Admin a = find(id);
        a.setName(name);
        a.setEmail(email);
        a.setRoleName(roleName);
        return adminRepository.save(a);
    }

    @Transactional
    public void delete(String id) {
        adminRepository.deleteById(id);
    }

    public Admin login(String email, String rawPassword) {
        Admin a = adminRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No admin with email " + email));
        if (!passwordEncoder.matches(rawPassword, a.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
        return a;
    }
}
