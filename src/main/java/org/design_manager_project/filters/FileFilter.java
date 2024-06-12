package org.design_manager_project.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileFilter extends BaseFilter{
    private String fileUrl;
    private String destinationDir;
    private String fileName;
}
