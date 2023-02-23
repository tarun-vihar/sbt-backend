package com.project.sbt.services;

import com.project.sbt.model.dto.StudentDTO;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {


    @Value("${sendgrid.api.key}")
    private String apiKey;




    public void sendEmail(StudentDTO student) throws IOException {
        Email from = new Email("sbt.applicants@gmail.com");



        String encodeStudentId = URLEncoder.encode(student.getStudentId(), StandardCharsets.UTF_8.toString());
        String encodedToken = URLEncoder.encode(student.getVerificationCode(), StandardCharsets.UTF_8.toString());

        String subject = "Verification Email";

        String content = "Dear " + student.getStudentName() + ", please click the link to verify your account: http://localhost:4200/verify?"
                + "&studentId=" + encodeStudentId + "&token=" + encodedToken;


        Content emailContent = new Content("text/plain",content);

        Mail mail = new Mail(from, subject, new Email(student.getStudentEmail()), emailContent);

        System.out.println("Api " + apiKey);
        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        Response response = sg.api(request);
    }
}
