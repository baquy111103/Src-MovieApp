package cms.cms.common;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.ByteArrayInputStream;

import java.util.Base64;

@Log4j2
@Component
public class UploadFile {

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.accessKeyId}")
    private String accessKeyId;

    @Value("${aws.s3.secretAccessKey}")
    private String secretAccessKey;

    public String createImageFile(String thumbUpload, String type) {
        try {
            if (thumbUpload == null || thumbUpload.equals("")) {
                return null;
            }

            // Tách dữ liệu Base64
            String[] dataArray = thumbUpload.trim().split(",");
            String imgBase64 = dataArray.length > 1 ? dataArray[1] : thumbUpload;
            String fileExtension = dataArray.length > 1
                    ? dataArray[0].replace("data:image/", "").replace(";base64", "")
                    : "jpg";

            // Tạo tên file
            String fileName = Helper.generateRandomString(32) + "." + fileExtension;
            String s3Path = type + "/" + fileName;

            // Chuẩn bị dữ liệu
            byte[] imageBytes = Base64.getDecoder().decode(imgBase64);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);

            // Tạo S3 Client
            S3Client s3Client = S3Client.builder()
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(accessKeyId, secretAccessKey)
                    ))
                    .build();

            // Tải lên S3
            s3Client.putObject(PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Path)
                    .contentType("image/" + fileExtension)
                    .build(), software.amazon.awssdk.core.sync.RequestBody.fromInputStream(inputStream, imageBytes.length));

            // Trả về đường dẫn S3
            return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + s3Path;
        } catch (S3Exception e) {
            log.error("AWS S3 ERROR: " + e.awsErrorDetails().errorMessage(), e);
            return null;
        } catch (Exception e) {
            log.error("ERROR: " + e.getMessage(), e);
            return null;
        }
    }

    public String uploadVideoToS3(String videoName, MultipartFile videoFile) {
        try {
            if (videoFile != null && !videoFile.isEmpty()) {
                // Handle video upload directly from the file (multipart)
                String fileName = Helper.generateRandomString(32) + ".mp4"; // You can change the file extension as needed
                String s3Path = "videos/" + fileName;

                // Create an S3 client and upload the file
                S3Client s3Client = S3Client.builder()
                        .region(Region.of(region))
                        .credentialsProvider(StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKeyId, secretAccessKey)
                        ))
                        .build();

                s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(s3Path)
                        .contentType("video/mp4")
                        .build(), RequestBody.fromInputStream(videoFile.getInputStream(), videoFile.getSize()));

                // Return the S3 URL
                return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + s3Path;
            } else if (videoName != null && !videoName.isEmpty()) {
                // Handle video upload from Base64 data
                String[] dataArray = videoName.trim().split(",");
                String videoData = dataArray.length > 1 ? dataArray[1] : videoName;

                byte[] videoBytes = Base64.getDecoder().decode(videoData);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(videoBytes);

                String fileName = Helper.generateRandomString(32) + ".mp4";
                String s3Path = "videos/" + fileName;

                // Create an S3 client and upload the video
                S3Client s3Client = S3Client.builder()
                        .region(Region.of(region))
                        .credentialsProvider(StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKeyId, secretAccessKey)
                        ))
                        .build();

                s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(s3Path)
                        .contentType("video/mp4")
                        .build(), RequestBody.fromInputStream(inputStream, videoBytes.length));

                // Return the S3 URL
                return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + s3Path;
            }
        } catch (S3Exception e) {
            log.error("AWS S3 ERROR: " + e.awsErrorDetails().errorMessage(), e);
        } catch (Exception e) {
            log.error("ERROR: " + e.getMessage(), e);
        }
        return null;
    }



}
