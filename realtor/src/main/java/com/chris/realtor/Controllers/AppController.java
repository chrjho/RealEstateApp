package com.chris.realtor.Controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.chris.realtor.Controllers.AppController.DeleteRequest;
import com.chris.realtor.Models.Email;
import com.chris.realtor.Models.EmailDB;
import com.chris.realtor.Models.House;
import com.chris.realtor.Models.SellRequest;
import com.chris.realtor.Models.UserRequest;
import com.chris.realtor.Processors.EmailsProcessor;
import com.chris.realtor.Processors.HouseProcessor;
import com.chris.realtor.Processors.UserProcessor;
import com.chris.realtor.Utils.ImageUploadService;
import com.chris.realtor.Utils.EmailListeningService;
import com.chris.realtor.Utils.EmailUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class AppController {

    @Autowired
    private UserProcessor userProcessor;
    
    @Autowired
    private HouseProcessor houseProcessor;

    @Autowired
    private EmailsProcessor emailsProcessor;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private EmailListeningService emailListeningService;

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody UserRequest request) {
        System.out.println("Received input: "+request.getUsername()+request.getEmail()+request.getPassword());
        //String response = "request.getText()";
        userProcessor.insertUser(request);
        return ResponseEntity.ok("{\"message\": \"User successfully signed up.\"}");
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserRequest request,HttpServletResponse response) {
        if(userProcessor.verifyPassword(request)==true){
            String username = "";
            if(request.getUsername() != null){
                username = request.getUsername();
            }
            else if(request.getEmail() != null){
                username = userProcessor.getUsername(request.getEmail());
            }
            Cookie cookie = new Cookie("userToken", request.getUsername());

            // Set additional attributes if needed
            cookie.setMaxAge(30 * 24 * 60 * 60); // 30 days expiration
            cookie.setPath("/"); // Cookie will be accessible on all paths of the website
            cookie.setSecure(false);
            // Add the cookie to the response
            response.addCookie(cookie);
            response.setStatus(HttpServletResponse.SC_CREATED);
            return ResponseEntity.ok("{\"message\": \"User successfully logged in: "+username+"\"}");
        }
        return ResponseEntity.ok("{\"message\": \"Incorrect password.\"}");
    }

    @PostMapping("/sell")
    public ResponseEntity sell(@RequestBody SellRequest request,HttpServletResponse response) {
        System.out.println("Received input: "+request.getAddress()+request.getState());
        String listing = houseProcessor.insertListing(request);
        return ResponseEntity.ok("{\"message\": \""+listing+"\"}");
    }

    @PostMapping("/uploads")
    public ResponseEntity<Map<String, String>> sellWithImages(
    @RequestParam("listing") String listing,
    @RequestParam("image") List<MultipartFile> imageFiles) throws IOException {
        for (MultipartFile imageFile : imageFiles) {
            // Decrypt the listing id
            System.out.println("TO DECRYPT: "+listing);
            // String decryptedString = HouseProcessor.encryptor.decrypt(listing+"==");
            byte[] decodedBytes = Base64.getUrlDecoder().decode(listing);
            String decryptedString = new String(decodedBytes);
            System.out.println("Decoded String: " + decryptedString);
            System.out.println("Decrypted: " + decryptedString);
            String objectKey = "images/" + decryptedString + imageFile.getOriginalFilename();

            // Set metadata for the S3 object
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(imageFile.getContentType());

            imageUploadService.uploadImage("realtorappimages", objectKey, imageFile.getBytes(), metadata.getContentType());
            houseProcessor.updateListing(decryptedString,999,999,999,999,objectKey);
        }
        // Build the response message
        Map<String, String> response = new HashMap<>();
        response.put("message", "Image uploaded to S3");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/edit-listing")
    public ResponseEntity<Map<String, String>> updateListing(
        @RequestParam("listing") String listing,
        @RequestParam("price") int price,
        @RequestParam("beds") int beds,
        @RequestParam("baths") int baths,
        @RequestParam("size") int size,
        @RequestParam(value = "images", required = false) MultipartFile[] images) {
    
        if(images.length != 0){
            for (MultipartFile imageFile : images) {
                // Generate a unique key for the S3 object (you can use UUID, timestamp, etc.)
                String objectKey = "images/" + listing + imageFile.getOriginalFilename();

                // Set metadata for the S3 object
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(imageFile.getContentType());
                try{
                    imageUploadService.uploadImage("realtorappimages", objectKey, imageFile.getBytes(), metadata.getContentType());
                    houseProcessor.updateListing(listing,price,beds,baths,size,objectKey);
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        else{
            houseProcessor.updateListing(listing,price,beds,baths,size);
        }

        System.out.println("GOT LISTING ID UPDATE: "+listing);
        // Build the response message
        Map<String, String> response = new HashMap<>();
        response.put("message", "updated");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/edit-listing-no-image")
    public ResponseEntity<Map<String, String>> updateListing(
        @RequestParam("listing") String listing,
        @RequestParam("price") int price,
        @RequestParam("beds") int beds,
        @RequestParam("baths") int baths,
        @RequestParam("size") int size){
    
        try{
            houseProcessor.updateListing(listing,price,beds,baths,size);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("GOT LISTING ID UPDATE: "+listing);
        // Build the response message
        Map<String, String> response = new HashMap<>();
        response.put("message", "updated");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/buy")
    public ResponseEntity buy(@RequestParam(value="username",required=true)String user) {
        System.out.println("FOUND KEY: "+System.getProperty("s3.access.key"));
        System.out.println("Getting listings...");
        List<House> listings = houseProcessor.getListings(user);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonListings = "";
        try{
            jsonListings = objectMapper.writeValueAsString(listings);
        }
        catch(JsonProcessingException e){
            e.printStackTrace();
        }        
        return ResponseEntity.ok(jsonListings);
    }

    @GetMapping("/mylistings")
    public ResponseEntity myListings(@RequestParam(value="username",required=true)String user) {
        List<House> listings = houseProcessor.getListingsForUser(user);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonListings = "";
        try{
            jsonListings = objectMapper.writeValueAsString(listings);
        }
        catch(JsonProcessingException e){
            e.printStackTrace();
        }
        return ResponseEntity.ok(jsonListings);
    }

    @GetMapping("/view/{listing}")
    public ResponseEntity viewListing(@PathVariable("listing") String listing) {
        House house = houseProcessor.getListing(listing);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonListings = "";
        try{
            System.out.println("VIEW LISTING: "+listing+'\n');
            jsonListings = objectMapper.writeValueAsString(house);
            emailListeningService.listenForEmails();
        }
        catch(JsonProcessingException e){
            e.printStackTrace();
        }
        return ResponseEntity.ok(jsonListings);
    }

    @PostMapping("/contact")
    public ResponseEntity sendEmail(@RequestBody Email emailInfo) {

        final String username = System.getProperty("gmail.username"); // email address
        final String password = System.getProperty("gmail.password"); // app password

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
	    
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
	    EmailUtil emailUtil = new EmailUtil();
        String fromEmail = userProcessor.getEmail(emailInfo.getFromUsername());
	    emailUtil.sendEmail(session, fromEmail, userProcessor.getEmail(emailInfo.getToUsername()) ,"Inquiry for listing "+emailInfo.getListing()+" from: "+emailInfo.getFromUsername()+ " ", "Hi "+emailInfo.getToUsername()+",\n\n"+emailInfo.getMessage());

        EmailDB emailDB = new EmailDB(fromEmail,userProcessor.getEmail(emailInfo.getToUsername()),emailInfo.getMessage(),emailInfo.getListing());
        emailsProcessor.insertEmail(emailDB);

        Map<String, String> response = new HashMap<>();
        response.put("message", "email sent");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/messages")
    public ResponseEntity getEmail(@RequestBody UserRequest user) {
        ArrayList<EmailDB> emails = emailsProcessor.getEmails(userProcessor.getEmail(user.getUsername()));
        ObjectMapper mapper = new ObjectMapper();
        String jsonEmails = "";
        System.out.println("GETTING EMAIL FOR USER: "+user.getUsername());
        try{
            jsonEmails = mapper.writeValueAsString(emails);
        }
        catch(JsonProcessingException e){
            e.printStackTrace();
        }
        return ResponseEntity.ok(jsonEmails);
    }

    @PostMapping("/delete")
    public ResponseEntity deleteData(@RequestBody String deleteRequest) {
        System.out.println(deleteRequest);
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Decrypt the encrypted listing ID
            DeleteRequest delete = mapper.readValue(deleteRequest, DeleteRequest.class);
            ArrayList<String> imageObjectKeys = houseProcessor.getImageObjectKeysForListing(delete.getListing());
            for (String objectKey : imageObjectKeys) {
                imageUploadService.deleteImage("realtorappimages", objectKey);
            }
            houseProcessor.deleteListing(delete.getListing());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> response = new HashMap<>();
        response.put("message", "listing deleted");
        return ResponseEntity.ok(response);
    }

    // Delete request payload class
    public static class DeleteRequest {
        private String listing;

        public String getListing() {
            return listing;
        }

        public void setListing(String listing) {
            this.listing = listing;
        }
    }
}
