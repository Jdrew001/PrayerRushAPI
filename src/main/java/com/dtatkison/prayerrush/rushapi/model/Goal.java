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
    @Column(name = "description")
    private String description;

    @Column(name = "succeeded")
    private boolean succeeded;

    public Goal() {}

    public Goal(Integer id, String name, String description)
    {
        this.goalId = id;
        this.name = name;
        this.description = description;
    }

    public Integer getGoalId() {
        return goalId;
    }

    public void setGoalId(Integer goalId) {
        this.goalId = goalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public void setSucceeded(boolean succeeded) {
        this.succeeded = succeeded;
    }
}
