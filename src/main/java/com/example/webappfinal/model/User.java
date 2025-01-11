package com.example.webappfinal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Use lombok for setters, getters and constructors. Inheritance JOINED means that there's a separate table, linked this entity's id for anything that inherits this class. DiscriminatorColumn is how tell there's a difference between a user and what inherits a user within a user table. I couldn't make the discriminator column the role column, since @DiscriminatorColumn actually creates said column, so I used Apache's default
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The not nulls aren't completely necessary since the web page makes it impossible to have this values as null, but still a nice check to have
    @NotNull(message = "Username cannot be null!")
    private String username;
    @NotNull(message = "Password cannot be null!")
    private String password;
    @NotNull(message = "Role cannot be null!")
    private String role;

}

