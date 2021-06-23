package com.ford.chibuisi.springcloudfunctiongcppubsub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ford.chibuisi.springcloudfunctiongcppubsub.model.Payment;
import com.ford.chibuisi.springcloudfunctiongcppubsub.model.PaymentReport;
import com.ford.chibuisi.springcloudfunctiongcppubsub.model.PubSubMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;
import java.util.function.Consumer;

@SpringBootApplication
public class SpringCloudFunctionGcpPubSubApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudFunctionGcpPubSubApplication.class, args);
    }

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public Consumer<PubSubMessage> pubSubFunction() {
        return message -> {
            System.out.println("The Pub/Sub message data: " + message.getData());
            String data = new String(Base64.getDecoder().decode(message.getData()));
            System.out.println("The Pub/Sub data is : " + data);
            Payment payment = null;
            try {
                payment = objectMapper.readValue(data, Payment.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            PaymentReport paymentReport = null;
            if(payment!=null){
                paymentReport = PaymentReport.builder()
                        .uuid(UUID.randomUUID())
                        .user(payment.getUser())
                        .amount(payment.getAmount())
                        .dateTime(LocalDateTime.now())
                        .status("success")
                        .build();
                sendEmail(paymentReport);
            }
        };
    }

    public void sendEmail(PaymentReport paymentReport){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        try{
            mimeMessageHelper.setTo(paymentReport.getUser());
            mimeMessageHelper.setSubject("Your payment has been received and processed successfully");
            mimeMessageHelper.setText("Payment for account: " + paymentReport.getUser()
                    + "\n Amount " + paymentReport.getAmount()
                    + "  has been processed successfully on " + paymentReport.getDateTime()
                    + "\n\nThank you for your continued support");
        }
        catch (MessagingException messagingException){
            messagingException.printStackTrace();
        }
        javaMailSender.send(mimeMessage);
    }

}
