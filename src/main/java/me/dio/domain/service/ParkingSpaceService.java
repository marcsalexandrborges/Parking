package me.dio.domain.service;

import me.dio.domain.model.ParkingSpace;
import me.dio.domain.repository.ParkingSpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ParkingSpaceService {
    private final ParkingSpaceRepository parkingSpaceRepository;

    @Autowired
    public ParkingSpaceService(ParkingSpaceRepository parkingSpaceRepository) {
        this.parkingSpaceRepository = parkingSpaceRepository;
    }

    public ResponseEntity<String> checkIn(Long parkingSpaceId, String owner) {
        Optional<ParkingSpace> parkingSpaceOptional = parkingSpaceRepository.findById(parkingSpaceId);
        if (parkingSpaceOptional.isPresent()) {
            ParkingSpace parkingSpace = parkingSpaceOptional.get();
            if ("available".equals(parkingSpace.getStatus())) {
                parkingSpace.setStatus("occupied");
                parkingSpace.setOwner(owner);
                parkingSpace.setCheckInTime(LocalDateTime.now());
                parkingSpaceRepository.save(parkingSpace);
                return ResponseEntity.ok("Check-in realizado com sucesso.");
            } else {
                return ResponseEntity.badRequest().body("A vaga não está disponível para check-in.");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<String> checkOut(Long parkingSpaceId) {
        Optional<ParkingSpace> parkingSpaceOptional = parkingSpaceRepository.findById(parkingSpaceId);
        if (parkingSpaceOptional.isPresent()) {
            ParkingSpace parkingSpace = parkingSpaceOptional.get();
            if ("occupied".equals(parkingSpace.getStatus())) {
                parkingSpace.setStatus("available");
                parkingSpace.setOwner(null);
                parkingSpace.setCheckOutTime(LocalDateTime.now());
                parkingSpaceRepository.save(parkingSpace);
                return ResponseEntity.ok("Check-out realizado com sucesso.");
            } else {
                return ResponseEntity.badRequest().body("A vaga não está ocupada para check-out.");
            }
        } else {
            return ResponseEntity.notFound().build();
        }

    }
}

