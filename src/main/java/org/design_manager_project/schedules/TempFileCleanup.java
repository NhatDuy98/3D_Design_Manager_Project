package org.design_manager_project.schedules;

import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import org.design_manager_project.services.FileService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TempFileCleanup {
    private final FileService fileService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupTempFile() throws MinioException {
        fileService.deleteTempFile();
        System.out.println("Deleted temp file.......");
    }

}
