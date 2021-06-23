package com.ford.chibuisi.springcloudfunctiongcppubsub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.function.Consumer;

@SpringBootApplication
public class SpringCloudFunctionGcpPubSubApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudFunctionGcpPubSubApplication.class, args);
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public Consumer<PubSubMessage> pubSubFunction() {
        return message -> {
            System.out.println("The Pub/Sub message data: " + message.getData());
            String data = message.getData();
            PaymentReport paymentReport = null;
            try {
                paymentReport = objectMapper.readValue(data, PaymentReport.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            if(paymentReport!=null){

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
                    + "\n\n Thank you for your continued support \n\nEA fanatics");
        }
        catch (MessagingException messagingException){
            messagingException.printStackTrace();
        }
        javaMailSender.send(mimeMessage);
    }

}
