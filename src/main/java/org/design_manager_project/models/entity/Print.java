package org.design_manager_project.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.design_manager_project.models.BaseModel;

import java.util.List;
import java.util.UUID;

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

    @Column(name = "latest_id")
    private UUID latestId;

    @OneToMany(mappedBy = "print", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Version> versions;

    @OneToMany(mappedBy = "print")
    private List<LabelPrint> labelPrints;
//
//    @PrePersist
//    @PreUpdate
//    private void updateLatestId(){
//        if (versions != null && !versions.isEmpty()){
//            versions.sort((v1, v2) -> v2.getCreatedAt().compareTo(v1.getCreatedAt()));
//            this.latestId = versions.get(0).getId();
//        }
//    }
//    @PostPersist
//    private void setLatestIdAfterPersist() {
//        updateLatestId();
//    }

}
