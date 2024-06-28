package org.design_manager_project.schedules;

import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.design_manager_project.services.CardService;
import org.design_manager_project.services.FileService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GlobalSchedule {
    private final FileService fileService;
    private final CardService cardService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupTempFile() throws MinioException {
        fileService.deleteTempFile();
        log.info("Deleted temp file.......");
    }

    @Scheduled(cron = "0 1 0 * * ?")
    public void sendNotificationOverDueCard(){
        cardService.sendNotificationOverDueCard();
        log.info("Send notification about card overdue......");
    }

    public void sendNotificationForReview(){
        cardService.sendNotificationForReview();
        log.info("Send notification for card review......");
    }

}
