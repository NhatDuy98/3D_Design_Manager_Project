package org.design_manager_project.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationFilter extends BaseFilter{
    private UUID cardId;
    private UUID printId;
    private UUID commentId;
    private UUID memberId;
}