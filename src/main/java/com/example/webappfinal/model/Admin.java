package com.example.webappfinal.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

// This inherits the attribute and is discriminated as a different set of data based on the discriminator value. This is so this entity can have specific attributes while still being a user
@Entity
@DiscriminatorValue("ROLE_ADMIN")
public class Admin extends User {
}
