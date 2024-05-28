package org.design_manager_project.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.design_manager_project.models.BaseModel;
import org.design_manager_project.models.enums.Role;

import java.time.Instant;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "members")
public class Member extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "space_id", nullable = false)
    private Space space;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 8)
    private Role role;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @OneToMany(mappedBy = "member")
    private List<Card> cards;

    @OneToMany(mappedBy = "member")
    private List<Print> prints;

    @OneToMany(mappedBy = "member")
    private List<Comment> comments;
}
