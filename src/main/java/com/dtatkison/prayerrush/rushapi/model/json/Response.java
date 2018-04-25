package com.dtatkison.prayerrush.rushapi.model.json;

public class Response {
    private String message;
    private boolean condition;

    public Response(String message, boolean condition)
    {
        this.message = message;
        this.condition = condition;
    }
}
