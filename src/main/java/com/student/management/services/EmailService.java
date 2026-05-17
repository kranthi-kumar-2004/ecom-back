package com.student.management.services;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import sendinblue.ApiClient;

import sibApi.TransactionalEmailsApi;

import sibModel.SendSmtpEmail;
import sibModel.SendSmtpEmailSender;
import sibModel.SendSmtpEmailTo;

@Service
public class EmailService {

    @Value("${BREVO_API_KEY}")
    private String apiKey;

    public void sendOtp(String toEmail, String otp) {

        try {

            ApiClient defaultClient = new ApiClient();

            defaultClient.setApiKey(apiKey);

            TransactionalEmailsApi apiInstance =
                    new TransactionalEmailsApi(defaultClient);

            SendSmtpEmail email = new SendSmtpEmail();

            email.setSender(
                    new SendSmtpEmailSender()
                            .email("sarvepallikranthikumar@gmail.com")
                            .name("Smart Ecommerce")
            );

            email.setTo(Collections.singletonList(
                    new SendSmtpEmailTo().email(toEmail)
            ));

            email.setSubject("Password Reset OTP");

            email.setHtmlContent(
                    "<h2>Your OTP is: " + otp + "</h2>"
            );

            apiInstance.sendTransacEmail(email);

            System.out.println("OTP SENT");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}