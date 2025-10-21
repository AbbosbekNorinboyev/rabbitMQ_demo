package uz.brb.rabbitMQ_demo;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentConsumer {
    private final RabbitTemplate rabbitTemplate;
    private int attemptCount = 0;

    @RabbitListener(queues = RabbitMQConfig.REQUEST_QUEUE)
    public void consume(String paymentId) {
        System.out.println("🔔 Received payment: " + paymentId + " | attempt=" + attemptCount);

        try {
            attemptCount++;

            // Simulyatsiya: 3-urinishgacha xato, keyin success
            if (attemptCount < 3) {
                throw new RuntimeException("Bank API timeout");
            }

            // Agar 3-urinishda ishlasa
            System.out.println("✅ Payment success: " + paymentId);

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());

            // Retry chain logikasi
            if (attemptCount == 1) {
                rabbitTemplate.convertAndSend(RabbitMQConfig.MAIN_EXCHANGE,
                        RabbitMQConfig.RETRY_5S_QUEUE, paymentId);
            } else if (attemptCount == 2) {
                rabbitTemplate.convertAndSend(RabbitMQConfig.MAIN_EXCHANGE,
                        RabbitMQConfig.RETRY_30S_QUEUE, paymentId);
            } else if (attemptCount == 3) {
                rabbitTemplate.convertAndSend(RabbitMQConfig.MAIN_EXCHANGE,
                        RabbitMQConfig.RETRY_2M_QUEUE, paymentId);
            } else {
                // Retry limit tugasa → DLQ
                rabbitTemplate.convertAndSend(RabbitMQConfig.MAIN_EXCHANGE,
                        RabbitMQConfig.DLQ, paymentId);
                System.out.println("🚨 Sent to DLQ: " + paymentId);
            }
        }
    }
}
