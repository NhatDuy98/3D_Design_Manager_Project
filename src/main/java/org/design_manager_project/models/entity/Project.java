package org.design_manager_project.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.design_manager_project.models.BaseModel;

import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "projects")
public class Project extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "space_id", nullable = false)
    private Space space;

    @Column(name = "project_name", nullable = false)
    private String projectName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @OneToMany(mappedBy = "project")
    private List<Card> cards;

    @OneToMany(mappedBy = "project", cascade = {CascadeType.PERSIST})
    private List<Member> members;

    @OneToMany(mappedBy = "project", cascade = {CascadeType.REMOVE})
    private List<Label> labels;

    @OneToMany(mappedBy = "project")
    private List<Notification> notifications;

    @PreRemove
    private void preRemove() {
        for (Member member : members) {
            member.setProject(null);
        }
    }
}
