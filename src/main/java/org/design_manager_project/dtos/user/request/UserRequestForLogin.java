package org.design_manager_project.dtos.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestForLogin {
    @Email(message = "Email invalid")
    private String email;
    @Size(min = 6, max = 18, message = "Size password error")
    private String password;
}
