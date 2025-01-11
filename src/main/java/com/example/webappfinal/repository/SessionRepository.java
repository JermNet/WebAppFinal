package com.example.webappfinal.repository;

import com.example.webappfinal.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
}
