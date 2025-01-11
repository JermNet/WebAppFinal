package com.example.webappfinal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

// Use lombok for setters, getters and constructors. This inherits the attribute and is discriminated as a different set of data based on the discriminator value. This is so this entity can have specific attributes while still being a user
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("ROLE_TRAINER")
public class Trainer extends User{

    // Link shifts based on a trainer. This is a one to many relationship since a shift should only be for one person, but one person can have more than one shift. Cascade all makes it so changes are applied to a trainer and shifts, and fetch type lazy means the associated data (shifts) is only loaded when explicitly accessed
    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Shift> shifts;

    // Simply adds a shift to this user, can't use @Setter for this since it sets the entire list
    public void addShift(Shift shift) {
        shifts.add(shift);
    }
}
