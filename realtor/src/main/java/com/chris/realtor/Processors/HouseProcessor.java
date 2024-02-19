package com.chris.realtor.Processors;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import com.chris.realtor.Models.House;
import com.chris.realtor.Models.SellRequest;

@Repository
public class HouseProcessor {
    public static StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public HouseProcessor(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        encryptor.setPassword("1234");
    }

    public String insertListing(SellRequest request){
        String encryptedString = "";
        String sql = "{ call InsertHouse(?,?,?,?,?,?,?,?,?,?,?,?) }";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, request.getUsername());
            cs.setString(2, request.getAddress());
            cs.setString(3, request.getAptnum());
            cs.setString(4, request.getCity());
            cs.setString(5, request.getState());
            cs.setString(6, request.getZip());
            cs.setString(7, request.getCountry());
            cs.setInt(8, request.getPrice());
            cs.setInt(9, request.getBeds());
            cs.setInt(10, request.getBaths());
            cs.setInt(11, request.getSize());
            cs.registerOutParameter(12, Types.NVARCHAR); // Output parameter for the result

            cs.execute();
            String listingId = cs.getString(12);

            // Encrypt the listing id
            // encryptor.setStringOutputType("base64");
            // encryptedString = encryptor.encrypt(listingId).replace("==","");
            // Encode the string to Base64URL
            encryptedString = Base64.getUrlEncoder().withoutPadding().encodeToString(listingId.getBytes());
            System.out.println("Encrypted: " + encryptedString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return encryptedString;
    }

    public List<House> getListings(String username){
        String sql = "{call GetListings(?) }";
        List<House> listings = new ArrayList<>();
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             CallableStatement cs = connection.prepareCall(sql)) {

            cs.setString(1, username);

            if (cs.execute()) {
                try (ResultSet rs = cs.getResultSet()) {
                    while (rs.next()) {
                        // Retrieve data from the ResultSet and create House objects
                        String seller = rs.getString("username");
                        String listing = rs.getString("listing");
                        String address = rs.getString("address");
                        String aptnum = rs.getString("aptnum");
                        String city = rs.getString("city");
                        String state = rs.getString("state");
                        String zip = rs.getString("zip");
                        String country = rs.getString("country");
                        int price = rs.getInt("price");
                        int beds = rs.getInt("beds");
                        int baths = rs.getInt("baths");
                        int size = rs.getInt("size");

                        // Create a House object and add it to the list
                        ArrayList<String> imageObjectKeys = getImageObjectKeysForListing(listing);
                        House house = new House(seller, listing, address,aptnum,city,state,zip,country,price,beds,baths,size);
                        house.setObjectKey(imageObjectKeys);; // Set the image object keys
                        listings.add(house);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listings;
    }

     public List<House> getListingsForUser(String username){
        String sql = "{call GetListingsForUser(?) }";
        List<House> listings = new ArrayList<>();
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             CallableStatement cs = connection.prepareCall(sql)) {

            cs.setString(1, username);

            if (cs.execute()) {
                try (ResultSet rs = cs.getResultSet()) {
                    while (rs.next()) {
                        // Retrieve data from the ResultSet and create House objects
                        String listing = rs.getString("listing");
                        String address = rs.getString("address");
                        String aptnum = rs.getString("aptnum");
                        String city = rs.getString("city");
                        String state = rs.getString("state");
                        String zip = rs.getString("zip");
                        String country = rs.getString("country");
                        int price = rs.getInt("price");
                        int beds = rs.getInt("beds");
                        int baths = rs.getInt("baths");
                        int size = rs.getInt("size");

                        // Create a House object and add it to the list
                        ArrayList<String> imageObjectKeys = getImageObjectKeysForListing(listing);
                        House house = new House(username, listing, address,aptnum,city,state,zip,country,price,beds,baths,size);
                        house.setObjectKey(imageObjectKeys);; // Set the image object keys
                        listings.add(house);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listings;
    }

    public House getListing(String listing_id){
        String sql = "{call GetListing(?) }";
        House house = null;
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             CallableStatement cs = connection.prepareCall(sql)) {

            cs.setString(1,  listing_id);

            if (cs.execute()) {
                try (ResultSet rs = cs.getResultSet()) {
                    while (rs.next()) {
                        // Retrieve data from the ResultSet and create House objects
                        String seller = rs.getString("username");
                        String listing = rs.getString("listing");
                        String address = rs.getString("address");
                        String aptnum = rs.getString("aptnum");
                        String city = rs.getString("city");
                        String state = rs.getString("state");
                        String zip = rs.getString("zip");
                        String country = rs.getString("country");
                        int price = rs.getInt("price");
                        int beds = rs.getInt("beds");
                        int baths = rs.getInt("baths");
                        int size = rs.getInt("size");

                        // Create a House object and add it to the list
                        ArrayList<String> imageObjectKeys = getImageObjectKeysForListing(listing);
                        house = new House(seller, listing, address,aptnum,city,state,zip,country,price,beds,baths,size);
                        house.setObjectKey(imageObjectKeys);; // Set the image object keys
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return house;
    }


    public ArrayList<String> getImageObjectKeysForListing(String listing) {
    String imageSql = "SELECT imageUrl FROM images WHERE listing = ?";
    ArrayList<String> imageObjectKeys = new ArrayList<>();

    try (Connection connection = jdbcTemplate.getDataSource().getConnection();
         PreparedStatement ps = connection.prepareStatement(imageSql)) {

        ps.setString(1, listing);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String objectKey = rs.getString("imageUrl");
                imageObjectKeys.add(objectKey);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
        return imageObjectKeys;
    }

     public void updateListing(String listing,int price,int beds,int baths,int size,String objectKey){
        String sql = "{call UpdateListing(?,?,?,?,?,?) }";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             CallableStatement cs = connection.prepareCall(sql)) {

            cs.setString(1, listing);
            //if all are 999 means only uploading image
            if(price == 999 && beds == 999 && baths == 999 && size == 999){
                cs.setNull(2,Types.INTEGER);
                cs.setNull(3,Types.INTEGER);
                cs.setNull(4,Types.INTEGER);
                cs.setNull(5,Types.INTEGER);
            }
            else{
                cs.setInt(2, price);
                cs.setInt(3, beds);
                cs.setInt(4, baths);
                cs.setInt(5, size);
            }
            cs.setString(6, objectKey);

            cs.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateListing(String listing,int price,int beds,int baths,int size){
        String sql = "{call UpdateListingNoImage(?,?,?,?,?) }";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             CallableStatement cs = connection.prepareCall(sql)) {

            cs.setString(1, listing);
            cs.setInt(2, price);
            cs.setInt(3, beds);
            cs.setInt(4, baths);
            cs.setInt(5, size);

            cs.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public House deleteListing(String listing_id){
        String sql = "{call DeleteListing(?) }";
        House house = null;
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             CallableStatement cs = connection.prepareCall(sql)) {

            cs.setString(1,  listing_id);

            cs.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return house;
    }
    
}