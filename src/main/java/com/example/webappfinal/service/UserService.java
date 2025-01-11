package com.example.webappfinal.service;

import com.example.webappfinal.model.Admin;
import com.example.webappfinal.model.Member;
import com.example.webappfinal.model.Trainer;
import com.example.webappfinal.repository.AdminRepository;
import com.example.webappfinal.repository.MemberRepository;
import com.example.webappfinal.repository.TrainerRepository;
import com.example.webappfinal.repository.UserRepository;
import com.example.webappfinal.model.User;
import com.example.webappfinal.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private final ValidationUtil validationUtil;
    private final TrainerRepository trainerRepository;
    private final AdminRepository adminRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ValidationUtil validationUtil, TrainerRepository trainerRepository, AdminRepository adminRepository, MemberRepository memberRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.validationUtil = validationUtil;
        this.trainerRepository = trainerRepository;
        this.adminRepository = adminRepository;
        this.memberRepository = memberRepository;
    }

    // Load a user by username, this is a built-in kind of thing with some special logic
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
        );
    }

    // If a user doesn't already exist, and the password passes checking against the Regex (using the validationUtil), save user and return true. If not, don't and return false
    public boolean registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null || !validationUtil.isValidPassword(user.getPassword())) {
            return false;
        }

        // Hash the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        saveUser(user);
        return true;
    }

    // Delete an account by username, but only if the user is not an admin and does exist
    public boolean deleteUserByUsername(User temp) {
        User user = userRepository.findByUsername(temp.getUsername());
        if (user != null && !user.getRole().equals("ROLE_ADMIN")) {
            userRepository.deleteById(user.getId());
            return true;
        }
        return false;
    }

    // Change an account only if it's a member account, does exist, and has a valid new password
    public boolean updateMember(String oldUsername, User newUser) {
        User user = userRepository.findByUsername(oldUsername);
        if (user != null && !user.getRole().equals("ROLE_ADMIN") && !user.getRole().equals("ROLE_TRAINER") && validationUtil.isValidPassword(newUser.getPassword()) && userRepository.findByUsername(newUser.getUsername()) == null) {
            user.setUsername(newUser.getUsername());
            user.setPassword(passwordEncoder.encode(newUser.getPassword()));
            saveUser(user);
            return true;
        }
        return false;
    }

    // Special save to do so based on user type
    public void saveUser(User user) {
        if (user instanceof Trainer) {
            trainerRepository.save((Trainer) user);
        } else if (user instanceof Admin) {
            adminRepository.save((Admin) user);
        } else if (user instanceof Member) {
            memberRepository.save((Member) user);
        } else {
            userRepository.save(user);
        }
    }
}

