package me.dio.domain.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.dio.domain.model.TransactionHistory;
import me.dio.domain.service.TransactionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Tag(name = "Transaction History", description = "Endpoints para histórico de transações")
@RestController
@RequestMapping("/api/transaction-history")
public class TransactionHistoryController {
    private final TransactionHistoryService transactionHistoryService;

    @Autowired
    public TransactionHistoryController(TransactionHistoryService transactionHistoryService) {
        this.transactionHistoryService = transactionHistoryService;
    }

    @GetMapping("/{clientId}")
    @Operation(summary = "Obter histórico de transações por cliente", description = "Obtém o histórico de transações para um cliente específico.")
    @ApiResponse(responseCode = "200", description = "Histórico de transações encontrado")
    @ApiResponse(responseCode = "204", description = "Histórico de transações não encontrado")
    public ResponseEntity<List<TransactionHistory>> getTransactionHistoryByClient(@PathVariable Long clientId) {
        List<TransactionHistory> history = transactionHistoryService.getTransactionsByClientId(clientId);

        if (history.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(history);
        }
    }
}
