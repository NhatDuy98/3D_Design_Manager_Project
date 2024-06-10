package org.design_manager_project.services;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.design_manager_project.configurations.MinioProperties;
import org.design_manager_project.exeptions.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    public String upload(MultipartFile file){
        try {
            createBucket();
        }catch (BadRequestException e){
            throw new BadRequestException("Image upload failed: " + e.getMessage());
        }

        if (file.isEmpty() || file.getOriginalFilename() == null){
            throw new BadRequestException("Image must have name");
        }

        String fileName = generateFileName(file);
        InputStream inputStream;

        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            throw new BadRequestException("Image upload failed: " + e.getMessage());
        }

        saveFile(inputStream, fileName);

        String prefix = minioProperties.getUrl() + "/" + minioProperties.getBucket() + "/";

        return prefix + fileName;
    }

    @SneakyThrows
    private void saveFile(InputStream inputStream, String fileName) {
        minioClient.putObject(PutObjectArgs.builder()
                        .stream(inputStream, inputStream.available(), -1)
                        .bucket(minioProperties.getBucket())
                        .object(fileName)
                .build());
    }

    private String generateFileName(MultipartFile file) {
        String extension = getExtension(file);
        return new Date().getTime() + "-" + UUID.randomUUID() + "." + extension;
    }

    private String getExtension(MultipartFile file){
        return Objects.requireNonNull(file.getOriginalFilename())
                .substring(file.getOriginalFilename().lastIndexOf(".") + 1);
    }

    @SneakyThrows
    private void createBucket() {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                        .bucket(minioProperties.getBucket())
                .build());

        if (!found){
            minioClient.makeBucket(MakeBucketArgs.builder()
                            .bucket(minioProperties.getBucket())
                    .build());
        }
    }

}
