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
@Table(name = "versions")
public class Version extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "print_id", nullable = false)
    private Print print;

    @Column(name = "image", nullable = false)
    private String image;

}
