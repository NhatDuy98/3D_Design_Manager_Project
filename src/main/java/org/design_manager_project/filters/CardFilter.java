package org.design_manager_project.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.design_manager_project.models.enums.Status;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardFilter extends BaseFilter{
    private UUID id;
    private UUID projectId;
    private Status status;
    private LocalDate date;
}
