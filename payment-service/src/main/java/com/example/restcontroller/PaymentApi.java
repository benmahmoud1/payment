package com.example.restcontroller;

import com.example.enums.PaymentStatus;
import com.example.model.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface PaymentApi {

    @PostMapping
    ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction);

    @PutMapping("/{id}/status")
    ResponseEntity<Transaction> updateTransactionStatus(@PathVariable Long id, @RequestParam PaymentStatus status);

    @GetMapping
    List<Transaction> getAllTransactions();
}
