package com.example.Testing.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

/**
 * Entity class representing a class in a high school.
 */
@Setter
@Getter
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="clasa")
public class Clasa {
    @Id
    @UuidGenerator
    @Column(name="clasa_id")
    private UUID id;

    /**
     * The grade level of the class.
     */
    @Column(nullable = false)
    private int grade;

    /**
     * The class number.
     */
    @Column(nullable = false)
    private int number;

    /**
     * The list of schedules for the class.
     */
    @OneToMany(mappedBy = "clasa")
    @JsonIgnore
    @Cascade({org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.REFRESH, org.hibernate.annotations.CascadeType.PERSIST, org.hibernate.annotations.CascadeType.DETACH})
    private List<Schedule> schedules;

    /**
     * The list of users who are in the class.
     */
    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "users_clasa",
            joinColumns = {
                    @JoinColumn(name = "clasa_id", referencedColumnName = "clasa_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
            })
    @Cascade({org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.REFRESH, org.hibernate.annotations.CascadeType.PERSIST, org.hibernate.annotations.CascadeType.DETACH})
    private List<User> users;
}
