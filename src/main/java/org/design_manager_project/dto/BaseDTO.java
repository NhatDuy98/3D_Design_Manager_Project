package org.design_manager_project.dto;

import jakarta.persistence.MappedSuperclass;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@MappedSuperclass
public class BaseDTO<ID extends String> {
    private ID id;
    private String createdAt;
    private String updatedAt;

}
