package org.anair.fn.service;


import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.nio.file.Path;
import java.util.List;

@Service
@Slf4j
@Setter
public class S3Service {

    @Value("${s3.bucket:my-bucket}")
    private String bucketName;

    @Autowired
    private S3Client s3Client;

    public boolean put(String key, Path file) {
        PutObjectResponse putObjectResponse =
        	s3Client.putObject(PutObjectRequest.builder().
        		bucket(bucketName).key(key).build(), file);
        if (putObjectResponse != null && StringUtils.isEmpty(putObjectResponse.eTag())) {
            log.error("Failed to upload file: {} for key: {} to bucket: {}", file.getFileName().toString(), key, bucketName);
            return false;
        }
        log.debug("Uploaded file: {} with key: {} to bucket: {}", file.getFileName().toString(), key, bucketName);
        return true;
    }

    public void delete(String key) {
        log.trace("Deleting S3 file: {}", key);
        s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(key).build());
        log.debug("Deleted S3 file: {}", key);
    }

    public List<S3Object> listFilesWithPrefix(String keyPrefix){
        log.trace("Listing S3 files for key: {}", keyPrefix);
        List<S3Object> s3Objects = null;
        ListObjectsV2Response listObjectsV2Response = s3Client.listObjectsV2(ListObjectsV2Request.builder().bucket(bucketName).prefix(keyPrefix).build());
        if(listObjectsV2Response != null && listObjectsV2Response.keyCount() > 0){
            s3Objects = listObjectsV2Response.contents();
            log.debug("Found {} S3 files for key: {}", listObjectsV2Response.keyCount(), keyPrefix);
        }
        return s3Objects;
    }
    
}
