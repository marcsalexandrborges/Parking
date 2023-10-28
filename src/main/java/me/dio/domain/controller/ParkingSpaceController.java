package me.dio.domain.controller;

import me.dio.domain.model.ParkingSpace;
import me.dio.domain.repository.ParkingSpaceRepository;
import me.dio.domain.service.ParkingSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.Optional;


@RestController
@RequestMapping("/api/parking-spaces")
public class ParkingSpaceController {
    private final ParkingSpaceService parkingSpaceService;

    @Autowired
    private ParkingSpaceRepository parkingSpaceRepository;

    @Autowired
    public ParkingSpaceController(ParkingSpaceService parkingSpaceService) {
        this.parkingSpaceService = parkingSpaceService;
    }

    @PostMapping("/check-in")
    public ResponseEntity<String> checkIn(@RequestParam Long parkingSpaceId, @RequestParam String owner) {
        return parkingSpaceService.checkIn(parkingSpaceId, owner);
    }

    @PostMapping("/check-out")
    public ResponseEntity<String> checkOut(@RequestParam Long parkingSpaceId) {
        return parkingSpaceService.checkOut(parkingSpaceId);
    }
    @PostMapping
    public ResponseEntity<ParkingSpace> createParkingSpace(@RequestBody ParkingSpace parkingSpace) {
        // Verifique se os dados fornecidos são válidos (você pode adicionar mais validações, se necessário).
        if (parkingSpace != null && parkingSpace.getNumber() != null && parkingSpace.getType() != null) {
            // Defina o status inicial da vaga de estacionamento (por exemplo, "available").
            parkingSpace.setStatus("available");

            // Salve a nova vaga de estacionamento no repositório.
            parkingSpace = parkingSpaceRepository.save(parkingSpace);

            // Retorne a vaga de estacionamento recém-criada com um status HTTP 201 (Created).
            return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpace);
        } else {
            // Se os dados não forem válidos, retorne um status HTTP 400 (Bad Request).
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

}
