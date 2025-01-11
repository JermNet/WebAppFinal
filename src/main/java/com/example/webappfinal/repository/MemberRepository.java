package com.example.webappfinal.repository;

import com.example.webappfinal.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

// Default member repository, didn't need to add any extra methods
public interface MemberRepository extends JpaRepository<Member, Long> {
}
