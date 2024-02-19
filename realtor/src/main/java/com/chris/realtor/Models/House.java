package com.chris.realtor.Models;

import java.util.ArrayList;

import org.springframework.lang.Nullable;

import com.chris.realtor.Utils.HouseCustomSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = HouseCustomSerializer.class)
public class House {

    private String username;
    private String listing;
    private String address;
    private String aptnum;
    private String city;
    private String state; 
    private String zip;
    private String country;
    private int price;
    private int beds;
    private int baths;
    private int size;
    @Nullable
    private ArrayList<String> objectKey;

    public House(String username, String listing, String address, String aptnum, String city, String state, String zip, String country, int price, int beds, int baths, int size) {
        this.username = username;
        this.listing = listing;
        this.address = address;
        this.aptnum = aptnum;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
        this.price = price;
        this.beds = beds;
        this.baths = baths;
        this.size = size;
    }

    public House(String username, String listing, String address, String aptnum, String city, String state, String zip, String country, int price, int beds, int baths, int size, String ObjectKey) {
        this.username = username;
        this.listing = listing;
        this.address = address;
        this.aptnum = aptnum;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
        this.price = price;
        this.beds = beds;
        this.baths = baths;
        this.size = size;
        this.objectKey = objectKey;
    }


    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getListing() {
        return this.listing;
    }

    public void setListing(String listing) {
        this.listing = listing;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAptnum() {
        return this.aptnum;
    }

    public void setAptnum(String aptnum) {
        this.aptnum = aptnum;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return this.zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getBeds() {
        return this.beds;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public int getBaths() {
        return this.baths;
    }

    public void setBaths(int baths) {
        this.baths = baths;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ArrayList<String> getObjectKey() {
        return this.objectKey;
    }

    public void setObjectKey(ArrayList<String> objectKey) {
        this.objectKey = objectKey;
    }

    public boolean isNotNullOrEmpty(String input){
        if(input == null || input.equals("")){
        return false;
        }
        else{
        return true;
        }
    }
    public String populateAddressLine(){
        StringBuilder sb = new StringBuilder();
        if(isNotNullOrEmpty(getAddress())){
            sb.append(getAddress().strip());
        }
        if(isNotNullOrEmpty(getAptnum())){
            sb.append(" ");
            sb.append(getAptnum().strip());
        }
        if(isNotNullOrEmpty(getCity())){
            sb.append(" ");
            sb.append(getCity().strip());
        }
        if(isNotNullOrEmpty(getState())){
            sb.append(", ");
            sb.append(getState().strip());
        }
        if(isNotNullOrEmpty(getZip())){
            sb.append(" ");
            sb.append(getZip().strip());
        }
        if(isNotNullOrEmpty(getCountry())){
            sb.append(" ");
            sb.append(getCountry().strip());
        }
        return sb.toString();
    }
}
