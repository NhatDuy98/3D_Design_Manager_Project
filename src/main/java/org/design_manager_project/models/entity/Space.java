package org.design_manager_project.models.entity;

import jakarta.persistence.*;
import lombok.*;
import org.design_manager_project.models.BaseModel;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "spaces")
public class Space extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false  )
    private User user;

    @Column(name = "space_name", nullable = false, length = 50)
    private String spaceName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "space", cascade = CascadeType.REMOVE)
    private List<Project> projects;

    @OneToMany(mappedBy = "space", cascade = CascadeType.REMOVE)
    private List<Member> members;
}