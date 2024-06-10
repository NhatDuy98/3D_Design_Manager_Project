package org.design_manager_project.dtos.print;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.design_manager_project.dtos.BaseDTO;
import org.design_manager_project.dtos.card.request.CardRequestWithID;
import org.design_manager_project.dtos.member.request.MemberRequestWithID;
import org.design_manager_project.dtos.version.VersionDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrintDTO extends BaseDTO<UUID> {

    private MultipartFile image;
    private CardRequestWithID card;
    private MemberRequestWithID member;
    private List<VersionDTO> images = new ArrayList<>();
}
