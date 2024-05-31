package org.design_manager_project.dtos.card;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.design_manager_project.dtos.BaseDTO;
import org.design_manager_project.dtos.member.request.MemberRequestWithID;
import org.design_manager_project.dtos.project.request.ProjectRequestWithID;
import org.design_manager_project.validations.validStatus.ValidStatus;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardDTO extends BaseDTO<UUID> {
    @NotBlank(message = "Name not blank")
    @Pattern(regexp = "^\\S+(\\s+[^\\s]+)*$",message = "Format error")
    @Size(min = 1, max = 100, message = "Size name error")
    private String cardName;

    @Pattern(regexp = "^\\S+(\\s+[^\\s]+)*$",message = "Format error")
    @Size(max = 65535, message = "Size error")
    private String description;

    @ValidStatus
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private ProjectRequestWithID project;
    private MemberRequestWithID member;

}
