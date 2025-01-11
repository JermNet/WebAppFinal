package com.example.webappfinal.controller;

import com.example.webappfinal.model.Member;
import com.example.webappfinal.model.Session;
import com.example.webappfinal.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

// Controller for all trainer-related things. Having the "@RequestMapping("/trainer")" line is especially important for this (and the other account controllers) since only trainers can access these due to logic had in WebSecurityConfig
@Controller
@RequestMapping("/trainer")
public class TrainerController {
    private final UserService userService;
    private final SessionService sessionService;
    private final MemberService memberService;

    // Autowire the services
    @Autowired
    public TrainerController(UserService userService, SessionService sessionService, MemberService memberService) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.memberService = memberService;
    }

    // This group of get mappings simply serve the pages and that's it
    @GetMapping("/sessionzone")
    public String sessionZonePage() {
        return "trainer/sessionzone";
    }

    // Do this so we can check if the user is logged in, then using isAuthenticated to check if the logout, login, etc. buttons should be displayed
    @GetMapping("/dashboard")
    public String dashboardPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
        model.addAttribute("isAuthenticated", isAuthenticated);
        return "trainer/dashboard";
    }

    // Get the "sessions" page, used to display and select a session to be edited. Uses a model attribute to be able to display a list of all the sessions
    @GetMapping("/sessions")
    public String sessionsPage(Model model) {
        List<Session> sessions = sessionService.getAllSessions();
        model.addAttribute("sessions", sessions);
        return "trainer/sessions";
    }

    // Get the "addsession" page. There's a model attribute of a list of all the members so they can be displayed and selected, as well as a new session so it can later be posted
    @GetMapping("/addsession")
    public String addSessionPage(Model model) {
        List<Member> members = memberService.getAllMembers();
        model.addAttribute("members", members);
        model.addAttribute("session", new Session());
        return "trainer/addsession";
    }

    // Obviously this can be accessed directly, but functionality wise, it takes the sessionId from the selected button on the shifts page. If the session does not exist, go to the error page, if it does, display the delete session page
    @GetMapping("/deletesession/{sessionId}")
    public String showDeleteSessionPage(@PathVariable("sessionId") Long sessionId, Model model) {
        Session session = sessionService.getSessionById(sessionId);
        if (session == null) {
            model.addAttribute("error", "Session not found!");
            return "failure";
        }
        model.addAttribute("var", session);
        return "trainer/deletesession";
    }

    // Obviously this can be accessed directly, but functionality wise, it takes the sessionId from the selected button on the sessions page. If the session does not exist, go to the error page, if it does, display the change session page
    @GetMapping("/changesession/{sessionId}")
    public String changeSessionPage(@PathVariable("sessionId") Long sessionId, Model model) {
        Session existingSession = sessionService.getSessionById(sessionId);
        if (existingSession == null) {
            model.addAttribute("error", "Session not found!");
            return "failure";
        }
        List<Member> members = memberService.getAllMembers();
        model.addAttribute("members", members);
        model.addAttribute("var", existingSession);
        return "trainer/changesession";
    }

    // Lets a session be added. Due to how it works on the website end, a member should not be null, but it is a good check to have.
    @PostMapping("/addsession")
    public String addSession(@ModelAttribute Session session, @RequestParam("memberId") Long memberId, Model model) {
        Member member = memberService.getMemberById(memberId);
        if (member == null) {
            model.addAttribute("error", "Member not found!");
            return "failure";
        }
        session.setMember(member);
        sessionService.saveSession(session);
        member.addSession(session);
        userService.saveUser(member);
        model.addAttribute("success", "Session successfully added!");
        return "success";
    }

    // Uses id to find session
    @PostMapping("/deletesession")
    public String deleteSession(@RequestParam("sessionId") Long sessionId, Model model) {
        Session session = sessionService.getSessionById(sessionId);
        if (session == null) {
            model.addAttribute("error", "Session not found!");
            return "failure";
        }
        sessionService.deleteSessionById(sessionId);
        model.addAttribute("success", "Session successfully deleted!");
        return "success";
    }

    // Mix of post and delete. A member or session should not be null and the session should belong to the selected member due to the way the pages are handled, but nice checks to have.
    @PostMapping("/changesession")
    public String changeSession(@RequestParam Long sessionId, @RequestParam Long memberId, @RequestParam String date, @RequestParam String startTime, @RequestParam String endTime, boolean attendance, Model model) {
        Member member = memberService.getMemberById(memberId);
        if (member == null) {
            model.addAttribute("error", "Member not found!");
            return "failure";
        }
        Session session = sessionService.getSessionById(sessionId);
        if (session == null || !session.getMember().getId().equals(memberId)) {
            model.addAttribute("error", "Session not found or does not belong to the selected member!");
            return "failure";
        }
        session.setDate(LocalDate.parse(date));
        session.setStartTime(LocalTime.parse(startTime));
        session.setEndTime(LocalTime.parse(endTime));
        session.setAttendance(attendance);
        sessionService.updateSession(session);
        model.addAttribute("success", "Session successfully changed!");
        return "success";
    }

}
