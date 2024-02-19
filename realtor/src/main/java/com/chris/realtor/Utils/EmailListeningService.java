package com.chris.realtor.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chris.realtor.Processors.UserProcessor;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.search.FlagTerm;
import javax.mail.search.HeaderTerm;


@Service
public class EmailListeningService {

    @Autowired
    private UserProcessor userProcessor;

    @Autowired
    private Session mailSession;

    @Autowired
    private EmailRelayService relayService;

    public void listenForEmails() {
        try {
            System.out.println("In listening...");
            Store store = mailSession.getStore("imaps");
            store.connect();

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

            for (Message message : messages) {
                // Process the email content and extract information
                String from = InternetAddress.toString(message.getFrom());
                InternetAddress[] fromAddresses = InternetAddress.parse(from, false);
                from = fromAddresses[0].getAddress();
                String subject = message.getSubject();
                String content = extractTextFromMessage(message);

                // Implement forwarding logic to relay the email to the proper end users
                relayEmailToProperUsers(InternetAddress.toString(message.getAllRecipients()), from, subject, content);

                // Mark the email as read
                message.setFlag(Flags.Flag.SEEN, true);
            }

            inbox.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String extractTextFromMessage(Message message) throws Exception {
        if (message.isMimeType("text/plain")) {
            // If the message is plain text, simply return its content
            return message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            // If the message is multipart, iterate through its parts and extract text
            Multipart multipart = (Multipart) message.getContent();
            StringBuilder textContent = new StringBuilder();
    
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart part = multipart.getBodyPart(i);
                if (part.isMimeType("text/plain")) {
                    // Append the text/plain part to the content
                    textContent.append(part.getContent().toString());
                }
            }
    
            return textContent.toString();
        } else {
            // Handle other content types as needed
            return ""; // Or return a message indicating that the content type is not supported
        }
    }

    // private String extractLatestMessageContent(Message message, Folder inbox) throws Exception {
    //     // Retrieve the "In-Reply-To" header of the current message
    //     String inReplyTo = message.getHeader("In-Reply-To")[0];
    
    //     // Search for messages with the same "In-Reply-To" header in the inbox
    //     Message[] threadMessages = inbox.search(new HeaderTerm("In-Reply-To", inReplyTo));
    
    //     // Find the latest message in the thread
    //     Message latestMessage = null;
    //     for (Message threadMessage : threadMessages) {
    //         if (latestMessage == null || threadMessage.getSentDate().compareTo(latestMessage.getSentDate()) > 0) {
    //             latestMessage = threadMessage;
    //         }
    //     }
    
    //     // Extract the text content of the latest message
    //     String content = extractTextFromMessage(latestMessage);
    
    //     return content;
    // }
    

    private void relayEmailToProperUsers(String to, String from, String subject, String content) {
        // System.out.println("RELAYING EMAIL FROM: "+to+", Subject: "+subject+", Message: "+content);
        try{
            System.out.println("EMAIL FROM: "+ from);
            String seller = userProcessor.getEmail(subject.split("Inquiry for listing ")[1].split("_")[0]).strip();
            System.out.println("SELLER: "+ seller);
            String inquirer = userProcessor.getEmail(subject.split("from: ")[1]).strip();
            if(!from.equals(seller)){
                System.out.println("is message to seller!!!");
                relayService.relayEmail(seller, subject, content);
            }
            else{
                System.out.println("is message to inquirer!!!");
                relayService.relayEmail(inquirer, subject, content);
            }
        }catch(MessagingException e){
            e.printStackTrace();
        }
    }
}
