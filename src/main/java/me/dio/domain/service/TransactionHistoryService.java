package me.dio.domain.service;

import me.dio.domain.model.TransactionHistory;
import me.dio.domain.repository.TransactionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionHistoryService {
    private final TransactionHistoryRepository transactionHistoryRepository;

    @Autowired
    public TransactionHistoryService(TransactionHistoryRepository transactionHistoryRepository) {
        this.transactionHistoryRepository = transactionHistoryRepository;
    }

    public List<TransactionHistory> getTransactionsByClientId(Long clientId) {
        return transactionHistoryRepository.findByClientId(clientId);
    }

    // Outros métodos do serviço...
}
