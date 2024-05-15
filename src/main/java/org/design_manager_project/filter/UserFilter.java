package org.design_manager_project.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserFilter extends BaseFilter{
    private boolean deletedAt;
    private boolean isActive;

}
