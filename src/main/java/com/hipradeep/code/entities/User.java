package com.hipradeep.code.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "sb_users")
public class User {

    @Id
    @Column(name = "USER_ID")
    private String userId;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ENTRY_ID")
    private Integer entryId;

    @Column(name = "NAME", length = 100)
    private String name;

    @Column(name = "EMAIL", unique = true, length = 100)
    private String email;

    @Column(name = "ABOUT", columnDefinition = "TEXT")
    private String about;
}