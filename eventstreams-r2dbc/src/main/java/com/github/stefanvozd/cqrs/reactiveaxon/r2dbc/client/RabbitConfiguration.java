package com.github.stefanvozd.cqrs.reactiveaxon.r2dbc.client;

import com.rabbitmq.client.Delivery;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.*;

import java.time.Duration;

@Configuration
public class RabbitConfiguration {

    @Bean
    Sender sender() {
        return RabbitFlux.createSender();
    }

    @Bean
    Receiver receiver() {
        return RabbitFlux.createReceiver();
    }

    @Bean
    Flux<Delivery> deliveryFlux(Sender sender,Receiver receiver) {
        return Mono.when(sender.declareQueue(QueueSpecification.queue(Messaging.BANK_QUEUE)))
                .thenMany(receiver.consumeNoAck(Messaging.BANK_QUEUE, new ConsumeOptions()
                        .exceptionHandler(new ExceptionHandlers.RetryAcknowledgmentExceptionHandler(
                                Duration.ofSeconds(10), Duration.ofMillis(500),
                                ExceptionHandlers.CONNECTION_RECOVERY_PREDICATE
                        ))));
    }

}
