package me.dio.domain.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.dio.domain.model.ParkingSpace;
import me.dio.domain.model.Transaction;
import me.dio.domain.repository.ParkingSpaceRepository;
import me.dio.domain.repository.TransactionRepository;
import me.dio.domain.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Tag(name = "Transaction", description = "Endpoints para gerenciamento de transações")
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;
    private final ParkingSpaceRepository parkingSpaceRepository; // Adicione o ParkingSpaceRepository

    @Autowired
    public TransactionController(
            TransactionService transactionService,
            TransactionRepository transactionRepository,
            ParkingSpaceRepository parkingSpaceRepository) {
        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
        this.parkingSpaceRepository = parkingSpaceRepository;
    }
    @Operation(summary = "Cria uma nova transação")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Transação criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
    })
    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody TransactionRequest request) {
        ParkingSpace parkingSpace = transactionService.getParkingSpaceById(request.getParkingSpaceId());

        if (parkingSpace != null && "occupied".equals(parkingSpace.getStatus())) {
            BigDecimal amount = request.getAmount();
            String paymentMethod = request.getPaymentMethod();

            // Adicione validações e, se necessário, retorne o erro 400 com uma mensagem personalizada.
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Amount must be greater than zero.");
            }

            // Crie a transação com os dados fornecidos no construtor.
            Transaction transaction = new Transaction(parkingSpace, amount, LocalDateTime.now(), paymentMethod);

            transaction = transactionRepository.save(transaction);

            // Atualize o status da vaga de estacionamento para "available" usando o ParkingService.
            parkingSpace.setStatus("available");
            transactionService.updateParkingSpace(parkingSpace.getId(), parkingSpace);

            return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Requisição inválida"); // Ou outra mensagem de erro, se desejar
        }
    }





    // ... outros métodos

    @Operation(summary = "Obtém uma vaga de estacionamento por ID")
    @ApiResponse(responseCode = "200", description = "Vaga de estacionamento encontrada")
    @ApiResponse(responseCode = "404", description = "Vaga de estacionamento não encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<ParkingSpace> getParkingSpaceById(
            @Parameter(description = "ID da vaga de estacionamento") @PathVariable Long id) {
        ParkingSpace parkingSpace = parkingSpaceRepository.findById(id)
                .orElse(null);

        if (parkingSpace != null) {
            return ResponseEntity.ok(parkingSpace);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public static class TransactionRequest {
        private Long parkingSpaceId;
        private BigDecimal amount;
        private String paymentMethod;

        public Long getParkingSpaceId() {
            return parkingSpaceId;
        }

        public void setParkingSpaceId(Long parkingSpaceId) {
            this.parkingSpaceId = parkingSpaceId;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }
    }
}
