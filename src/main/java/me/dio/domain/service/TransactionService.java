package me.dio.domain.service;

import me.dio.domain.model.ParkingSpace;
import me.dio.domain.model.Transaction;
import me.dio.domain.repository.ParkingSpaceRepository;
import me.dio.domain.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService {
    private final ParkingSpaceRepository parkingSpaceRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(ParkingSpaceRepository parkingSpaceRepository, TransactionRepository transactionRepository) {
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.transactionRepository = transactionRepository;
    }

    // Rest of the code in your TransactionService class


    public ParkingSpace getParkingSpaceById(Long id) {
        return parkingSpaceRepository.findById(id).orElse(null);
    }

    public void updateParkingSpace(Long id, ParkingSpace updatedParkingSpace) {
        ParkingSpace existingParkingSpace = parkingSpaceRepository.findById(id).orElse(null);
        if (existingParkingSpace != null) {
            // Atualize os campos desejados no objeto existingParkingSpace com os valores do objeto updatedParkingSpace.
            existingParkingSpace.setStatus(updatedParkingSpace.getStatus());

            // Salve as alterações no repositório de vagas de estacionamento.
            parkingSpaceRepository.save(existingParkingSpace);
        }
    }

    // Outros métodos da classe TransactionService
    public Transaction createTransaction(Long spaceId, BigDecimal amount, String paymentMethod) {
        ParkingSpace parkingSpace = parkingSpaceRepository.findById(spaceId).orElse(null);
        if (parkingSpace != null) {
            Transaction transaction = new Transaction(parkingSpace, amount, LocalDateTime.now(), paymentMethod);

            // Salve a transação no repositório de transações
            transaction = transactionRepository.save(transaction);

            // Atualize o status da vaga de estacionamento para "disponível" ou "ocupada", conforme apropriado
            if ("occupied".equals(parkingSpace.getStatus())) {
                parkingSpace.setStatus("available");
                parkingSpace.setCheckOutTime(LocalDateTime.now());
                parkingSpaceRepository.save(parkingSpace);
            }

            return transaction;
        }
        return null;
    }

}
