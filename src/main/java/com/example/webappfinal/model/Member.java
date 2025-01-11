package com.example.webappfinal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

// This inherits the attribute and is discriminated as a different set of data based on the discriminator value. This is so this entity can have specific attributes while still being a user
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("ROLE_MEMBER")
public class Member extends User {
    // Link sessions based on a member. This is a one to many relationship since a session should only be for one person, but one person can have more than one session. Cascade all makes it so changes are applied to a trainer and shifts, and fetch type lazy means the associated data (shifts) is only loaded when explicitly accessed
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Session> sessions;

    // Simply adds a member to this user, can't use @Setter for this since it sets the entire list
    public void addSession(Session session) {
        sessions.add(session);
    }
}
