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
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link shifts based on a trainer's id. This is a many to one relationship since a shift should only be for one person, but one person can have more than one shift
    @ManyToOne
    @JoinColumn(name = "trainer_id")
    @NotNull(message = "Trainer cannot be null!")
    private Trainer trainer;

    // The not nulls aren't completely necessary since the web page makes it impossible to have this values as null, but still a nice check to have
    @NotNull(message = "Date cannot be null!")
    private LocalDate date;
    @NotNull(message = "Start Time cannot be null!")
    private LocalTime startTime;
    @NotNull(message = "End Time cannot be null!")
    private LocalTime endTime;
}
