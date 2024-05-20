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

    @NotBlank(message = "name not blank")
    @Size(min = 1, max = 30, message = "size name error")
    @Pattern(regexp = "^[^\\d\\s]\\D*$",message = "format error for name")
    private String spaceName;
    @Pattern(regexp = "^[^\\d\\s]\\D*$",message = "format error for name")
    @Size(max = 65535, message = "size error")
    private String description;
    private UserRequestWithID user;
}
