package uz.brb.rabbitMQ_demo;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String MAIN_EXCHANGE = "payment-exchange";
    public static final String REQUEST_QUEUE = "payment.request.queue";
    public static final String RETRY_5S_QUEUE = "payment.retry.5s.queue";
    public static final String RETRY_30S_QUEUE = "payment.retry.30s.queue";
    public static final String RETRY_2M_QUEUE = "payment.retry.2m.queue";
    public static final String DLQ = "payment.dlq";

    @Bean
    public DirectExchange mainExchange() {
        return new DirectExchange(MAIN_EXCHANGE);
    }

    // Asosiy queue
    @Bean
    public Queue requestQueue() {
        return QueueBuilder.durable(REQUEST_QUEUE)
                .withArgument("x-dead-letter-exchange", MAIN_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", RETRY_5S_QUEUE)
                .build();
    }

    // Retry 5s
    @Bean
    public Queue retry5sQueue() {
        return QueueBuilder.durable(RETRY_5S_QUEUE)
                .withArgument("x-message-ttl", 5000)
                .withArgument("x-dead-letter-exchange", MAIN_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", REQUEST_QUEUE)
                .build();
    }

    // Retry 30s
    @Bean
    public Queue retry30sQueue() {
        return QueueBuilder.durable(RETRY_30S_QUEUE)
                .withArgument("x-message-ttl", 30000)
                .withArgument("x-dead-letter-exchange", MAIN_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", REQUEST_QUEUE)
                .build();
    }

    // Retry 2m
    @Bean
    public Queue retry2mQueue() {
        return QueueBuilder.durable(RETRY_2M_QUEUE)
                .withArgument("x-message-ttl", 120000)
                .withArgument("x-dead-letter-exchange", MAIN_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", REQUEST_QUEUE)
                .build();
    }

    // DLQ
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DLQ).build();
    }

    // Bindinglar
    @Bean
    public Binding requestBinding() {
        return BindingBuilder.bind(requestQueue())
                .to(mainExchange())
                .with(REQUEST_QUEUE);
    }

    @Bean
    Binding retry5sBinding() {
        return BindingBuilder.bind(retry5sQueue())
                .to(mainExchange())
                .with(RETRY_5S_QUEUE);
    }


    @Bean
    Binding retry30sBinding() {
        return BindingBuilder.bind(retry30sQueue())
                .to(mainExchange())
                .with(RETRY_30S_QUEUE);
    }

    @Bean
    Binding retry2mBinding() {
        return BindingBuilder.bind(retry2mQueue())
                .to(mainExchange())
                .with(RETRY_2M_QUEUE);
    }

    @Bean
    Binding dlqBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(mainExchange())
                .with(DLQ);
    }
}
