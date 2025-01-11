package com.example.webappfinal.repository;

import com.example.webappfinal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// Almost default user repository, just need a method to find users by username but is still JPA default
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    boolean existsByRole(String role);
}
