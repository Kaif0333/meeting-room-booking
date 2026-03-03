package com.kaif.meetingroombooking.service;

import com.kaif.meetingroombooking.model.User;
import com.kaif.meetingroombooking.model.UserRole;
import com.kaif.meetingroombooking.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;

    public UserService(UserRepository repo, PasswordEncoder passwordEncoder, AuditLogService auditLogService) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.auditLogService = auditLogService;
    }

    public User createUser(User u) {
        if (repo.existsByEmail(u.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        UserRole role = u.getRole() == null ? UserRole.USER : u.getRole();
        u.setRole(role);
        u.setPasswordHash(passwordEncoder.encode(u.getPasswordHash()));
        User saved = repo.save(u);
        auditLogService.log("USER_CREATED", "USER", saved.getId(), "User created with role " + saved.getRole().name());
        return saved;
    }

    public Optional<User> findByEmail(String email) { return repo.findByEmail(email); }
    public Optional<User> findById(String id) { return repo.findById(id); }
    public List<User> getAll() { return repo.findAll(); }
}
