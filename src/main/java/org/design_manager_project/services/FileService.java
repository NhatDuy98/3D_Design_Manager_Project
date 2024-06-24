package org.design_manager_project.services;

import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.design_manager_project.configurations.MinioProperties;
import org.design_manager_project.dtos.file.FileDTO;
import org.design_manager_project.dtos.file.response.FileResponse;
import org.design_manager_project.exeptions.BadRequestException;
import org.design_manager_project.filters.FileFilter;
import org.design_manager_project.mappers.FileMapper;
import org.design_manager_project.models.entity.FileObject;
import org.design_manager_project.models.enums.FileStatus;
import org.design_manager_project.repositories.FileRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.design_manager_project.utils.Constants.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;
    private final FileRepository fileRepository;
    private static final FileMapper fileMapper = FileMapper.INSTANCE;

    public FileResponse upload(FileDTO dto){
        if (dto.getFile().size() > 1){
            throw new BadRequestException(BAD_REQUEST);
        }

        MultipartFile file = dto.getFile().get(0);

        if (file.getSize() > 2_000_000){
            throw new BadRequestException(BAD_REQUEST);
        }

        String fileName = uploadMinio(file);

        var fileRepo = updateFile(fileName, file);

        fileRepository.save(fileRepo);

        return fileMapper.convertToDTO(fileRepo);
    }

    public List<FileResponse> uploadBulk(FileDTO dtos){
        try {
            createBucket();
        }catch (BadRequestException e){
            throw new BadRequestException(UPLOAD_FAILED);
        }

        List<FileObject> fileObjects = dtos.getFile().stream().map(e -> {

            if (e.getSize() > 2_000_000){
                throw new BadRequestException(BAD_REQUEST);
            }

            String fileName = uploadMinio(e);

            return updateFile(fileName, e);
        }).toList();

        fileRepository.saveAll(fileObjects);

        return fileMapper.convertListToDTO(fileObjects);
    }

    private FileObject updateFile(String fileName, MultipartFile fileUpload) {
        String prefix = minioProperties.getUrl() + "/" + minioProperties.getBucket() + "/";

        var file = new FileObject();
        file.setName(fileName);
        file.setUrl(prefix + fileName);
        file.setStatus(FileStatus.TEMP);
        file.setType(fileUpload.getContentType());

        return file;
    }

    private String uploadMinio(MultipartFile file){
        try {
            createBucket();
        }catch (BadRequestException e){
            throw new BadRequestException(UPLOAD_FAILED);
        }

        if (file.isEmpty() || file.getOriginalFilename() == null){
            throw new BadRequestException("Image must have name");
        }

        String fileName = generateFileName(file);
        InputStream inputStream;

        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            throw new BadRequestException(UPLOAD_FAILED);
        }

        saveFile(inputStream, fileName, file);

        return fileName;
    }

    @SneakyThrows
    private void saveFile(InputStream inputStream, String fileName, MultipartFile file) {
        minioClient.putObject(PutObjectArgs.builder()
                        .stream(inputStream, inputStream.available(), -1)
                        .bucket(minioProperties.getBucket())
                        .contentType(file.getContentType())
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

    public Page<FileResponse> getAllFiles(FileFilter filter) {
        return fileMapper.convertPageToDTO(fileRepository.findAllWithFilter(filter.getPageable(), filter));
    }

    @Transactional
    public void delete(UUID id){
        FileObject fileObject = fileRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(OBJECT_DELETED));

        fileRepository.deleteById(id);

        deleteMinio(fileObject);
    }

    public void deleteBulk(List<UUID> ids){
        List<FileObject> fileObjects = fileRepository.findAllById(ids);

        fileRepository.deleteAllById(ids);

        deleteMinioBulk(fileObjects);
    }

    @SneakyThrows
    private void deleteMinioBulk(List<FileObject> fileObjects) {
        List<DeleteObject> objects = fileObjects.stream().map(e -> new DeleteObject(e.getName())).toList();

        RemoveObjectsArgs args = RemoveObjectsArgs.builder()
                .bucket(minioProperties.getBucket())
                .objects(objects)
                .build();
        Iterable<Result<DeleteError>> results = minioClient.removeObjects(args);

        for (var result : results) {
            DeleteError error = result.get();

            log.info("MinioUtil | removeObject | error : " + error.objectName() + " " + error.message());
        }
    }

    @SneakyThrows
    private void deleteMinio(FileObject fileObject){
        RemoveObjectArgs args = RemoveObjectArgs.builder()
                .bucket(minioProperties.getBucket())
                .object(fileObject.getName())
                .build();
        minioClient.removeObject(args);
    }

    public void updateFileActive(String image) {
        var file = fileRepository.findByFileUrl(image);

        if (file == null){
            throw new BadRequestException(OBJECT_DELETED);
        }

        file.setStatus(FileStatus.ACTIVE);
        fileRepository.save(file);
    }

    public void updateFileStatusBulk(List<String> name){
        List<FileObject> fileObjects = name.stream().map(e -> {
            var file = fileRepository.findByFileUrl(e);

            if (file == null){
                throw new BadRequestException(OBJECT_DELETED);
            }

            file.setStatus(FileStatus.ACTIVE);

            return file;
        }).toList();

        fileRepository.saveAll(fileObjects);
    }

    public void deleteTempFile() {
        Instant thresholdTime = Instant.now().minus(1, ChronoUnit.DAYS);

        List<FileObject> fileObjects = fileRepository.findAllByStatusAndUploadTime(FileStatus.TEMP, thresholdTime);

        fileRepository.deleteAll(fileObjects);

        deleteMinioBulk(fileObjects);
    }

    public FileResponse getFile(UUID id) {
        var file = fileRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(DATA_NOT_FOUND));

        return fileMapper.convertToDTO(file);
    }

    @SneakyThrows
    public InputStream downloadMinio(FileObject fileObject){
        return minioClient.getObject(GetObjectArgs.builder()
                .bucket(minioProperties.getBucket())
                .object(fileObject.getName())
                .build());
    }

    public FileFilter download(UUID id) {
        var file = fileRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(DATA_NOT_FOUND));

        FileFilter filter = new FileFilter();
        filter.setFileName(file.getName());
        filter.setStream(downloadMinio(file));

        return filter;
    }
}
