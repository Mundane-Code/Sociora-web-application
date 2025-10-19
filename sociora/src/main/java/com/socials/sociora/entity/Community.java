package com.socials.sociora.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "community")
@Data
public class Community {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "name_of_community", nullable = false)
    private String nameOfCommunity;

    private String theme;

    @Column(name = "total_members")
    private Integer totalMembers = 0;
}