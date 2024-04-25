package org.design_manager_project.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.design_manager_project.model.BaseModel;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "labels")
public class Label extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "label_name", nullable = false, length = 50)
    private String labelName;

    @Column(name = "color", length = 50)
    private String color;

    @OneToMany(mappedBy = "label")
    private List<LabelPrint> labelPrints;
}
