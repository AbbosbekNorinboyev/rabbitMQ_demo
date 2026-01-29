package uz.brb.rabbitMQ_demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.brb.rabbitMQ_demo.producer.PaymentProducer;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentProducer paymentProducer;

    // To'lovni boshlash
    @PostMapping("/{paymentId}")
    public ResponseEntity<String> makePayment(@PathVariable String paymentId) {
        paymentProducer.sendPaymentRequest(paymentId);
        return ResponseEntity.ok("✅ Payment request sent for ID: " + paymentId);
    }
}
