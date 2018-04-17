package com.dtatkison.prayerrush.rushapi.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;

@Entity
@Table(name = "List")
public class List {

    @Id
    @GeneratedValue
    @Column(name = "listId")
    private Integer listId;

    @Column(name = "date")
    @NotNull
    private Date date;

    public List()
    {
    }

    public List(Integer id, Date date, java.util.List<List> items)
    {
        this.listId = id;
        this.date = date;
    }

    public Integer getListId() {
        return listId;
    }

    public void setListId(Integer listId) {
        this.listId = listId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
