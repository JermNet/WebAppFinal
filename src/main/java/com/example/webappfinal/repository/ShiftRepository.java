package com.example.webappfinal.repository;

import com.example.webappfinal.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

// Default shift repository, didn't need to add any extra methods
public interface ShiftRepository extends JpaRepository<Shift, Long> {
}
