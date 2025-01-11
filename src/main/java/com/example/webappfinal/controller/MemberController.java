package com.example.webappfinal.controller;

import com.example.webappfinal.model.Session;
import com.example.webappfinal.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

// Controller for all member-related things. Having the "@RequestMapping("/member")" line is especially important for this (and the other account controllers) since only members can access these due to logic had in WebSecurityConfig
@Controller
@RequestMapping("/member")
public class MemberController {

    SessionService sessionService;

    @Autowired
    public MemberController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    // Do this so we can check if the user is logged in, then using isAuthenticated to check if the logout, login, etc. buttons should be displayed
    @GetMapping("/dashboard")
    public String dashboardPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
        model.addAttribute("isAuthenticated", isAuthenticated);
        return "member/dashboard";
    }

    // Get the "sessions" page, used to display and select a session to be edited. Uses a model attribute to be able to display a list of all the sessions
    @GetMapping("/sessions")
    public String sessionsPage(Model model) {
        List<Session> sessions = sessionService.getAllSessions();
        model.addAttribute("sessions", sessions);
        return "member/sessions";
    }
}
