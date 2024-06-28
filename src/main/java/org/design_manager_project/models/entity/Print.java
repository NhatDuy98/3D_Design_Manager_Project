package org.design_manager_project.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.design_manager_project.models.BaseModel;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "prints")
public class Print extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "image", nullable = false)
    private String image;

    @OneToMany(mappedBy = "print", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Version> versions = new ArrayList<>();

    @OneToMany(mappedBy = "print")
    private List<LabelPrint> labelPrints;

    @OneToMany(mappedBy = "print")
    private List<Notification> notifications;
}
