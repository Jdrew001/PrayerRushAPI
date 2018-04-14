package com.dtatkison.prayerrush.rushapi.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "goal")
public class Goal {

    @Id
    @GeneratedValue
    @Column(name = "goalId")
    private Integer goalId;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "timeToSpend")
    private Integer timeToSpend;


}
