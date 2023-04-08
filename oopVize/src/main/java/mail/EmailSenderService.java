package mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    /*Mail gönderilirken çağrılan fonksiyon*/
    public void sendSimpleEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mahoo3447@gmail.com"); /*Gönderen*/
        message.setTo(toEmail);/*Maili alan*/
        message.setText(body);/*Mail açıklaması*/
        message.setSubject(subject);/*Mail başlığı*/
        mailSender.send(message);/*Gönderme işlemi*/
        System.out.println("Mail Gönderildi...");


    }

}
