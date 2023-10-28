package me.dio.domain.service;

import me.dio.domain.model.Parking;
import me.dio.domain.model.ParkingSpace;
import me.dio.domain.repository.ParkingSpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentService {
    private final ParkingSpaceRepository parkingSpaceRepository;

    @Autowired
    public PaymentService(ParkingSpaceRepository parkingSpaceRepository) {
        this.parkingSpaceRepository = parkingSpaceRepository;
    }

    // Adicione métodos para calcular o valor do pagamento aqui

    public BigDecimal calculatePayment(Long spaceId) {
        ParkingSpace parkingSpace = parkingSpaceRepository.findById(spaceId).orElse(null);
        if (parkingSpace != null && "occupied".equals(parkingSpace.getStatus())) {
            LocalDateTime checkInTime = parkingSpace.getCheckInTime();
            LocalDateTime checkOutTime = LocalDateTime.now();

            Duration duration = Duration.between(checkInTime, checkOutTime);
            long hours = duration.toHours();

            Parking parking = parkingSpace.getParking();
            BigDecimal hourlyRate = BigDecimal.valueOf(parking.getHourlyRate());
            BigDecimal amountToPay = hourlyRate.multiply(BigDecimal.valueOf(hours));

            return amountToPay;
        }
        return BigDecimal.ZERO;
    }

}