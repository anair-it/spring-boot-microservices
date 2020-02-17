package org.anair.fn.service;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class S3ServiceTest {

    @Mock
    private S3Client s3Client;

    @InjectMocks
    private S3Service s3Service;

    @Test
    public void put_success() {
        Path file = Mockito.mock(Path.class);
        Path fileName = Mockito.mock(Path.class);
        Mockito.when(file.getFileName()).thenReturn(fileName);
        Mockito.when(fileName.toString()).thenReturn("my-file.xml");

        s3Service.setBucketName("bucket");
        Mockito.when(s3Client.putObject(Mockito.any(PutObjectRequest.class), Mockito.any(Path.class))).thenReturn(PutObjectResponse.builder().eTag("123232").build());
        assertTrue(s3Service.put("1234", file));
        Mockito.verify(s3Client).putObject(Mockito.any(PutObjectRequest.class), Mockito.any(Path.class));
        Mockito.verify(file).getFileName();
    }

    @Test
    public void put_fail() {
        Path file = Mockito.mock(Path.class);
        Path fileName = Mockito.mock(Path.class);
        Mockito.when(file.getFileName()).thenReturn(fileName);
        Mockito.when(fileName.toString()).thenReturn("my-file.xml");
        s3Service.setBucketName("bucket");
        Mockito.when(s3Client.putObject(Mockito.any(PutObjectRequest.class), Mockito.any(Path.class))).thenReturn(PutObjectResponse.builder().build());
        assertFalse(s3Service.put("1234", file));
        Mockito.verify(s3Client).putObject(Mockito.any(PutObjectRequest.class), Mockito.any(Path.class));
        Mockito.verify(file).getFileName();
    }

    @Test
    public void delete_success() {
        Mockito.when(s3Client.deleteObject(Mockito.any(DeleteObjectRequest.class))).thenReturn(DeleteObjectResponse.builder().build());
        s3Service.delete("1234");
        Mockito.verify(s3Client).deleteObject(Mockito.any(DeleteObjectRequest.class));
    }

    @Test
    public void delete_fail() {
        Mockito.when(s3Client.deleteObject(Mockito.any(DeleteObjectRequest.class))).thenThrow(S3Exception.builder().cause(new Exception()).build());
        Assertions.assertThrows(S3Exception.class, () -> s3Service.delete("1234"));
        Mockito.verify(s3Client).deleteObject(Mockito.any(DeleteObjectRequest.class));
    }

    @Test
    public void listObjects_found() {
        S3Object s3Object1 = S3Object.builder().key("anair-abc").build();
        S3Object s3Object2 = S3Object.builder().key("anair-def").build();
        Mockito.when(s3Client.listObjectsV2(Mockito.any(ListObjectsV2Request.class))).thenReturn(ListObjectsV2Response.builder().keyCount(2).contents(Arrays.asList(s3Object1, s3Object2)).build());
        List<S3Object> s3Objects = s3Service.listFilesWithPrefix("anair-");

        assertTrue(CollectionUtils.isNotEmpty(s3Objects));
        Assertions.assertEquals(2, s3Objects.size());

        Mockito.verify(s3Client).listObjectsV2(Mockito.any(ListObjectsV2Request.class));
    }

    @Test
    public void listObjects_not_found() {
        Mockito.when(s3Client.listObjectsV2(Mockito.any(ListObjectsV2Request.class))).thenReturn(ListObjectsV2Response.builder().keyCount(0).build());
        List<S3Object> s3Objects = s3Service.listFilesWithPrefix("anair-1");

        assertTrue(CollectionUtils.isEmpty(s3Objects));

        Mockito.verify(s3Client).listObjectsV2(Mockito.any(ListObjectsV2Request.class));
    }

}