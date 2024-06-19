package org.design_manager_project.dtos.user.response;

import lombok.*;
import org.design_manager_project.models.enums.StatusUser;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserOnlineDTO {
    private UUID id;
    private String lastName;
    private String firstName;
    private String email;
    private String status = String.valueOf(StatusUser.ONLINE);
}
