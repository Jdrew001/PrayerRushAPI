package com.dtatkison.prayerrush.rushapi.model.DAO;

public class FriendDAO
{
    private String firstname, lastname, email;

    public FriendDAO() { }
    public FriendDAO(String firstname, String lastname, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }
    public FriendDAO(FriendDAO friendDAO) {
        this.firstname = friendDAO.getFirstname();
        this.lastname = friendDAO.getLastname();
        this.email = friendDAO.getEmail();
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