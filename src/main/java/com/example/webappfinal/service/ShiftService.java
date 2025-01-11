package com.example.webappfinal.service;

import com.example.webappfinal.model.Shift;
import com.example.webappfinal.repository.ShiftRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShiftService {
    @Autowired
    private ShiftRepository shiftRepository;

    // Save a shift. Since I had to do special logic for updating a shift and getting a shift by ID, I decided to make every use of the shift repository take place in here
    public void saveShift(Shift shift) {
        shiftRepository.save(shift);
    }

    // Update a shift, sending out an exception if the shift does not exist, which shouldn't be possible due to the website logic, but a good check to have
    public void updateShift(Shift updatedShift) {
        Shift existingShift = shiftRepository.findById(updatedShift.getId()).orElse(null);
        if (existingShift != null) {
            existingShift.setDate(updatedShift.getDate());
            existingShift.setStartTime(updatedShift.getStartTime());
            existingShift.setEndTime(updatedShift.getEndTime());
            shiftRepository.save(existingShift);
        } else {
            throw new EntityNotFoundException("Shift not found with ID: " + updatedShift.getId());
        }
    }

    // Get a shift by id, sending null if it doesn't exist, which shouldn't be possible due to the website logic, but a good check to have
    public Shift getShiftById(long id) {
        return shiftRepository.findById(id).orElse(null);
    }

    // These just use the shiftRepository methods
    public List<Shift> getAllShifts() {
        return shiftRepository.findAll();
    }

    public void deleteShiftById(long id) {
        shiftRepository.deleteById(id);
    }
}
