package org.design_manager_project.dtos.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.design_manager_project.dtos.BaseDTO;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest extends BaseDTO<UUID> {

    @NotBlank(message = "first name not blank")
    @Size(min = 1, max = 30, message = "size first name error")
    @Pattern(regexp = "^[^\\d\\s]\\D*$",message = "format error for first name")
    private String firstName;

    @NotBlank(message = "last name not blank")
    @Size(min = 1, max = 30, message = "size last name error")
    @Pattern(regexp = "^[^\\d\\s]\\D*$",message = "format error for last name")
    private String lastName;

    private String password = "123456";

    @Email(message = "Email invalid")
    private String email;

    private String avatar;
    private boolean isActive;

}
