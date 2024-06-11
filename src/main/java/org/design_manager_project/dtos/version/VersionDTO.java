package org.design_manager_project.dtos.version;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.design_manager_project.dtos.BaseDTO;
import org.design_manager_project.dtos.print.request.PrintRequestWithID;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VersionDTO extends BaseDTO<UUID> {
    private PrintRequestWithID print;
    private String image;
}
