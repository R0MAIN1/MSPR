package com.mspr.payetonkawasrvc.email;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Slf4j
@Component
@RequiredArgsConstructor
@Service
public class EmailSenderServiceServiceImpl implements EmailSenderService {

   private final JavaMailSender mailSender;

   @Override
   @Async
   public void send(String to, String mail) {
      try {
         String senderName = "User Registration Portal Service";
         MimeMessage mimeMessage = mailSender.createMimeMessage();
         MimeMessageHelper helper =
                 new MimeMessageHelper(mimeMessage, "utf-8");
         helper.setText(mail, true);
         helper.setTo(to);
         helper.setSubject("Confirm your email");
         helper.setFrom("lepdg237@gmai.com", senderName);
         mailSender.send(mimeMessage);
         log.info("message  envoye ");
      } catch (MessagingException | UnsupportedEncodingException e) {
         log.error(e.getMessage());
         throw new RuntimeException(e);
      }
   }
}