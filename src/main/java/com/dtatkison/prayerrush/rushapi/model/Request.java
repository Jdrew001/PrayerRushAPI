package com.dtatkison.prayerrush.rushapi.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "request")
public class Request {

    @Id
    @GeneratedValue
    @Column(name = "requestId")
    private Integer requestId;

    @Column(name = "description")
    private String description;

    @Column(name = "date")
    private Date date;

    public Request()
    {}

    public Request(String description, Date date)
    {
        this.description = description;
        this.date = date;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
