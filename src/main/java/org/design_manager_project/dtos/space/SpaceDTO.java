package org.design_manager_project.dtos.space;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.design_manager_project.dtos.BaseDTO;
import org.design_manager_project.dtos.user.request.UserRequestWithID;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SpaceDTO extends BaseDTO<UUID> {

    @NotBlank(message = "Name can not blank")
    @Size(min = 1, max = 30, message = "Size name error")
    @Pattern(regexp = "^\\S+(\\s+[^\\s]+)*$",message = "Format error for name")
    private String spaceName;
    @Pattern(regexp = "^\\S+(\\s+[^\\s]+)*$",message = "Format error for name")
    @Size(max = 65535, message = "Size error")
    private String description;
    private UserRequestWithID user;
}
