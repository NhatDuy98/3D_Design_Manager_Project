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
@Table(name = "versions")
public class Version extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "print_id", nullable = false)
    private Print print;

    @Column(name = "image", nullable = false)
    private String image;

}
