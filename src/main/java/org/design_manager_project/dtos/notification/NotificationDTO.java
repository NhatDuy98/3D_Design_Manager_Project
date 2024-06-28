package org.design_manager_project.dtos.notification;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.design_manager_project.dtos.BaseDTO;
import org.design_manager_project.dtos.card.CardDTO;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationDTO extends BaseDTO<UUID> {
    private String content;
    private String url;
    private Boolean isRead;
    private CardDTO card;
}
