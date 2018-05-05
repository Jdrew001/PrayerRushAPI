package com.dtatkison.prayerrush.rushapi.model.DAO;

import com.dtatkison.prayerrush.rushapi.model.User;

import java.util.Date;

public class RequestDAO {

    private Integer requestId;
    private String description;
    private Date date;
    private String firstname;
    private String lastname;
    private String email;

    public RequestDAO() {}
    public RequestDAO(String description, Date date, String firstname, String lastname, String email) {
        this.description = description;
        this.date = date;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }
    public RequestDAO(Integer requestId, String description, Date date, String firstname, String lastname, String email) {
        this.requestId = requestId;
        this.description = description;
        this.date = date;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
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

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
