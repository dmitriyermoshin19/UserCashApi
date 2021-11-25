package com.example.usercashapi.domains;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "phones")
public class Phone {
    @JsonView(Views.Create.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JsonView(Views.Create.class)
    @Column(name = "value")
    private String value;
}
