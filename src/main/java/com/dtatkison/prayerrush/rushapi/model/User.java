package com.dtatkison.prayerrush.rushapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.FilterJoinTable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue
    @Column(name = "userId")
    private Integer id;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    @NotNull
    private String email;

    @NotNull
    @Column(name = "password")
    private String password;

    @NotNull
    @Column(name = "salt")
    private String salt;

    @ManyToMany(cascade={CascadeType.ALL})
    @JsonIgnore
    @JoinTable(name="friend",
        joinColumns = {@JoinColumn(name = "userId", referencedColumnName = "userId")},
        inverseJoinColumns = {@JoinColumn(name = "friendId", referencedColumnName = "userId")})
    private List<User> followers = new ArrayList<>();

    @ManyToMany(mappedBy = "followers", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<User> following = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
    @JoinColumn(name = "userId")
    private List<com.dtatkison.prayerrush.rushapi.model.List> lists = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "userId")
    private List<Goal> goals = new ArrayList<>();

    public User() {}

    public User(Integer id, String firstname, String lastname, String username, String email)
    {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
    }

    public User(String email, String password, String salt)
    {
        this.username = username;
        this.email = email;
        this.password = password;
        this.salt = salt;
    }

    public User(User user)
    {
        this.id = user.id;
        this.firstname = user.firstname;
        this.lastname = user.lastname;
        this.username = user.username;
        this.email = user.email;
        this.password = user.password;
        this.salt = user.salt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void addToList(com.dtatkison.prayerrush.rushapi.model.List list) {
        this.lists.add(list);
    }

    public void removeFromList(int index) {
        this.lists.remove(index);
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public List<User> getFollowing() {
        return following;
    }

    public void setFollowing(List<User> following) {
        this.following = following;
    }

    public List<com.dtatkison.prayerrush.rushapi.model.List> getLists() {
        return lists;
    }

    public void removeAllLists() {
        this.getLists().clear();
    }
}
