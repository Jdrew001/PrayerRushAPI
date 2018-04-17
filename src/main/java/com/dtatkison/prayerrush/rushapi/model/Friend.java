package com.dtatkison.prayerrush.rushapi.model;

import javax.persistence.*;

@Entity
@Table(name = "Friend")
public class Friend {

    @Id
    @GeneratedValue
    @Column(name = "friendId")
    private Integer id;

    @Column(name = "userId")
    private Integer userId;

    public Friend()
    {}

    public Friend(Integer id, Integer userId)
    {
        this.id = id;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
