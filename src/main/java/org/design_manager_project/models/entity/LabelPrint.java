package org.design_manager_project.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.design_manager_project.models.BaseModel;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "label_prints")
public class LabelPrint extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "label_id", nullable = false)
    private Label label;

    @ManyToOne
    @JoinColumn(name = "print_id", nullable = false)
    private Print print;

    @Column(name = "x_coordinate", nullable = false)
    private Double xCoordinate;

    @Column(name = "y_coordinate", nullable = false)
    private Double yCoordinate;

    @Column(name = "z_coordinate", nullable = false)
    private Double zCoordinate;

}
