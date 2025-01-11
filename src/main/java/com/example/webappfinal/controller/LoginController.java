package com.example.webappfinal.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// This controller is for all pages available to all users, which is mostly login related pages, hence the name
@Controller
public class LoginController {

    // This group of get mappings simply serve the pages and that's it
    @GetMapping("/logoutsuccess")
    public String logoutPage() {
        return "logoutsuccess";
    }

    @GetMapping("/failure")
    public String failurePage() {
        return "failure";
    }

    @GetMapping("/success")
    public String successPage() {
        return "success";
    }

    // Do this so we can check if the user is logged in, then using isAuthenticated to check if the logout, login, etc. buttons should be displayed
    @GetMapping("/")
    public String indexPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && !(authentication instanceof AnonymousAuthenticationToken);

        assert authentication != null;
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        boolean isTrainer = authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_TRAINER"));
        boolean isMember = authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_MEMBER"));

        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isTrainer", isTrainer);
        model.addAttribute("isMember", isMember);
        model.addAttribute("isAuthenticated", isAuthenticated);
        return "index";
    }

    // Use Authentication to redirect based on if the user is logged in already or not and based on their role
    @GetMapping("/login")
    public String login(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            // Since the user is redirected with the config class based on role, this only goes through if the user is already logged in
            model.addAttribute("error", "You are already logged in!");
            return "failure";
        }
        // Goes to login page if user does not have a valid role

        return "login";
    }
}
