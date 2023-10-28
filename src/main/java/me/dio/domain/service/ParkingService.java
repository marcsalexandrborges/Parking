package me.dio.domain.service;


import me.dio.domain.model.Parking;
import me.dio.domain.model.ParkingSpace;
import me.dio.domain.repository.ParkingRepository;
import me.dio.domain.repository.ParkingSpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class ParkingService {
    private final ParkingRepository parkingRepository;
    private final ParkingSpaceRepository parkingSpaceRepository;

    @Autowired
    public ParkingService(ParkingRepository parkingRepository, ParkingSpaceRepository parkingSpaceRepository) {
        this.parkingRepository = parkingRepository;
        this.parkingSpaceRepository = parkingSpaceRepository;
    }

    public ParkingSpace createParkingSpace(ParkingSpace parkingSpace) {
        // Implemente a lógica para criar um novo espaço de estacionamento.
        // Isso pode incluir a validação dos dados e a persistência no banco de dados.
        return parkingSpaceRepository.save(parkingSpace); // Exemplo usando um repositório JPA.
    }

    public Parking createParking(Parking parking) {
        // Implemente a lógica de validação, se necessário.
        return parkingRepository.save(parking);
    }

    public List<Parking> getAllParkings() {
        return parkingRepository.findAll();
    }
    public ParkingSpace getParkingSpaceById(Long id) {
        return parkingSpaceRepository.findById(id).orElse(null);
    }
    public ParkingSpace updateParkingSpace(Long id, ParkingSpace updatedParkingSpace) {
        ParkingSpace existingParkingSpace = parkingSpaceRepository.findById(id).orElse(null);
        if (existingParkingSpace != null) {
            // Atualize os campos da vaga, como o status, o horário de reserva, etc.
            existingParkingSpace.setStatus(updatedParkingSpace.getStatus());
            existingParkingSpace.setCheckInTime(updatedParkingSpace.getCheckInTime());

            return parkingSpaceRepository.save(existingParkingSpace);
        }
        return null;
    }


    public boolean isParkingSpaceAvailable(Long id) {
        ParkingSpace parkingSpace = parkingSpaceRepository.findById(id).orElse(null);
        return parkingSpace != null && "available".equals(parkingSpace.getStatus());
    }

    public Parking getParkingById(Long id) {
        return parkingRepository.findById(id).orElse(null);
    }
    public List<ParkingSpace> getAllParkingSpaces() {
        return parkingSpaceRepository.findAll();
    }

    public void deleteParking(Long id) {
        parkingRepository.deleteById(id);
    }

    public Parking updateParking(Long id, Parking updatedParking) {
        // Implemente a lógica para atualizar os campos do estacionamento existente.
        Parking existingParking = parkingRepository.findById(id).orElse(null);
        if (existingParking != null) {
            existingParking.setName(updatedParking.getName());
            existingParking.setAddress(updatedParking.getAddress());
            existingParking.setCapacity(updatedParking.getCapacity());
            existingParking.setHourlyRate(updatedParking.getHourlyRate());
            return parkingRepository.save(existingParking);
        }
        return null;
    }
    public BigDecimal checkOutVehicle(Long spaceId) {
        ParkingSpace parkingSpace = parkingSpaceRepository.findById(spaceId).orElse(null);
        if (parkingSpace != null && "occupied".equals(parkingSpace.getStatus())) {
            LocalDateTime checkInTime = parkingSpace.getCheckInTime();
            LocalDateTime checkOutTime = LocalDateTime.now();

            Duration duration = Duration.between(checkInTime, checkOutTime);
            long hours = duration.toHours();

            Parking parking = parkingSpace.getParking();
            BigDecimal hourlyRate = BigDecimal.valueOf(parking.getHourlyRate());
            BigDecimal amountToPay = hourlyRate.multiply(BigDecimal.valueOf(hours));

            parkingSpace.setCheckOutTime(checkOutTime);
            parkingSpace.setStatus("available");
            parkingSpaceRepository.save(parkingSpace);

            // Registre a transação (se necessário)

            return amountToPay;
        }
        return BigDecimal.ZERO; // Pode ser tratado de forma adequada
    }
    public void checkInVehicle(Long spaceId) {
        ParkingSpace parkingSpace = parkingSpaceRepository.findById(spaceId).orElse(null);
        if (parkingSpace != null && "available".equals(parkingSpace.getStatus())) {
            parkingSpace.setStatus("occupied");
            parkingSpace.setCheckInTime(LocalDateTime.now());
            parkingSpaceRepository.save(parkingSpace);
        }
    }
}
