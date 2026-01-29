package uz.brb.rabbitMQ_demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.brb.rabbitMQ_demo.producer.MessageProducer;

import static uz.brb.rabbitMQ_demo.config.RabbitMQConfig.REQUEST_QUEUE;

@Service
@RestController
@RequiredArgsConstructor
public class MessageController {
    private final RabbitTemplate rabbitTemplate;
    private final MessageProducer messageProducer;

    @PostMapping("/send")
    public String sendMessage(@RequestParam String message) {
        messageProducer.send(message);
        return "Message sent: " + message;
    }

    @GetMapping("/api/read-message")
    public String readMessage() {
        // Xabarni o‘qiydi va avtomatik queue’dan o‘chadi
        return (String) rabbitTemplate.receiveAndConvert(REQUEST_QUEUE);
    }
}
