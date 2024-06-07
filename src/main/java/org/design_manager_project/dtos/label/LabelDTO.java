package org.design_manager_project.dtos.label;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.design_manager_project.dtos.BaseDTO;
import org.design_manager_project.dtos.project.request.ProjectRequestWithID;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LabelDTO extends BaseDTO<UUID> {
    private String labelName;
    private String color;
    private ProjectRequestWithID project;

}
