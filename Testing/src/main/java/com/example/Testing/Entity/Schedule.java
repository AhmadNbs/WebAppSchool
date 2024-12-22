package com.example.Testing.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

/**
 * Entity class representing a schedule.
 */
@Setter
@Getter
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="schedule")
public class Schedule {
    @Id
    @UuidGenerator
    @Column(name="schedule_id", nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String day;

    @Column(nullable = false)
    private String time;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="materia_id") // Use the actual foreign key column name
    @Cascade({org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.REFRESH, org.hibernate.annotations.CascadeType.PERSIST, org.hibernate.annotations.CascadeType.DETACH})
    private Materia materia;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="clasa_id")
    @Cascade({org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.REFRESH, org.hibernate.annotations.CascadeType.PERSIST, org.hibernate.annotations.CascadeType.DETACH})
    private Clasa clasa;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="user_id")
    @Cascade({org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.REFRESH, org.hibernate.annotations.CascadeType.PERSIST, org.hibernate.annotations.CascadeType.DETACH})
    private User user; // professor (validation is done in Service and Controller)

}
