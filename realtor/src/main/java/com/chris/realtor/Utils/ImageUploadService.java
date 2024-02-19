package com.chris.realtor.Utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageUploadService {

    private final AmazonS3 s3Client;

    @Autowired
    public ImageUploadService(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    public void uploadImage(String bucketName, String key, byte[] imageData, String contentType) {
        try{
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);

            PutObjectRequest request = new PutObjectRequest(bucketName, key, new ByteArrayInputStream(imageData), metadata);

            s3Client.putObject(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteImage(String bucketName, String key) {
        try {
            // Delete object from S3
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, key));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
