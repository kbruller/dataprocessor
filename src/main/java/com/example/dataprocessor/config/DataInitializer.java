package com.example.dataprocessor.config;

import com.example.dataprocessor.entity.UserEntity;
import com.example.dataprocessor.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override

    public void run(String... args) {

        if (userRepository.findByUsername("admin").isEmpty()) {

            UserEntity adminUser = new UserEntity(
                    "admin",
                    passwordEncoder.encode("password"),
                    "ADMIN"
            );

            userRepository.save(adminUser);
        }

        if (userRepository.findByUsername("user").isEmpty()) {

            UserEntity normalUser = new UserEntity(
                    "user",
                    passwordEncoder.encode("password"),
                    "USER"
            );

            userRepository.save(normalUser);
        }
    }
}