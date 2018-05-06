package com.dtatkison.prayerrush.rushapi.model.json;

import com.dtatkison.prayerrush.rushapi.model.DAO.FriendDAO;

public class FriendRequestResponse {

    private FriendDAO friendDAO;
    private String title;
    private String message;

    public FriendRequestResponse(FriendDAO friendDAO, String title, String message) {
        this.friendDAO = friendDAO;
        this.title = title;
        this.message = message;
    }

    public FriendDAO getFriendDAO() {
        return friendDAO;
    }

    public void setFriendDAO(FriendDAO friendDAO) {
        this.friendDAO = friendDAO;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
