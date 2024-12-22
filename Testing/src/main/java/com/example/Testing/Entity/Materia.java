package com.example.Testing.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

/**
 * Entity class representing a subject or course.
 */
@Setter
@Getter
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="materia")
public class Materia {
    @Id
    @UuidGenerator
    @Column(name="materia_id",nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "subjects")
    @JsonIgnore
    @Cascade({org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.REFRESH, org.hibernate.annotations.CascadeType.PERSIST, org.hibernate.annotations.CascadeType.DETACH})
    private List<Note> marks;

    @OneToMany(mappedBy = "materia")
    @JsonIgnore
    @Cascade({org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.REFRESH, org.hibernate.annotations.CascadeType.PERSIST, org.hibernate.annotations.CascadeType.DETACH})
    private List<Schedule> schedules;

    @ManyToMany
    @JsonIgnore
    @Cascade({org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.REFRESH, org.hibernate.annotations.CascadeType.PERSIST, org.hibernate.annotations.CascadeType.DETACH})
    private List<User> users;
}
