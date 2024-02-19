package com.chris.realtor.Models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EmailDB {
    private String sender;
    private String recipient;
    private String message;
    private String timestamp;
    private String listing;
    private String thread;


    public EmailDB(String sender, String recipient, String message, String listing) {
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
        this.listing = listing;
    }

    public EmailDB(String sender, String recipient, String message, String timestamp, String listing, String thread) {
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
        setTimestamp(timestamp);
        this.listing = listing;
        this.thread = thread;
    }

    public String getSender() {
        return this.sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return this.recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String timestamp) {
        // Define your desired date and time format (without seconds)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    
        try {
            // Parse the timestamp string into a Date object
            Date date = dateFormat.parse(timestamp);
            this.timestamp = dateFormat.format(date);
        } catch (ParseException e) {
            // Handle the parse exception (e.g., log an error or set a default value)
            e.printStackTrace();
            this.timestamp = null; // Set the timestamp to null or handle the error appropriately
        }
    }
    

    public String getListing() {
        return this.listing;
    }

    public void setListing(String listing) {
        this.listing = listing;
    }

    
    public String getThread() {
        return this.thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    // public String getFormattedTimestamp() {
    //     if (timestamp != null) {
    //         // Convert the Timestamp to a Date
    //         Date date = new Date(getTimestamp());

    //         // Define your desired date and time format (without seconds)
    //         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    //         // Format the Date object to the desired format
    //         return dateFormat.format(date);
    //     }
    //     return null; // Handle the case where the timestamp is null
    // }
}
