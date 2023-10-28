package me.dio.domain.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_transaction_history")
public class TransactionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long clientId;  // Campo que associa a transação a um cliente

    private LocalDateTime transactionTime;
    private BigDecimal amount;
    private String paymentMethod;
    // Outros campos, como cliente, podem ser adicionados conforme necessário.

    public TransactionHistory() {
        // Construtor vazio
    }

    public TransactionHistory(Long id, Long clientId, LocalDateTime transactionTime, BigDecimal amount, String paymentMethod) {
        this.id = id;
        this.clientId = clientId;
        this.transactionTime = transactionTime;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    // Getters e setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
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