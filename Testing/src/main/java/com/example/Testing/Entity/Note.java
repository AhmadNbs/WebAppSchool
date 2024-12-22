package com.example.Testing.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Entity class representing a note.
 */
@Setter
@Getter
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="note")
public class Note {
    @Id
    @UuidGenerator
    @Column(name="nota_id", nullable = false)
    private UUID id;

    @Column(nullable = false)
    private float nota;

    @ManyToMany(mappedBy = "marks")
    @JsonIgnore
    @Cascade({org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.REFRESH, org.hibernate.annotations.CascadeType.PERSIST, org.hibernate.annotations.CascadeType.DETACH})
    private List<User> users;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "subjects_marks",
            joinColumns = {
                    @JoinColumn(name = "nota_id", referencedColumnName = "nota_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "materia", referencedColumnName = "name"),
            })
    @Cascade({org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.REFRESH, org.hibernate.annotations.CascadeType.PERSIST, org.hibernate.annotations.CascadeType.DETACH})
    private List<Materia> subjects;
}
