package com.example.demo.config;

import com.example.demo.entity.Role;
import com.example.demo.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role("CUSTOMER"));
            roleRepository.save(new Role("STAFF"));
            roleRepository.save(new Role("ADMIN"));
            roleRepository.save(new Role("FACULTY"));
            roleRepository.save(new Role("REVIEWER"));
            roleRepository.save(new Role("STUDENT"));
        }
    }
}