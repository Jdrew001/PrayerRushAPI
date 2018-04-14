package com.dtatkison.prayerrush.rushapi.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Stats")
public class Stats {

    @Id
    @GeneratedValue
    @Column(name = "statsId")
    private Integer statsId;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "userId")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @Column(name = "daysPrayed")
    private Integer daysPrayed;

    @Column(name = "hoursPrayed")
    private Integer hoursPrayed;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "goalId")
    private List<Goal> goals = new ArrayList<>();

    public Stats()
    {

    }

    public Stats(String name, User user, Integer daysPrayed, Integer hoursPrayed, List<Goal> goals)
    {
        this.name = name;
        this.user = user;
        this.daysPrayed = daysPrayed;
        this.hoursPrayed = hoursPrayed;
        this.goals = goals;
    }

    public Integer getStatsId() {
        return statsId;
    }

    public void setStatsId(Integer statsId) {
        this.statsId = statsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getDaysPrayed() {
        return daysPrayed;
    }

    public void setDaysPrayed(Integer daysPrayed) {
        this.daysPrayed = daysPrayed;
    }

    public Integer getHoursPrayed() {
        return hoursPrayed;
    }

    public void setHoursPrayed(Integer hoursPrayed) {
        this.hoursPrayed = hoursPrayed;
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }
}
