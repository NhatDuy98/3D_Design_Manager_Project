package org.design_manager_project.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "prints")
public class Print {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private int id;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "latest_id", nullable = false)
    private int latestId;

    @OneToMany(mappedBy = "print")
    private List<Version> versions;

    @OneToMany(mappedBy = "print")
    private List<LabelPrint> labelPrints;
}
