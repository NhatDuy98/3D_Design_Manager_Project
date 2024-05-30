package org.design_manager_project.dtos.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.design_manager_project.dtos.BaseDTO;
import org.design_manager_project.dtos.project.request.ProjectRequestWithID;
import org.design_manager_project.dtos.space.request.SpaceRequestWithID;
import org.design_manager_project.dtos.user.request.UserRequestWithID;
import org.design_manager_project.validations.validRole.ValidRole;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO extends BaseDTO<UUID> {

    private UserRequestWithID user;
    private ProjectRequestWithID project;
    private SpaceRequestWithID space;
    @ValidRole
    private String role;

}
