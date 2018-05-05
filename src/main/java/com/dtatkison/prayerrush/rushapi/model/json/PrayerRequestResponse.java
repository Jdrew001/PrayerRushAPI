package com.dtatkison.prayerrush.rushapi.model.json;

import com.dtatkison.prayerrush.rushapi.model.DAO.RequestDAO;
import com.dtatkison.prayerrush.rushapi.model.Request;
import com.dtatkison.prayerrush.rushapi.model.User;

public class PrayerRequestResponse {

    private RequestDAO requestDAO;
    private String title;
    private String message;

    public PrayerRequestResponse() { }

    public PrayerRequestResponse(RequestDAO requestDAO, String title, String message) {
        this.requestDAO = requestDAO;
        this.title = title;
        this.message = message;
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

    public RequestDAO getRequestDAO() {
        return requestDAO;
    }

    public void setRequestDAO(RequestDAO requestDAO) {
        this.requestDAO = requestDAO;
    }
}
