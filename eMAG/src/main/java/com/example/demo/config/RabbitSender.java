package com.example.demo.config;

import com.example.demo.dtos.EmailRequestDto;
import com.example.demo.entities.EmailRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.example.demo.config.AMQPConfig.EXCHANGE_NAME;
import static com.example.demo.config.AMQPConfig.ROUTING_KEY;

@Component
public class RabbitSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void send(EmailRequestDto payload) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, payload);
    }
}

