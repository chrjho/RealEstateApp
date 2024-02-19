package com.chris.realtor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.chris.realtor.Utils.EmailListeningService;

@EnableScheduling
@Component
public class EmailListeningScheduler {

    @Autowired
    private EmailListeningService emailListeningService;

    @Scheduled(fixedRate = 15000) // Check for new emails every minute
    public void checkForNewEmails() {
        System.out.println("LISTENING FOR EMAILS...");
        emailListeningService.listenForEmails();
    }
}