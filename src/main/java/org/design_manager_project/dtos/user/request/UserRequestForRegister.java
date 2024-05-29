package org.design_manager_project.dtos.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestForRegister {

    @NotBlank(message = "First name not blank")
    @Size(min = 1, max = 30, message = "Size first name error")
    @Pattern(regexp = "^[^\\d\\s]\\D*$",message = "Format error for first name")
    private String firstName;

    @NotBlank(message = "Last name not blank")
    @Size(min = 1, max = 30, message = "Size last name error")
    @Pattern(regexp = "^[^\\d\\s]\\D*$",message = "Format error for last name")
    private String lastName;

    @Email(message = "Email invalid")
    private String email;

    private String password;
    private Boolean isActive;

}
