package com.example.webappfinal.service;

import com.example.webappfinal.model.Session;
import com.example.webappfinal.repository.SessionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;

    // Save a session. Since I had to do special logic for updating a session and getting a session by ID, I decided to make every use of the session repository take place in here
    public void saveSession(Session session) {
        sessionRepository.save(session);
    }

    // Update a session, sending out an exception if the session does not exist, which shouldn't be possible due to the website logic, but a good check to have
    public void updateSession(Session updatedSession) {
        Session existingSession = sessionRepository.findById(updatedSession.getId()).orElse(null);
        if (existingSession != null) {
            existingSession.setDate(updatedSession.getDate());
            existingSession.setStartTime(updatedSession.getStartTime());
            existingSession.setEndTime(updatedSession.getEndTime());
            existingSession.setAttendance(updatedSession.getAttendance());
            sessionRepository.save(existingSession);
        } else {
            throw new EntityNotFoundException("Session not found with ID: " + updatedSession.getId());
        }
    }

    // Get a session by id, sending null if it doesn't exist, which shouldn't be possible due to the website logic, but a good check to have
    public Session getSessionById(long id) {
        return sessionRepository.findById(id).orElse(null);
    }

    // These just use the sessionRepository methods
    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public void deleteSessionById(long id) {
        sessionRepository.deleteById(id);
    }
}
