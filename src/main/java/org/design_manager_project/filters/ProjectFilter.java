package org.design_manager_project.filters;

<<<<<<< HEAD
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
    private UUID userId;
    private String startDate;
    private String endDate;
=======
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectFilter extends BaseFilter{
>>>>>>> dea9dbf8a6b7c5571c7fb46fcc99091044abf573
}
