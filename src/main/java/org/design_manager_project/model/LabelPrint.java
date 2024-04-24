package org.design_manager_project.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "label_prints")
public class LabelPrint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private int id;

    @ManyToOne
    @JoinColumn(name = "label_id", nullable = false)
    private Label label;

    @ManyToOne
    @JoinColumn(name = "print_id", nullable = false)
    private Print print;

    @Column(name = "x_coordinate", nullable = false)
    private int xCoordinate;

    @Column(name = "y_coordinate", nullable = false)
    private int yCoordinate;

    @Column(name = "z_coordinate", nullable = false)
    private int zCoordinate;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
