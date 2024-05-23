package org.design_manager_project.models.entity;

import jakarta.persistence.*;
import lombok.*;
import org.design_manager_project.models.BaseModel;
import org.design_manager_project.models.enums.Role;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "members")
public class Member extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "space_id", nullable = false, unique = true)
    private Space space;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 8)
    private Role role;


    @OneToMany(mappedBy = "member")
    private List<Card> cards;

    @OneToMany(mappedBy = "member")
    private List<Print> prints;

    @OneToMany(mappedBy = "member")
    private List<Comment> comments;
}
