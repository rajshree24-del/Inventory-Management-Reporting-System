package com.enterprise.inventory.config;

import com.enterprise.inventory.entity.User;
import com.enterprise.inventory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // ---------------- ADMIN ----------------
        if (userRepository.findByUsername("admin").isEmpty()) {

            String rawPassword = "admin123";

            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode(rawPassword))
                    .role("ADMIN")
                    .build();

            userRepository.save(admin);

            System.out.println("====================================");
            System.out.println("ADMIN USER CREATED");
            System.out.println("Username : admin");
            System.out.println("Password : " + rawPassword);
            System.out.println("Role     : ADMIN");
            System.out.println("====================================");
        } else {
            System.out.println("Admin already exists");
        }

        // ---------------- USER ----------------
        if (userRepository.findByUsername("user1").isEmpty()) {

            String rawPassword = "admin123";

            User user = User.builder()
                    .username("user1")
                    .password(passwordEncoder.encode(rawPassword))
                    .role("USER")
                    .build();

            userRepository.save(user);

            System.out.println("====================================");
            System.out.println("USER CREATED");
            System.out.println("Username : user1");
            System.out.println("Password : " + rawPassword);
            System.out.println("Role     : USER");
            System.out.println("====================================");
        } else {
            System.out.println("User1 already exists");
        }
    }
}