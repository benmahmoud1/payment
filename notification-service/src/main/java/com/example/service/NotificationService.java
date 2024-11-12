package com.example.service;

import com.example.events.PaymentCreatedEvent;
import com.example.events.PaymentEvent;
import com.example.events.PaymentStatusUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class NotificationService {

 

    // Consommateur Kafka avec logique avancée de notification
    @KafkaListener(topics = "payment_events", groupId = "notificationGroup")
    public void consumeEvent(ConsumerRecord<String, PaymentEvent> record) {
        PaymentEvent event = record.value();

        try {
            // Log de l'événement reçu
            log.info("Received event: {} at offset: {}", event, record.offset());

            // Traitement en fonction du type d'événement
            if (event instanceof PaymentCreatedEvent) {
                handlePaymentCreated((PaymentCreatedEvent) event);
            } else if (event instanceof PaymentStatusUpdatedEvent) {
                handlePaymentStatusUpdated((PaymentStatusUpdatedEvent) event);
            } else {
                log.warn("Unknown event type received: {}", event.getClass().getSimpleName());
            }

        } catch (Exception e) {

            log.error("Failed to process event: {}", event, e);

        }
    }

    private void handlePaymentCreated(PaymentCreatedEvent event) {

        log.info("Processing PaymentCreatedEvent for Transaction ID: {}", event.getTransactionId());

        notify("Payment created", "Transaction ID: " + event.getTransactionId() +
                ", Amount: " + event.getTotalAmount() +
                ", Type: " + event.getPaymentType());
    }

    private void handlePaymentStatusUpdated(PaymentStatusUpdatedEvent event) {
        log.info("Processing PaymentStatusUpdatedEvent for Transaction ID: {}, New Status: {}",
                event.getTransactionId(), event.getNewStatus());

        notify("Payment status updated", "Transaction ID: " + event.getTransactionId() +
                ", New Status: " + event.getNewStatus());
    }


    private void notify(String subject, String message) {

        log.info("Sending notification - Subject: {}, Message: {}", subject, message);

    }
}
