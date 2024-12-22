package com.example.Testing.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Entity class representing a User.
 */
@Setter
@Getter
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="user")
public class User {
    @Id
    @UuidGenerator
    @Column(name="user_id")
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String role; // professor or student-parent

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @ManyToMany(mappedBy = "users")
    @JsonIgnore
    @Cascade({org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.REFRESH, org.hibernate.annotations.CascadeType.PERSIST, org.hibernate.annotations.CascadeType.DETACH})
    private List<Clasa> clase;


    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "user_marks",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "nota_id")
    )
    @MapKeyJoinColumn(name = "materia_id")
    @Cascade({org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.REFRESH, org.hibernate.annotations.CascadeType.PERSIST, org.hibernate.annotations.CascadeType.DETACH})
    private Map<Materia, Note> marks;

    @OneToMany(mappedBy = "user") //professor
    @JsonIgnore
    @Cascade({org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.REFRESH, org.hibernate.annotations.CascadeType.PERSIST, org.hibernate.annotations.CascadeType.DETACH})
    private List<Schedule> schedules;

}