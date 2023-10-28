package me.dio.domain.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import me.dio.domain.model.Parking;
import me.dio.domain.model.ParkingSpace;
import me.dio.domain.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Parking", description = "Endpoints para gerenciamento de estacionamentos")
@RestController
@RequestMapping("/api/parkings")
public class ParkingController {
    @Autowired
    private ParkingService parkingService;

    @Operation(summary = "Criar um novo estacionamento")
    @ApiResponse(responseCode = "201", description = "Estacionamento criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida")
    @PostMapping
    public ResponseEntity<Parking> createParking(@Valid @RequestBody Parking parking) {
        Parking createdParking = parkingService.createParking(parking);
        if (createdParking != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdParking);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @Operation(summary = "Listar todos os estacionamentos")
    @ApiResponse(responseCode = "200", description = "Lista de estacionamentos")
    @GetMapping
    public List<Parking> getAllParkings() {
        return parkingService.getAllParkings();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Parking> getParkingById(@PathVariable Long id) {
        Parking parking = parkingService.getParkingById(id);
        if (parking != null) {
            return ResponseEntity.ok(parking);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Parking> updateParking(@PathVariable Long id, @RequestBody Parking updatedParking) {
        Parking updated = parkingService.updateParking(id, updatedParking);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParking(@PathVariable Long id) {
        parkingService.deleteParking(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/parking-spaces")
    public ResponseEntity<ParkingSpace> createParkingSpace(@RequestBody ParkingSpace parkingSpace) {
        // Implement the logic to create a new parking space using your service.
        ParkingSpace createdParkingSpace = parkingService.createParkingSpace(parkingSpace);

        if (createdParkingSpace != null) {
            // Return a ResponseEntity with the created parking space and HttpStatus.CREATED.
            return ResponseEntity.status(HttpStatus.CREATED).body(createdParkingSpace);
        } else {
            // Return a ResponseEntity with an appropriate error status (e.g., HttpStatus.BAD_REQUEST).
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @GetMapping("/parking-spaces")
    public ResponseEntity<List<ParkingSpace>> listParkingSpaces() {
        List<ParkingSpace> parkingSpaces = parkingService.getAllParkingSpaces();
        if (!parkingSpaces.isEmpty()) {
            return ResponseEntity.ok(parkingSpaces);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/parking-spaces/{spaceId}/reserve")
    public ResponseEntity<String> reserveParkingSpace(@PathVariable Long spaceId) {
        ParkingSpace parkingSpace = parkingService.getParkingSpaceById(spaceId);

        if (parkingSpace != null && "available".equals(parkingSpace.getStatus())) {
            // Marque a vaga como reservada (atualize o status)
            parkingSpace.setStatus("reserved");

            // Salve a alteração no repositório
            parkingService.updateParkingSpace(spaceId, parkingSpace);

            // Pode retornar uma mensagem de sucesso
            return ResponseEntity.ok("Vaga reservada com sucesso.");
        } else {
            // Pode retornar uma mensagem de erro se a vaga não estiver disponível
            return ResponseEntity.badRequest().body("A vaga não está disponível para reserva.");
        }
    }

}