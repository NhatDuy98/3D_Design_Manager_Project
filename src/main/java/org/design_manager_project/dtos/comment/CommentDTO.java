package org.design_manager_project.dtos.comment;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.design_manager_project.dtos.BaseDTO;
import org.design_manager_project.dtos.card.request.CardRequestWithID;
import org.design_manager_project.dtos.member.request.MemberRequestWithID;
import org.design_manager_project.dtos.print.request.PrintRequestWithID;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDTO extends BaseDTO<UUID> {
    private MemberRequestWithID member;
    private CardRequestWithID card;
    private PrintRequestWithID print;
    private String content;
}
