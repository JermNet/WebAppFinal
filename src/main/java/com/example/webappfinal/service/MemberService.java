package com.example.webappfinal.service;

import com.example.webappfinal.model.Member;
import com.example.webappfinal.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    // Get all member. Since I had to do special logic for getting a member by ID, I decided to make every use of the member repository take place in here
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    // Get a trainer by id, sending null if it doesn't exist, which shouldn't be possible due to the website logic, but a good check to have
    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElse(null);
    }
}
