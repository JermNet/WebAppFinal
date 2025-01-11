package com.example.webappfinal.service;

import com.example.webappfinal.model.Trainer;
import com.example.webappfinal.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerService {
    @Autowired
    private TrainerRepository trainerRepository;

    // Get all trainers. Since I had to do special logic for getting a trainer by ID, I decided to make every use of the trainer repository take place in here
    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll();
    }

    // Get a trainer by id, sending null if it doesn't exist, which shouldn't be possible due to the website logic, but a good check to have
    public Trainer getTrainerById(Long id) {
        return trainerRepository.findById(id).orElse(null);
    }

}
