package org.design_manager_project.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectFilter extends BaseFilter{
    private UUID id;
    private String startDate;
    private String endDate;
}
