package org.design_manager_project.dtos.file.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.design_manager_project.models.enums.FileStatus;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FileResponse {
    private UUID id;
    private String name;
    private String url;
    private FileStatus status;
    private Instant createdAt;
    private String type;
}
