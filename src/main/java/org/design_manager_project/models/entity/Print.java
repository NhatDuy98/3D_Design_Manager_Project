package org.design_manager_project.models.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "prints")
public class Print{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

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
