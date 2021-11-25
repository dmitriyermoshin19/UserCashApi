package com.example.usercashapi.domains;


import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Slf4j
@Data
@Entity
@Table(name = "profiles")
public class Profile {
    @JsonView(Views.Create.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Min(value = 10)
    @JsonView(Views.Create.class)
    @Column(name = "cash")
    private float cash;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "profile")
    private User user;
}
