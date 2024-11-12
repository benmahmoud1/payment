package com.example.services;

import com.example.enums.PaymentStatus;
import com.example.events.PaymentCreatedEvent;
import com.example.events.PaymentEvent;
import com.example.events.PaymentStatusUpdatedEvent;
import com.example.model.Transaction;
import com.example.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PaymentService {


    private final TransactionRepository transactionRepository;
    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    public Transaction createTransaction(Transaction transaction) {
        transaction.setPaymentStatus(PaymentStatus.NEW);
        transaction = transactionRepository.save(transaction);

        // Publier l'événement de création de transaction
        PaymentCreatedEvent createdEvent =PaymentCreatedEvent.builder()
                .transactionId(transaction.getId())
                .paymentType(transaction.getPaymentType())
                .totalAmount(transaction.getTotalAmount())
                .build();

        kafkaTemplate.send("payment_events", createdEvent);

        return transaction;
    }

    public Transaction updateTransactionStatus(Long id, PaymentStatus newStatus) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // Vérifications du statut
        if (transaction.getPaymentStatus().equals(PaymentStatus.CAPTURED) || transaction.getPaymentStatus().equals(PaymentStatus.CANCELED)) {
            throw new IllegalStateException("Cannot update a captured or canceled transaction");
        }

        if (newStatus.equals(PaymentStatus.CAPTURED) && !transaction.getPaymentStatus().equals(PaymentStatus.AUTHORIZED)) {
            throw new IllegalStateException("Can only capture an authorized transaction");
        }

        transaction.setPaymentStatus(newStatus);
        transaction = transactionRepository.save(transaction);

        // Publier l'événement de mise à jour du statut de la transaction
        PaymentStatusUpdatedEvent statusUpdatedEvent = PaymentStatusUpdatedEvent.builder()
                .newStatus(newStatus)
                .transactionId(transaction.getId())
                .build();
        kafkaTemplate.send("payment_events", statusUpdatedEvent);

        return transaction;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
