package com.r3tech.springbootmailservice.service;

import com.r3tech.springbootmailservice.model.MailContent;
import com.r3tech.springbootmailservice.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private static final String PAYMENT_COMPLETE_TOPIC = "paymentCompleteTopic";

    @Autowired
    private JavaMailSender javaMailSender;
    public void sendMessage(MailContent message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setTo(message.getEmailId());
        simpleMailMessage.setSubject(message.getSubject());
        simpleMailMessage.setText(message.getContent());

        javaMailSender.send(simpleMailMessage);
    }

    @KafkaListener(topics = PAYMENT_COMPLETE_TOPIC , groupId = "group_json")
    public void prepareMailContentForOrder(Order order){
        MailContent message = new MailContent();
        message.setEmailId("rajuy369@gmail.com");
        message.setSubject("Order # "+ order.getOrderId() +"has been placed succefully");
        message.setContent("Your order placed succesfuly and payment has been done for amount" + order.getTotalAmount());

        sendMessage(message);

    }
    @Bean
    public StringJsonMessageConverter jsonConverter() {
        return new StringJsonMessageConverter();
    }

}
