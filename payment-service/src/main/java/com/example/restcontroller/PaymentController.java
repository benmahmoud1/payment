package com.example.restcontroller;

import com.example.enums.PaymentStatus;
import com.example.model.Transaction;
import com.example.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController implements PaymentApi{


    private final PaymentService paymentService;

    @Override
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        return ResponseEntity.ok(paymentService.createTransaction(transaction));
    }

    @Override
    public ResponseEntity<Transaction> updateTransactionStatus(@PathVariable Long id, @RequestParam PaymentStatus status) {
        return ResponseEntity.ok(paymentService.updateTransactionStatus(id, status));
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return paymentService.getAllTransactions();
    }
}
