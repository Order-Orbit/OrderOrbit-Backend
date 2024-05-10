package com.orderorbit.orderorbit.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;


@Component
public class AwsS3Util {
    
    @Value("${AWS_ACCESS_KEY}")
    private String accessKey;

    @Value("${AWS_SECRET_KEY}")
    private String secretKey;

    @Value("${S3_REGION}")
    private String region;

    @Value("${S3_BUCKET_NAME}")
    private String bucketName;

    @Value("${S3_BUCKET_URL}")
    private String bucketUrl;


    public AmazonS3 getAmazonS3Client(){
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(region)
                    .build();
    }

    public String uploadFileToS3(MultipartFile multipartFile) throws IOException{
        if (multipartFile.getOriginalFilename() == null){
            return null;
        }
        String fileName = UUID.randomUUID().toString()+"_"+multipartFile.getOriginalFilename();
        byte[] fileBytes = multipartFile.getBytes();
        
        AmazonS3 amazonS3 = getAmazonS3Client();
        
        File file = new File(fileName);
        try (OutputStream os = new FileOutputStream(fileName) ){
            os.write(fileBytes);
        } catch (Exception e) {
            return e.getStackTrace().toString();
        }
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file));
        
        file.delete();

        return bucketUrl+"/"+fileName;
    }
    public String deleteFileFromS3(String url){
        if (url.equals(null)){
            return null;
        }
        String[] urlParts = url.split("/");
        String fileToDelete = urlParts[-1];
        System.out.println(fileToDelete);
        AmazonS3 amazonS3 = getAmazonS3Client();

        try {
            amazonS3.deleteObject(bucketName, fileToDelete);
            return "delete success";
        } catch (Exception e) {
            return e.getStackTrace().toString();
        }
    }



}
