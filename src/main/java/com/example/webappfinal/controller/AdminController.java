package com.example.webappfinal.controller;

import com.example.webappfinal.model.Member;
import com.example.webappfinal.model.Shift;
import com.example.webappfinal.model.Trainer;
import com.example.webappfinal.model.User;
import com.example.webappfinal.service.ShiftService;
import com.example.webappfinal.service.TrainerService;
import com.example.webappfinal.service.UserService;
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

// Controller for all admin-related things. Having the "@RequestMapping("/admin")" line is especially important for this (and the other account controllers) since only admins can access these due to logic had in WebSecurityConfig
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final ShiftService shiftService;
    private final TrainerService trainerService;

    // Autowire the services
    @Autowired
    public AdminController(UserService userService, ShiftService shiftService, TrainerService trainerService) {
        this.userService = userService;
        this.shiftService = shiftService;
        this.trainerService = trainerService;
    }

    // This group of get mappings simply serve the pages and that's it
    @GetMapping("/makeaccount")
    public String makeAccountPage() {
        return "admin/makeaccount";
    }

    @GetMapping("/deleteaccount")
    public String deleteAccountPage() {
        return "admin/deleteaccount";
    }

    @GetMapping("/changeaccount")
    public String changeAccountPage() {
        return "admin/changeaccount";
    }

    @GetMapping("/shiftzone")
    public String shiftZonePage() {
        return "admin/shiftzone";
    }

    @GetMapping("/accountzone")
    public String accountZonePage() {
        return "admin/accountzone";
    }

    // Do this so we can check if the user is logged in, then using isAuthenticated to check if the logout, login, etc. buttons should be displayed
    @GetMapping("/dashboard")
    public String dashboardPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
        model.addAttribute("isAuthenticated", isAuthenticated);
        return "admin/dashboard";
    }

    // Get the "addshift" page. There's a model attribute of a list of all the trainers so they can be displayed and selected, as well as a new shift so it can later be posted
    @GetMapping("/addshift")
    public String addShiftPage(Model model) {
        List<Trainer> trainers = trainerService.getAllTrainers();
        model.addAttribute("trainers", trainers);
        model.addAttribute("shift", new Shift());
        return "admin/addshift";
    }

    // Get the "shifts" page, used to display and select a shift to be edited. Uses a model attribute to be able to display a list of all the shifts
    @GetMapping("/shifts")
    public String shiftsPage(Model model) {
        List<Shift> shifts = shiftService.getAllShifts();
        model.addAttribute("shifts", shifts);
        return "admin/shifts";
    }

    // Obviously this can be accessed directly, but functionality wise, it takes the shiftId from the selected button on the shifts page. If the shift does not exist, go to the error page, if it does, display the delete shift page
    @GetMapping("/deleteshift/{shiftId}")
    public String showDeleteShiftPage(@PathVariable("shiftId") Long shiftId, Model model) {
        Shift shift = shiftService.getShiftById(shiftId);
        if (shift == null) {
            model.addAttribute("error", "Shift not found!");
            return "failure";
        }
        model.addAttribute("shift", shift);
        return "admin/deleteshift";
    }

    // Obviously this can be accessed directly, but functionality wise, it takes the shiftId from the selected button on the shifts page. If the shift does not exist, go to the error page, if it does, display the change shift page
    @GetMapping("/changeshift/{shiftId}")
    public String changeShiftPage(@PathVariable("shiftId") Long shiftId, Model model) {
        Shift existingShift = shiftService.getShiftById(shiftId);
        if (existingShift == null) {
            model.addAttribute("error", "Shift not found!");
            return "failure";
        }
        List<Trainer> trainers = trainerService.getAllTrainers();
        model.addAttribute("trainers", trainers);
        model.addAttribute("shift", existingShift);
        return "admin/changeshift";
    }

    // Lets an admin create an account, making it as the proper class depending on the user's role. Admins cannot make other admin accounts, so there's no if statement for that. The user service also saves a user in its proper repository
    @PostMapping("/makeaccount")
    public String makeAccount(@ModelAttribute User user, Model model) {
        User newUser;
        if (user.getRole().equals("ROLE_TRAINER")) {
            newUser = new Trainer();
        } else if (user.getRole().equals("ROLE_MEMBER")) {
            newUser = new Member();
        } else {
            newUser = new User();
        }
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());
        newUser.setRole(user.getRole());
        boolean success = userService.registerUser(newUser);
        if (!success) {
            model.addAttribute("error", "Username is already in use or password is not secure. (Must be at least 8 characters, have 1 special, number, up and lower case character).");
            return "failure";
        }
        model.addAttribute("success", "User successfully registered!");
        return "success";
    }

    // Use the service to delete an account, using the model to let know if it worked or if there was an error
    @PostMapping("/deleteaccount")
    public String deleteAccount(@ModelAttribute User user, Model model) {
        boolean success = userService.deleteUserByUsername(user);
        if (!success) {
            model.addAttribute("error", "Username or password does not exist or is an admin account.");
            return "failure";
        }
        model.addAttribute("success", "User successfully deleted!");
        return "success";
    }

    // Mix of the standard post and delete, letting the user know if the changing of information worked or not
    @PostMapping("/changeaccount")
    public String changeAccount(String oldUsername, @ModelAttribute("user") User user, Model model) {
        boolean success = userService.updateMember(oldUsername, user);
        if (!success) {
            model.addAttribute("error", "Account does not exist, is not a member account, new username already exists or password is not secure (Must be at least 8 characters, have 1 special, number, up and lower case character).");
            return "failure";
        }
        model.addAttribute("success", "Account successfully changed!");
        return "success";
    }

    // Lets a shift be added. Due to how it works on the website end, a trainer should not be null, but it is a good check to have.
    @PostMapping("/addshift")
    public String addShift(@ModelAttribute Shift shift, @RequestParam("trainerId") Long trainerId, Model model) {
        Trainer trainer = trainerService.getTrainerById(trainerId);
        if (trainer == null) {
            model.addAttribute("error", "Trainer not found!");
            return "failure";
        }
        shift.setTrainer(trainer);
        shiftService.saveShift(shift);
        trainer.addShift(shift);
        userService.saveUser(trainer);
        model.addAttribute("success", "Shift successfully added!");
        return "success";
    }

    // Basically the same as the delete account, but uses id instead of username to find the shift
    @PostMapping("/deleteshift")
    public String deleteShift(@RequestParam("shiftId") Long shiftId, Model model) {
        Shift shift = shiftService.getShiftById(shiftId);
        if (shift == null) {
            model.addAttribute("error", "Shift not found!");
            return "failure";
        }
        shiftService.deleteShiftById(shiftId);
        model.addAttribute("success", "Shift successfully deleted!");
        return "success";
    }

    // Mix of post and delete. A trainer or shift should not be null and the shift should belong to the selected trainer due to the way the pages are handled, but nice checks to have.
    @PostMapping("/changeshift")
    public String changeShift(@RequestParam Long shiftId, @RequestParam Long trainerId, @RequestParam String date, @RequestParam String startTime, @RequestParam String endTime, Model model) {
        Trainer trainer = trainerService.getTrainerById(trainerId);
        if (trainer == null) {
            model.addAttribute("error", "Trainer not found!");
            return "failure";
        }
        Shift shift = shiftService.getShiftById(shiftId);
        if (shift == null || !shift.getTrainer().getId().equals(trainerId)) {
            model.addAttribute("error", "Shift not found or does not belong to the selected trainer!");
            return "failure";
        }
        shift.setDate(LocalDate.parse(date));
        shift.setStartTime(LocalTime.parse(startTime));
        shift.setEndTime(LocalTime.parse(endTime));
        shiftService.updateShift(shift);
        model.addAttribute("success", "Shift successfully changed!");
        return "success";
    }
}