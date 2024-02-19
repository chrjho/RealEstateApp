package com.chris.realtor.Models;

public class Email {
    // private String name;
    private String fromUsername;
    // private String fromEmail;
    private String toUsername;
    private String message;
    private String listing;



    // public String getName() {
    //     return this.name;
    // }

    // public void setName(String name) {
    //     this.name = name;
    // }

    public String getFromUsername() {
        return this.fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    // public String getFromEmail() {
    //     return this.fromEmail;
    // }

    // public void setFromEmail(String fromEmail) {
    //     this.fromEmail = fromEmail;
    // }

    public String getToUsername() {
        return this.toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getListing() {
        return this.listing;
    }

    public void setListing(String listing) {
        this.listing = listing;
    }
}
