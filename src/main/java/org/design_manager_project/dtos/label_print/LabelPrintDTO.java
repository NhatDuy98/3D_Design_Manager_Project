package org.design_manager_project.dtos.label_print;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.design_manager_project.dtos.BaseDTO;
import org.design_manager_project.dtos.label.LabelDTO;
import org.design_manager_project.dtos.print.request.PrintRequestWithID;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LabelPrintDTO extends BaseDTO<UUID> {
    private PrintRequestWithID print;
    private LabelDTO label;
    private Double x;
    private Double y;
    private Double z;

}
