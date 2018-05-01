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

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "description")
    @NotNull
    private String description;

    @Column(name = "date")
    @NotNull
    private Date date;

    public List()
    {
    }

    public List(List list) {
        this.listId = list.listId;
        this.name = list.name;
        this.description = list.description;
        this.date = list.date;
    }

    public List(Integer id, Date date, String name, String description)
    {
        this.listId = id;
        this.date = date;
        this.name = name;
        this.description = description;
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
}
