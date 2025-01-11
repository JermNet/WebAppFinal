package com.example.webappfinal.repository;

import com.example.webappfinal.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

// Default admin repository, didn't need to add any extra methods
public interface AdminRepository extends JpaRepository<Admin, Long> {
}
