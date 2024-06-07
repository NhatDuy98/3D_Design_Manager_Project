package org.design_manager_project.dtos.label;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LabelDTO extends BaseDTO<UUID> {

    @Pattern(regexp = "^\\S+(\\s+[^\\s]+)*$",message = "Format error")
    @Size(min = 1, max = 50, message = "Size name error")
    private String labelName;
    private String color;
    private ProjectRequestWithID project;

}
