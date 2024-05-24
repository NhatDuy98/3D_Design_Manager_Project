package org.design_manager_project.dtos.project;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.design_manager_project.dtos.BaseDTO;
import org.design_manager_project.dtos.space.request.SpaceRequestWithId;
import org.design_manager_project.dtos.user.request.UserRequestWithID;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDTO extends BaseDTO<UUID> {

    @NotBlank(message = "Name not blank")
    @Pattern(regexp = "^\\S+(\\s+[^\\s]+)*$",message = "Format error")
    @Size(min = 1, max = 30, message = "Size name error")
    private String projectName;

    @Pattern(regexp = "^\\S+(\\s+[^\\s]+)*$",message = "Format error")
    @Size(max = 65535, message = "Size error")
    private String description;

    private LocalDate startDate;
    private LocalDate endDate;
    private SpaceRequestWithId space;
    private UserRequestWithID user;
}
