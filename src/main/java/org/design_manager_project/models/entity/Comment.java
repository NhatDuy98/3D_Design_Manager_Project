package org.design_manager_project.models.entity;

import jakarta.persistence.*;
import lombok.*;
import org.design_manager_project.models.BaseModel;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "comments")
public class Comment extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    @ManyToOne
    @JoinColumn(name = "print_id")
    private Print print;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

}
