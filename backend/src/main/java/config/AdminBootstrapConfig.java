package com.kaif.meetingroombooking.config;

import com.kaif.meetingroombooking.model.User;
import com.kaif.meetingroombooking.model.UserRole;
import com.kaif.meetingroombooking.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminBootstrapConfig {
    @Bean
    CommandLineRunner seedAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String email = "admin@meeting.local";
            if (userRepository.existsByEmail(email)) {
                return;
            }
            User admin = new User();
            admin.setName("System Admin");
            admin.setEmail(email);
            admin.setPasswordHash(passwordEncoder.encode("Admin@12345"));
            admin.setRole(UserRole.ADMIN);
            userRepository.save(admin);
        };
    }
}
