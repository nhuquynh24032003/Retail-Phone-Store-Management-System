package com.example.ecommerce.services.impl;

import com.example.ecommerce.models.EmailDetail;
import com.example.ecommerce.services.EmailDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailDetailServiceImpl implements EmailDetailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public String sendSimpleMail(EmailDetail emailDetail){
        try{
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(emailDetail.getRecipient());
            mailMessage.setText(emailDetail.getMsgBody());
            mailMessage.setSubject(emailDetail.getSubject());

            javaMailSender.send(mailMessage);
            return "Send mail successfully";
        }
        catch(Exception e){
            return "Cannot send mail or email not exist";
        }
    }

}
