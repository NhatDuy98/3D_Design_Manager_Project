package org.design_manager_project.dto;

import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@MappedSuperclass
public class BaseDTO<ID extends UUID> {
    private ID id;

}
