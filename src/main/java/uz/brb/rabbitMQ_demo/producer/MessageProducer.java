package uz.brb.rabbitMQ_demo.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static uz.brb.rabbitMQ_demo.config.RabbitMQConfig.MAIN_EXCHANGE;
import static uz.brb.rabbitMQ_demo.config.RabbitMQConfig.REQUEST_QUEUE;

@Service
@RequiredArgsConstructor
public class MessageProducer {
    private final RabbitTemplate rabbitTemplate;

    public void send(String message) {
        rabbitTemplate.convertAndSend(MAIN_EXCHANGE, REQUEST_QUEUE, message);
        System.out.println("✅ Message sent: " + message);
    }
}
