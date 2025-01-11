package com.example.webappfinal.repository;

import com.example.webappfinal.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

// Default trainer repository, didn't need to add any extra methods
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
}
