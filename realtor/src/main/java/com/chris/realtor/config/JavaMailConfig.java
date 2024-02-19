package com.chris.realtor.config;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class JavaMailConfig {
    @Value("${gmail.username}")
    private String username;

    @Value("${gmail.password}")
    private String password;

    @Bean
    public Session mailSession() {
        // final String username = System.getProperty("gmail.username"); // email address
        // final String password = System.getProperty("gmail.password"); // app password


        System.out.println("GMAIL USERNAME: "+username);
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        props.put("mail.store.protocol", "imaps"); // Use "imaps" for SSL/TLS
        props.put("mail.imaps.host", "imap.gmail.com");
        props.put("mail.imaps.port", "993"); // Port 993 for SSL/TLS
        props.put("mail.imaps.starttls.enable", "true"); // Enable STARTTLS for secure connection

        
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        System.out.println("Setting up mail session...");
        return session;
    }
}
