package com.dtatkison.prayerrush.rushapi.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "UserPray")
public class UserPray {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "date")
    @NotNull
    private Date date;

    @Column(name = "didPray")
    @NotNull
    private boolean didPray;

}
