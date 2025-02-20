package com.zerozero.core.util;

import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AWSS3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public String getPreSignedUrl(String prefix, String fileName) {
        if (prefix == null || prefix.isEmpty()) {
            return null;
        }
        String filePath = createFilePath(prefix, fileName);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = createGeneratePresignedUrlRequest(bucket, filePath);
        try {
            URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
            return url.toString();
        } catch (SdkClientException e) {
            return null;
        }
    }

    public String getObjectUrlFromPreSignedUrl(String preSignedUrl) {
        if (preSignedUrl == null || preSignedUrl.isEmpty()) {
            return null;
        }
        try {
            URL url = new URL(preSignedUrl);
            return String.format("%s://%s%s", url.getProtocol(), url.getHost(), url.getPath());
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public String uploadImage(String prefix, MultipartFile multipartFile) throws IOException {
        String fileName = createFilePath(prefix, multipartFile.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, fileName, multipartFile.getInputStream(), objectMetadata);
        return getUrl(bucket, fileName);
    }

    public List<String> uploadImages(String prefix, List<MultipartFile> multipartFiles) {
        List<String> imageUrls = new ArrayList<>();

        multipartFiles.forEach(file -> {
            String fileName = createFilePath(prefix, file.getOriginalFilename());

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try (InputStream inputStream = file.getInputStream()) {
                amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
            }
            imageUrls.add(getUrl(bucket, fileName));
        });
        return imageUrls;
    }

    private GeneratePresignedUrlRequest createGeneratePresignedUrlRequest(String bucket, String filePath) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, filePath)
                .withMethod(HttpMethod.PUT)
                .withExpiration(getPreSignedUrlExpiration());
        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL,
                CannedAccessControlList.PublicRead.toString());
        return generatePresignedUrlRequest;
    }

    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60;
        expiration.setTime(expTimeMillis);
        return expiration;
    }

    private String createFileUuid() {
        return UUID.randomUUID().toString();
    }

    private String createFilePath(String prefix, String fileName) {
        String fileUuid = createFileUuid();
        return String.format("%s/%s-%s", prefix, fileUuid, fileName);
    }

    private String getUrl(String bucket, String fileName) {
        return amazonS3.getUrl(bucket, fileName).toString();
    }

}
