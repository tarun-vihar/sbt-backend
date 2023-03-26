package com.project.sbt.services;

import com.project.sbt.constants.EmailConstants;
import com.project.sbt.model.dto.StudentDTO;
import com.project.sbt.model.request.EmailRequest;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class EmailService {


    @Value("${sendgrid.api.key}")
    private String apiKey;


    public Response sendEmail(EmailRequest emailRequest) throws IOException{

        Email from = new Email(EmailConstants.senderEmail);
        Content emailContent = new Content("text/plain", emailRequest.getContent());
        Mail mail = new Mail(from, emailRequest.getSubject(), new Email(emailRequest.getToAddress()), emailContent);

        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        Response response = sg.api(request);
        return response;

    }
}
