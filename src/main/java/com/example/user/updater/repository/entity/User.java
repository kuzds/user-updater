package com.example.user.updater.repository.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @SequenceGenerator(name = "user_seq",
            sequenceName = "user_seq",
            initialValue = 1, allocationSize = 20)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    private UserRole role;

    @Column(name = "email")
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "birth_day", nullable = false)
    private OffsetDateTime birthDay;

    @Column(name = "active", columnDefinition = "boolean default false not null")
    private Boolean active;

    @Column(name = "update_date", nullable = false)
    private OffsetDateTime updateDate;
}
