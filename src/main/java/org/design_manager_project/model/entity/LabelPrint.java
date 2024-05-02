package org.design_manager_project.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.design_manager_project.model.BaseModel;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "label_prints")
public class LabelPrint extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "label_id", nullable = false)
    private Label label;

    @ManyToOne
    @JoinColumn(name = "print_id", nullable = false)
    private Print print;

    @Column(name = "x_coordinate", nullable = false)
    private double xCoordinate;

    @Column(name = "y_coordinate", nullable = false)
    private double yCoordinate;

    @Column(name = "z_coordinate", nullable = false)
    private double zCoordinate;

}
