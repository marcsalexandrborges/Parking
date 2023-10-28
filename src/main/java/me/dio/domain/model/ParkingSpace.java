package me.dio.domain.model;

import org.springframework.http.ResponseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity(name = "tb_parking_space")
public class ParkingSpace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number")
    private String number;

    @Column(name = "type")
    private String type;

    @Column(name = "status")
    private String status;

    @Column(name = "owner")
    private String owner;

    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;

    @Column(name = "check_out_time")
    private LocalDateTime checkOutTime;

    @ManyToOne
    @JoinColumn(name = "parking_id")
    private Parking parking;


    public ParkingSpace() {
        // Construtor vazio
    }

    public ParkingSpace(Long id, String number, String type, String status, String owner, LocalDateTime checkInTime, LocalDateTime checkOutTime, Parking parking) {
        this.id = id;
        this.number = number;
        this.type = type;
        this.status = status;
        this.owner = owner;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.parking = parking;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public LocalDateTime getCheckOutTime() {
        return checkOutTime;
    }

    public Parking getParking() {
        return parking;
    }

    public void setParking(Parking parking) {
        this.parking = parking;
    }

    public ResponseEntity<String> checkIn() {
        if ("available".equals(this.status)) {
            this.status = "occupied";
            this.checkInTime = LocalDateTime.now();
            return ResponseEntity.ok("Check-in realizado com sucesso.");
        } else {
            return ResponseEntity.badRequest().body("A vaga não está disponível para check-in.");
        }
    }
    public ResponseEntity<String> checkOut() {
        if ("occupied".equals(this.status)) {
            LocalDateTime checkOutTime = LocalDateTime.now();
            BigDecimal amountToPay = calculateAmountToPay(checkOutTime);
            this.status = "available";
            setCheckOutTime(checkOutTime); // Chama o método para definir a data de check-out
            return ResponseEntity.ok("Valor a ser pago: " + amountToPay);
        } else {
            return ResponseEntity.badRequest().body("A vaga não está ocupada para check-out.");
        }
    }

    public void setCheckOutTime(LocalDateTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }


    private BigDecimal calculateAmountToPay(LocalDateTime checkOutTime) {
        LocalDateTime checkInTime = this.checkInTime;
        Duration duration = Duration.between(checkInTime, checkOutTime);
        long hours = duration.toHours();

        BigDecimal hourlyRate = BigDecimal.valueOf(parking.getHourlyRate());

        return hourlyRate.multiply(BigDecimal.valueOf(hours));
    }
}