package com.example.webappfinal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

// Use lombok for setters, getters and constructors
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link shifts based on a member's id. This is a many to one relationship since a session should only be for one person, but one person can have more than one session
    @ManyToOne
    @JoinColumn(name = "member_id")
    @NotNull(message = "Member cannot be null!")
    private Member member;

    // The not nulls aren't completely necessary since the web page makes it impossible to have this values as null, but still a nice check to have
    @NotNull(message = "Date cannot be null!")
    private LocalDate date;
    @NotNull(message = "Start Time cannot be null!")
    private LocalTime startTime;
    @NotNull(message = "End Time cannot be null!")
    private LocalTime endTime;
    @NotNull(message = "Attendance cannot be null!")
    private boolean attendance;

    // Lombok handles getters for booleans differently, so I have to manually add a getter
    public boolean getAttendance() {
        return attendance;
    }
}
