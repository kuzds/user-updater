package com.example.user.updater.repository.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "department")
public class Department {

    @Id
    @SequenceGenerator(name = "department_seq",
            sequenceName = "department_seq",
            initialValue = 1, allocationSize = 20)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "department_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "active", columnDefinition = "boolean default false not null")
    private Boolean active;
}
