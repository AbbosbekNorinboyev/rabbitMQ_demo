package uz.brb.rabbitMQ_demo.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import uz.brb.rabbitMQ_demo.config.RabbitMQConfig;

@Service
@RequiredArgsConstructor
public class PaymentProducer {
    private final RabbitTemplate rabbitTemplate;

    public void sendPaymentRequest(String paymentId) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.MAIN_EXCHANGE,
                RabbitMQConfig.REQUEST_QUEUE,
                paymentId
        );
        System.out.println("✅ Payment request sent: " + paymentId);
    }
}