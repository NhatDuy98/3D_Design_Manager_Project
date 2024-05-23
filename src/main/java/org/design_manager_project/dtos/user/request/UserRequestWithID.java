package org.design_manager_project.dtos.user.request;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestWithID {
    private UUID id;
}
